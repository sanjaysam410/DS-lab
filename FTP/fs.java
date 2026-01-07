import java.net.*;
import java.io.*;

class fs {
    public static void main(String args[]) throws Exception {
        try (ServerSocket ss = new ServerSocket(5217)) {
            System.out.println("FTP Server Started");

            while (true)
                new TransferFile(ss.accept());
        }
    }
}

class TransferFile extends Thread {
    Socket soc;
    DataInputStream in;
    DataOutputStream out;

    TransferFile(Socket s) throws Exception {
        soc = s;
        in = new DataInputStream(soc.getInputStream());
        out = new DataOutputStream(soc.getOutputStream());
        start();
    }

    void sendFile() throws Exception {
        String file = in.readUTF();
        File f = new File(file);

        if (!f.exists()) {
            out.writeUTF("File Not Found");
            return;
        }

        out.writeUTF("READY");
        FileInputStream fin = new FileInputStream(f);
        int ch;
        while ((ch = fin.read()) != -1)
            out.writeUTF(String.valueOf(ch));
        out.writeUTF("-1");

        fin.close();
        out.writeUTF("File Sent Successfully");
    }

    void receiveFile() throws Exception {
        String file = in.readUTF();
        File f = new File(file);

        if (f.exists()) {
            out.writeUTF("File Already Exists");
            if (!in.readUTF().equalsIgnoreCase("Y"))
                return;
        } 
        else
            out.writeUTF("SendFile");
        FileOutputStream fout = new FileOutputStream(f);
        int ch;
        while ((ch = Integer.parseInt(in.readUTF())) != -1)
            fout.write(ch);

        fout.close();
        out.writeUTF("File Received Successfully");
    }

    public void run() {
        try {
            while (true) {
                String cmd = in.readUTF();
                if (cmd.equals("GET"))
                    sendFile();
                else if (cmd.equals("SEND"))
                    receiveFile();
                else
                    break;
            }
        } catch (Exception e) {}
    }
}
