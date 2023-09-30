import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner; 

public class Server {
	public static void main(String [] args) {
		try {
		File confFile = new File(args[0]);
//      read config from file
        Scanner scanner = new Scanner(confFile);
        String bindName = scanner.nextLine();
		int arrayCapacity = Integer.parseInt(scanner.nextLine());
		String[] stringArray = scanner.nextLine().split(" ");
		
        System.out.println("Server is booting....");
        System.setProperty("java.rmi.server.hostname","127.0.0.1");

		 

        // Create objects from RemoteStringArrayImpl.java class and share them 

		RemoteStringArrayImpl s = new RemoteStringArrayImpl(arrayCapacity, stringArray);

		// Export s object before registered in Registry. 
		RemoteStringArray stub1 = (RemoteStringArray) UnicastRemoteObject.exportObject(s, 0);


		// Get the RMI registry.
		Registry registry = LocateRegistry.getRegistry("127.0.0.1", 9100);

		// Registered the exported object in rmi registry so that client can
		// lookup in this registry and call the object methods.
		registry.rebind(bindName, stub1);
		System.out.println("Server Started ... 🥳");

		} catch (Exception e) {
			System.out.println("Server error: " + e);
            System.exit(0);
		}

	}
}