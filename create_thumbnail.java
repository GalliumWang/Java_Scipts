import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

public class create_thumbnail{
    private static final float MAX_WIDTH = 100;
    private static final float MAX_HEIGHT = 100;
    private static final String JPG_TYPE = (String) "jpg";
    private static final String JPG_MIME = (String) "image/jpeg";
    private static final String PNG_TYPE = (String) "png";
    private static final String PNG_MIME = (String) "image/png";

    public static void main(String[] args) {
        String imgPath=args[0];
        resize_image(imgPath);
        
    }


    private static boolean resize_image(String imgPath) {
        try {
            // // Infer the image type.
            // Matcher matcher = Pattern.compile(".*\\.([^\\.]*)").matcher(imgPath);
            // if (!matcher.matches()) {
            //     System.out.println("Unable to infer image type for file path "
            //             + imgPath);
            //     return false;
            // }
            // String imageType = matcher.group(1);

            String[] filePathInfo=imgPath.split(".");
            //check str nums and return related info

            String imageType=filePathInfo[2];
            

            if (!(JPG_TYPE.equals(imageType)) && !(PNG_TYPE.equals(imageType))) {
                System.out.println("Skipping non-image " + imgPath);
                return false;
            }

            // Read the source image
            BufferedImage srcImage = ImageIO.read(new File(imgPath));
            int srcHeight = srcImage.getHeight();
            int srcWidth = srcImage.getWidth();

            if(Math.max(srcHeight, srcWidth)<=100){
                System.out.println("no need for resize");
                return false;
            }

            // Infer the scaling factor to avoid stretching the image
            // unnaturally
            float scalingFactor = Math.min(MAX_WIDTH / srcWidth, MAX_HEIGHT
                    / srcHeight);
            int width = (int) (scalingFactor * srcWidth);
            int height = (int) (scalingFactor * srcHeight);

            BufferedImage resizedImage = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);    //blank new image

            Graphics2D g = resizedImage.createGraphics();
            // Fill with white before applying semi-transparent (alpha) images
            g.setPaint(Color.white);
            g.fillRect(0, 0, width, height);

            // Simple bilinear resize
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);   //  TODO:can be contorled by CLI command
            g.drawImage(srcImage, 0, 0, width, height, null);
            g.dispose();

            // // Re-encode image to target format
            // ByteArrayOutputStream os = new ByteArrayOutputStream();
            // ImageIO.write(resizedImage, imageType, os);
            // InputStream is = new ByteArrayInputStream(os.toByteArray());

            // save img to folder
            File finalImg=new File(filePathInfo[0]+"_resized"+"."+filePathInfo[2]);
            ImageIO.write(resizedImage, filePathInfo[2], finalImg);
            return true;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}