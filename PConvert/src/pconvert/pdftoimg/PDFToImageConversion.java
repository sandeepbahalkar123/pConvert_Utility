/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pconvert.pdftoimg;

import com.icafe4j.util.FileUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import pconvert.ConfigInfoModel;
import pconvert.Constants;
import pconvert.IConversion;
import pconvert.Utility;

/**
 *
 * @author riteshpandhurkar
 */
public class PDFToImageConversion implements Callable<Boolean>, IConversion {

    private final ConfigInfoModel model;

    public PDFToImageConversion(ConfigInfoModel model) {
        this.model = model;
    }

    @Override
    public Boolean call() throws Exception {
        return convert();
    }

    @Override
    public boolean convert() {

        //this is done to get conversion type and set as a extension to file
        String replace = Utility.getFileExtension(new File(model.getNameOfDestinationFile()));
        if (model.getConversionType().equalsIgnoreCase(Constants.PDF_TO_SVG)) {
            return convertToSVGImageType(replace);
        } else {
            return convertToOtherImageType(replace);
        }

    }

    private boolean convertToOtherImageType(String extension) {
        try {
            PDDocument document = PDDocument.load(new File(model.getPathOfSourceFolder() + model.getNameOfSourceFile()));
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            //create file excluding extension of file
            File destinationFile = new File(model.getPathOfDestinationFolder() + FileUtils.getNameWithoutExtension(
                    new File(model.getPathOfDestinationFolder() + model.getNameOfDestinationFile())));

            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                ImageIOUtil.writeImage(bim, destinationFile + "_page_" + (page + 1) + "." + extension, 300);
            }
            document.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(PDFToImageConversion.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return false;
    }

    // This is done , to forcefully close app, after conversion done.
    private void deleteTempFiles(HashMap<String, Process> runningProcessList) {

        ScheduledExecutorService scheduler
                = Executors.newScheduledThreadPool(1);

        final Thread taskToCheckAllFutureCompleted = new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Process> tempList = new HashMap<>(runningProcessList);
                if (runningProcessList.isEmpty()) {

                    scheduler.shutdown();
                    System.out.println(Constants.CONVERSION_SUCCESS);
                } else {
                    for (Map.Entry<String, Process> entry : tempList.entrySet()) {
                        if (!entry.getValue().isAlive()) {
                            boolean delete = new File(entry.getKey()).delete();
                            runningProcessList.remove(entry.getKey());
                        }
                    }
                }
            }
        });

        int initialDelay = 10;
        int periodicDelay = 5;

        scheduler.scheduleAtFixedRate(taskToCheckAllFutureCompleted, initialDelay, periodicDelay,
                TimeUnit.SECONDS);
    }

    private boolean convertToSVGImageType(String extension) {
        String tempFolderPath = model.getTempFolder();
        HashMap<String, Process> processListMap = new HashMap<String, Process>();
        try {

            // TODO code application logic here
            PDDocument document = PDDocument.load(new File(model.getPathOfSourceFolder() + model.getNameOfSourceFile()));
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                PDPage page1 = document.getPage(page);

                // Create a new empty document
                PDDocument newDocument = new PDDocument();

                // Create a new blank page and add it to the document
                newDocument.addPage(page1);

                // Save the newly created document
                String tempCreatedPDF = tempFolderPath + model.getNameOfSourceFile() + "_" + page + ".pdf";

                newDocument.save(tempCreatedPDF);

                // finally make sure that the document is properly closed.
                newDocument.close();

                String fileNameWithoutExtension = FileUtils.getNameWithoutExtension(new File(model.getNameOfDestinationFile()));

                String generatedCMD = "inkscape --without-gui --file " + tempCreatedPDF + " --export-text-to-path --export-plain-svg=" + model.getPathOfDestinationFolder() + fileNameWithoutExtension + "_page_" + page + "." + extension;

                processListMap.put(tempCreatedPDF, Runtime.getRuntime().exec(generatedCMD));

            }
            document.close();

            return true;

        } catch (IOException ex) {
            Logger.getLogger(PDFToImageConversion.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } finally {
            deleteTempFiles(processListMap);
        }
        return false;

    }

}
