import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
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

            String clientId = s.sayHello();
            System.out.println("Your UniqueId is " + clientId);

            Scanner sc = new Scanner(System.in);
            Map<Integer, String> fetchedElement = new HashMap<>();
            try {
                while (true) {
                    System.out.println("\n***********************************");
                    System.out.println("*             Options             *");
                    System.out.println("***********************************");
                    System.out.println("1. Get_Array_Capacity");
                    System.out.println("2. Fetch_Element_Read <i>");
                    System.out.println("3. Fetch_Element_Write <i>");
                    System.out.println("4. Print_Element <i>");
                    System.out.println("5. Concatenate <i> Str");
                    System.out.println("6. Writeback <i>");
                    System.out.println("7. Release_Lock <i>");
                    System.out.println("8. Exit");
                    System.out.println("***********************************");
                    System.out.print("\nEnter Your Choice: ");
                    String cc = null;
                    int index = -1;
                    int choice = sc.nextInt();
                    if (choice >= 2 && choice < 8) {
                        System.out.print("\nEnter the Index of Element to fetch: ");
                        index = sc.nextInt();
                    }

                    System.out.print("\n");
                    switch (choice) {
                        case 1:
                            int capacity = s.getCapacity();
                            System.out.println("Capacity of the string array: " + capacity);
                            break;
                        case 2:
                            String readElement = s.fetchElementRead(index, clientId);
                            fetchedElement.put(index, readElement);
                            if (readElement == null)
                                System.out.println("🚨 Error: Failure reading element at index " + index);
                            else
                                System.out.println("🥳 Success: Element has been Fetched in read mode");
                            break;
                        case 3:
                            // TODO: Output Formatting
                            String writeElement = s.fetchElementWrite(index, clientId);
                            fetchedElement.put(index, writeElement);
                            if (writeElement == null)
                                System.out.println("🚨 Error: Failure reading element at index " + index);
                            else
                                System.out.println("🥳 Success: Element has been Fetched in write mode.");
                            break;
                        case 4:
                            // TODO: Output Formatting
                            if (fetchedElement.containsKey(index)) {
                                System.out.println("Element at index " + index + " : " + fetchedElement.get(index));
                            } else
                                System.out.println(" 🚨 Error: You Need to first fetch the element at index " + index);
                            break;
                        case 5:
                            // TODO: Output Formatting
                            if (fetchedElement.containsKey(index)) {
                                System.out.print("\nEnter a String to concatenate: ");
                                cc = sc.next();
                                String concated = fetchedElement.get(index) + cc;
                                fetchedElement.put(index, concated);
                                System.out.println("Concated String : " + concated);
                            } else
                                System.out.println(" 🚨 Error: You Need to first fetch the element at index " + index);
                            break;
                        case 6:
                            // TODO: Output Formatting
                            boolean op = s.writeBackElement(fetchedElement.get(index), index, clientId);
                            if (op) {
                                System.out.println("🥳 Element Successfully written back.");
                            } else {
                                System.out.println(
                                        "🚨 Error: Failed to write back. (You don't have write access at index "
                                                + index + ".)");
                            }
                            break;
                        case 7:
                            // TODO: Output Formatting
                            s.releaseLock(index, clientId);
                            if (fetchedElement.containsKey(index)) {
                                fetchedElement.remove(index);
                            }
                            break;
                        case 8:
                            System.out.println("Exiting...");
                            for (int i = 0; i < s.getCapacity(); i++) {
                                s.releaseLock(i, clientId);
                            }
                            System.exit(0);
                            break;
                        default:
                            System.out.println("ALERT 🚨 : Invalid choice. Please try again !!!");
                            break;
                    }
                    System.out.println(
                            "\n************************************************************************************************");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());

            } finally {
                for (int i = 0; i < s.getCapacity(); i++) {
                    s.releaseLock(i, clientId);
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            System.exit(0);
        } catch (NumberFormatException e) {
            System.err.println("Invalid capacity specified in the file.");
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Exception in client side" + e);
            e.printStackTrace();
            System.exit(0);
        }

    }
}