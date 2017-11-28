import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
public class JuliaMaker
{
    public static void main(String[] args) throws Exception
    {
        double realS, realE, realI;
		double imagS, imagE, imagI;

		realS = Double.parseDouble(args[0]);
		realE = Double.parseDouble(args[1]);
		realI = Double.parseDouble(args[2]);
		
		imagS = Double.parseDouble(args[3]);
		imagE = Double.parseDouble(args[4]);
		imagI = Double.parseDouble(args[5]);

		String writerNames[] = ImageIO.getWriterFormatNames();
		for(int i = 0; i < writerNames.length; i++){
			System.out.println(writerNames[i]);
		}

		JuliaGenerator jg;
		Complex c;
		File output;
		BufferedImage bi;

		for(double real = realS; real <= realE; real += realI){
			for(double imag = imagS; imag <= imagE; imag += imagI){
				c = new Complex(real, imag);
				System.out.println(c.toString());
				output = new File("output\\" + c.toString() + ".bmp");
				jg = new JuliaGenerator(c);
				bi = jg.makeImage(1500);
				ImageIO.write(bi, "bmp", output);
				System.out.println("done");
			}
		}

		for(double a = .2; a <= .28; a += 0.0005){
			c = new Complex(a, 2*a);
			output = new File("output\\" + c.toString() + ".bmp");
			jg = new JuliaGenerator(c);
			bi = jg.makeImage(1500);
			ImageIO.write(bi, "bmp", output);
			System.out.println("done");
		}

	}
}
