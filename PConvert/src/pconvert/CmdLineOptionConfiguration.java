package pconvert;

import com.sun.star.frame.XComponentLoader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import pconvert.image_to_image_conversion.ImageToImageConversion;
import pconvert.oo_to_pdf_conversion.ConfigureOOXDesktop;
import pconvert.oo_to_pdf_conversion.OfficeToPDFConversion;
import pconvert.pdftoimg.PDFToImageConversion;

/**
 *
 * @author riteshpandhurkar
 */
public class CmdLineOptionConfiguration {

    /**
     * "Definition" stage of command-line parsing with Apache Commons CLI.
     *
     * @return Definition of command-line options.
     */
    public static Options generateOptions(String types) {

        final Option helpOption = Option.builder(Constants.HELP_OPTION)
                .longOpt(Constants.HELP_LONG_OPTION)
                .desc(Constants.HELP_LONG_OPTION)
                .build();
        final Option sfpOption = Option.builder(Constants.SOURCE_FOLDER_PATH_OPTION)
                .longOpt(Constants.SOURCE_FOLDER_PATH_LONG_OPTION)
                .hasArg()
                .desc(Constants.DESC_SFP)
                .build();
        final Option sfnOption = Option.builder(Constants.SOURCE_FILE_NAME_OPTION)
                .longOpt(Constants.SOURCE_FILE_NAME_LONG_OPTION)
                .hasArg()
                .desc(Constants.DESC_SFN)
                .build();
        final Option dfpOption = Option.builder(Constants.DEST_FOLDER_PATH_OPTION)
                .longOpt(Constants.DEST_FOLDER_PATH_LONG_OPTION)
                .hasArg()
                .desc(Constants.DESC_DFP)
                .build();
        final Option dfnOption = Option.builder(Constants.DEST_FILE_NAME_OPTION)
                .longOpt(Constants.DEST_FILE_NAME_LONG_OPTION)
                .hasArg()
                .desc(Constants.DESC_DFN)
                .build();
        final Option typeOption = Option.builder(Constants.TYPE_OPTION)
                .hasArg()
                .longOpt(Constants.TYPE_LONG_OPTION)
                .desc(Constants.DESC_CONVERSION_TYPE + types + "]")
                .build();
        final Option configOption = Option.builder(Constants.CONFIG_OPTION)
                .longOpt(Constants.CONFIG_LONG_OPTION)
                .hasArg()
                .desc(Constants.DESC_CONFIG)
                .build();
        final Option tempOption = Option.builder(Constants.TEMP_FOLDER_OPTION)
                .longOpt(Constants.TEMP_LONG_OPTION)
                .hasArg()
                .desc(Constants.DESC_TEMP)
                .build();
        final Options options = new Options();
        // options.addOption(configOption);
        options.addOption(helpOption);
        options.addOption(sfpOption);
        options.addOption(sfnOption);
        options.addOption(dfpOption);
        options.addOption(dfnOption);
        options.addOption(typeOption);
        options.addOption(configOption);
        options.addOption(tempOption);
        return options;
    }

    /**
     * Generate usage information with Apache Commons CLI.
     *
     * @param options Instance of Options to be used to prepare usage formatter.
     * @return HelpFormatter instance that can be used to print usage
     * information.
     */
    private static void printUsage(final Options options) {
        final HelpFormatter formatter = new HelpFormatter();
        final String syntax = "PConvert.jar";
        out.println("=====");
        out.println("USAGE");
        out.println("=====");
        final PrintWriter pw = new PrintWriter(out);
        formatter.printUsage(pw, 80, syntax, options);
        pw.flush();
    }

    /**
     * Generate help information with Apache Commons CLI.
     *
     * @param options Instance of Options to be used to prepare help formatter.
     * @return HelpFormatter instance that can be used to print help
     * information.
     */
    private static void printHelp(final Options options) {
        final HelpFormatter formatter = new HelpFormatter();
        final String syntax = "PConvert.jar";
        out.println("\n====");
        out.println("HELP");
        out.println("====");
        formatter.printHelp(syntax, "", options, "");
    }

    /**
     * "Parsing" stage of command-line processing demonstrated with Apache
     * Commons CLI.
     *
     * @param options Options from "definition" stage.
     * @param commandLineArguments Command-line arguments provided to
     * application.
     * @return Instance of CommandLine as parsed from the provided Options and
     * command line arguments; may be {@code null} if there is an exception
     * encountered while attempting to parse the command line options.
     */
    public static CommandLine generateCommandLine(
            final Options options, final String[] commandLineArguments) {
        final CommandLineParser cmdLineParser = new DefaultParser();
        CommandLine commandLine = null;
        try {
            commandLine = cmdLineParser.parse(options, commandLineArguments);
        } catch (ParseException parseException) {
            out.println(
                    Constants.ERR_CMD_ARG_NOT_PARSED
                    + Arrays.toString(commandLineArguments) + " due to: "
                    + parseException);
        }
        return commandLine;
    }

