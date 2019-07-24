package socket;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    @SneakyThrows
    private Server(int port) {
        try (ServerSocket server = new ServerSocket(port, 1)) {
            System.out.println("Server started");
            System.out.println("Waiting for a client ...");
            while (!server.isClosed())
                executorService.submit(new ClientHandler(server.accept()));
        }
    }

    @RequiredArgsConstructor
    private class ClientHandler implements Runnable {
        private final Socket socket;

        @Override
        @SneakyThrows
        public void run() {
            System.out.println("Client accepted");
            Thread.sleep(5000);
        }
    }

    public static void main(String[] args) {
        new Server(8090);
    }
}