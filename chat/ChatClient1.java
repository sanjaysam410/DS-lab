import java.io.*;
import java.net.*;

class ChatClient1 {
    public static void main(String[] args) throws Exception {

        Socket s = new Socket("localhost", 12345);
        BufferedReader user = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);

        new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null)
                    System.out.println(msg);
            } catch (Exception e) {}
        }).start();

        String msg;
        while ((msg = user.readLine()) != null)
            out.println(msg);
    }
}