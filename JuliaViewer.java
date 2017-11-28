import javax.swing.JFrame;
import java.awt.Toolkit;
public class JuliaViewer
{
    public static void main(String[] args) throws Exception
    {
		double real = Double.parseDouble(args[0]);
		double imag = Double.parseDouble(args[1]);

        JFrame frame = new JFrame("Mandelbrot");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JuliaGenerator jg = new JuliaGenerator(new Complex(real, imag));
        frame.add(jg);
        jg.setSize(frame.getContentPane().getSize());
        new Thread(jg).start();
    }
}
