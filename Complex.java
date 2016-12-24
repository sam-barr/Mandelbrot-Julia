public class Complex
{
    private double real;
    private double imaginary;
    
    /**
     * Creates a Complex number with a given Real part and imaginary part of 0
     * @param double real
     */
    public Complex(double real)
    {
        this(real, 0);
    }
    
    /**
     * Creates a Complex number with a given real part and imaginary part
     * @param double real
     * @param double imaginary
     */
    public Complex(double real, double imaginary)
    {
        this.real = real;
        this.imaginary = imaginary;
    }
    
    /**
     * Returns the sum of this Complex and a given Complex
     * @param Complex c
     * @return Complex
     */
    public Complex add(Complex c)
    {
        return new Complex(this.real + c.real, this.imaginary + c.imaginary);
    }
    
    /**
     * Returns the square of this Complex
     * @return Complex
     */
    public Complex square()
    {
        return new Complex(real * real - imaginary * imaginary, 2 * real * imaginary);
    }
    
    public int mandelbrotLoop(int maxIteration)
    {
        double x = 0;
        double y = 0;
        int i = 0;
        while(x * x + y * y < 4 && i < maxIteration)
        {
            double xTemp = x * x - y * y + real;
            y = 2 * x * y + imaginary;
            x = xTemp;
            i++;
        }
        return i;
    }
    
    public int mandelbrot(int maxIteration)
    {
        return mandelbrotRec(this, 0, maxIteration);
    }
    
    private int mandelbrotRec(Complex z, int n, int maxIteration)
    {
        if(n >= maxIteration)
        {
            return 0;
        }
        else if(z.real * z.real + z.imaginary * z.imaginary > 4)
        {
            return 1;
        }
        return 1 + mandelbrotRec(this.add(z.square()), n + 1, maxIteration);
    }
    
    public int juliaLoop(Complex z, int maxIteration)
    {
        for(int i = 0; i < maxIteration; i++)
        {
            double x = z.real * z.real - z.imaginary * z.imaginary + this.real;
            double y = 2 * z.real* z.imaginary + this.imaginary;
            z = new Complex(x, y);
            if(z.real * z.real + z.imaginary * z.imaginary > 4) return i;
        }
        return maxIteration - 1;
    }
    
    public int julia(Complex z, int maxIteration)
    {
        return juliaRec(z, 0, maxIteration);
    }
    
    public int juliaRec(Complex z, int n, int maxIteration)
    {
        if(n >= maxIteration)
        {
            return 0;
        }
        else if(z.real * z.real + z.imaginary * z.imaginary > 4)
        {
            return 1;
        }
        return 1 + juliaRec(this.add(z.square()), n + 1, maxIteration);
    }
    
    public String toString()
    {
        return real + " + " + imaginary + "i";
    }
}