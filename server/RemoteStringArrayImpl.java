import java.rmi.*;
import java.rmi.server.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RemoteStringArrayImpl implements RemoteStringArray {

    // Define attributes and implement all the methods defined in product interface.

    // Define attributes.
    private String[] stringArray;
    private Map<String, String> clientIds = new HashMap<>();
    private Map<Integer, Arraylist<String>> readLock = new HashMap<>();
    private Map<Integer, String> writeLock = new HashMap<>();

    // Parametrized constructor.
    public RemoteStringArrayImpl(int n) throws RemoteException {
        this.stringArray = new String[n];
    }

    public boolean getReadLock(int idx, String clientId) {
        if (this.writeLock.containsKey(idx) && this.writeLock.get(idx) != clientId)
            return false;
        if (this.readLock.containsKey(idx))
            this.readLock.get(idx).add(clientId);
        else {
            ArrayList<String> clientList = new ArrayList<>();
            clientList.add(clientId);
            this.readLock.put(idx, clientList);
        }
        return true;
    }

    public boolean getWriteLock(int idx, String clientId) {
        if (this.writeLock.containsKey(idx) && this.writeLock.get(idx) != clientId)
            return false;
        this.writeLock.put(idx, clientId);
        return true;
    }

    @Override
    public String sayHello() throws RemoteException {
        String uniqueId = UUID.randomUUID().toString();
        String clientName = "Client:" + (this.clientIds.size() + 1);
        this.clientIds.put(uniqueId, clientName); // mapping ID to name instead for lookup in Map, as client knows its
                                                  // id and not its name
        System.out.println("Hi 👋🏻 Server😘 " + clientName + " has joined with ID: " + uniqueId); // Server is flirty
        return uniqueId;
    }

    @Override
    public int getCapacity() throws RemoteException {
        return this.stringArray.length;
    }

    @Override
    public int readElement(int idx, String clientId) throws RemoteException {
        // Boolean readLock = getReadLock(idx);
        if (getReadLock(idx, clientId)) {
            return this.stringArray[idx];
        } else
            return null;
    }

}
