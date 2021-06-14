package swing;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;

public class ClientGUI extends JFrame {

    public static final int SERVER_PORT = 3000;
    public static final String SERVER_HOST = "localhost";
    public static String name, lastLine = "initial";
    public static MySocket sc = new MySocket(SERVER_HOST, SERVER_PORT);
    public static PrintWriter out = new PrintWriter(sc.MyGetOutputStream(), true);
    public JFrame window = this;
    public static JTextArea txtRx, usersJoined;
    public static JLabel label;

    public ClientGUI() {
        initComponents();
        ClientGUI.RXthread reader = new RXthread();
        ClientGUI.userClose updater = new userClose();
        reader.execute();
        updater.execute();
    }

    public Color randomColor() {
        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        return (new Color(r, g, b));
    }

    class userClose extends SwingWorker<String, Object> {

        @Override
        protected String doInBackground() throws Exception {
            window.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    out.print("ClosingOperation_ACK_1234\n");
                    out.flush();
                    sc.close();
                    System.out.println("CLOSING CONNECTION");
                    System.exit(0);
                }
            });
            return null;
        }
    }

    class RXthread extends SwingWorker<String, Object> {

        @Override
        protected String doInBackground() throws Exception {
            BufferedReader in = new BufferedReader(new InputStreamReader(sc.MyGetInputStream()));
            String msg;
            while ((msg = in.readLine()) != null) {
                System.out.println(name);
                System.out.println(lastLine);
                System.out.println(msg);
                if (msg.contains(lastLine) && msg.contains("has joined")) {
                    name = lastLine;
                    txtRx.append(msg + "\n");
                } else if (msg.contains("updateUser")) {
                    String[] temp;
                    temp = msg.split("-");
                    usersJoined.setText("");
                    for (int i = 1; i < temp.length; i++) {
                        usersJoined.append("- " + temp[i] + "\n");
                    }
                } else {
                    txtRx.append(msg + "\n");
                }
            }
            return msg;
        }
    }

    private void initComponents() {
        sendButton = new javax.swing.JButton();
        titleTextLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtRx = new javax.swing.JTextArea();
        txtTx = new javax.swing.JTextField();
        Color colour = randomColor();
        usersJoined = new javax.swing.JTextArea();
        txtRx.setLineWrap(true);
        txtRx.setWrapStyleWord(true);
        txtRx.setEditable(false);
        txtRx.setForeground(colour);
        usersJoined.setLineWrap(true);
        usersJoined.setWrapStyleWord(true);
        usersJoined.setEditable(false);
        usersJoined.setForeground(Color.GREEN);
        sendButton.setText("Send");
        sendButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            sendButtonActionPerformed(evt, out);
        });
        titleTextLabel.setText("CHAT");
        txtRx.setColumns(20);
        txtRx.setRows(5);
        jScrollPane2.setViewportView(txtRx);
        usersJoined.setColumns(20);
        usersJoined.setRows(5);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(192, 192, 192)
                                        .addComponent(titleTextLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(txtTx, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addComponent(usersJoined, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(titleTextLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(txtTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(sendButton)))
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(usersJoined, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
        );
        pack();
        setLocationRelativeTo(null);
    }

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt, PrintWriter out) {
        String txtToSend = txtTx.getText();
        lastLine = txtToSend;
        txtTx.setText("");
        out.print(txtToSend + "\n");
        out.flush();
        if (name != null) {
            txtRx.append("** " + name + ": " + txtToSend + "\n");
        } else {
            txtRx.append(txtToSend + "\n");
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new ClientGUI().setVisible(true);
        });
    }
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton sendButton;
    private javax.swing.JTextField txtTx;
    private javax.swing.JLabel titleTextLabel;
}
