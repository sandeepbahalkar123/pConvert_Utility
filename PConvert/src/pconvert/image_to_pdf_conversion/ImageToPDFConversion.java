/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pconvert.image_to_pdf_conversion;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import pconvert.ConfigInfoModel;
import pconvert.Constants;
import pconvert.IConversion;
import pconvert.Utility;

/**
 *
 * @author riteshpandhurkar
 */
public class ImageToPDFConversion implements Callable<Boolean>, IConversion {

    private final ConfigInfoModel model;

    public ImageToPDFConversion(ConfigInfoModel model) {
        this.model = model;
    }

    @Override
    public Boolean call() throws Exception {
        return convert();
    }

    @Override
    public boolean convert() {
        //this is done to get conversion type and set as a extension to file
        String sourceFileExtension = Utility.getFileExtension(new File(model.getNameOfSourceFile()));
        if (sourceFileExtension.toUpperCase().endsWith(Constants.TIFF)) {
            return tiffToPDFConversion();
        } else {
            return imageToPDFConversion();
        }
    }

    private boolean imageToPDFConversion() {
        boolean status = false;
        try {
            // Create a new empty document
            String pathOfSourceFile = model.getPathOfSourceFolder() + model.getNameOfSourceFile();
            String pathOfDestFile = model.getPathOfDestinationFolder() + model.getNameOfDestinationFile();
            BufferedImage awtImage = ImageIO.read(new File(pathOfSourceFile));

            int width = awtImage.getWidth();
            int height = awtImage.getHeight();

            PDDocument document = new PDDocument();

            PDPage pDPage = new PDPage(new PDRectangle(width, height));

            //Creating PDImageXObject object
            PDImageXObject pdImage = PDImageXObject.createFromFile(pathOfSourceFile, document);

            //creating the PDPageContentStream object
            PDPageContentStream contents = new PDPageContentStream(document, pDPage);

            //Drawing the image in the PDF document
            //contents.drawImage(pdImage, 20, 30);
            contents.drawImage(pdImage, 0, 0);

            document.addPage(pDPage);

            //Closing the PDPageContentStream object
            contents.close();

            //Saving the document
            document.save(pathOfDestFile);

            //Closing the document
            document.close();
            status = true;
        } catch (Exception ex) {
            Logger.getLogger(ImageToPDFConversion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    private boolean tiffToPDFConversion() {

        boolean status = false;
        try {
            String pathOfSourceFile = model.getPathOfSourceFolder() + model.getNameOfSourceFile();
            String pathOfDestFile = model.getPathOfDestinationFolder() + model.getNameOfDestinationFile();
            BufferedImage awtImage = ImageIO.read(new File(pathOfSourceFile));

            int width = awtImage.getWidth();
            int height = awtImage.getHeight();
            PDDocument doc = new PDDocument();
            PDPage pDPage = new PDPage(new PDRectangle(width, height));

            PDImageXObject pdImageXObject = LosslessFactory.createFromImage(doc, awtImage);
            PDPageContentStream contentStream = new PDPageContentStream(doc, pDPage);
            contentStream.drawImage(pdImageXObject, 0, 0);
            doc.addPage(pDPage);
            contentStream.close();
            doc.save(pathOfDestFile);
            doc.close();
            status = true;
        } catch (IOException ex) {
            Logger.getLogger(ImageToPDFConversion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ImageToPDFConversion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

}
