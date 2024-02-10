// SimpleEchoServer.java
// This class is the server side of a simple echo server based on
// UDP/IP. The server receives from a client a packet containing a character
// string, then echoes the string back to the client.
// Last edited January 9th, 2016

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Server {

   DatagramPacket sendPacket, receivePacket;
   DatagramSocket sendSocket, receiveSocket;

   public static String byteToHex(byte[] b){
      final StringBuilder builder = new StringBuilder();
      for(byte e : b){
         builder.append(String.format("%02x ",e));
      }
      return builder.toString();
   }

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

   public Server()
   {
      try {
         sendSocket = new DatagramSocket();
         receiveSocket = new DatagramSocket(69);
         
         // to test socket timeout (2 seconds)
         //receiveSocket.setSoTimeout(2000);
      } catch (SocketException se) {
         se.printStackTrace();
         System.exit(1);
      } 
   }

   public void receiveAndEcho()
   {
      // Construct a DatagramPacket for receiving packets up 
      // to 100 bytes long (the length of the byte array).
      while(true) {
         byte data[] = new byte[20];
         receivePacket = new DatagramPacket(data, data.length);
         System.out.println("SERVER> Waiting for Packet.\n");

         // Block until a datagram packet is received from receiveSocket.
         try {
            System.out.println("Waiting..."+"\n"); // so we know we're waiting
            receiveSocket.receive(receivePacket);
         } catch (IOException e) {
            System.out.print("IO Exception: likely:");
            System.out.println("Receive Socket Timed Out.\n" + e);
            e.printStackTrace();
            System.exit(1);
         }

         // Process the received datagram.
         System.out.println("SERVER> Packet received");

         String s = new String(makeReadable(data), StandardCharsets.UTF_8); // data packet as a string
         String pattern = "0[12].*?0.*?0"; // pattern template to make sure the packet is valid

         try{
            if(!(s.matches(pattern))){
               sendSocket.close();
               receiveSocket.close();
               throw new IllegalArgumentException("Invalid Packet Received");
            }
         } catch(IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
         }

         System.out.println("Host: " + receivePacket.getAddress());
         System.out.println("Port: " + receivePacket.getPort());
         System.out.println("Length: " + receivePacket.getLength());
         System.out.println("Content String: " + s);
         System.out.println("Content Bytes: " + byteToHex(data)+"\n");

         // Slow things down (wait 5 seconds)
         try {
            Thread.sleep(500);
         } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
         }

         byte[] returnMsg = {0, 0, 0, 0};
         if(data[1]==(byte)1) {
            returnMsg[1] = 3;
            returnMsg[3] = 1;
         } else {
            returnMsg[1] = 4;
         }

         try{
            sendPacket = new DatagramPacket(returnMsg, returnMsg.length,
                    InetAddress.getLocalHost(), 23);
         } catch(UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
         }

         String s2 = new String(makeReadable(returnMsg), StandardCharsets.UTF_8); // data packet as a string
         System.out.println("SERVER> Sending packet");
         System.out.println("Host: " + sendPacket.getAddress());
         System.out.println("Port: " + sendPacket.getPort());
         System.out.println("Length: " + sendPacket.getLength());
         System.out.println("Content String: " + s2);
         System.out.println("Content Bytes: " + byteToHex(returnMsg)+"\n");

         try {
            sendSocket.send(sendPacket);
         } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
         }
         System.out.println("SERVER> Packet sent\n");
      }

   }

   public static void main( String args[] )
   {
      Server c = new Server();
      c.receiveAndEcho();
   }
}

