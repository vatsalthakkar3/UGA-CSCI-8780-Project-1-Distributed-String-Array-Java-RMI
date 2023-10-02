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
    public RemoteStringArrayImpl(int n) throws RemoteException {
        this.stringArray = new String[n];
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
        if (requestReadLock(idx, clientId)) {
            return this.stringArray[idx];
        } else {
            return null;
        }
    }

    @Override
    public void insertArrayElement(int index, String str) throws RemoteException {
        this.stringArray[index] = str;
    }

    @Override
    public String fetchElementRead(int index, String clientID) throws RemoteException {
        boolean op = this.requestReadLock(index, clientID);
        if (op) {
            return this.stringArray[index];
        }
        return null;

    }

    @Override
    public String fetchElementWrite(int index, String clientID) throws RemoteException {
        boolean op = this.requestWriteLock(index, clientID);
        if (op) {
            return this.stringArray[index];
        }
        return null;
    }

    @Override
    public boolean writeBackElement(String str, int index, String clientID) throws RemoteException {
        if (this.writeLock.get(index).equals(clientID)) {
            this.insertArrayElement(index, str);
            System.out.println(Arrays.toString(this.stringArray));
            return true;
        }
        return false;
    }

    @Override
    public boolean requestReadLock(int index, String clientID) throws RemoteException {
        if (this.writeLock.containsKey(index) && !(this.writeLock.get(index).equals(clientID)))
            return false;
        if (this.writeLock.containsKey(index) && this.writeLock.get(index).equals(clientID)) {
            this.writeLock.remove(index);
        }
        if (this.readLock.containsKey(index))
            this.readLock.get(index).add(clientID);
        else {
            ArrayList<String> clientList = new ArrayList<String>();
            clientList.add(clientID);
            this.readLock.put(index, clientList);
        }
        return true;
    }

    @Override
    public boolean requestWriteLock(int index, String clientID) throws RemoteException {
        if (this.writeLock.containsKey(index) && !(this.writeLock.get(index).equals(clientID))) {
            return false;
        }

        if (this.readLock.containsKey(index) && this.readLock.get(index).size() > 1) {
            return false;
        }
        if (this.readLock.containsKey(index) && this.readLock.get(index).size() == 1
                && !(this.readLock.get(index).get(0).equals(clientID))) {
            return false;
        }
        this.writeLock.put(index, clientID);
        return true;
    }

    @Override
    public void releaseLock(int index, String clientID) throws RemoteException {
        if (this.writeLock.containsKey(index) && this.writeLock.get(index) != clientID) {
            this.writeLock.remove(index);
        }
        if (this.readLock.containsKey(index) && this.readLock.get(index).contains(clientID)) {
            this.readLock.get(index).remove(clientID);
        }
    }

}
