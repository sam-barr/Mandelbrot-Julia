import javax.swing.SwingWorker;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Stack;
public class MandelbrotGenerator extends JComponent implements Runnable
{
    private int boxX, boxY, boxW;
    private boolean boxing;
    private Stack<WindowConstraints> windowStack;
    private Stack<BufferedImage> imageStack;

    private static int ic = 8;
    public static final int COLOR_CONSTANT = 100000000;

    /**
     * Creates a MandelbrotGenerator object
     */
    public MandelbrotGenerator()
    {
        super();
        windowStack = new Stack<WindowConstraints>();
        windowStack.push(new WindowConstraints(-2, 1, -1, 1));
        imageStack = new Stack<BufferedImage>();
        boxing = false;
        MouseAdapter ma = new MouseAdapter()
            {
                public void mousePressed(MouseEvent e)
                {
                    if(SwingUtilities.isLeftMouseButton(e))
                    {
                        if(imageStack.size() == 0 || windowStack.size() == 0) return;
                        WindowConstraints wc = windowStack.peek();
                        BufferedImage image = imageStack.peek();
                        boxX = e.getX();
                        boxY = e.getY();
                        boxing = true;
                    }
                    else if(SwingUtilities.isRightMouseButton(e))
                    {
                        if(windowStack.size() <= 1 || imageStack.size() <= 1) return;
                        windowStack.pop();
                        imageStack.pop();
                        repaint();
                    }
                }

                public void mouseClicked(MouseEvent e)
                {
                    if(!SwingUtilities.isMiddleMouseButton(e)) return;
                    if(windowStack.size() == 0 || imageStack.size() == 0) return;
                    WindowConstraints wc = windowStack.peek();
                    double real = e.getX() * (wc.xMax - wc.xMin) / (double)getWidth() + wc.xMin;
                    double imag = e.getY() * (wc.yMax - wc.yMin) / (double)getHeight() + wc.yMin;
                    Complex c = new Complex(real, imag);

                    JFrame frame = new JFrame("Julia: " + c);
                    frame.setVisible(true);
                    frame.setResizable(false);
                    frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

                    JuliaGenerator jg = new JuliaGenerator(c);
                    frame.add(jg);
                    jg.setSize(getSize());
                    new Thread(jg).start();
                }

                public void mouseDragged(MouseEvent e)
                {
                    boxW = e.getX();
                    repaint();
                }

                public void mouseReleased(MouseEvent e)
                {
                    if(SwingUtilities.isLeftMouseButton(e))
                    {
                        if(windowStack.size() == 0 || imageStack.size() == 0) return;
                        WindowConstraints wc = windowStack.peek();
                        BufferedImage image = imageStack.peek();
                        double startX = boxX * (wc.xMax - wc.xMin) / (double)getWidth() + wc.xMin;
                        double startY = boxY * (wc.yMax - wc.yMin) / (double)getHeight() + wc.yMin;
                        double x = boxW * (wc.xMax - wc.xMin) / (double)getWidth() + wc.xMin;
                        double y = startY + (x - startX) * (wc.yMax - wc.yMin) / (wc.xMax - wc.xMin);
                        if(x < startX) return;
                        windowStack.push(new WindowConstraints(startX, x, startY, y));
                        boxing = false;
                        new Thread(MandelbrotGenerator.this).start();
                    }
                }
            };
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    /**
     * Paints the component
     * @param Graphics g
     */
    public void paintComponent(Graphics g)
    {
        if(imageStack.size() == 0) return;
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(imageStack.peek(), 0, 0, null);
        if(boxing) g2.drawRect(boxX, boxY, boxW - boxX, (boxW - boxX) * getHeight() / getWidth());
    }

    /**
     * Draws the fractal image
     */
    public void run()
    {
        WindowConstraints wc = windowStack.peek();
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < getWidth(); i++)
        {
            for(int j = 0; j < getHeight(); j++)
            {
                double x = i * (wc.xMax - wc.xMin) / getWidth() + wc.xMin;
                double y = j * (wc.yMax - wc.yMin) / getHeight() + wc.yMin;
                Complex c = new Complex(x, y);
                int n = c.mandelbrot(ic * ic * ic);
                bi.setRGB(i, j, n * COLOR_CONSTANT);
            }
        }
        imageStack.push(bi);
        repaint();
    }
    
    /**
     * Returns a mandelbrot image with a given width
     * @param int xRes
     * @return BufferedImage
     */
    public static BufferedImage makeImage(int xRes)
    {
        int yRes = xRes * 2 / 3;
        BufferedImage bi = new BufferedImage(xRes, yRes, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < xRes; i++)
        {
            for(int j = 0; j < yRes; j++)
            {
                double x = i * 3. / xRes - 2;
                double y = j * 2. / yRes - 1;
                Complex c = new Complex(x, y);
                int n = c.mandelbrot(ic * ic * ic);
                bi.setRGB(i, j, n * COLOR_CONSTANT);
            }
        }
        return bi;
    }
}
