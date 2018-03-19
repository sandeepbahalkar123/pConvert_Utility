package pconvert;

/**
 *
 * @author riteshpandhurkar
 */
public class Constants {

    //--- Config.properties 
    public final static String CONFIG_OO_LIB_PATH = "ooLibPath";
    public final static String CONFIG_TEMP_FOLDER_PATH = "tempFolder";
    public final static String CONFIG_DEST_FOLDER_PATH = "pathOfDestinationFolder";
    public final static String CONFIG_DEST_FILE_NAME = "nameOfDestinationFile";
    public final static String CONFIG_SOURCE_FOLDER_PATH = "pathOfSourceFolder";
    public final static String CONFIG_SOURCE_FILE_NAME = "nameOfSourceFile";
    //------------
    public final static String SOURCE_FOLDER_PATH_OPTION = "sfp";
    public final static String SOURCE_FOLDER_PATH_LONG_OPTION = "source_folder_path";
    public final static String SOURCE_FILE_NAME_OPTION = "sfn";
    public final static String SOURCE_FILE_NAME_LONG_OPTION = "source_file_name";
    public final static String DEST_FOLDER_PATH_OPTION = "dfp";
    public final static String DEST_FOLDER_PATH_LONG_OPTION = "dest_folder_path";
    public final static String DEST_FILE_NAME_OPTION = "dfn";
    public final static String DEST_FILE_NAME_LONG_OPTION = "dest_file_name";
    public final static String TYPE_OPTION = "t";
    public final static String TYPE_LONG_OPTION = "type";
    public final static String CONFIG_OPTION = "c";
    public final static String CONFIG_LONG_OPTION = "config";
    public final static String HELP_OPTION = "h";
    public final static String HELP_LONG_OPTION = "help";
    //------------

    //--- Conversion types --------------------
    //-------------MS-OFFICE to PDF
    public final static String DOC_TO_PDF = "doctopdf";
    public final static String DOCX_TO_PDF = "docxtopdf";
    public final static String PPT_TO_PDF = "ppttopdf";
    public final static String PPTX_TO_PDF = "pptxtopdf";
    public final static String XLS_TO_PDF = "xlstopdf";
    public final static String XLSX_TO_PDF = "xlsxtopdf";
    //------------IMAGE TO OTHER IMAGE FORMAT
    public final static String TIFF_TO_BMP = "tifftobmp";
    public final static String TIFF_TO_GIF = "tifftogif";
    public final static String TIFF_TO_PNG = "tifftopng";
    public final static String TIFF_TO_JPG = "tifftojpg";

    public final static String BMP_TO_TIFF = "bmptotiff";
    public final static String BMP_TO_GIF = "bmptogif";
    public final static String BMP_TO_PNG = "bmptopng";
    public final static String BMP_TO_JPG = "bmptojpg";

    public final static String GIF_TO_TIFF = "giftotiff";
    public final static String GIF_TO_BMP = "giftobmp";
    public final static String GIF_TO_PNG = "giftopng";
    public final static String GIF_TO_JPG = "giftojpg";

    public final static String PNG_TO_TIFF = "pngtotiff";
    public final static String PNG_TO_BMP = "pngtobmp";
    public final static String PNG_TO_GIF = "pngtogif";
    public final static String PNG_TO_JPG = "pngtojpg";

    public final static String JPG_TO_TIFF = "jpgtotiff";
    public final static String JPG_TO_BMP = "jpgtobmp";
    public final static String JPG_TO_PNG = "jpgtopng";
    public final static String JPG_TO_GIF = "jpgtogif";

    // messages
    public final static String CONVERSION_SUCCESS = "Conversion done successfully";
    public final static String ERROR = "ERROR: ";
    public final static String DESC_SFP = "Source folder path";
    public final static String DESC_SFN = "Name of source file";
    public final static String DESC_DFP = "Destination folder path";
    public final static String DESC_DFN = "Name of destination file";
    public final static String DESC_CONFIG = "Configuration file name with path";
    public final static String DESC_CONVERSION_TYPE = "Type of conversion [" + DOC_TO_PDF + "," + DOCX_TO_PDF + "," + PPT_TO_PDF + "," + PPTX_TO_PDF + "," + XLS_TO_PDF + "," + XLSX_TO_PDF + "]";

    //---- Error messages
    public final static String ERR_SOURCE_DEST_FOLDER_NOT_EXISTS = ERROR + "Source/destination path does not exists";
    public final static String ERR_SOURCE_FILE_NOT_EXISTS = ERROR + "Source file does not exists on specified location";
    public final static String ERR_MISS_CONFIG_OR_CONVERSION_TYPE = ERROR + "Missing configuration file or conversion type";
    public final static String ERR_CONFIG_NOT_FOUND = ERROR + "Could not get configuration from config.properties:";
    public final static String ERR_NOT_LOAD_SOURCE_FILE = ERROR + "Cannot load sourceFile";
    public final static String ERR_CMD_ARG_NOT_PARSED = ERROR + "Unable to parse command-line arguments";
    public final static String ERR_NO_CONVERSION_TYPE_FOUND = ERROR + "Coversion type not matched ";

}
