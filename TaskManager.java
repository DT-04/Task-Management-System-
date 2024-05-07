import java.text.*;
import java.util.*;

class Task 
{
    int id;
    String title;
    String description;
    Date deadline;
    int priority;
    List<Task> dependencies;
    boolean completed;

    public Task(int id, String title, String description, Date deadline, int priority) 
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.dependencies = new ArrayList<>();
        this.completed = false;
    }
}

class TaskManager 
{
    HashMap<Integer, Task> taskM;
    HashMap<Task, List<Task>> dependency;
    PriorityQueue<Task> priorityQueue;
    TreeSet<Task> deadline;

    public TaskManager() 
    {
        taskM = new HashMap<>();
        dependency = new HashMap<>();
        priorityQueue = new PriorityQueue<>(Comparator.comparingInt(task -> task.priority));
        deadline = new TreeSet<>(Comparator.comparing(task -> task.deadline));
    }

    public void addTask(Task task) 
    {
        taskM.put(task.id, task);
        dependency.put(task, new ArrayList<>());
        priorityQueue.add(task);
        deadline.add(task);
        System.out.println("Task added successfully.");
    }

    public void modifyTask(int taskId, String newTitle, String newDescription, Date newDeadline, int newPriority) 
    {
        Task task = taskM.get(taskId);
        if (task != null) 
        {
            task.title = newTitle;
            task.description = newDescription;
            task.deadline = newDeadline;     
            
            task.priority = newPriority;
            System.out.println("Task modified successfully.");
        } 
        else 
        {
            System.out.println("Task not found.");
        }
    }

    public void viewTask() 
{
    if (taskM.isEmpty()) 
    {
        System.out.println("No tasks added yet.");
        return;
    }

    System.out.println("All Tasks:");

    for (Task task : taskM.values()) 
    {
        System.out.println("Task Details:");
        System.out.println("ID: " + task.id);
        System.out.println("Title: " + task.title);
        System.out.println("Description: " + task.description);
        System.out.println("Deadline: " + task.deadline);
        System.out.println("Priority: " + task.priority);
        System.out.println("Completed: " + task.completed);
    }
}


    public void addDependency(int d1, int d2) 
    {
        Task dT1 = taskM.get(d1);
        Task dT2 = taskM.get(d2);

        if (dT1 != null && dT2 != null) 
        {
            dependency.get(dT1).add(dT2);
            System.out.println("Dependency added successfully.");
        } 
        else 
        {
            System.out.println("Invalid Task ID");
        }
    }

    public void showOverdue() 
    {
        Date currentDate = new Date();

        for (Task task : deadline) 
        {
            if (!task.completed && task.deadline.before(currentDate)) 
            {
                System.out.println("Your Task is Overdued");
            }
        }
    }

    public void completeTask(int taskId) 
    {
        Task task = taskM.get(taskId);
        if (task != null && Complete(task)) 
        {
            task.completed = true;
            priorityQueue.remove(task);
            deadline.remove(task);
            System.out.println("Task marked as completed.");    
        }
        else 
        {
            System.out.println("Cannot complete Task due to dependencies or it's already completed.");
        }
    }

    private boolean Complete(Task task) 
    {
        for (Task dependency : dependency.get(task)) 
        {
            if (taskM.containsKey(dependency.id) || !dependency.completed) 
            {
                return false;
            }
        }
        return true;
    }

    public void displayPriority() 
    {
        for (Task task : priorityQueue) 
        {
            System.out.println("Task: " + task.title + " ");
        }
    }

    public static void main(String[] args) 
    {
        TaskManager taskManager = new TaskManager();
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-YY");

        while (true) 
        {
            System.out.println("Welcome to Task Management System");
            System.out.println("1. Add Task");
            System.out.println("2. Modify Task");
            System.out.println("3. View Task");
            System.out.println("4. Add Dependency");
            System.out.println("5. Show Overdue Tasks");
            System.out.println("6. Complete Task");
            System.out.println("7. Display Tasks by Priority");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) 
            {
                case 1:
                    try 
                    {
                        System.out.println("Enter task details:");
                        System.out.println("Task ID: ");
                        int taskId = scanner.nextInt();
                        scanner.nextLine(); 
                        System.out.println("Title: ");
                        String title = scanner.nextLine();
                        System.out.println("Description: ");
                        String description = scanner.nextLine();
                        System.out.println("Deadline (dd-MM-YY): ");
                        Date deadline = dateFormat.parse(scanner.next());
                        System.out.println("Priority: ");
                        int priority = scanner.nextInt();

                        Task task = new Task(taskId, title, description, deadline, priority);
                        taskManager.addTask(task);
                    } 
                    catch (ParseException | InputMismatchException e) 
                    {
                        System.out.println("Invalid input. Please try again.");
                        scanner.nextLine(); 
                    }
                    break;

                case 2:
                    try 
                    {
                        System.out.println("Enter task details to modify:");
                        System.out.print("Task ID: ");
                        int taskToModify = scanner.nextInt();
                        System.out.println("Enter new task details:");
                        scanner.nextLine(); 
                        System.out.println("Title: ");
                        String newTitle = scanner.nextLine();
                        System.out.println("Description: ");
                        String newDescription = scanner.nextLine();
                        System.out.println("Deadline (dd-MM-YY): ");
                        Date newDeadline = dateFormat.parse(scanner.next());
                        System.out.println("Priority: ");
                        int newPriority = scanner.nextInt();

                        taskManager.modifyTask(taskToModify, newTitle, newDescription, newDeadline, newPriority);
                    } 
                    catch (ParseException | InputMismatchException e) 
                    {
                        System.out.println("Invalid input. Please try again.");
                        scanner.nextLine();  // Consume the invalid input
                    }
                    break;

                case 3:
                    taskManager.viewTask();
                    break;

                case 4:
                    try 
                    {
                        System.out.println("Enter dependency details:");
                        System.out.print("Dependent Task ID: ");
                        int dependentTask1 = scanner.nextInt();
                        System.out.print("Task Id on which Dependent task is dependent: ");
                        int dependentTask2 = scanner.nextInt();

                        taskManager.addDependency(dependentTask1, dependentTask2);
                    } 
                    catch (InputMismatchException e) 
                    {
                        System.out.println("Invalid input. Please enter valid Task IDs.");
                        scanner.nextLine();  
                    }
                    break;

                case 5:
                    taskManager.showOverdue();
                    break;

                case 6:
                    System.out.print("Enter Task ID to mark as completed: ");
                    int taskToComplete = scanner.nextInt();
                    taskManager.completeTask(taskToComplete);
                    break;

                case 7:
                    System.out.println("Tasks by Priority:");
                    taskManager.displayPriority();
                    break;

                case 8:
                    System.out.println("Exiting Task Management System");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 8.");
                    break;
            }
        }
    }
}
