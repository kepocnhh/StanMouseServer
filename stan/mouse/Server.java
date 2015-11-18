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
    static int tempListSize = 4;
    static int differenceSize = 85;
    static int summ = 0;
    
    public static void main(String[] args) throws SocketException, IOException, AWTException, InterruptedException
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int widthScreen = (int)screenSize.getWidth();
        int heightScreen = (int)screenSize.getHeight();
        xTemp = new ArrayList<>();
            for(int i=0; i<tempListSize-1; i++)
            {
                xTemp.add(0);
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
//            float z = Float.parseFloat(OrientationData[2]);
//            System.out.println("RECEIVED: " + x + "\t" + y + "\t" + z + "\t]");
//            System.out.println("RECEIVED: " + x + "\t" + y + "\t]");
            if(x<0)
            {
                x+=3124*2;
            }
            int xForMouse = (int)x;
                xTemp.add((int)x);
//            if(xTemp.size()<tempListSize)
//            {
//                continue;
//            }
            for(int i=0; i<xTemp.size(); i++)
            {
                xForMouse += xTemp.get(i);
            }
            xTemp.remove(0);
//            xTemp.clear();
//            xForMouse /= tempListSize;
            
//            System.out.println("xForMouse: " + xForMouse);
//            int difference = Math.abs( Math.abs(xForMouse)-Math.abs(mouseX) );
            boolean flag = false;
            int difference = Math.abs( xForMouse - mouseX );
            if(xForMouse > mouseX + differenceSize)
            {
                if(!cursor.checkMaxX())
                {
                    cursor.changeX(5 * ((difference/differenceSize)+1));
                    flag = true;
                }
            }
            else if(xForMouse < mouseX - differenceSize)
            {
                if(!cursor.checkMinX())
                {
                    cursor.changeX(-5 * ((difference/differenceSize)+1));
                    flag = true;
                }
            }
            if(flag)
            {
                mouseX = xForMouse;
                System.out.println("new x mouse: " + mouseX);
                cursor.move();
            }
        }
    }
    
}