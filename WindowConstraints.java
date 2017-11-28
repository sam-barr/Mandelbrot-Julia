public class WindowConstraints
{
    protected double xMin, xMax, yMin, yMax;
    
    /**
     * Creates a WindowConstraints object with a given xMax, xMax, yMin, yMax
     * @param double xMin
     * @param double xMax
     * @param double yMin
     * @param double yMax
     */
    public WindowConstraints(double xMin, double xMax, double yMin, double yMax)
    {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }
}
