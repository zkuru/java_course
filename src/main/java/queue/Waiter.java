package queue;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.BlockingQueue;

@RequiredArgsConstructor
public class Waiter implements Runnable {
    private final BlockingQueue<Integer> queue;

    public void run() {
        try {
            Thread.sleep(1000);
            for (int i = 0; i < 10; i++) {
                System.out.println("Put " + i);
                Thread.sleep(1000);
                queue.put(i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
