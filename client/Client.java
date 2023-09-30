import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Client <config_file>\nFor Example: java Client client-config.txt");

            System.exit(1);
        }

        String filename = args[0];

        try {

            // Read Configuration File

            BufferedReader reader = new BufferedReader(new FileReader(filename));

            // Read Bind Name from the first line of the file
            String bindName = reader.readLine().strip();
            // Read the host IP / Name
            String host = reader.readLine().strip();
            // Read the capacity from the second line of the file
            int port = Integer.parseInt(reader.readLine().strip());

            reader.close();

            Registry registry = LocateRegistry.getRegistry(host, port);

            // lookup the rsa object and assign in s variable
            RemoteStringArray s = (RemoteStringArray) registry.lookup(bindName);

            String uniqueId = s.sayHello();
            System.out.println("Your UniqueId is " + uniqueId);

            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.println("\n***********************************");
                System.out.println("*             Options             *");
                System.out.println("***********************************");
                System.out.println("1. Get_Array_Capacity");
                System.out.println("2. Exit");
                System.out.println("***********************************");
                System.out.print("\nEnter Your Choice: ");

                int choice = sc.nextInt();
                sc.nextLine();

                System.out.print("\n");
                switch (choice) {
                    case 1:
                        int capacity = s.getCapacity();
                        System.out.println("Capacity of the string array: " + capacity);
                        break;
                    case 2:
                        System.out.println("Exiting...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("ALERT 🚨 : Invalid choice. Please try again !!!");
                        break;
                }

            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid capacity specified in the file.");
        } catch (Exception e) {
            System.out.println("Exception in client side" + e);
            System.exit(0);
        }
    }
}