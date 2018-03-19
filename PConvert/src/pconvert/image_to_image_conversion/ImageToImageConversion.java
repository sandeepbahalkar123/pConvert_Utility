/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pconvert.image_to_image_conversion;

import com.icafe4j.image.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

import com.icafe4j.image.ImageParam;
import com.icafe4j.image.ImageType;
import com.icafe4j.image.options.BMPOptions;
import com.icafe4j.image.options.GIFOptions;
import com.icafe4j.image.options.JPEGOptions;
import com.icafe4j.image.options.PNGOptions;
import com.icafe4j.image.options.TIFFOptions;
import com.icafe4j.image.png.Filter;
import com.icafe4j.image.tiff.TiffFieldEnum.PhotoMetric;
import com.icafe4j.image.tiff.TiffFieldEnum.Compression;
import com.icafe4j.image.writer.ImageWriter;
import java.util.concurrent.Callable;
import pconvert.ConfigInfoModel;
import pconvert.Constants;

/**
 *
 * @author riteshpandhurkar
 */
public class ImageToImageConversion implements Callable<Boolean> {

    private final ConfigInfoModel model;

    public ImageToImageConversion(ConfigInfoModel model) {
        this.model = model;
    }

    private boolean convert() {
        try {
            BufferedImage img = ImageIO.read(model.getPathOfSourceFolder() + model.getNameOfSourceFile());
            ImageType destImageType = getDestImageType(model.getConversionType());
            FileOutputStream fo = new FileOutputStream(model.getPathOfDestinationFolder() + destImageType.getExtension());

            ImageWriter writer = ImageIO.getWriter(destImageType);
            ImageParam.ImageParamBuilder builder = ImageParam.getBuilder();
            switch (destImageType) {
                case TIFF:// Set TIFF-specific options
                    TIFFOptions tiffOptions = new TIFFOptions();
                    tiffOptions.setApplyPredictor(true);
                    tiffOptions.setTiffCompression(Compression.JPG);
                    tiffOptions.setJPEGQuality(60);
                    tiffOptions.setPhotoMetric(PhotoMetric.SEPARATED);
                    tiffOptions.setWriteICCProfile(true);
                    builder.imageOptions(tiffOptions);
                    break;
                case PNG:
                    PNGOptions pngOptions = new PNGOptions();
                    pngOptions.setApplyAdaptiveFilter(true);
                    pngOptions.setCompressionLevel(6);
                    pngOptions.setFilterType(Filter.NONE);
                    builder.imageOptions(pngOptions);
                    break;
                case JPG:
                    JPEGOptions jpegOptions = new JPEGOptions();
                    jpegOptions.setQuality(60);
                    jpegOptions.setColorSpace(JPEGOptions.COLOR_SPACE_YCCK);
                    jpegOptions.setWriteICCProfile(true);
                    builder.imageOptions(jpegOptions);
                    break;
                case GIF:
                    GIFOptions gifOptions = new GIFOptions();
                    builder.imageOptions(gifOptions);
                    break;
                case BMP:
                    BMPOptions bmpOptions = new BMPOptions();
                    builder.imageOptions(bmpOptions);
                    break;
                default:
            }
            writer.setImageParam(builder.build());
            writer.write(img, fo);
            fo.close();
            System.out.println(Constants.CONVERSION_SUCCESS);

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private ImageType getDestImageType(String conversionType) {
        ImageType type = ImageType.JPG;
        switch (conversionType) {
            case Constants.TIFF_TO_BMP:
            case Constants.GIF_TO_BMP:
            case Constants.JPG_TO_BMP:
            case Constants.PNG_TO_BMP:
                type = ImageType.BMP;
                break;
            case Constants.TIFF_TO_GIF:
            case Constants.BMP_TO_GIF:
            case Constants.JPG_TO_GIF:
            case Constants.PNG_TO_GIF:
                type = ImageType.GIF;
                break;
            case Constants.TIFF_TO_PNG:
            case Constants.GIF_TO_PNG:
            case Constants.JPG_TO_PNG:
            case Constants.BMP_TO_PNG:
                type = ImageType.PNG;
                break;
            case Constants.TIFF_TO_JPG:
            case Constants.GIF_TO_JPG:
            case Constants.BMP_TO_JPG:
            case Constants.PNG_TO_JPG:
                type = ImageType.JPG;
                break;
            case Constants.JPG_TO_TIFF:
            case Constants.GIF_TO_TIFF:
            case Constants.BMP_TO_TIFF:
            case Constants.PNG_TO_TIFF:
                type = ImageType.TIFF;
                break;
        }
        return type;
    }

    @Override
    public Boolean call() throws Exception {
        return convert();
    }
}
