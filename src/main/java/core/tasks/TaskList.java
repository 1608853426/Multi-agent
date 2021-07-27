package core.tasks;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author SoonMachine
 */
public class TaskList extends LinkedList implements Serializable {

    private static final long serialVersionUID = 381917241183088451L;

    private int current = 0;

    public TaskList() {
    }

    public synchronized void addElement(Task b) {
        this.add(b);
    }

    public synchronized boolean removeElement(Task b) {
        int index = this.indexOf(b);
        if (index != -1) {
            this.remove(b);
            if (index < this.current) {
                --this.current;
            } else if (index == this.current && this.current == this.size()) {
                this.current = 0;
            }
        }

        return index != -1;
    }

    public Task getCurrent() {
        Task b = null;

        try {
            b = (Task) this.get(this.current);
        } catch (IndexOutOfBoundsException var3) {
        }

        return b;
    }

    public synchronized void begin() {
        this.current = 0;
    }

    private boolean currentIsLast() {
        return this.current == this.size() - 1;
    }

    public synchronized Task next() {
        if (!this.currentIsLast() && !this.isEmpty()) {
            ++this.current;
        } else {
            this.current = 0;
        }

        return this.getCurrent();
    }
}
