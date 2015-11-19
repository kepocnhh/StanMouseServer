package stan.mouse;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server
{
    static final int serverPort = 9876;
    static int mouseX = 0;
    static int mouseY = 0;
    static Cursor cursor;
    static List<Integer> xTemp;
    static List<Integer> yTemp;
    static int tempListSize = 6;
    static int differenceSize = 25;
    static int summ = 0;
    
    public static void main(String[] args) throws SocketException, IOException, AWTException, InterruptedException
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int widthScreen = (int)screenSize.getWidth();
        int heightScreen = (int)screenSize.getHeight();
        xTemp = new ArrayList<>();
        yTemp = new ArrayList<>();
            for(int i=0; i<tempListSize-1; i++)
            {
                xTemp.add(0);
                yTemp.add(0);
            }
        System.out.println("width: " + widthScreen + "\t" + "height: " + heightScreen);
        cursor = new Cursor(0, 500, widthScreen, heightScreen);
        DatagramSocket serverSocket = new DatagramSocket(serverPort);
        while(true)
        {
            byte[] receiveData = new byte[50];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String( receivePacket.getData());
            String[] OrientationData = sentence.split("/");
            float x = Float.parseFloat(OrientationData[0]);
            float y = Float.parseFloat(OrientationData[1]);
            y *= -1;
            if(x<0 || y<0)
            {
                
                continue;
            }
            
            int xForMouse = (int)x;
            int yForMouse = (int)y;
                xTemp.add((int)x);
                yTemp.add((int)y);
            for(int i=0; i<xTemp.size(); i++)
            {
                xForMouse += xTemp.get(i);
                yForMouse += yTemp.get(i);
            }
            xTemp.remove(0);
            yTemp.remove(0);
            xForMouse /= tempListSize;
            yForMouse /= tempListSize;
            
            boolean flag = false;
            if(checkX(xForMouse))
            {
                flag = true;
                mouseX = xForMouse;
                System.out.println("new x mouse: " + mouseX);
            }
            if(checkY(yForMouse))
            {
                flag = true;
                mouseY = yForMouse;
                System.out.println("new y mouse: " + mouseY);
            }
            if(flag)
            {
                cursor.move();
            }
        }
    }
    
    static int checkCursorMouseCoordinate(int cx, int mx)
    {
            int difference = Math.abs( cx - mx );
            if(cx > mx + differenceSize)
            {
                return ((difference/differenceSize)+1);
            }
            else if(cx < mx - differenceSize)
            {
                return -((difference/differenceSize)+1);
            }
            return 0;
    }
    static boolean checkX(int x)
    {
        int difference = checkCursorMouseCoordinate(x, mouseX);
        if(difference > 0)
        {
            if(!cursor.checkMaxX())
            {
                cursor.changeX(5 * difference);
                return true;
            }
        }
        else if(difference < 0)
        {
            if(!cursor.checkMinX())
            {
                cursor.changeX(5 * difference);
                return true;
            }
        }
        return false;
    }
    static boolean checkY(int y)
    {
        int difference = checkCursorMouseCoordinate(y, mouseY);
        if(difference > 0)
        {
            if(!cursor.checkMaxY())
            {
                cursor.changeY(5 * difference);
                return true;
            }
        }
        else if(difference < 0)
        {
            if(!cursor.checkMinY())
            {
                cursor.changeY(5 * difference);
                return true;
            }
        }
        return false;
    }
}