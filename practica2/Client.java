import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    public static final String SERVER_HOST = "localhost";
    public static final int SERVER_PORT = 3000;
    public static String name, lastLine = "initial";

    public static void main(String[] args) throws IOException {
        MySocket sc = new MySocket(SERVER_HOST, SERVER_PORT);
        Scanner sk = new Scanner(System.in);
        BufferedReader in = new BufferedReader(new InputStreamReader(sc.MyGetInputStream()));
        PrintWriter out = new PrintWriter(sc.MyGetOutputStream(), true);
        Thread inputFlowThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String line;
                while ((line = sk.nextLine()) != null) {
                    lastLine = line;
                    out.println(line);
                    out.flush();
                }

            }
        });
        Thread outputFlowThread = new Thread(new Runnable() {
            public void run() {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        if (msg.contains(lastLine) && msg.contains("joined")) {
                            name = lastLine;
                        } else if (name == null) {
                            System.out.println(msg);
                        } else if (!msg.contains(name)) {
                            System.out.println(msg);
                        }
                    }
                } catch (Exception e) {
                    Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, e);

                }
            }
        });
        inputFlowThread.start();
        outputFlowThread.start();
        System.out.println("CLIENT HAS STARTED");
    }
}
