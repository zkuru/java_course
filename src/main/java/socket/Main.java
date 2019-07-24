package socket;

import lombok.SneakyThrows;

import java.net.URL;
import java.net.URLConnection;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            URLConnection urlConnection = new URL("http://localhost:8090/").openConnection();
            urlConnection.connect();
        }
    }
}