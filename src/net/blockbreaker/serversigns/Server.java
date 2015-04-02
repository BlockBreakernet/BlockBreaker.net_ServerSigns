package net.blockbreaker.serversigns;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;

public class Server {

    private String host;
    private int port;

    public Server(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Server(String host) {
        this.host = host;
        this.port = 25565;
    }

    public Server() {
        this.host = "127.0.0.1";
        this.port = 25565;
    }

    public String parseData(Connection connection) {
        try {

            Socket socket = new Socket();
            OutputStream os;
            DataOutputStream dos;
            InputStream is;
            InputStreamReader isr;

            socket.setSoTimeout(2500);
            socket.connect(new InetSocketAddress(host, port));

            os = socket.getOutputStream();
            dos = new DataOutputStream(os);

            is = socket.getInputStream();
            isr = new InputStreamReader(is, Charset.forName("UTF-16BE"));

            dos.write(new byte[] { (byte) 0xFE, (byte) 0x01 });

            int packetID = is.read();

            if(packetID == -1) {
                System.out.println("Invalid Packet ID! (End Of Stream)");
            }
            if(packetID != 0xFF) {
                System.out.println("Invalid Packet Id! " + packetID);
            }

            int length = isr.read();

            if(length == -1) {
                System.out.println("End Of Stream");
            }

            if(length == 0) {
                System.out.println("Invalid length");
            }

            char[] chars = new char[length];

            if(isr.read(chars, 0, length) != length) {
                System.out.println("End Of Stream");
            }

            String string = new String(chars);
            String[] data = string.split("\0");

            if(connection == Connection.ONLINE_PLAYERS) {
                return data[4];
            } else if(connection == Connection.MOTD) {
                return data[3];
            } else if(connection == Connection.MAX_PLAYERS) {
                return data[5];
            } else {
                System.out.println("Connection value not handled!");
            }

        } catch (Exception e) {

        }
        return null;
    }

    public enum Connection {
        ONLINE_PLAYERS, MAX_PLAYERS, MOTD
    }
}
