import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteStringArray extends Remote {

    public String sayHello() throws RemoteException;

    public int getCapacity() throws RemoteException;

    public String readElement(int index, String clientId) throws RemoteException;

    public void insertArrayElement(int index, String str) throws RemoteException;

    public String fetchElementRead(int index, String clientID) throws RemoteException;

    public String fetchElementWrite(int index, String clientID) throws RemoteException;

    public boolean writeBackElement(String str, int index, String clientID) throws RemoteException;

    public boolean requestReadLock(int index, String clientID) throws RemoteException;

    public boolean requestWriteLock(int index, String clientID) throws RemoteException;

    public void releaseLock(int index, String clientID) throws RemoteException;

}
