import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
   public static void main(String[] args) {
      try {

        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 9100);

        // lookup the rsa object and assign in s variable
        RemoteStringArray s = (RemoteStringArray) registry.lookup("rsa");
    
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

            String choice = sc.next();
            sc.nextLine();

            System.out.print("\n");
            switch (choice) {
                case "getCapacity":
                    int capacity = s.getCapacity();
                    System.out.println("Capacity of the string array: " + capacity);
                    break;
                case "exit":
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("ALERT 🚨 : Invalid choice. Please try again !!!");
                    break;
            }
       
        }
      } catch (Exception e) {
         System.out.println("Exception in client side" + e);
         System.exit(0);
      }
   }
}