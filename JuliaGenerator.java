import javax.swing.SwingWorker;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Stack;
public class JuliaGenerator extends JComponent implements Runnable
{
    public static final Color[] rainbow;
    private Complex complex;
    private Stack<WindowConstraints> windowStack;
    private Stack<BufferedImage> imageStack;
    private int boxX, boxY, boxW;
    private boolean boxing;

    private static int ic = 8;

    static
    {
        rainbow = new Color[ic * ic * ic + 1];
        int iterator = 0;
        outer:
        for(int i = 0; i <= ic; i++)
        {
            for(int j = 0; j <= ic; j++)
            {
                for(int k = 0; k <= ic; k++)
                {
                    int r = 255 * i / ic;
                    int g = 255 * j / ic;
                    int b = 255 * k / ic;
                    rainbow[iterator] = new Color(r, g, b);
                    iterator++;
                    if(iterator == ic * ic * ic + 1) break outer;
                }
            }
        }
    }

    public JuliaGenerator(Complex complex)
    {
        this.complex = complex;
        windowStack = new Stack<WindowConstraints>();
        windowStack.push(new WindowConstraints(-2, 2, -1, 1));
        imageStack = new Stack<BufferedImage>();

        MouseAdapter ma = new MouseAdapter()
            {
                public void mousePressed(MouseEvent e)
                {
                    if(SwingUtilities.isLeftMouseButton(e))
                    {
                        if(windowStack.size() == 0 || imageStack.size() == 0) return;
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
                        new Thread(JuliaGenerator.this).start();
                    }
                }
            };
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    public void paintComponent(Graphics g)
    {
        if(imageStack.size() == 0) return;
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(imageStack.peek(), 0, 0, null);
        if(boxing) g2.drawRect(boxX, boxY, (boxW - boxX), (boxW - boxX) * getHeight() / getWidth());
    }

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
                int n = complex.julia(c, ic * ic * ic);
                bi.setRGB(i, j, n * MandelbrotGenerator.COLOR_CONSTANT);
            }
        }
        imageStack.push(bi);
        repaint();
    }
}