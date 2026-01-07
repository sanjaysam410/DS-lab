import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

// Remote interface that declares the remote method
public interface DateService extends Remote {
    Date getServerDate() throws RemoteException;
}

