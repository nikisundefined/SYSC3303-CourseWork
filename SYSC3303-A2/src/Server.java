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
         builder.append(String.format("%02x:",e));
      }
      return builder.toString();
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
         System.out.println("Server: Waiting for Packet.\n");

         // Block until a datagram packet is received from receiveSocket.
         try {
            System.out.println("Waiting..."); // so we know we're waiting
            receiveSocket.receive(receivePacket);
         } catch (IOException e) {
            System.out.print("IO Exception: likely:");
            System.out.println("Receive Socket Timed Out.\n" + e);
            e.printStackTrace();
            System.exit(1);
         }

         // Process the received datagram.
         System.out.println("Server: Packet received:");

         int len = receivePacket.getLength();

         byte[] printable = new byte[data.length];

         for(int i = 0;i<data.length;i++){ // this loop will make it so that the data sent in the packet is printable in the UTF-8 format as characters from 0-9 do not show unless shifted up by 48 to display their ascii equivalents
            if(data[i]<10) {
               printable[i] = (byte) (data[i] + 48);
            } else {
               printable[i] = data[i];
            }
         }

         String s = new String(printable, StandardCharsets.UTF_8); // data packet as a string
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

         System.out.println("String: " + s);
         System.out.println("Bytes: " + byteToHex(data));

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

         System.out.println("Server: Sending packet:");
         System.out.println("To host: " + sendPacket.getAddress());
         System.out.println("Destination host port: " + sendPacket.getPort());
         len = sendPacket.getLength();
         System.out.println("Length: " + len);
         // or (as we should be sending back the same thing)
         // System.out.println(received);

         // Send the datagram packet to the client via the send socket.
         try {
            sendSocket.send(sendPacket);
         } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
         }
         byte[] printable2 = new byte[returnMsg.length];

         for(int i = 0;i<returnMsg.length;i++){ // this loop will make it so that the data sent in the packet is printable in the UTF-8 format as characters from 0-9 do not show unless shifted up by 48 to display their ascii equivalents
            if(returnMsg[i]<10) {
               printable2[i] = (byte) (returnMsg[i] + 48);
            } else {
               printable2[i] = returnMsg[i];
            }
         }

         String s2 = new String(printable2, StandardCharsets.UTF_8); // data packet as a string
         System.out.println("Server: packet sent");
         System.out.println("String: " + s2);
         System.out.println("Bytes: " + byteToHex(returnMsg));
      }

   }

   public static void main( String args[] )
   {
      Server c = new Server();
      c.receiveAndEcho();
   }
}

