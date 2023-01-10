package academy.prog;

import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Enter your login: ");
            String login = scanner.nextLine();

            Thread th = new Thread(new GetThread());
            th.setDaemon(true);
            th.start();

            Thread thPrivate = new Thread(new PrivateThread(login));
            thPrivate.setDaemon(true);
            thPrivate.start();

            System.out.println("Enter your message: ");
            while (true) {
                String text = scanner.nextLine();
                if (text.isEmpty()) break;

                // @test Hello
                String[] s = parsText(text);
                // users
                getUsers(text);

                Message m = new Message(login, s[0], s[1]);
                int res = m.send(Utils.getURL() + "/add");

                if (res != 200) { // 200 OK
                    System.out.println("HTTP error occurred: " + res);
                    return;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    public static void getUsers(String s) throws IOException {
        if (s.equals("users")) {
            URL url = new URL(Utils.getURL() + "/users");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream is = http.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[10240];
            int r;
            do {
                r = is.read(buf);
                if (r > 0) bos.write(buf, 0, r);
            } while (r != -1);
            String json = bos.toString(StandardCharsets.UTF_8);
            Set<String> set = new GsonBuilder().create().fromJson(json, Set.class);
            System.out.println(set);
        }
    }

    public static String[] parsText(String text) {
        String to;
        if (text.startsWith("@") && text.contains(" ") && text.split(" ").length > 1) {
            to = text.split(" ")[0].substring(1);
            text = text.substring(text.indexOf(" ") + 1);
        } else to = null;
        return new String[]{to, text};
    }
}
