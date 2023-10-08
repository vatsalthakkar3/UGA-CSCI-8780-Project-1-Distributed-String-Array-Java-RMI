import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import java.lang.Thread;
import java.util.stream.Collectors;

/**
 * The `Client` class represents a client application that interacts with a remote
 * server to manipulate a distributed string array. It reads commands from the user,
 * communicates with the server, and performs various operations on the array.
 * 
 * @author Ratish Jha
 * @author Vatsal Thakkar
 */

public class Client {
    /**
     * The main method for the `Client` class.
     *
     * @param args Command-line arguments; expects a single argument: the path to
     *             the configuration file.
     */
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
                System.out.println("\n***********************************");
                System.out.println("*             Options             *");
                System.out.println("***********************************");
                System.out.println("1. getsize");
                System.out.println("2. readf <i>");
                System.out.println("3. writef <i>");
                System.out.println("4. print <i>");
                System.out.println("5. cat <i> Str");
                System.out.println("6. write <i>");
                System.out.println("7. release <i>");
                System.out.println("8. exit");
                System.out.println("***********************************");
                
                while (true) {
                    Thread.sleep(500);
                    System.out.print("\nEnter Your Choice: ");
                    String cc = null;
                    int index = -1;

                    String choice = sc.nextLine();
                    // if (choice >= 2 && choice < 8) {
                    //     System.out.print("\nEnter the Index of Element to fetch: ");
                    //     index = sc.nextInt();
                    // }


                    System.out.print("\n");
                    if(choice.trim().equals("getsize")) {
                            int capacity = s.getCapacity();
                            System.out.println("Capacity of the string array: " + capacity);
                        }
                    else if(choice.trim().split("\\s+")[0].equals("readf")){
                            index = Integer.valueOf(choice.trim().split("\\s+")[1]);
                            String readElement = s.fetchElementRead(index, clientId);
                            
                           if (readElement == null) {
                                System.out.println(
                                        "🚨 Error: Failed to fetch element at index " + index + " in READ mode." +
                                                "\nThis may occur if the resource is already occupied by someone else.");

                            } else {
                                fetchedElement.put(index, readElement);
                                System.out.println(
                                        "🥳 Success: Element at index " + index + " has been Fetched in READ mode.");
                                System.out.println("Fetched Element at index " + index + " : " + readElement);
                            }
                        }
                    else if(choice.trim().split("\\s+")[0].equals("writef")) {
                            index = Integer.valueOf(choice.trim().split("\\s+")[1]);
                            String writeElement = s.fetchElementWrite(index, clientId);
                            
                           if (writeElement == null) {
                                System.out.println(
                                        "🚨 Error: Failed to fetch element at index " + index + " in READ/WRITE mode." +
                                                "\nThis may occur if the resource is already occupied by someone else.");
                            } else {
                                fetchedElement.put(index, writeElement);
                                System.out.println("🥳 Success: Element at index " + index
                                        + " has been Fetched in READ/WRITE mode.");
                                System.out.println("Fetched Element at index " + index + " : " + writeElement);
                            }
                        }
                    else if(choice.trim().split("\\s+")[0].equals("print")){

                            // TODO: Output Formatting
                            index = Integer.valueOf(choice.trim().split("\\s+")[1]);
                            if (fetchedElement.containsKey(index)) {
                                System.out.println("Element at index " + index + " : " + fetchedElement.get(index));
                            } else {
                                System.out.println(" 🚨 Error: You Need to first fetch the element at index " + index
                                        + " in READ or READ/WRITE mode.");
                                System.out.println(fetchedElement.isEmpty() ? "You haven't fetched any elements."
                                        : "Available Fetched Indexes : " + fetchedElement.keySet().stream()
                                                .map(Object::toString).collect(Collectors.joining(" ")));
                            }
                        }
                    else if(choice.trim().split("\\s+")[0].equals("cat")) {
                            index = Integer.valueOf(choice.trim().split("\\s+")[1]);
                            if (fetchedElement.containsKey(index)) {
                                System.out.print("\nEnter a String to concatenate: ");
                                cc = sc.next();
                                String concated = fetchedElement.get(index) + cc;
                                fetchedElement.put(index, concated);
                                System.out.println("Concated String : " + concated);
                            } else
                                System.out.println(" 🚨 Error: You Need to first fetch the element at index " + index
                                        + " in READ or READ/WRITE mode.");
                                System.out.println(fetchedElement.isEmpty() ? "You haven't fetched any elements."
                                        : "Available Fetched Indexes : " + fetchedElement.keySet().stream()
                                                .map(Object::toString).collect(Collectors.joining(" ")));
                        }
                    else if(choice.trim().split("\\s+")[0].equals("write")) {
                            index = Integer.valueOf(choice.trim().split("\\s+")[1]);

                            // TODO: Output Formatting
                            boolean op = s.writeBackElement(fetchedElement.get(index), index, clientId);
                            if (op) {
                                System.out.println("🥳 Element Successfully written back.");
                            } else {
                                System.out.println(
                                        "🚨 Error: Failed to write back. (You don't have write access at index "
                                                + index + ".)");
                            }
                        }
                    else if(choice.trim().split("\\s+")[0].equals("release")) {
                            index = Integer.valueOf(choice.trim().split("\\s+")[1]);
                            // TODO: Output Formatting
                            s.releaseLock(index, clientId);
                            System.out.println("Lock has been released on Element at index : " + index);
                            if (fetchedElement.containsKey(index)) {
                                fetchedElement.remove(index);
                            }
                        }
                    else if(choice.trim().split("\\s+")[0].equals("exit")) {
                        System.out.println("Exiting...");
                        for (int i = 0; i < s.getCapacity(); i++) {
                            s.releaseLock(i, clientId);
                        }
                        System.exit(0);
                        }
                    else {
                        if(!choice.equals(""))
                            System.out.println("ALERT 🚨 : Invalid choice. Please try again !!!");
                        }
                    System.out.println(
                            "\n************************************************************************************************");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e);

                for (int i = 0; i < s.getCapacity(); i++) {
                    s.releaseLock(i, clientId);
                }

            } finally {
                for (int i = 0; i < s.getCapacity(); i++) {
                    s.releaseLock(i, clientId);
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e);
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