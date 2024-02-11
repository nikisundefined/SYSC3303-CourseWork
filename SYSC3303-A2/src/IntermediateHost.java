// Written by Nicholas Nemec - 101211060
// Inspiration taken from the example code
// posted on the class brightspace

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

    // this method is used to convert a byte array to a nice printable string of Hex values.
    public static String byteToHex(byte[] b){
        final StringBuilder builder = new StringBuilder();
        for(byte e : b){
            builder.append(String.format("%02x ",e));
        }
        return builder.toString();
    }

    public IntermediateHost(){
        try {
            sendSocket = new DatagramSocket(); // open the sendSocket
            receiveSocket = new DatagramSocket(23); // open the receiveSocket to port 23
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    public void receiveAndPass(){
        while(true){
            byte[] data = new byte[20];

            int clientPort; // initialize a variable to contain the port of the
                            // Client class so that it can send packets back to it
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

            clientPort = receivePacket.getPort(); // register the port tied to the Client

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
                sendPacket = new DatagramPacket(data, data.length, // attempt to send a packet to the server at port 69
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
            receivePacket = new DatagramPacket(serverData, serverData.length); // initialize the packet that will be received from the server
            if(data[1] == (byte)3){ // if the packet is the invalid packet close the sockets and exit the program.
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
                sendPacket = new DatagramPacket(serverData, receivePacket.getLength(), // attempt to send the packet received from the server to the client
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
                sendSocket.send(sendPacket); // send off the packet to the client
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
