import java.io.*;
import java.util.*;
import java.nio.file.*;

class Main {
    private static String fileName = "inventory.txt";
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {        
        while(true) {
            System.out.println("\nProduct Inventory");
            System.out.println("1) Add New Product");
            System.out.println("2) View Active Products");
            System.out.println("3) Update Product");
            System.out.println("4) Delete Product)");
            System.out.print("Select: ");
            
            String choice = input.nextLine();

            if (choice.equals("1")) {
                addProduct();
            } else if (choice.equals("2")) {
                showActiveProducts();
            } else if (choice.equals("3")) {
                editProduct();
            } else if (choice.equals("4")) {
                archiveProduct();
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }

    private static void addProduct() {
        try {
            FileWriter writer = new FileWriter(fileName, true);
            
            System.out.print("Enter ProductID: ");
            String id = input.nextLine();
            
            try {
                Integer.parseInt(id);
            } catch (Exception e) {
                System.out.println("ID must be numeric!");
                writer.close();
                return;
            }

            System.out.print("Enter Product Name: ");
            String name = input.nextLine();

            writer.write("ProductID: " + id + ", Product Name: " + name + ", Status: Active\n");
            writer.close(); 
            System.out.println("Successfully added.");
        } catch (IOException e) { 
            System.out.println("Error saving."); 
        }
    }

    private static void showActiveProducts() {
        try {
            File myFile = new File(fileName);
            if (!myFile.exists()) {
                System.out.println("No records found.");
                return;
            }
            
            Scanner reader = new Scanner(myFile);
            boolean hasRecords = false;

            while (reader.hasNextLine()) {
                String line = reader.nextLine();

                if (line.contains("Status: Active")) {
                    System.out.println(line);
                    hasRecords = true;
                }
            }
            if (!hasRecords) System.out.println("No active products found");
            
            reader.close();
        } catch (Exception e) { 
            System.out.println("Error reading file."); 
        }
    }

    private static void editProduct() {
        try {
            System.out.print("Enter ID to update: ");
            String searchId = input.nextLine();
            
            List<String> list = Files.readAllLines(Paths.get(fileName));
            boolean isFound = false;

            for (int i = 0; i < list.size(); i++) {
                String line = list.get(i);
                
                if (line.startsWith("ProductID: " + searchId) && line.contains("Status: Active")) {
                    System.out.print("Enter new name: ");
                    String newName = input.nextLine();
                    
                    list.set(i, "ProductID: " + searchId + ", Product Name: " + newName + ", Status: Active");
                    isFound = true;
                    break; 
                }
            }

            if (isFound) {
                Files.write(Paths.get(fileName), list);
                System.out.println("Updated!");
            } else {
                System.out.println("ID not found or archived.");
            }
        } catch (Exception e) {
            System.out.println("Problem updating record.");
        }
    }

    private static void archiveProduct() {
        try {
            System.out.print("Enter ID to Archive: ");
            String targetId = input.nextLine();

            List<String> lines = Files.readAllLines(Paths.get(fileName));
            List<String> updatedLines = new ArrayList<>();
            boolean isFound = false;

            for (String line : lines) {
                if (line.startsWith("ProductID: " + targetId) && line.contains("Status: Active")) {
                    String archivedLine = line.replace("Status: Active", "Status: Archived");
                    updatedLines.add(archivedLine);
                    isFound = true;
                } else {
                    updatedLines.add(line);
                }
            }

            if (isFound) {
                Files.write(Paths.get(fileName), updatedLines);
                System.out.println("Product Archived.");
            } else {
                System.out.println("ID not found or already archived.");
            }
        } catch (Exception e) {
            System.out.println("Archiving failed");
        }
    }
}