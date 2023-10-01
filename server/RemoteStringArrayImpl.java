import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RemoteStringArrayImpl implements RemoteStringArray {

    // Define attributes and implement all the methods defined in product interface.

    // Define attributes.
    private String[] stringArray;
    private Map<String, String> clientIds = new HashMap<>();
    private Map<Integer, ArrayList<String>> readLock = new HashMap<>();
    private Map<Integer, String> writeLock = new HashMap<>();

    // Parametrized constructor.
    public RemoteStringArrayImpl(int n, String arrayOfString) throws RemoteException {
        this.stringArray = new String[n];
        String[] inputStrings = arrayOfString.split("\\s*,\\s*");

        // Populate the array with the split strings
        for (int i = 0; i < Math.min(n, inputStrings.length); i++) {
            this.stringArray[i] = inputStrings[i].strip();
        }

        // Initialize the remaining elements with empty strings
        for (int i = inputStrings.length; i < n; i++) {
            this.stringArray[i] = "";
        }
    }

    public boolean getReadLock(int index, String clientId) {
        if (this.writeLock.containsKey(index) && this.writeLock.get(index) != clientId)
            return false;
        if (this.readLock.containsKey(index))
            this.readLock.get(index).add(clientId);
        else {
            ArrayList<String> clientList = new ArrayList<>();
            clientList.add(clientId);
            this.readLock.put(index, clientList);
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
    public String readElement(int idx, String clientId) throws RemoteException {
        // Boolean readLock = getReadLock(idx);
        if (getReadLock(idx, clientId)) {
            return this.stringArray[idx];
        } else {
            return null;
        }
    }

    @Override
    public void insertArrayElement(int index, String str) throws RemoteException {
        // TODO: implement insertArrayElement
    }

    @Override
    public String fetchElementRead(int index, int clientID) throws RemoteException {
        // TODO: implement fetchElementRead
        return null;
    }

    @Override
    public String fetchElementWrite(int index, int clientID) throws RemoteException {
        // TODO: implement fetchElementWrite
        return null;
    }

    @Override
    public boolean writeBackElement(String str, int index, int clientID) throws RemoteException {
        // TODO: implement writeBackElement
        return false;
    }

    @Override
    public boolean requestReadLock(int index, int clientID) throws RemoteException {
        // TODO: implement requestReadLock
        return false;
    }

    @Override
    public boolean requestWriteLock(int index, int clientID) throws RemoteException {
        // TODO: implement requestWriteLock
        return false;
    }

    @Override
    public void releaseLock(int index, int clientID) throws RemoteException {
        // TODO: implement releaseLock

    }

}
