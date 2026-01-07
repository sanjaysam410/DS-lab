import java.net.*;
import java.io.*;

public class DNS{
    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int choice;

        do {
            System.out.println("\nMenu:");
            System.out.println("1. DNS");
            System.out.println("2. Reverse DNS");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            choice = Integer.parseInt(br.readLine());

            switch (choice) {
                case 1:
                    System.out.print("\nEnter Host Name: ");
                    InetAddress addr1 = InetAddress.getByName(br.readLine());
                    System.out.println("Host Name: " + addr1.getHostName());
                    System.out.println("IP: " + addr1.getHostAddress());
                    break;

                case 2:
                    System.out.print("\nEnter IP Address: ");
                    String ip = br.readLine();
                    InetAddress addr2 = InetAddress.getByName(ip);
                    System.out.println("IP: " + ip);
                    System.out.println("Host Name: " + addr2.getHostName());
                    break;

                case 3:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 3);
    }
}