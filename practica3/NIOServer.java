import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class NIOServer {
    public static final int PORT = 3000;
    public static final String ADDRESS = "localhost";
    private Selector sel;
    private ServerSocketChannel ssc;
    private InetSocketAddress socketAddress;
    private String name;
    private BufferedReader in = null;
    private List<String> clients;

    public void run() throws IOException {
        sel = Selector.open();
        ssc.configureBlocking(false);
        ssc = ServerSocketChannel.open();
        socketAddress = new InetSocketAddress(ADDRESS, PORT);
        ssc.bind(socketAddress);
        ssc.register(sel, SelectionKey.OP_ACCEPT);
        while (true) {
            int readyChannels = sel.selectNow();
            if (readyChannels == 0)
                continue;
            Set<SelectionKey> selectedKSet = sel.selectedKeys();
            Iterator<SelectionKey> it = selectedKSet.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    ssc.configureBlocking(false);
                    ssc.register(sel, SelectionKey.OP_READ);
                    sc.write(ByteBuffer.wrap("ENTER USERNAME: ".getBytes()));
                } else if (key.isReadable()) {
                    try {
                        this.name = this.in.readLine();
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                    SocketChannel sc = (SocketChannel) key.channel();
                    clients.add(this.name);
                    System.out.println("Welcome " + this.name);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new NIOServer().run();
    }
}
