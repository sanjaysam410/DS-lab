import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;



public class DateServer {
    public static void main(String[] args) {
        try {
            
	     
	      LocateRegistry.createRegistry(8000);

             // Create an instance of implementation class
            DateServiceImpl dateService = new DateServiceImpl();

            // Bind the remote object in the RMI registry
            Naming.rebind("rmi://localhost:8000/DateService", dateService);

            System.out.println("Date Service is running...");
        } catch (Exception e) {
            System.out.println("Server Exception: " + e);
        }
    }
}
