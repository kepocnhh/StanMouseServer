package stan.mouse;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server
{
    static final int serverPort = 9876;
    
    public static void main(String[] args) throws SocketException, IOException
    {
        DatagramSocket serverSocket = new DatagramSocket(serverPort);
        byte[] receiveData = new byte[11];
        while(true)
        {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String( receivePacket.getData());
            String[] OrientationData = sentence.split("/");
            float x = Float.parseFloat(OrientationData[0]);
            float y = Float.parseFloat(OrientationData[1]);
            float z = Float.parseFloat(OrientationData[2]);
            System.out.println("RECEIVED: " + x + " " + y + " " + z + " ]");
        }
    }
    
}