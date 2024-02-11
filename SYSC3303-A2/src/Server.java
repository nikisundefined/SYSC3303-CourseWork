// Written by Nicholas Nemec - 101211060
// Inspiration taken from the example code
// posted on the class brightspace

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Server {

   DatagramPacket sendPacket, receivePacket; // packet definitions
   DatagramSocket sendSocket, receiveSocket; // socket definitions

   // this method is used to convert a byte array to a nice printable string of Hex values.
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
         sendSocket = new DatagramSocket(); // initialize the sendSocket
         receiveSocket = new DatagramSocket(69); // initialize the receiveSocket to port 69
      } catch (SocketException se) {
         se.printStackTrace();
         System.exit(1);
      } 
   }

   public void receiveAndProcess()
   {
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
            if(!(s.matches(pattern))){ // if the contents of the packet received do not match the template previously defined
               sendSocket.close(); // close the sendSocket
               receiveSocket.close(); // close the receiveSocket
               throw new IllegalArgumentException("Invalid Packet Received"); // throw the invalid packet received message
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

         try {
            Thread.sleep(500);
         } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
         }

         byte[] returnMsg = {0, 0, 0, 0}; // initialize the empty return message
         if(data[1]==(byte)1) { // check for the code that correlates to read
            returnMsg[1] = 3;   // change the entries in the byte array to match the
            returnMsg[3] = 1;   // requested codes in the assignment outline
         } else {
            returnMsg[1] = 4;   // otherwise return the code for write
         }

         try{
            sendPacket = new DatagramPacket(returnMsg, returnMsg.length, // create the new packet containing the return
                    InetAddress.getLocalHost(), 23);                // message that we just created and designate it to port 23
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
            sendSocket.send(sendPacket); // attempt to send the new packet to the intermediate host
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
      c.receiveAndProcess();
   }
}

