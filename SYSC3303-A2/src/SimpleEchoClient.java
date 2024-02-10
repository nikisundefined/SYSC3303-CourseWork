import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;



public class SimpleEchoClient {

   DatagramPacket sendPacket, receivePacket;
   DatagramSocket sendReceiveSocket;

   public SimpleEchoClient()
   {
      try {
         // Construct a datagram socket and bind it to any available 
         // port on the local host machine. This socket will be used to
         // send and receive UDP Datagram packets.
         sendReceiveSocket = new DatagramSocket();
      } catch (SocketException se) {   // Can't create the socket.
         se.printStackTrace();
         System.exit(1);
      }
   }

   public void sendAndReceive(byte[] msg)
   {
      String str = new String(msg, StandardCharsets.UTF_8);
      System.out.println("Client: sending a packet containing:\n" + str);

      try {
         sendPacket = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(), 5000);
      } catch (UnknownHostException e) {
         e.printStackTrace();
         System.exit(1);
      }

      System.out.println("Client: Sending packet:");
      System.out.println("To host: " + sendPacket.getAddress());
      System.out.println("Destination host port: " + sendPacket.getPort());
      int len = sendPacket.getLength();
      System.out.println("Length: " + len);
      System.out.print("Containing: ");
      System.out.println(new String(sendPacket.getData(),0,len)); // or could print "s"

      // Send the datagram packet to the server via the send/receive socket. 

      try {
         sendReceiveSocket.send(sendPacket);
      } catch (IOException e) {
         e.printStackTrace();
         System.exit(1);
      }

      System.out.println("Client: Packet sent.\n");

      if(msg[1] == (byte)3){
         System.out.println("Client socket closed.");
         sendReceiveSocket.close();
      }

      byte data[] = new byte[100];
      receivePacket = new DatagramPacket(data, data.length);

      try {
         // Block until a datagram is received via sendReceiveSocket.  
         sendReceiveSocket.receive(receivePacket);
      } catch(IOException e) {
         e.printStackTrace();
         System.exit(1);
      }

      // Process the received datagram.
      System.out.println("Client: Packet received:");
      System.out.println("From host: " + receivePacket.getAddress());
      System.out.println("Host port: " + receivePacket.getPort());
      len = receivePacket.getLength();
      System.out.println("Length: " + len);
      System.out.print("Containing: ");

      // Form a String from the byte array.
      String received = new String(data,0,len);   
      System.out.println(received);
   }

   public static byte[] createServerRequest(byte rw, String filename, String mode){
      byte[] read = new byte[]{0,rw};
      byte[] z = new byte[]{0};
      byte[] message = new byte[read.length + z.length + filename.getBytes().length + mode.getBytes().length + z.length];
      System.arraycopy(read,0,message,0,read.length);
      System.arraycopy(filename.getBytes(),0,message,read.length,filename.getBytes().length);
      System.arraycopy(z,0,message,read.length + filename.getBytes().length,z.length);
      System.arraycopy(mode.getBytes(),0,message,read.length + filename.getBytes().length + z.length,mode.getBytes().length);
      System.arraycopy(z,0,message,read.length + filename.getBytes().length + z.length +mode.getBytes().length,z.length);
      return message;
   }

   public static void main(String args[])
   {
      SimpleEchoClient c = new SimpleEchoClient();
      for(int i=0; i<11; i++){
         if(i%2 > 0){
            c.sendAndReceive(createServerRequest((byte)1,"filename.txt","read")); // read
         }
         else {
            c.sendAndReceive(createServerRequest((byte)2,"filename.txt","write")); // write
         }
      }
      c.sendAndReceive(createServerRequest((byte)3,"shutdown","off")); // invalid
   }
}
