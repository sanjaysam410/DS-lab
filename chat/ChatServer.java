import java.io.*;
import java.net.*;
import java.util.*;

class ChatServer {
    static Set<Client> clients = new HashSet<>();
    static int clientCount = 0;   // 🔑 client numbering

    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(12345);
        System.out.println("Server started");

        while (true) {
            Socket s = ss.accept();
            Client c = new Client(s, ++clientCount);  // assign ID
            clients.add(c);
            new Thread(c).start();
        }
    }

    static void broadcast(String msg, Client from) {
        for (Client c : clients)
            if (c != from)
                c.send("Client-" + from.id + ": " + msg);
    }

    static class Client implements Runnable {
        Socket s;
        BufferedReader in;
        PrintWriter out;
        int id;

        Client(Socket s, int id) throws Exception {
            this.s = s;
            this.id = id;
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
            out.println("You are Client-" + id);
        }

        public void run() {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    System.out.println("Client-" + id + ": " + msg);
                    broadcast(msg, this);
                }
            } catch (Exception e) {}
            finally {
                try { s.close(); } catch (Exception e) {}
                clients.remove(this);
                System.out.println("Client-" + id + " disconnected");
            }
        }

        void send(String msg) {
            out.println(msg);
        }
    }
}