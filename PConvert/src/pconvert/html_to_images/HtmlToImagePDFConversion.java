/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pconvert.html_to_images;

import com.icafe4j.util.FileUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import pconvert.ConfigInfoModel;
import pconvert.Constants;
import pconvert.IConversion;
import pconvert.Utility;
import pconvert.image_to_image_conversion.ImageToImageConversion;
import pconvert.image_to_image_conversion.img_to_svg_.ImageToSVGConversion;

/**
 *
 * @author riteshpandhurkar
 *
 */
public class HtmlToImagePDFConversion implements Callable<Boolean>, IConversion {

    private final ConfigInfoModel model;
    private String createdTempJSName;
    private String convertedFilePath;
    private boolean isConversionSupportedByPhantomJS = true;

    public HtmlToImagePDFConversion(ConfigInfoModel model) {
        this.model = model;
    }

    @Override
    public Boolean call() throws Exception {
        return convert();
    }

    @Override
    public boolean convert() {

        createdTempJSName = model.getTempFolder() + "temp.js";

        if (model.getConversionType().toUpperCase().endsWith(Constants.PNG)
                || model.getConversionType().toUpperCase().endsWith(Constants.JPG)
                || model.getConversionType().toUpperCase().endsWith(Constants.PDF)) {
            convertedFilePath = model.getPathOfDestinationFolder() + model.getNameOfDestinationFile();
        } else {
            isConversionSupportedByPhantomJS = false;
            convertedFilePath = model.getTempFolder() + FileUtils.getNameWithoutExtension(new File(model.getNameOfDestinationFile())) + ".png";
        }
        boolean createDynamicJS = createDynamicJS(convertedFilePath);
        if (createDynamicJS) {
            doExecuteDynamicCreatedJS();
        }

        return true;
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

                            //key = createdTempJSName
                            String tempAndDestFileName = entry.getKey();
                            //---------
                            if (!isConversionSupportedByPhantomJS) {
                                try {
                                    File tempAndDestFileNameFileObj = new File(tempAndDestFileName);

                                    ConfigInfoModel tempModel = (ConfigInfoModel) model.clone();
                                    System.out.println(".run()" + tempAndDestFileNameFileObj.getParent());
                                    tempModel.setPathOfSourceFolder(tempAndDestFileNameFileObj.getParent() + "/");
                                    tempModel.setNameOfSourceFile(tempAndDestFileNameFileObj.getName());

                                    boolean convert = false;
                                    if (model.getConversionType().toUpperCase().equalsIgnoreCase(Constants.HTML_TO_SVG)) {
                                        convert = new ImageToSVGConversion(tempModel).convert();
                                    } else {
                                        convert = new ImageToImageConversion(tempModel).convert();
                                    }
                                    if (convert) {
                                        tempAndDestFileNameFileObj.delete();
                                    }
                                } catch (CloneNotSupportedException ex) {
                                    Logger.getLogger(HtmlToImagePDFConversion.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            } else {
                                boolean delete = new File(createdTempJSName).delete();
                            }
                            //---------
                            runningProcessList.remove(tempAndDestFileName);
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

    private boolean doExecuteDynamicCreatedJS() {

        HashMap<String, Process> processListMap = new HashMap<String, Process>();
        try {

            String generatedCMD = "phantomjs " + createdTempJSName;
            processListMap.put(convertedFilePath, Runtime.getRuntime().exec(generatedCMD));

            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            deleteTempFiles(processListMap);
        }
        return false;

    }

    private boolean createDynamicJS(String nameOfCreatedFile) {

        try {
            String nameOfSourceHtmlFile = "file:///" + model.getPathOfSourceFolder() + model.getNameOfSourceFile();
            String text = "  var page = require('webpage').create();\n"
                    + "  //viewportSize being the actual size of the headless browser\n"
                    + "  page.viewportSize = { width: 1024, height: 1024 };\n"
                    + "  //the clipRect is the portion of the page you are taking a screenshot of\n"
                    + " // page.clipRect = { top: 0, left: 0, width: 1024, height: 1024 };\n"
                    + "\n"
                    + "var homePage = \"" + nameOfSourceHtmlFile + "\";"
                    + "\n"
                    + "page.open(homePage);\n"
                    + "page.onLoadFinished = function(status) {\n"
                    + "  var url = page.url;\n"
                    + "\n"
                    + "  console.log(\"Status:  \" + status);\n"
                    + "  console.log(\"Loaded:  \" + url);\n"
                    + "  page.render(\"" + nameOfCreatedFile + "\");\n"
                    + "  phantom.exit();\n"
                    + "};\n";

            System.out.println("htmltoimageconversion.UsingPhantomJS.createSampleTemplate()::\n" + text);

            FileWriter fw = new FileWriter(createdTempJSName);
            fw.write(text);
            fw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

}
