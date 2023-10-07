// package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The RemoteStringArray interface defines a set of remote methods for interacting
 * with a distributed string array. Remote objects that implement this interface
 * can be accessed remotely via RMI (Remote Method Invocation).
 *
 * All methods in this interface throw RemoteException to indicate potential
 * remote communication issues.
 * 
 * @author Ratish Jha
 * @author Vatsal Thakkar
 */

public interface RemoteStringArray extends Remote {

    /**
     * Say hello and obtain a unique client identifier.
     *
     * @return A unique client identifier.
     * @throws RemoteException if there is a problem with remote communication.
     */
    public String sayHello() throws RemoteException;

    /**
     * Get the capacity of the string array.
     *
     * @return The capacity of the string array.
     * @throws RemoteException if there is a problem with remote communication.
     */
    public int getCapacity() throws RemoteException;

    /**
     * Read an element from the string array.
     *
     * @param index    The index of the element to read.
     * @param clientId The client identifier making the request.
     * @return The element at the specified index.
     * @throws RemoteException if there is a problem with remote communication.
     */
    public String readElement(int index, String clientId) throws RemoteException;

    /**
     * Insert an element into the string array.
     *
     * @param index The index at which to insert the element.
     * @param str   The string to insert.
     * @throws RemoteException if there is a problem with remote communication.
     */
    public void insertArrayElement(int index, String str) throws RemoteException;

    /**
     * Fetch an element from the string array with a read lock.
     *
     * @param index    The index of the element to fetch.
     * @param clientID The client identifier making the request.
     * @return The element at the specified index.
     * @throws RemoteException if there is a problem with remote communication.
     */
    public String fetchElementRead(int index, String clientID) throws RemoteException;

    /**
     * Fetch an element from the string array with a write lock.
     *
     * @param index    The index of the element to fetch.
     * @param clientID The client identifier making the request.
     * @return The element at the specified index.
     * @throws RemoteException if there is a problem with remote communication.
     */
    public String fetchElementWrite(int index, String clientID) throws RemoteException;

    /**
     * Write back an element into the string array with a write lock.
     *
     * @param str      The string to write back.
     * @param index    The index at which to write the element.
     * @param clientID The client identifier making the request.
     * @return True if the write operation is successful; false otherwise.
     * @throws RemoteException if there is a problem with remote communication.
     */
    public boolean writeBackElement(String str, int index, String clientID) throws RemoteException;

    /**
     * Request a read lock for an element in the string array.
     *
     * @param index    The index of the element to lock.
     * @param clientID The client identifier making the request.
     * @return True if the read lock is granted; false otherwise.
     * @throws RemoteException if there is a problem with remote communication.
     */
    public boolean requestReadLock(int index, String clientID) throws RemoteException;

    /**
     * Request a write lock for an element in the string array.
     *
     * @param index    The index of the element to lock.
     * @param clientID The client identifier making the request.
     * @return True if the write lock is granted; false otherwise.
     * @throws RemoteException if there is a problem with remote communication.
     */
    public boolean requestWriteLock(int index, String clientID) throws RemoteException;

    /**
     * Release a lock on an element in the string array.
     *
     * @param index    The index of the element to release the lock for.
     * @param clientID The client identifier releasing the lock.
     * @throws RemoteException if there is a problem with remote communication.
     */
    public void releaseLock(int index, String clientID) throws RemoteException;

}