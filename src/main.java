import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int n = sc.nextInt();

        System.out.println("\nChoose the scheduling algorithm:");
        System.out.println("1. Shortest Job First (SJF)");
        System.out.println("2. Priority Scheduling");

        int choice = sc.nextInt();

        // ===================== SJF =====================
        if (choice == 1) {

            SJF.Process[] sjfProcesses = new SJF.Process[n];

            for (int i = 0; i < n; i++) {

                sjfProcesses[i] = new SJF.Process();

                System.out.print("Enter Process ID for process " + (i + 1) + ": ");
                sjfProcesses[i].id = sc.nextInt();

                while (true) {
                    System.out.print("Enter Arrival Time: ");
                    sjfProcesses[i].at = sc.nextInt();
                    if (sjfProcesses[i].at >= 0) break;
                    System.out.println("Invalid Arrival Time!");
                }

                while (true) {
                    System.out.print("Enter Burst Time: ");
                    sjfProcesses[i].bt = sc.nextInt();
                    if (sjfProcesses[i].bt > 0) break;
                    System.out.println("Invalid Burst Time!");
                }
            }

            SJF.SJF(sjfProcesses, n);

            try {

                File resultsDir = new File("results");
                if (!resultsDir.exists()) resultsDir.mkdirs();

                PrintWriter writer = new PrintWriter(
                        new FileWriter("results/sjf_results.txt", true));

                writer.println("\nProcess\tArrival\tBurst\tWaiting\tTurnaround\tResponse");

                for (SJF.Process p : sjfProcesses) {
                    writer.printf("%d\t%d\t%d\t%d\t%d\t%d\n",
                            p.id, p.at, p.bt, p.wt, p.tat, p.rt);
                }

                writer.close();
                System.out.println("\nSJF Results saved");

            } catch (IOException e) {
                System.out.println("Error saving file");
            }
        }

        // ===================== PRIORITY =====================
        else if (choice == 2) {

            List<PriorityScheduler.Process_Priority> processes =
                    new ArrayList<>();

            for (int i = 0; i < n; i++) {

                System.out.print("Enter Process ID: ");
                String id = sc.next();

                int burst;
                while (true) {
                    System.out.print("Burst Time: ");
                    burst = sc.nextInt();
                    if (burst > 0) break;
                }

                int priority;
                while (true) {
                    System.out.print("Priority: ");
                    priority = sc.nextInt();
                    if (priority > 0) break;
                }

                int arrival;
                while (true) {
                    System.out.print("Arrival Time: ");
                    arrival = sc.nextInt();
                    if (arrival >= 0) break;
                }

                processes.add(
                        new PriorityScheduler.Process_Priority(
                                id, burst, arrival, priority
                        )
                );
            }

            // 🔥 STATIC CALL (important fix)
            PriorityScheduler.scheduleProcesses(processes);

            System.out.println(
                    "\nProcess\tBurst\tPriority\tArrival\tWaiting\tTurnaround\tResponse"
            );

            for (PriorityScheduler.Process_Priority p : processes) {

                System.out.printf(
                        "%s\t%d\t%d\t\t%d\t%d\t%d\t\t%d\n",
                        p.processId,
                        p.burstTime,
                        p.priority,
                        p.arrivalTime,
                        p.waitingTime,
                        p.turnaroundTime,
                        p.responseTime
                );
            }

            System.out.println("\nPriority Scheduling Done");
        }

        else {
            System.out.println("Invalid choice");
        }

        sc.close();
    }
}