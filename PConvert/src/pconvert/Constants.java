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
    public final static String PDF = "PDF";
    public final static String PNG = "PNG";
    public final static String BMP = "BMP";
    public final static String TIFF = "TIFF";
    public final static String JPG = "JPG";
    public final static String GIF = "GIF";
    public final static String SVG = "SVG";
    //------Command Options------
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
    public final static String TEMP_FOLDER_OPTION = "tmp";
    public final static String TEMP_LONG_OPTION = "temp";
    //------------

    //--- Conversion types --------------------
    //-------------MS-OFFICE to PDF
    public final static String DOC_TO_PDF = "doctopdf";
    public final static String DOCX_TO_PDF = "docxtopdf";
    public final static String PPT_TO_PDF = "ppttopdf";
    public final static String PPTX_TO_PDF = "pptxtopdf";
    public final static String XLS_TO_PDF = "xlstopdf";
    public final static String XLSX_TO_PDF = "xlsxtopdf";
    //------------MS-OFFICE TO IMAGE
    public final static String DOC_TO_PNG = "doctopng";
    public final static String DOC_TO_JPG = "doctojpg";
    public final static String DOC_TO_SVG = "doctosvg";
    public final static String DOC_TO_GIF = "doctogif";
    public final static String DOC_TO_BMP = "doctobmp";

    public final static String DOCX_TO_PNG = "docxtopng";
    public final static String DOCX_TO_JPG = "docxtojpg";
    public final static String DOCX_TO_SVG = "docxtosvg";
    public final static String DOCX_TO_GIF = "docxtogif";
    public final static String DOCX_TO_BMP = "docxtobmp";

    public final static String PPT_TO_PNG = "ppttopng";
    public final static String PPT_TO_JPG = "ppttojpg";
    public final static String PPT_TO_SVG = "ppttosvg";
    public final static String PPT_TO_GIF = "ppttogif";
    public final static String PPT_TO_BMP = "ppttobmp";

    public final static String PPTX_TO_PNG = "pptxtopng";
    public final static String PPTX_TO_JPG = "pptxtojpg";
    public final static String PPTX_TO_SVG = "pptxtosvg";
    public final static String PPTX_TO_GIF = "pptxtogif";
    public final static String PPTX_TO_BMP = "pptxtobmp";

    public final static String XLS_TO_PNG = "xlstopng";
    public final static String XLS_TO_JPG = "xlstojpg";
    public final static String XLS_TO_SVG = "xlstosvg";
    public final static String XLS_TO_GIF = "xlstogif";
    public final static String XLS_TO_BMP = "xlstobmp";

    public final static String XLSX_TO_PNG = "xlsxtopng";
    public final static String XLSX_TO_JPG = "xlsxtojpg";
    public final static String XLSX_TO_SVG = "xlsxtosvg";
    public final static String XLSX_TO_GIF = "xlsxtogif";
    public final static String XLSX_TO_BMP = "xlsxtobmp";
    //------------IMAGE TO OTHER IMAGE FORMAT
    public final static String TIFF_TO_BMP = "tifftobmp";
    public final static String TIFF_TO_GIF = "tifftogif";
    public final static String TIFF_TO_PNG = "tifftopng";
    public final static String TIFF_TO_JPG = "tifftojpg";
    public final static String TIFF_TO_SVG = "tifftosvg";

    public final static String BMP_TO_TIFF = "bmptotiff";
    public final static String BMP_TO_GIF = "bmptogif";
    public final static String BMP_TO_PNG = "bmptopng";
    public final static String BMP_TO_JPG = "bmptojpg";
    public final static String BMP_TO_SVG = "bmptosvg";

    public final static String GIF_TO_TIFF = "giftotiff";
    public final static String GIF_TO_BMP = "giftobmp";
    public final static String GIF_TO_PNG = "giftopng";
    public final static String GIF_TO_JPG = "giftojpg";
    public final static String GIF_TO_SVG = "giftosvg";

    public final static String PNG_TO_TIFF = "pngtotiff";
    public final static String PNG_TO_BMP = "pngtobmp";
    public final static String PNG_TO_GIF = "pngtogif";
    public final static String PNG_TO_JPG = "pngtojpg";
    public final static String PNG_TO_SVG = "pngtosvg";

    public final static String JPG_TO_TIFF = "jpgtotiff";
    public final static String JPG_TO_BMP = "jpgtobmp";
    public final static String JPG_TO_PNG = "jpgtopng";
    public final static String JPG_TO_GIF = "jpgtogif";
    public final static String JPG_TO_SVG = "jpgtosvg";
    //------------ PDF TO IMAGE-----------------------------
    public final static String PDF_TO_SVG = "pdftosvg";
    public final static String PDF_TO_JPG = "pdftojpg";
    public final static String PDF_TO_PNG = "pdftopng";
    public final static String PDF_TO_BMP = "pdftobmp";
    public final static String PDF_TO_GIF = "pdftogif";
    //----------- HTML TO IMAGE/PDF ------------------------
    public final static String HTML_TO_PDF = "htmltopdf";
    public final static String HTML_TO_PNG = "htmltopng";
    public final static String HTML_TO_JPG = "htmltojpg";
    public final static String HTML_TO_SVG = "htmltosvg";
    public final static String HTML_TO_TIFF = "htmltotiff";
    public final static String HTML_TO_GIF = "htmltogif";
    public final static String HTML_TO_BMP = "htmltobmp";

    // messages
    public final static String CONVERSION_SUCCESS = "Conversion done successfully";
    public final static String ERROR = "ERROR: ";
    public final static String DESC_SFP = "Source folder path";
    public final static String DESC_SFN = "Name of source file";
    public final static String DESC_DFP = "Destination folder path";
    public final static String DESC_DFN = "Name of destination file";
    public final static String DESC_CONFIG = "Configuration file name with path";
    public final static String DESC_TEMP = "Temporary folder path.";
    public final static String DESC_CONVERSION_TYPE = "Type of conversion [";

    //---- Error messages
    public final static String ERR_SOURCE_DEST_FOLDER_NOT_EXISTS = ERROR + "Source/destination path does not exists";
    public final static String ERR_SOURCE_FILE_NOT_EXISTS = ERROR + "Source file does not exists on specified location";
    public final static String ERR_MISS_CONFIG_OR_CONVERSION_TYPE = ERROR + "Missing configuration file or conversion type";
    public final static String ERR_CONFIG_NOT_FOUND = ERROR + "Could not get configuration from config.properties:";
    public final static String ERR_NOT_LOAD_SOURCE_FILE = ERROR + "Cannot load sourceFile";
    public final static String ERR_CMD_ARG_NOT_PARSED = ERROR + "Unable to parse command-line arguments";
    public final static String ERR_NO_CONVERSION_TYPE_FOUND = ERROR + "Coversion type not matched ";
    public final static String ERR_TEMP_FOLDER_NO_FOUND = ERROR + "For this conversion, please configure temporary folder path.\n Also make sure it has read/write permissions.";
    public final static String ERR_CONVERSION_TYPE_DEST_FILE_EXTENSION_NOT_MATCHED = ERROR + "Conversion type and destination file extension did not matched.";

}
