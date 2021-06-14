import swing.MySocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final int PORT = 3000;
    public static ConcurrentHashMap<String, Handler> clients = new ConcurrentHashMap<String, Handler>();

    public static void main(String[] args) throws Exception {
        System.out.println("SERVER IS RUNNING");
        ExecutorService pool = Executors.newFixedThreadPool(500);
        try (MyServerSocket listener = new MyServerSocket(PORT)) {
            while (true) {
                pool.execute((Runnable) new Handler(listener.accept()));
            }
        }
    }

    public static class Handler implements Runnable {
        private String name, lastMsg;
        private MySocket socket;
        public BufferedReader in = null;
        public PrintWriter out = null;

        public Handler(MySocket sc) {
            this.socket = sc;
            this.in = new BufferedReader(new InputStreamReader(this.socket.MyGetInputStream()));
            this.out = new PrintWriter(socket.MyGetOutputStream(), true);
        }

        public void run() {
            while (true) {
                this.out.println("ENTER USERNAME: ");
                this.out.flush();
                try {
                    this.name = this.in.readLine();
                } catch (IOException e) {
                    System.out.println(e);
                }
                if (!clients.containsKey(this.name)) {
                    clients.put(this.name, this);
                    for (Handler ms : Server.clients.values()) {
                        ms.out.println(this.name + " has joined the chat");
                        ms.out.flush();
                    }
                    System.out.println("USERNAME: " + this.name);
                    break;
                } else {
                    this.out.println("USERNAME IS TAKEN");
                    this.out.flush();
                }
                clients.put(this.name, this);
            }
            while (true) {
                try {
                    if (this.in.ready()) {
                        this.lastMsg = this.in.readLine();
                        System.out.println("RECEIVED MESSAGE: " + this.lastMsg);
                        if (this.lastMsg.equalsIgnoreCase("logout")) {
                            clients.remove(this.name);
                            for (Handler ms : Server.clients.values()) {
                                ms.out.println(this.name + " has left the chat");
                                ms.out.flush();
                            }
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                if (!this.lastMsg.equalsIgnoreCase("reseted") || this.lastMsg == null) {
                    for (Handler ms : Server.clients.values()) {
                        ms.out.println(this.name + ": " + this.lastMsg);
                        ms.out.flush();
                    }
                    this.lastMsg = "reseted";
                }
                this.socket.close();
                try {
                    this.in.close();
                } catch (IOException e) {
                    Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, e);
                }
                this.out.close();
            }
        }
    }
}