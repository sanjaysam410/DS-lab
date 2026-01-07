import java.net.*;
import java.io.*;

class fc {
    public static void main(String args[]) throws Exception {
        Socket soc = new Socket("localhost", 5217);
        TransferClient t = new TransferClient(soc);
        t.menu();
    }
}

class TransferClient {
    Socket soc;
    DataInputStream in;
    DataOutputStream out;
    BufferedReader br;

    TransferClient(Socket s) throws Exception {
        soc = s;
        in = new DataInputStream(soc.getInputStream());
        out = new DataOutputStream(soc.getOutputStream());
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    void sendFile() throws Exception {
        System.out.print("Enter file name: ");
        String file = br.readLine();

        File f = new File(file);
        if (!f.exists()) {
            out.writeUTF("File not found");
            return;
        }

        out.writeUTF(file);
        if (in.readUTF().equals("File Already Exists")) {
            System.out.print("Overwrite (Y/N): ");
            if (!br.readLine().equalsIgnoreCase("Y")) {
                out.writeUTF("N");
                return;
            }
            out.writeUTF("Y");
        }

        FileInputStream fin = new FileInputStream(f);
        int ch;
        while ((ch = fin.read()) != -1)
            out.writeUTF(String.valueOf(ch));
        out.writeUTF("-1");
        fin.close();

        System.out.println(in.readUTF());
    }

    void receiveFile() throws Exception {
        System.out.print("Enter file name: ");
        String file = br.readLine();
        out.writeUTF(file);

        if (in.readUTF().equals("File Not Found")) {
            System.out.println("File not found on server");
            return;
        }

        FileOutputStream fout = new FileOutputStream(file);
        int ch;
        while ((ch = Integer.parseInt(in.readUTF())) != -1)
            fout.write(ch);

        fout.close();
        System.out.println(in.readUTF());
    }

    void menu() throws Exception {
        while (true) {
            System.out.println("\n1.Send  2.Receive  3.Exit");
            int ch = Integer.parseInt(br.readLine());

            if (ch == 1) {
                out.writeUTF("SEND");
                sendFile();
            } else if (ch == 2) {
                out.writeUTF("GET");
                receiveFile();
            } else {
                out.writeUTF("DISCONNECT");
                break;
            }
        }
    }
}
