/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pconvert.pdftosvg;

import com.icafe4j.util.FileUtils;
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
import pconvert.ConfigInfoModel;
import pconvert.Constants;
import pconvert.IConversion;
import pconvert.Utility;
import pconvert.oo_to_pdf_conversion.ConfigureOOXDesktop;

/**
 *
 * @author riteshpandhurkar
 */
public class PDFToSVGConversion implements Callable<Boolean>, IConversion {

    private final ConfigInfoModel model;

    public PDFToSVGConversion(ConfigInfoModel model) {
        this.model = model;
    }

    @Override
    public Boolean call() throws Exception {
        return convert();
    }

    @Override
    public boolean convert() {

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

                String generatedCMD = "inkscape --without-gui --file " + tempCreatedPDF + " --export-text-to-path --export-plain-svg=" + model.getPathOfDestinationFolder() + model.getNameOfDestinationFile() + "_" + page + ".svg";

                processListMap.put(tempCreatedPDF, Runtime.getRuntime().exec(generatedCMD));

            }
            document.close();

            return true;

        } catch (IOException ex) {
            Logger.getLogger(PDFToSVGConversion.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } finally {
            deleteTempFiles(processListMap);
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

}
