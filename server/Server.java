

// package Server;
import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * The Server class initializes and starts an RMI server based on the configuration
 * specified in a provided file.
 * @author Ratish Jha
 * @author Vatsal Thakkar
 */
public class Server {
	/**
     * The main method of the Server class.
     *
     * @param args Command-line arguments; expects a single argument: the path to
     *             the configuration file.
     */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: java Server <config_file>\nFor Example: java Server server-config.txt");
			System.exit(1);
		}

		String filename = args[0];

		try {

			// Read Configuration File

			BufferedReader reader = new BufferedReader(new FileReader(filename));

			// Read Bind Name from the first line of the file
			String bindName = reader.readLine().strip();
			// Read the capacity from the second line of the file
			int n = Integer.parseInt(reader.readLine().strip());
			// Read the line containing comma- and space-separated strings
			String arrayOfString = reader.readLine().strip();
			String[] inputStrings = arrayOfString.split("\\s*,\\s*"); // List of Strings
			// Read hostname
			String host = reader.readLine().strip();
			// Read port number
			int port = Integer.parseInt(reader.readLine().strip());

			reader.close();

			System.out.println("Server is booting....");
			System.setProperty("java.rmi.server.hostname", host);

			// Create objects from RemoteStringArrayImpl.java class and share them
			RemoteStringArrayImpl s = new RemoteStringArrayImpl(n);

			// Populate the array with the split strings
			for (int i = 0; i < Math.min(n, inputStrings.length); i++) {
				s.insertArrayElement(i, inputStrings[i].strip());
			}

			// Initialize the remaining elements with empty strings
			for (int i = inputStrings.length; i < n; i++) {
				s.insertArrayElement(i, "");
			}

			// Export s object before registered in Registry.
			RemoteStringArray stub1 = (RemoteStringArray) UnicastRemoteObject.exportObject(s, 0);

			// Get the RMI registry.
			Registry registry = LocateRegistry.getRegistry(host, port);

			// Registered the exported object in rmi registry so that client can
			// lookup in this registry and call the object methods.
			registry.rebind(bindName, stub1);
			System.out.println("Server Started ... 🥳");

		} catch (IOException e) {
			System.err.println("Error reading the file: " + e.getMessage());
			System.exit(0);
		} catch (NumberFormatException e) {
			System.err.println("Invalid capacity specified in the file.");
			System.exit(0);
		} catch (Exception e) {
			System.out.println("Server error: " + e);
			e.printStackTrace();
			System.exit(0);
		}

	}
}
