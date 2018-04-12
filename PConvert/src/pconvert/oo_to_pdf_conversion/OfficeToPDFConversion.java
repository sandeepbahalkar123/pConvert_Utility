package pconvert.oo_to_pdf_conversion;

import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.io.IOException;
import com.sun.star.lang.XComponent;
import com.sun.star.uno.UnoRuntime;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import pconvert.ConfigInfoModel;
import pconvert.Constants;
import pconvert.IConversion;
import pconvert.Utility;

/**
 * @author riteshpandhurkar
 *
 * Used Callable to get callback on thread completion.
 */
public class OfficeToPDFConversion implements Callable<Boolean>, IConversion {

    private final XComponentLoader xComponentLoader;

    private final ConfigInfoModel model;

    public OfficeToPDFConversion(XComponentLoader xComponentLoader, ConfigInfoModel configInfoModel) {
        this.model = configInfoModel;
        this.xComponentLoader = xComponentLoader;
    }

    @Override
    public boolean convert() {

        try {

            String sourceFileExtension = model.getNameOfSourceFile();

            // Load the Document
            String workingDir = model.getPathOfSourceFolder();
            String sourceFile = workingDir + model.getNameOfSourceFile();

            if (!new File(sourceFile).canRead()) {
                throw new RuntimeException(Constants.ERR_NOT_LOAD_SOURCE_FILE + new File(sourceFile));
            }

            String sUrl = "file:///" + sourceFile;

            PropertyValue[] propertyValues = new PropertyValue[0];

            propertyValues = new PropertyValue[1];
            propertyValues[0] = new PropertyValue();
            propertyValues[0].Name = "Hidden";
            propertyValues[0].Value = true;

            XComponent xComp = xComponentLoader.loadComponentFromURL(
                    sUrl, "_blank", 0, propertyValues);

            // save as a PDF 
            XStorable xStorable = (XStorable) UnoRuntime
                    .queryInterface(XStorable.class, xComp);

            propertyValues = new PropertyValue[2];
            // Setting the flag for overwriting
            propertyValues[0] = new PropertyValue();
            propertyValues[0].Name = "Overwrite";
            propertyValues[0].Value = true;
            // Setting the filter name
            propertyValues[1] = new PropertyValue();
            propertyValues[1].Name = "FilterName";

            //--------------
            String fileExtension = Utility.getFileExtension(new File(sourceFileExtension));
            if ("doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
                propertyValues[1].Value = "writer_pdf_Export";
            } else if ("xls".equalsIgnoreCase(fileExtension) || "xlsx".equalsIgnoreCase(fileExtension)) {
                propertyValues[1].Value = "calc_pdf_Export";
            } else if ("ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
                propertyValues[1].Value = "draw_pdf_Export";
            }
            //--------------

            String newFolderName = model.getPathOfDestinationFolder();

            // creating folder to save pdf
            new File(newFolderName).mkdir();

            // Appending the favoured extension to the origin document name
            //  String myResult = workingDir + "letterOutput.pdf";
            String myResult = newFolderName + model.getNameOfDestinationFile();

            xStorable.storeToURL("file:///" + myResult, propertyValues);

            System.out.println(Constants.CONVERSION_SUCCESS + myResult);

            return true;
        } catch (IOException | RuntimeException ex) {
            Logger.getLogger(OfficeToPDFConversion.class.getName()).log(Level.SEVERE, null, ex);

        }
        return false;
    }

    @Override
    public Boolean call() throws Exception {
        boolean convert = convert();
        //delete temporary created .~lock files.
        if (convert) {
            File file = new File(model.getPathOfSourceFolder() + ".~lock." + model.getNameOfSourceFile()
                    + "#");
            if (file.exists()) {
                file.delete();
            }
        }
        return convert;
    }
}
