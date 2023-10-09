import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.*;
/**
 * The RemoteStringArrayImpl class implements the RemoteStringArray interface and provides
 * remote methods for interacting with a distributed string array.
 *
 * @author Ratish Jha
 * @author Vatsal Thakkar
 */
public class RemoteStringArrayImpl implements RemoteStringArray {

    // Define attributes and implement all the methods defined in product interface.

    // Define attributes.
    private String[] stringArray;
    private Map<String, String> clientIds = new HashMap<>();
    private ConcurrentHashMap<Integer, ArrayList<String>> readLock = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, String> writeLock = new ConcurrentHashMap<>();

    ReadWriteLock lock = new ReentrantReadWriteLock();
    Lock writeReLock = lock.writeLock();
    Lock readReLock = lock.readLock();

    /**
     * Parameterized constructor to initialize the RemoteStringArrayImpl.
     *
     * @param n The capacity of the string array.
     * @throws RemoteException if there is a problem with remote object creation.
     */
    public RemoteStringArrayImpl(int n) throws RemoteException {
        this.stringArray = new String[n];
    }

    // Implementation of RemoteStringArray interface methods...

    /**
     * {@inheritDoc}
     */
    @Override
    public String sayHello() throws RemoteException {
        String uniqueId = UUID.randomUUID().toString();
        String clientName = "Client:" + (this.clientIds.size() + 1);
        this.clientIds.put(uniqueId, clientName); // mapping ID to name instead for lookup in Map, as client knows its
                                                  // id and not its name
        System.out.println("Hi 👋🏻 Server😘 " + clientName + " has joined with ID: " + uniqueId); // Server is flirty
        return uniqueId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCapacity() throws RemoteException {
        return this.stringArray.length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String readElement(int idx, String clientId) throws RemoteException {
        // Boolean readLock = getReadLock(idx);
        if (requestReadLock(idx, clientId)) {
            return this.stringArray[idx];
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertArrayElement(int index, String str) throws RemoteException {
        this.stringArray[index] = str;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String fetchElementRead(int index, String clientID) throws RemoteException {
        boolean op = this.requestReadLock(index, clientID);
        if (op) {
            return this.stringArray[index];
        }
        return null;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String fetchElementWrite(int index, String clientID) throws RemoteException {
            boolean op = this.requestWriteLock(index, clientID);
            if (op) {
                return this.stringArray[index];
            }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeBackElement(String str, int index, String clientID) throws RemoteException {

        try {
        if (this.writeLock.containsKey(index) && writeLock.get(index).equals(clientID)) {
            this.insertArrayElement(index, str);
            System.out.println(Arrays.toString(this.stringArray));
            return true;
            }
        else{
            return false;
        }
            
        } catch(NullPointerException ne) {
            System.out.println("error: client "+this.clientIds.get(clientID)+" never requested a write lock on element "+index+"th.");
            // ne.printStackTrace();
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean requestReadLock(int index, String clientID) throws RemoteException {
        readReLock.lock();
        if (this.writeLock.containsKey(index) && !(this.writeLock.get(index).equals(clientID)))
            return false;
        if (this.writeLock.containsKey(index) && this.writeLock.get(index).equals(clientID)) {
            writeReLock.lock();
            this.writeLock.remove(index);
            writeReLock.unlock();
        }
        if (this.readLock.containsKey(index)){
            if (!this.readLock.get(index).contains(clientID)){
                writeReLock.lock();
                this.readLock.get(index).add(clientID);
                writeReLock.unlock();
            }   
        }
            
        else {
            ArrayList<String> clientList = new ArrayList<String>();
            clientList.add(clientID);
            writeReLock.lock();
            this.readLock.put(index, clientList);
            writeReLock.unlock();
        }
        readReLock.unlock();
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean requestWriteLock(int index, String clientID) throws RemoteException {
        readReLock.lock();
        if (this.writeLock.containsKey(index) && !(this.writeLock.get(index).equals(clientID))) {
            return false;
        }

        if (this.readLock.containsKey(index) && this.readLock.get(index).size() > 1) {
            return false;
        }
        if (this.readLock.containsKey(index) && this.readLock.get(index).size() == 1
                && !(this.readLock.get(index).contains(clientID))) {
            return false;
        }
        writeReLock.lock();
        this.writeLock.put(index, clientID);
        writeReLock.unlock();
        readReLock.unlock();
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void releaseLock(int index, String clientID) throws RemoteException {
        if (this.writeLock.containsKey(index) && this.writeLock.get(index).equals(clientID)) { //release writeLock only if client requested is the one who has the lock 
            writeReLock.lock();
            this.writeLock.remove(index);
            writeReLock.unlock();
        }
        if (this.readLock.containsKey(index) && this.readLock.get(index).contains(clientID)) {
            writeReLock.lock();
            this.readLock.get(index).remove(clientID);
            writeReLock.unlock();
        }
    }

}