    /*
     * initialize of application
     *@param args, argument from command line
     */
    public void init(String[] args) {
        try {

            Options generateOptions = generateOptions(readDataConfig());

            final CommandLine commandLine = generateCommandLine(generateOptions, args);

            if (commandLine != null) {
                //---------Show HELP and exit programme-------------
                if (commandLine.hasOption(Constants.HELP_OPTION)) {
                    printUsage(generateOptions);
                    printHelp(generateOptions);
                    System.exit(-1);
                }

                //------------Parse config and initialize ConfigModelInfo ----------
                String configOptionValue = commandLine.getOptionValue(Constants.CONFIG_OPTION);
                String typeOptionValue = commandLine.getOptionValue(Constants.TYPE_OPTION);
                if (configOptionValue != null && typeOptionValue != null) {
                    doCallConversionMethods(setAppConfiguration(configOptionValue, commandLine));
                } else {
                    out.println(Constants.ERR_MISS_CONFIG_OR_CONVERSION_TYPE);
                    System.exit(-1);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(CmdLineOptionConfiguration.class.getName()).log(Level.WARNING, null, ex);
        }

    }

    public ConfigInfoModel setAppConfiguration(String configPath, CommandLine commandLine) {

        ConfigInfoModel pdfc = null;
        String ooLibPath;
        String pathOfDestinationFolder;
        String nameOfDestinationFile;
        String pathOfSourceFolder;
        String nameOfSourceFile;
        String tempFolder;
        String conversionType;
        try {
            FileReader reader = new FileReader(configPath);
            Properties props = new Properties();
            props.load(reader);
            reader.close();
            //-----Get property value from config.properties ----
            ooLibPath = props.getProperty(Constants.CONFIG_OO_LIB_PATH);
            pathOfDestinationFolder = commandLine.hasOption(Constants.DEST_FOLDER_PATH_OPTION) ? commandLine.getOptionValue(Constants.DEST_FOLDER_PATH_OPTION) : props.getProperty(Constants.CONFIG_DEST_FOLDER_PATH);
            nameOfDestinationFile = commandLine.hasOption(Constants.DEST_FILE_NAME_OPTION) ? commandLine.getOptionValue(Constants.DEST_FILE_NAME_OPTION) : props.getProperty(Constants.CONFIG_DEST_FILE_NAME);
            pathOfSourceFolder = commandLine.hasOption(Constants.SOURCE_FOLDER_PATH_OPTION) ? commandLine.getOptionValue(Constants.SOURCE_FOLDER_PATH_OPTION) : props.getProperty(Constants.CONFIG_SOURCE_FOLDER_PATH);
            nameOfSourceFile = commandLine.hasOption(Constants.SOURCE_FILE_NAME_OPTION) ? commandLine.getOptionValue(Constants.SOURCE_FILE_NAME_OPTION) : props.getProperty(Constants.CONFIG_SOURCE_FILE_NAME);
            tempFolder = commandLine.hasOption(Constants.TEMP_FOLDER_OPTION) ? commandLine.getOptionValue(Constants.TEMP_FOLDER_OPTION) : props.getProperty(Constants.CONFIG_TEMP_FOLDER_PATH);
            conversionType = commandLine.getOptionValue(Constants.TYPE_OPTION);
            //---------

            // Configure the converter
            pdfc = new ConfigInfoModel();
            pdfc.setOOLibPath(ooLibPath.trim());
            pdfc.setPathOfDestinationFolder(pathOfDestinationFolder.trim());
            pdfc.setNameOfDestinationFile(nameOfDestinationFile.trim());
            pdfc.setPathOfSourceFolder(pathOfSourceFolder.trim());
            pdfc.setNameOfSourceFile(nameOfSourceFile.trim());
            pdfc.setTempFolder(tempFolder.trim());
            pdfc.setConversionType(conversionType.trim());

            // Exit program if paths are not valid.
            if (!validate(pdfc)) {
                System.exit(-1);
            }
        } catch (IOException ex) {
            System.out.println(Constants.ERR_CONFIG_NOT_FOUND
                    + ex.getMessage());
        }
        return pdfc;
    }

    /**
     * To validate given source/destination folder and source file exists or
     * not.
     *
     * @param model
     * @return boolean. If folder/file NOT exists return false. Return true if
     * folder/file exists.
     */
    private boolean validate(ConfigInfoModel model) {
        boolean validate = false;
        if (!Utility.isDirecotryPathExists(new File(model.getPathOfDestinationFolder()))) {
            System.err.println(Constants.ERR_SOURCE_DEST_FOLDER_NOT_EXISTS);
        } else if (!Utility.isDirecotryPathExists(new File(model.getPathOfSourceFolder()))) {
            System.err.println(Constants.ERR_SOURCE_DEST_FOLDER_NOT_EXISTS);
        } else if (!Utility.isFilePathExists(new File(model.getPathOfSourceFolder() + model.getNameOfSourceFile()))) {
            System.err.println(Constants.ERR_SOURCE_FILE_NOT_EXISTS);
        } else if (!Utility.isDirecotryPathExists(new File(model.getTempFolder())) && Constants.PDF_TO_SVG.equalsIgnoreCase(model.getConversionType())) {
            System.err.println(Constants.ERR_TEMP_FOLDER_NO_FOUND);
        } else if (!model.getConversionType().endsWith(Utility.getFileExtension(new File(model.getNameOfDestinationFile())))) {
            System.err.println(Constants.ERR_CONVERSION_TYPE_DEST_FILE_EXTENSION_NOT_MATCHED);
        } else {
            validate = true;
        }
        return validate;
    }

    /**
     * This use to navigate to various conversion process based on -type
     * argument. Created executor pool of 2 threads
     *
     * @param configInfoModel
     *
     */
    private void doCallConversionMethods(ConfigInfoModel configInfoModel) {

        try {
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
            final CompletionService<Boolean> executorCompletionService = new ExecutorCompletionService<Boolean>(executor);
            switch (configInfoModel.getConversionType()) {
                case Constants.DOC_TO_PDF:
                case Constants.DOCX_TO_PDF:
                case Constants.PPT_TO_PDF:
                case Constants.PPTX_TO_PDF:
                case Constants.XLS_TO_PDF:
                case Constants.XLSX_TO_PDF:
                    // created new thread and submit to excecuter
                    XComponentLoader xComponentLoader = ConfigureOOXDesktop.getXComponentLoader(configInfoModel.getOOLibPath());
                    OfficeToPDFConversion officeToPDFConversion = new OfficeToPDFConversion(xComponentLoader, configInfoModel);
                    executorCompletionService.submit(officeToPDFConversion);
                    executor.shutdown();
                    doScheduleToCloseApp();
                    //---
                    break;
                case Constants.TIFF_TO_BMP:
                case Constants.GIF_TO_BMP:
                case Constants.JPG_TO_BMP:
                case Constants.PNG_TO_BMP:
                case Constants.TIFF_TO_GIF:
                case Constants.BMP_TO_GIF:
                case Constants.JPG_TO_GIF:
                case Constants.PNG_TO_GIF:
                case Constants.TIFF_TO_PNG:
                case Constants.GIF_TO_PNG:
                case Constants.JPG_TO_PNG:
                case Constants.BMP_TO_PNG:
                case Constants.TIFF_TO_JPG:
                case Constants.GIF_TO_JPG:
                case Constants.BMP_TO_JPG:
                case Constants.PNG_TO_JPG:
                case Constants.JPG_TO_TIFF:
                case Constants.GIF_TO_TIFF:
                case Constants.BMP_TO_TIFF:
                case Constants.PNG_TO_TIFF:
                    executorCompletionService.submit(new ImageToImageConversion(configInfoModel));
                    executor.shutdown();
                    break;
                case Constants.PDF_TO_SVG:
                case Constants.PDF_TO_BMP:
                case Constants.PDF_TO_GIF:
                case Constants.PDF_TO_JPG:
                case Constants.PDF_TO_PNG:
                    executorCompletionService.submit(new PDFToImageConversion(configInfoModel));
                    executor.shutdown();
                    break;
                default:
                    System.out.println(Constants.ERR_NO_CONVERSION_TYPE_FOUND);
            }

            //---------
        } catch (Exception ex) {
            Logger.getLogger(CmdLineOptionConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(Constants.ERROR + " EXCEPTION OCCURE");
            ex.printStackTrace();

        }

    }

    // This is done , to forcefully close app, after conversion done.
    private void doScheduleToCloseApp() {

        ScheduledExecutorService scheduler
                = Executors.newScheduledThreadPool(1);

        final Thread taskToCheckAllFutureCompleted = new Thread(new Runnable() {
            @Override
            public void run() {
                scheduler.shutdown();
                ConfigureOOXDesktop.stopXDesktop();
            }
        });

        int initialDelay = 50;
        int periodicDelay = 10;

        scheduler.scheduleAtFixedRate(taskToCheckAllFutureCompleted, initialDelay, periodicDelay,
                TimeUnit.SECONDS);
    }

    private String readDataConfig() {
        try {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            InputStream is = loader.getResourceAsStream("pconvert/data.properties");
            Properties props = new Properties();
            props.load(is);
            is.close();
            return props.getProperty("conversion_types");

        } catch (Exception ex) {
            System.out.println("Could not get configuration from config.properties: "
                    + ex.getMessage());
        }
        return "";
    }
}
