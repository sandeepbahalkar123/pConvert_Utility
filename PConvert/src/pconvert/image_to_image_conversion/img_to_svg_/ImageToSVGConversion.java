/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pconvert.image_to_image_conversion.img_to_svg_;

import com.icafe4j.util.FileUtils;
import com.sun.corba.se.impl.orbutil.closure.Constant;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import pconvert.ConfigInfoModel;
import pconvert.Constants;
import pconvert.IConversion;
import pconvert.Utility;
import pconvert.image_to_image_conversion.ImageToImageConversion;
import pconvert.image_to_pdf_conversion.ImageToPDFConversion;
import pconvert.pdftoimg.PDFToImageConversion;

/**
 *
 * @author riteshpandhurkar
 *
 * Approach will be: 1. Firstly convert image to PDF(using pdfbox library).
 * 2.Then, PDF to SVG conversion will be use.
 */
public class ImageToSVGConversion implements Callable<Boolean>, IConversion {

    private final ConfigInfoModel model;

    public ImageToSVGConversion(ConfigInfoModel model) {
        this.model = model;
    }

    @Override
    public Boolean call() throws Exception {
        return convert();
    }

    @Override
    public boolean convert() {
        return doConversion();
    }

    private boolean doConversion() {
        try {
            //Firstly, make clone of ConfigInfoModel to create img to PDF conversion
            ConfigInfoModel tempModelForImgToPDFConversion = (ConfigInfoModel) model.clone();

            String nameDestFileWithOutExtension = FileUtils.getNameWithoutExtension(new File(model.getNameOfDestinationFile()));

            tempModelForImgToPDFConversion.setNameOfSourceFile(model.getNameOfSourceFile());
            tempModelForImgToPDFConversion.setPathOfSourceFolder(model.getPathOfSourceFolder());
            //set temp folder path as destination to convert to PDF first.
            tempModelForImgToPDFConversion.setPathOfDestinationFolder(model.getTempFolder());
            tempModelForImgToPDFConversion.setNameOfDestinationFile(nameDestFileWithOutExtension + ".pdf");

            boolean convert = new ImageToPDFConversion(tempModelForImgToPDFConversion).convert();

            if (convert) {
                //secondly, make clone of ConfigInfoModel to create PDF to SVG conversion
                ConfigInfoModel tempModelForPDFToSVGConversion = (ConfigInfoModel) model.clone();

                //set sourceFolder=tempFolder path. So that, pdf --> image conversion done from this path
                tempModelForPDFToSVGConversion.setPathOfSourceFolder(tempModelForImgToPDFConversion.getTempFolder());
                //set sourceFileName=nameDestFileWithExtension
                tempModelForPDFToSVGConversion.setNameOfSourceFile(tempModelForImgToPDFConversion.getNameOfDestinationFile());
                //---Do conversion for pdf--> Images.
                PDFToImageConversion pdfToImageConversion = new PDFToImageConversion(tempModelForPDFToSVGConversion);
                boolean pdfToImageConvertStatus = pdfToImageConversion.convert();

                if (pdfToImageConvertStatus) {
                    String tempFile = model.getTempFolder() + tempModelForImgToPDFConversion.getNameOfDestinationFile();
                    System.err.println(tempFile);
                    //Delete files from TempFolder.
                    File file = new File(tempFile);
                    file.delete();
                }
            }
            return true;

        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(ImageToSVGConversion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private boolean isTIFFImage() {

        boolean status = false;
        try {
            if (model.getConversionType().toUpperCase().startsWith(Constants.TIFF)) {

                String nameDestFileWithOutExtension = FileUtils.getNameWithoutExtension(new File(model.getNameOfDestinationFile()));

                //Firstly, make clone of ConfigInfoModel to convert tiff to jpeg conversion
                ConfigInfoModel tempModelForTIFFToJPGConversion = (ConfigInfoModel) model.clone();

                tempModelForTIFFToJPGConversion.setPathOfDestinationFolder(model.getTempFolder());
                tempModelForTIFFToJPGConversion.setNameOfDestinationFile(nameDestFileWithOutExtension + ".jpeg");
                tempModelForTIFFToJPGConversion.setConversionType(Constants.TIFF_TO_JPG);

                boolean convert = new ImageToImageConversion(tempModelForTIFFToJPGConversion).convert();

                if (convert) {
                    status = doConversion();
                }
            } else {
                status = doConversion();
            }
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(ImageToSVGConversion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }
}
