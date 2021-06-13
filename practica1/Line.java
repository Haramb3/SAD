import java.util.*;

/**
 *
 * @author p0p
 */

public class Line extends Observable {

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

    ArrayList<Integer> lineBuffer;
    Boolean insert;
    int cursor, length;
    char lastChar;

    public int getPos() {
        return this.cursor;
    }

    public boolean getInsert() {
        return insert;
    }

    public char getLastChar() {

        return this.lastChar;
    }

    public Line() {
        this.insert = false;
        this.lineBuffer = new ArrayList<>();
        this.cursor = 0;
        this.length = 0;

    }

    public String returnStr() {
        String str = "";
        int i = 0;
        int aux = 0;
        for (i = 0; i < this.lineBuffer.size(); i++) {
            aux = this.lineBuffer.get(i);
            str += (char) aux;
        }

        return str;
    }

    public void removeCaracter(int pos) {
        this.lineBuffer.remove(pos);
    }

    public void changeInsert() {
        this.insert = !this.insert;
        this.setChanged();
        this.notifyObservers(SEC_INSERT);
    }

    public void addCaracter(int a) {
        if (this.insert) {
            if (this.cursor < this.lineBuffer.size()) {
                this.lineBuffer.set(this.cursor, a);
            } else {
                this.lineBuffer.add(this.cursor, a);
            }
        } else {
            int i = 0;
            this.length = this.lineBuffer.size();
            if (this.cursor < this.length) {
                for (i = this.length; i > this.cursor; i--) {
                    this.lineBuffer.add(i, this.lineBuffer.get(i - 1));
                    this.lineBuffer.remove(i - 1);
                }
            }
            this.lineBuffer.add(this.cursor, a);
        }
        this.lastChar = (char) a;
        this.cursor++;
        this.setChanged();
        this.notifyObservers(CHARACTER);
    }

    public void Home() {
        this.cursor = 0;
        this.setChanged();
        this.notifyObservers(SEC_HOME);
    }

    public void Fin() {
        this.cursor = this.lineBuffer.size();
        this.setChanged();
        this.notifyObservers(SEC_FIN);
    }

    public void Delete() {
        if (this.cursor < this.lineBuffer.size()) {
            this.lineBuffer.remove(this.cursor);
            this.length--;

        }
        this.setChanged();
        this.notifyObservers(SEC_DELETE);
    }

    public void Backspace() {
        if ((this.cursor <= this.lineBuffer.size()) && (this.lineBuffer.size() > 0) && this.cursor > 0) {
            this.lineBuffer.remove(this.cursor - 1);
            this.Left();
            this.length--;
        }
        this.setChanged();
        this.notifyObservers(SEC_BACKSPACE);

    }

    public void Right() {
        if (this.cursor < this.lineBuffer.size()) {
            this.cursor++;
        }
        this.setChanged();
        this.notifyObservers(SEC_RIGHT);
    }

    public void Left() {
        if (this.cursor > 0) {
            this.cursor--;
        }
        this.setChanged();
        this.notifyObservers(SEC_LEFT);
    }

    public void enter() {
        this.setChanged();
        this.notifyObservers(FINAL);

    }

}
