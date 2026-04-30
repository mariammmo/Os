import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;




class main {
    public static void main(String[] args) 
    {
        Scanner sc = new Scanner(System.in);
        //chiose the scheduling algorithm
        System.out.println("Choose the scheduling algorithm:");
        System.out.println("1. Shortest Job First (SJF)");
        System.out.println("2. Priority Scheduling");
        int choice = sc.nextInt();
        List<Process> processes = new ArrayList<>();
     if(choice == 1) {
          System.out.print("Enter the number of processes: ");
            int n = sc.nextInt();

            SJF.Process[] sjfProcesses = new SJF.Process[n];

            for (int i = 0; i < n; i++) {

                sjfProcesses[i] = new SJF.Process();

                System.out.print("Enter Process ID for process " + (i + 1) + ": ");
                sjfProcesses[i].id = sc.nextInt();

                while (true) {

                    System.out.print("Enter Burst Time for process " + sjfProcesses[i].id + ": ");
                    sjfProcesses[i].bt = sc.nextInt();

                    if (sjfProcesses[i].bt > 0) {
                        break;
                    }

                    System.out.println("Invalid input! Burst Time must be greater than 0.");
                }
            }

            SJF.sjf(sjfProcesses, n);
                    
        }
     else if(choice == 2) {
                    System.out.print("Enter the number of processes: ");
                int n = sc.nextInt();

                for (int i = 0; i < n; i++) {

            System.out.print("Enter Process ID for process " + (i + 1) + ": ");
            String processId = sc.next();

            int burstTime;

            while (true) {
                System.out.print("Enter Burst Time for process " + processId + ": ");
                burstTime = sc.nextInt();

                if (burstTime > 0) {
                    break;
                }

                System.out.println("Invalid input! Burst Time must be greater than 0.");
            }

            int priority;

            while (true) {
                System.out.print("Enter Priority for process " + processId + ": ");
                priority = sc.nextInt();

                if (priority > 0) {
                    break;
                }

                System.out.println("Invalid input! Priority must be greater than 0.");
            }

            int arrivalTime;

            while (true) {
                System.out.print("Enter Arrival Time for process " + processId + ": ");
                arrivalTime = sc.nextInt();

                if (arrivalTime >= 0) {
                    break;
                }

                System.out.println("Invalid input! Arrival Time must be >= 0.");
            }

            Process p = new Process(processId, burstTime, arrivalTime, priority);

            processes.add(p);
        }

        sc.close();

        PriorityScheduler scheduler = new PriorityScheduler();
        scheduler.scheduleProcesses(processes);

        // Display results
        System.out.println("\nProcess\tBurst Time\tPriority\tArrival Time\tWaiting Time\tTurnaround Time\tResponse Time");
        System.out.println("---------------------------------------------------------------------------------------------");
        for (Process p : processes) {
            System.out.printf("%s\t%d\t\t%d\t\t%d\t\t%d\t\t%d\t\t%d\n", 
                p.processId, p.burstTime, p.priority, p.arrivalTime, 
                p.waitingTime, p.turnaroundTime, p.responseTime);
        }
        // Calculate and display averages for priority scheduling
        double avgWaitingTime = processes.stream().mapToInt(p -> p.waitingTime).average().orElse(0);
        double avgTurnaroundTime = processes.stream().mapToInt(p -> p.turnaroundTime).average().orElse(0);
        double avgResponseTime = processes.stream().mapToInt(p -> p.responseTime).average().orElse(0);
        System.out.printf("\nAverage Waiting Time: %.2f\n", avgWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", avgTurnaroundTime);
        System.out.printf("Average Response Time: %.2f\n", avgResponseTime);

        } else {
            System.out.println("Invalid choice. Please run the program again and select either 1 or 2.");
        }
     
    }
    
    
}   