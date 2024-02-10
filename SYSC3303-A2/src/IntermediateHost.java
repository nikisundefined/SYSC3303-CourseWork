import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class IntermediateHost {

    DatagramPacket sendPacket, receivePacket;
    DatagramSocket sendSocket, receiveSocket;

    public IntermediateHost(){
        try {
            // Construct a datagram socket and bind it to any available
            // port on the local host machine. This socket will be used to
            // send UDP Datagram packets.
            sendSocket = new DatagramSocket();

            // Construct a datagram socket and bind it to port 23
            // on the local host machine. This socket will be used to
            // receive UDP Datagram packets.
            receiveSocket = new DatagramSocket(23);

            // to test socket timeout (2 seconds)
            //receiveSocket.setSoTimeout(2000);
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
            System.out.println("Intermediate Host: Waiting for Packet.\n");

            try {
                System.out.println("Waiting..."); // so we know we're waiting
                receiveSocket.receive(receivePacket);
            } catch (IOException e) {
                System.out.print("IO Exception: likely:");
                System.out.println("Receive Socket Timed Out.\n" + e);
                e.printStackTrace();
                System.exit(1);
            }
            clientPort = receivePacket.getPort();
            clientAd = receivePacket.getAddress();
            System.out.println("Intermediate Host: Packet received:");

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

            System.out.print("String: " + s+"\n");
            System.out.println("Bytes: ");
            for(byte b : data){
                System.out.println(b);
            }

            // Slow things down (wait 5 seconds)
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

            System.out.println("Intermediate Host: Sending packet:");
            System.out.println("To host: " + sendPacket.getAddress());
            System.out.println("Destination host port: " + sendPacket.getPort());
            len = sendPacket.getLength();
            System.out.println("Length: " + len);

            try {
                sendSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            System.out.println("Intermediate Host: packet sent");
            System.out.println("String: " + s + "\n");
            System.out.println("Bytes: ");
            for(byte b : data){
                System.out.println(b);
            }

            byte serverData[] = new byte[4];
            receivePacket = new DatagramPacket(serverData, serverData.length);
            if(data[1] == (byte)3){
                System.out.println("Intermediate Host sockets closed.");
                receiveSocket.close();
                sendSocket.close();
                System.exit(1);
            }
            System.out.println("Intermediate Host: Waiting for Packet.\n");
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
            System.out.println("Intermediate Host: Packet received:");

            len = receivePacket.getLength();

            byte[] printable2 = new byte[serverData.length];

            for(int i = 0;i<serverData.length;i++){ // this loop will make it so that the data sent in the packet is printable in the UTF-8 format as characters from 0-9 do not show unless shifted up by 48 to display their ascii equivalents
                if(serverData[i]<10) {
                    printable2[i] = (byte) (serverData[i] + 48);
                } else {
                    printable2[i] = serverData[i];
                }
            }

            String s2 = new String(printable2, StandardCharsets.UTF_8);

            System.out.print("String: " + s2+"\n");
            System.out.println("Bytes: ");
            for(byte b : serverData){
                System.out.println(b);
            }


            // Slow things down (wait 5 seconds)
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
            sendPacket = new DatagramPacket(serverData, receivePacket.getLength(),
                    clientAd, clientPort);

            System.out.println("Intermediate: Sending packet:");
            System.out.println("To host: " + sendPacket.getAddress());
            System.out.println("Destination host port: " + sendPacket.getPort());
            len = sendPacket.getLength();
            System.out.println("Length: " + len);

            try {
                sendSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            System.out.println("Intermediate: packet sent");
        }
    }
    public static void main( String args[] )
    {
        IntermediateHost c = new IntermediateHost();
        c.receiveAndPass();
    }
}
