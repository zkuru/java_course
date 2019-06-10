package queue;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.BlockingQueue;

@RequiredArgsConstructor
public class Guest implements Runnable{
    private final BlockingQueue<Integer> queue;

    public void run() {
        try {
            Thread.sleep(6000);
            for (int i = 0; i < 10; i++) {
                System.out.println("Take " + i);
                Thread.sleep(100);
                queue.take();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
