import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class IntermediateHost {

    DatagramPacket sendPacket, receivePacket;
    DatagramSocket sendSocket, receiveSocket;

    public static byte[] makeReadable(byte[] b){
        byte[] printable = new byte[b.length];
        // this loop will make it so that the data sent in the packet is
        // printable in the UTF-8 format as characters from 0-9 do not
        // show unless shifted up by 48 to display their ascii equivalents
        for(int i = 0;i<b.length;i++){
            if(b[i]<10) {
                printable[i] = (byte) (b[i] + 48);
            } else {
                printable[i] = b[i];
            }
        }
        return printable;
    }

    public static String byteToHex(byte[] b){
        final StringBuilder builder = new StringBuilder();
        for(byte e : b){
            builder.append(String.format("%02x ",e));
        }
        return builder.toString();
    }

    public IntermediateHost(){
        try {
            sendSocket = new DatagramSocket();
            receiveSocket = new DatagramSocket(23);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    public void receiveAndPass(){
        while(true){
            byte[] data = new byte[20];

            int clientPort;
            InetAddress clientAd;

            receivePacket = new DatagramPacket(data, data.length);

            System.out.println("INTERMEDIATE HOST> Waiting for Packet.\n");

            try {
                System.out.println("Waiting...\n"); // so we know we're waiting
                receiveSocket.receive(receivePacket);
            } catch (IOException e) {
                System.out.println("IO Exception: likely:");
                System.out.println("Receive Socket Timed Out.\n" + e);
                e.printStackTrace();
                System.exit(1);
            }

            clientPort = receivePacket.getPort();

            System.out.println("INTERMEDIATE HOST> Packet received");

            String s = new String(makeReadable(data), StandardCharsets.UTF_8); // data packet as a string
            System.out.println("Host: " + receivePacket.getAddress());
            System.out.println("Port: " + receivePacket.getPort());
            System.out.println("Length: " + receivePacket.getLength());
            System.out.println("Content String: " + s);
            System.out.println("Content Bytes: "+ byteToHex(data)+"\n");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }

            try{
                sendPacket = new DatagramPacket(data, data.length,
                        InetAddress.getLocalHost(), 69);
            } catch(UnknownHostException e) {
                e.printStackTrace();
                System.exit(1);
            }

            System.out.println("INTERMEDIATE HOST> Sending packet");
            System.out.println("Host: " + sendPacket.getAddress());
            System.out.println("Port: " + sendPacket.getPort());
            System.out.println("Length: " + sendPacket.getLength());
            System.out.println("Content String: " + s);
            System.out.println("Content Bytes: "+ byteToHex(data)+"\n");

            try {
                sendSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            System.out.println("INTERMEDIATE HOST> packet sent\n");

            byte serverData[] = new byte[4];
            receivePacket = new DatagramPacket(serverData, serverData.length);
            if(data[1] == (byte)3){
                System.out.println("Intermediate Host sockets closed.");
                receiveSocket.close();
                sendSocket.close();
                System.exit(1);
            }

            System.out.println("INTERMEDIATE HOST> Waiting for Packet.\n");
            // Block until a datagram packet is received from receiveSocket.
            try {
                System.out.println("Waiting...\n"); // so we know we're waiting
                receiveSocket.receive(receivePacket);
            } catch (IOException e) {
                System.out.print("IO Exception: likely:");
                System.out.println("Receive Socket Timed Out.\n" + e);
                e.printStackTrace();
                System.exit(1);
            }
            System.out.println("INTERMEDIATE HOST> Packet received");

            String s2 = new String(makeReadable(serverData), StandardCharsets.UTF_8);

            System.out.println("Host: " + receivePacket.getAddress());
            System.out.println("Port: " + receivePacket.getPort());
            System.out.println("Length: " + receivePacket.getLength());
            System.out.println("Content String: " + s2);
            System.out.println("Content Bytes: "+ byteToHex(serverData)+"\n");


            // Slow things down (wait 5 seconds)
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }

            try{
                sendPacket = new DatagramPacket(serverData, receivePacket.getLength(),
                        InetAddress.getLocalHost(), clientPort);
            } catch(UnknownHostException e) {
                e.printStackTrace();
                System.exit(1);
            }

            System.out.println("INTERMEDIATE HOST> Sending packet");
            System.out.println("Host: " + sendPacket.getAddress());
            System.out.println("Port: " + clientPort);
            System.out.println("Length: " + sendPacket.getLength());
            System.out.println("Content String: " + s2);
            System.out.println("Content Bytes: "+ byteToHex(serverData)+"\n");

            try {
                sendSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            System.out.println("INTERMEDIATE HOST> Packet sent.\n");
        }
    }
    public static void main( String args[] )
    {
        IntermediateHost c = new IntermediateHost();
        c.receiveAndPass();
    }
}
