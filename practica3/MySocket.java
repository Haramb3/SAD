package swing;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySocket extends Socket {
    Socket sc;

    public MySocket(String host, int port) {
        try {
            this.sc = new Socket(host, port);
        } catch (IOException e) {
            Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public MySocket(Socket soc) {
        this.sc = soc;
    }

    public void MyConnect(SocketAddress endpoint) {
        try {
            this.sc.connect(endpoint);
        } catch (IOException e) {
            Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public InputStream MyGetInputStream() {
        try {
            return sc.getInputStream();
        } catch (IOException e) {
            Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public OutputStream MyGetOutputStream() {
        try {
            return sc.getOutputStream();
        } catch (IOException e) {
            Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public Scanner inStream() {
        try {
            return new Scanner(sc.getInputStream());
        } catch (IOException e) {
            Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public PrintWriter outStream() {
        try {
            return new PrintWriter(sc.getOutputStream());
        } catch (IOException e) {
            Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public void close() {
        try {
            sc.close();
        } catch (IOException e) {
            Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}