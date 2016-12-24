import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
public class MandelbrotMaker
{
    public static void main(String[] args) throws Exception
    {
        File output = new File("mandelbrot.png");
        BufferedImage bi = MandelbrotGenerator.makeImage(15000);
        ImageIO.write(bi, "png", output);
        System.out.println("done");
    }
}