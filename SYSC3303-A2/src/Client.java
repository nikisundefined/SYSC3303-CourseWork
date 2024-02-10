import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;


public class Client {

   DatagramPacket sendPacket, receivePacket, receivePacket2;
   DatagramSocket sendReceiveSocket;

   public Client()
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
      byte[] printable = new byte[msg.length];

      for(int i = 0;i<msg.length;i++){ // this loop will make it so that the data sent in the packet is printable in the UTF-8 format as characters from 0-9 do not show unless shifted up by 48 to display their ascii equivalents
         if(msg[i]<10) {
            printable[i] = (byte) (msg[i] + 48);
         } else {
            printable[i] = msg[i];
         }
      }

      String s = new String(printable, StandardCharsets.UTF_8); // data packet as a string
      System.out.println("Client: sending a packet containing:\n" + s);

      try {
         sendPacket = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(), 23);
      } catch (UnknownHostException e) {
         e.printStackTrace();
         System.exit(1);
      }

      System.out.println("Client: Sending packet:");
      System.out.println("To host: " + sendPacket.getAddress());
      System.out.println("Destination host port: " + sendPacket.getPort());
      int len = sendPacket.getLength();
      System.out.println("Length: " + len);

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
         System.exit(1);
      }
      byte data[] = new byte[20];
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
      Client c = new Client();
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
