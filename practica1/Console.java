
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author p0p
 */

public class Console implements Observer {
    static final String ANSI_RIGHT = "\033[C";
    static final String ANSI_LEFT = "\033[D";
    static final String ANSI_INSERT = "\033[4h";
    static final String ANSI_BACKSPACE = "\b";
    static final String ANSI_DELETE = "\033[P";
    static final String ANSI_HOME = "\033[1~";
    static final String ANSI_FIN = "\033[4~";
    static final String ANSI_ESPAI = "\033[ ";
    static final String ANSI_BLANK_SPACE = "\033[@";
    private Line linia;

    public Console(Line value) {
        this.setRaw();
        this.linia = value;
    }

    static final int CHARACTER = 3006;
    static final int FINAL = 3007;
    static final int SEC_BACKSPACE = 127;
    static final int ESCAPE_SEC = 3000;
    static final int SEC_HOME = 3000;
    static final int SEC_RIGHT = 3001;
    static final int SEC_LEFT = 3002;
    static final int SEC_FIN = 3003;
    static final int SEC_INSERT = 3004;
    static final int SEC_DELETE = 3005;

    public void update(Observable obs, Object obj) {
        int var = (int) obj;
        switch (var) {
        case CHARACTER:
            boolean aux = this.linia.getInsert();
            this.printChar(this.linia.getLastChar(), aux);
            break;

        case SEC_RIGHT:
            this.right();
            break;

        case SEC_LEFT:
            this.left();
            break;

        case SEC_HOME:
            this.updateCursor(this.linia.getPos());
            break;

        case SEC_FIN:
            this.updateCursor(this.linia.getPos());
            break;

        case SEC_BACKSPACE:
            this.backspace();
            break;

        case SEC_DELETE:
            this.delete();
            break;

        case FINAL:
            this.unsetRaw();
            break;

        case SEC_INSERT:
            this.insert();
            break;
        default:
            break;
        }
    }

    public void insert() {
        System.out.print(ANSI_INSERT);
    }

    public void setRaw() {
        String cmd[] = { "/bin/sh", "-c", "stty -echo raw </dev/tty" };
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unsetRaw() {
        String cmd[] = { "/bin/sh", "-c", "stty echo cooked </dev/tty" };
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        System.out.print(ANSI_DELETE);
    }

    public void backspace() {
        System.out.print(ANSI_DELETE);
    }

    public void right() {
        System.out.print(ANSI_RIGHT);
    }

    public void left() {
        System.out.print(ANSI_LEFT);
    }

    public void printChar(char a, boolean insert) {
        if (insert) {
            System.out.print(a);
        } else {
            System.out.print(ANSI_BLANK_SPACE);
            System.out.print(a);
        }
    }

    public void updateCursor(int cursorPos) {
        int aux = cursorPos + 1;
        System.out.print("\033[" + aux + "G");
    }
}
