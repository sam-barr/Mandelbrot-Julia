import javax.swing.JFrame;
import java.awt.Toolkit;
public class MandelbrotViewer
{
    public static void main(String[] args) throws Exception
    {
        JFrame frame = new JFrame("Mandelbrot");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        MandelbrotGenerator mg = new MandelbrotGenerator();
        frame.add(mg);
        mg.setSize(frame.getContentPane().getSize());
        new Thread(mg).start();
    }
}