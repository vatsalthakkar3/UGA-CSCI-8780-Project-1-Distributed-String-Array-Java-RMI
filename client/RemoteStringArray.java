// package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteStringArray extends Remote {

    public String sayHello() throws RemoteException;

    public int getCapacity() throws RemoteException;

    public int readElement(int idx, String clientId) throws RemoteException;

}