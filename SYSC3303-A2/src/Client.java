// Written by Nicholas Nemec - 101211060
// Inspiration taken from the example code
// posted on the class brightspace

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Client {

   DatagramPacket sendPacket, receivePacket;
   DatagramSocket sendReceiveSocket;

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
         builder.append(String.format("%02x ",e)); // designate the builder format
      }
      return builder.toString();
   }

   public Client()
   {
      try {
         sendReceiveSocket = new DatagramSocket(); // Initialize the Send/Receive Socket
      } catch (SocketException se) {   // Can't create the socket.
         se.printStackTrace();
         System.exit(1);
      }
   }

   public void sendAndReceive(byte[] msg)
   {

      try {
         sendPacket = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(), 23); // create the packet to be sent to the intermediate
      } catch (UnknownHostException e) {
         e.printStackTrace();
         System.exit(1);
      }

      System.out.println("CLIENT> Sending packet");
      System.out.println("Host: " + sendPacket.getAddress());
      System.out.println("Port: " + sendPacket.getPort());
      System.out.println("Length: " + sendPacket.getLength());
      String s = new String(makeReadable(msg), StandardCharsets.UTF_8); // data packet as a string
      System.out.println("Content String: " + s); // print string contents of the packet
      System.out.println("Content Bytes: "+ byteToHex(msg)+"\n"); // print Byte contents of the packet

      try {
         sendReceiveSocket.send(sendPacket); // send the sendPacket through the sendReceiveSocket
      } catch (IOException e) {
         e.printStackTrace();
         System.exit(1);
      }

      System.out.println("CLIENT> Packet sent.\n");
      if(msg[1] == (byte)3){ // if the Invalid packet is sent
         System.out.println("Client socket closed.");
         sendReceiveSocket.close(); // close the sendReceiveSocket
         System.exit(1); // exit program
      }
      byte data[] = new byte[4];
      receivePacket = new DatagramPacket(data, data.length); // create the shell packet for the response

      try {
         System.out.println("Waiting...\n");
         sendReceiveSocket.receive(receivePacket); // receive the response from the Server from the Intermediate
      } catch(IOException e) {
         e.printStackTrace();
         System.exit(1);
      }

      // Process the received datagram.
      System.out.println("CLIENT> Packet received");
      System.out.println("Host: " + receivePacket.getAddress());
      System.out.println("Port: " + receivePacket.getPort());
      System.out.println("Length: " + receivePacket.getLength());

      String s2 = new String(makeReadable(data), StandardCharsets.UTF_8);
      System.out.println("Content String: " + s2);
      System.out.println("Content Bytes: "+ byteToHex(data)+"\n");
   }

   // this method creates the byte array that will be sent in the packet and has 3 parameters
   // that describe the contents, it is then
   public static byte[] createServerRequest(byte rw, String filename, String mode){
      byte[] read = new byte[]{0,rw};
      byte[] z = new byte[]{0};
      byte[] message = new byte[read.length + z.length + filename.getBytes().length + mode.getBytes().length + z.length];
      // this block of code sticks all the parts of the packet contents together
      System.arraycopy(read,0,message,0,read.length);
      System.arraycopy(filename.getBytes(),0,message,read.length,filename.getBytes().length);
      System.arraycopy(z,0,message,read.length + filename.getBytes().length,z.length);
      System.arraycopy(mode.getBytes(),0,message,read.length + filename.getBytes().length + z.length,mode.getBytes().length);
      System.arraycopy(z,0,message,read.length + filename.getBytes().length + z.length +mode.getBytes().length,z.length);
      return message;
   }

   public static void main(String args[])
   {
      Client c = new Client();
      // this loop sends 11 total messages alternating between read/write by taking the modulus
      // of the index and then terminating in a invalid message denoted by the code 03.
      for(int i=0; i<11; i++){
         if(i%2 > 0){
            c.sendAndReceive(createServerRequest((byte)1,"filename.txt","mode")); // read
         }
         else {
            c.sendAndReceive(createServerRequest((byte)2,"filename.txt","mode")); // write
         }
      }
      c.sendAndReceive(createServerRequest((byte)3,"filename.txt","mode")); // invalid
   }
}
