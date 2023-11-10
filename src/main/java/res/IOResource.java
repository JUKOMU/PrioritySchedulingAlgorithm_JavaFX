package res;

import java.util.concurrent.atomic.AtomicInteger;

class IOResource {
    private final AtomicInteger surplus = new AtomicInteger(1);

    int getSurplus() {
        return surplus.get();
    }

    boolean request() {
        this.surplus.decrementAndGet();
        return surplus.get() >= 0;
    }

    void release() {
        this.surplus.incrementAndGet();
    }
}
