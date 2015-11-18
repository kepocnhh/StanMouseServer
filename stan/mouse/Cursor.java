package stan.mouse;

import java.awt.AWTException;
import java.awt.Robot;

public class Cursor
{
    private int cursorX = 0;
    private int cursorY = 0;
    private double maxX = 0;
    private double maxY = 0;
    
    private Robot robot;
    
    public Cursor(int x, int y, int mx, int my) throws AWTException
    {
        cursorX = x;
        cursorY = y;
        maxX = mx;
        maxY = my;
        robot = new Robot();
    }
    public boolean checkMaxX()
    {
        return cursorX == maxX;
    }
    public boolean checkMinX()
    {
        return cursorX == 0;
    }
    
    public void changeX(int x)
    {
        cursorX += x;
    }
    public void move()
    {
        robot.mouseMove(cursorX, cursorY);
    }
    
}