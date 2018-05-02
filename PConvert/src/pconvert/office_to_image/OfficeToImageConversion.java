/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pconvert.office_to_image;

import com.icafe4j.util.FileUtils;
import com.sun.star.frame.XComponentLoader;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import pconvert.ConfigInfoModel;
import pconvert.IConversion;
import pconvert.Utility;
import pconvert.oo_to_pdf_conversion.ConfigureOOXDesktop;
import pconvert.oo_to_pdf_conversion.OfficeToPDFConversion;
import pconvert.pdftoimg.PDFToImageConversion;

/**
 * This class do office to images conversion
 * Steps : Doc to PDF then PDF to Images
 * @author riteshpandhurkar
 */
public class OfficeToImageConversion implements Callable<Boolean>, IConversion {

    private final ConfigInfoModel model;

    public OfficeToImageConversion(ConfigInfoModel model) {
        this.model = model;

    }

    @Override
    public Boolean call() throws Exception {

        return convert();
    }

    @Override
    public boolean convert() {
        try {
            // created new thread and submit to excecuter
            XComponentLoader xComponentLoader = ConfigureOOXDesktop.getXComponentLoader(model.getOOLibPath());
            String nameDestFileWithExtension = FileUtils.getNameWithoutExtension(new File(model.getNameOfDestinationFile())) + ".pdf";

            // make clone of ConfigInfoModel to create doc to pdf conversion first
            ConfigInfoModel tempModelForOfficeToPDFConversion = (ConfigInfoModel) model.clone();

            // Set temp folder as dest folder. Converts doc-->PDF in temp folder with fileName = nameOfDestinationFile
            tempModelForOfficeToPDFConversion.setPathOfDestinationFolder(tempModelForOfficeToPDFConversion.getTempFolder());
            tempModelForOfficeToPDFConversion.setNameOfDestinationFile(nameDestFileWithExtension);

            OfficeToPDFConversion officeToPDFConversion = new OfficeToPDFConversion(xComponentLoader, tempModelForOfficeToPDFConversion);
            boolean convert = officeToPDFConversion.convert();
            if (convert) {

                //Secondly, make clone of ConfigInfoModel to create pdf to images conversion
                ConfigInfoModel tempModelForPDFToImgConversion = (ConfigInfoModel) model.clone();

                //set sourceFolder=tempFolder path. So that, pdf --> image conversion done from this path
                tempModelForPDFToImgConversion.setPathOfSourceFolder(tempModelForPDFToImgConversion.getTempFolder());
                //set sourceFileName=nameDestFileWithExtension
                tempModelForPDFToImgConversion.setNameOfSourceFile(nameDestFileWithExtension);
                //---Do conversion for pdf--> Images.
                PDFToImageConversion pdfToImageConversion = new PDFToImageConversion(tempModelForPDFToImgConversion);
                convert = pdfToImageConversion.convert();

                //Delete files from TempFolder.
                File file = new File(model.getTempFolder() + nameDestFileWithExtension);

                //to forcefully stop application, after all conversion done.
                Utility.doScheduleToCloseApp();
            }
            return convert;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(OfficeToImageConversion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
