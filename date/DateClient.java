import java.rmi.Naming;
import java.util.Date;

public class DateClient {
    public static void main(String[] args) {
        try {
            // Look up the remote object from the registry
            DateService dateService = (DateService) Naming.lookup("rmi://localhost:8000/DateService");

            // Call the remote method
            Date currentDate = dateService.getServerDate();

            System.out.println("Current Date and Time from Server: " + currentDate);
        } catch (Exception e) {
            System.out.println("Client Error: " + e);
        }
    }
}


