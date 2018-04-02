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
import pconvert.IConversion;

/**
 * @author riteshpandhurkar
 *
 * Used Callable to get callback on thread completion. Image to image
 * conversion, supports only JPG,TIFF,PNG,GIF,BMP
 */
public class ImageToImageConversion implements Callable<Boolean>, IConversion {

    private final ConfigInfoModel model;

    public ImageToImageConversion(ConfigInfoModel model) {
        this.model = model;
    }

    @Override
    public boolean convert() {
        try {

            // find dest image type
            ImageType destImageType = ImageType.JPG;
            if (model.getConversionType().toUpperCase().endsWith(Constants.BMP)) {
                destImageType = ImageType.BMP;
            } else if (model.getConversionType().toUpperCase().endsWith(Constants.GIF)) {
                destImageType = ImageType.GIF;
            } else if (model.getConversionType().toUpperCase().endsWith(Constants.PNG)) {
                destImageType = ImageType.PNG;
            } else if (model.getConversionType().toUpperCase().endsWith(Constants.TIFF)) {
                destImageType = ImageType.TIFF;
            } else if (model.getConversionType().toUpperCase().endsWith(Constants.JPG) || model.getConversionType().toLowerCase().endsWith("jpeg")) {
                destImageType = ImageType.JPG;
            }

            BufferedImage img = ImageIO.read(model.getPathOfSourceFolder() + model.getNameOfSourceFile());
            FileOutputStream fo = new FileOutputStream(model.getPathOfDestinationFolder() + model.getNameOfDestinationFile());

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
                    jpegOptions.setColorSpace(JPEGOptions.COLOR_SPACE_RGB);
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

    @Override
    public Boolean call() throws Exception {
        return convert();
    }
}
