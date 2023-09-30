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

    @Override
    public String sayHello() throws RemoteException {
        String uniqueId = UUID.randomUUID().toString();
        String clientName = "Client:" + (this.clientIds.size() + 1);
        this.clientIds.put(clientName, uniqueId);
        System.out.println("Hi 👋🏻, Server😘 " + clientName + " has joined with ID: " + uniqueId);
        return uniqueId;
    }

    @Override
    public int getCapacity() throws RemoteException {
        return this.stringArray.length;
    }

}
