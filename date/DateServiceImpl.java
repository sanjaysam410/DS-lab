import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.Date;

// Implementation of the remote interface
public class DateServiceImpl extends UnicastRemoteObject implements DateService {

    protected DateServiceImpl() throws RemoteException {
        super();
    }

    // Actual logic for remote method
    public Date getServerDate() throws RemoteException {
        return new Date();
    }
}

