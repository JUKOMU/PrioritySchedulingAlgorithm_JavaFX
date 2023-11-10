package thread;

import process.ProcessManager;
import res.IOManager;

public class IOThread {
    private final IOManager ioManager;
    public IOThread() {
        this.ioManager = new IOManager();
    }
}
