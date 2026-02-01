import java.util.*;
import java.io.*;


class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int empno;
    private String ename;
    private double salary;
    private boolean deleted;

    public Employee(int empno, String ename, double salary) {
        this.empno = empno;
        this.ename = ename;
        this.salary = salary;
        this.deleted = false;
    }

    public int getEmpno() {
        return empno;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Id: " + empno + "  " + "Name: " + ename + "  " +"Salary: " + salary;
    }
}


class EmployeeDataManager {
    private static final String DATA_FILE = "employees.dat";
    private List<Employee> employees;

    public EmployeeDataManager() {
        this.employees = new ArrayList<>();
        loadFromFile();
    }

    public void addEmployee(Employee employee) {
        if (employeeExists(employee.getEmpno())) {
            System.out.println("Error: Employee ID already exists!");
            return;
        }
        employee.setDeleted(false);
        employees.add(employee);
        saveToFile();
        System.out.println("Employee added successfully!");
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    public Employee getEmployeeById(int id) {
        for (Employee emp : employees) {
            if (emp.getEmpno() == id && !emp.isDeleted()) {
                return emp;
            }
        }
        return null;
    }

    public Employee getEmployeeByIdAny(int id) {
        for (Employee emp : employees) {
            if (emp.getEmpno() == id) {
                return emp;
            }
        }
        return null;
    }

    public void updateEmployee(int id, String name, double salary) {
        Employee emp = getEmployeeById(id);
        if (emp == null) {
            System.out.println("Error: Employee not found!");
            return;
        }
        emp.setEname(name);
        emp.setSalary(salary);
        saveToFile();
        System.out.println("Employee updated successfully!");
    }


    public void deleteEmployee(int id) {
        Employee emp = getEmployeeById(id);
        if (emp == null) {
            System.out.println("Error: Employee not found!");
            return;
        }
        emp.setDeleted(true);
        saveToFile();
        System.out.println("Employee deleted successfully!");
    }

    private boolean employeeExists(int id) {
        return getEmployeeById(id) != null;
    }

    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(employees);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            employees = new ArrayList<>();
            initializeSampleData();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            employees = (List<Employee>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
            employees = new ArrayList<>();
        }
    }

    private void initializeSampleData() {
        employees.add(new Employee(101, "Chicken seller", 50000));
        employees.add(new Employee(102, "Sarah Labati", 60000));
        employees.add(new Employee(103, "Mark Tuan", 55000));
        employees.add(new Employee(104, "Jung Kook", 65000));
        employees.add(new Employee(105, "Tobio Kageyama", 58000));
        saveToFile();
    }

    public void displayAllEmployees() {
        List<Employee> activeEmployees = new ArrayList<>();
        for (Employee emp : employees) {
            if (!emp.isDeleted()) {
                activeEmployees.add(emp);
            }
        }
        
        if (activeEmployees.isEmpty()) {
            System.out.println("No employees found!");
            return;
        }
        System.out.println("\n===== Employee List =====");
        for (Employee emp : activeEmployees) {
            System.out.println(emp);
        }
        System.out.println("=========================\n");
    }
}


public class CrudEmployee {
    private static EmployeeDataManager manager = new EmployeeDataManager();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;

        do {
            System.out.println("\n===== EMPLOYEE SYSTEM =====");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Retrieve Employee by ID");
            System.out.println("4. Update Employee");
            System.out.println("5. Delete Employee");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            
            choice = getIntInput();

            switch (choice) {
                case 1:
                    addEmployeeMenu();
                    break;
                case 2:
                    manager.displayAllEmployees();
                    break;
                case 3:
                    retrieveEmployeeMenu();
                    break;
                case 4:
                    updateEmployeeMenu();
                    break;
                case 5:
                    deleteEmployeeMenu();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 0);

        sc.close();
    }

    private static void addEmployeeMenu() {
        System.out.print("Enter Employee ID: ");
        int id = getIntInput();
        
        sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine().trim();
        
        System.out.print("Enter Salary: ");
        double salary = getDoubleInput();

        if (name.isEmpty()) {
            System.out.println("Error: Name cannot be empty!");
            return;
        }

        Employee emp = new Employee(id, name, salary);
        manager.addEmployee(emp);
        System.out.println("Employee Name Saved: " + name);
    }

    private static void retrieveEmployeeMenu() {
        System.out.print("Enter Employee ID to retrieve: ");
        int id = getIntInput();

        Employee emp = manager.getEmployeeByIdAny(id);
        if (emp == null) {
            System.out.println("Error: Employee not found!");
            return;
        }

        System.out.println("\n===== Employee Details =====");
        System.out.println(emp);
        System.out.println("=============================\n");

        if (emp.isDeleted()) {
            emp.setDeleted(false);
            manager.saveToFile();
            System.out.println("Employee automatically restored!");
        }
    }

    private static void updateEmployeeMenu() {
        System.out.print("Enter Employee ID to update: ");
        int id = getIntInput();

        Employee emp = manager.getEmployeeById(id);
        if (emp == null) {
            System.out.println("Error: Employee not found!");
            return;
        }

        sc.nextLine();
        System.out.print("Enter new name: ");
        String name = sc.nextLine().trim();
        
        System.out.print("Enter new salary: ");
        double salary = getDoubleInput();

        if (name.isEmpty()) {
            System.out.println("Error: Name cannot be empty!");
            return;
        }

        manager.updateEmployee(id, name, salary);
        System.out.println("Employee Name Updated: " + name);
    }

    private static void deleteEmployeeMenu() {
        System.out.print("Enter Employee ID to delete: ");
        int id = getIntInput();

        Employee emp = manager.getEmployeeById(id);
        if (emp == null) {
            System.out.println("Error: Employee not found!");
            return;
        }

        System.out.println("Employee: " + emp);
        System.out.print("Confirm delete? (yes/no): ");
        sc.nextLine();
        String confirm = sc.nextLine().trim().toLowerCase();

        if (confirm.equals("yes")) {
            manager.deleteEmployee(id);
        }
    }

    private static int getIntInput() {
        try {
            int value = sc.nextInt();
            return value;
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("Invalid input!");
            return -1;
        }
    }

    private static double getDoubleInput() {
        try {
            double value = sc.nextDouble();
            return value;
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("Invalid input!");
            return -1;
        }
    }
}
