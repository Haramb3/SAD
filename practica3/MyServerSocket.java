import swing.MySocket;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyServerSocket extends ServerSocket {
    ServerSocket ss;
    MySocket sc;

    public MyServerSocket(int port) throws IOException {
        this.ss = new ServerSocket(port);
    }

    @Override
    public MySocket accept() {
        try {
            this.sc = new MySocket(ss.accept());
            return sc;
        } catch (IOException e) {
            Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public void close() {
        try {
            this.ss.close();
        } catch (IOException e) {
            Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}