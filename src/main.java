import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class main {

    // ===================== TABLE HEADER =====================
    static void printHeader() {

        System.out.println(
                "============================================================================");

        System.out.printf(
                "%-10s %-10s %-10s %-10s %-12s %-12s\n",
                "Process",
                "Arrival",
                "Burst",
                "Waiting",
                "Turnaround",
                "Response"
        );

        System.out.println(
                "============================================================================");
    }

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

                // ID موحد
                sjfProcesses[i].id = i + 1;

                while (true) {

                    System.out.print(
                            "Enter Arrival Time for P"
                                    + sjfProcesses[i].id + ": ");

                    sjfProcesses[i].at = sc.nextInt();

                    if (sjfProcesses[i].at >= 0) {
                        break;
                    }

                    System.out.println("Invalid Arrival Time!");
                }

                while (true) {

                    System.out.print(
                            "Enter Burst Time for P"
                                    + sjfProcesses[i].id + ": ");

                    sjfProcesses[i].bt = sc.nextInt();

                    if (sjfProcesses[i].bt > 0) {
                        break;
                    }

                    System.out.println("Invalid Burst Time!");
                }
            }

            // تشغيل SJF
            SJF.SJF(sjfProcesses, n);

            // ===================== DISPLAY =====================
            System.out.println("\n================ SJF RESULTS ================\n");

            printHeader();

            for (SJF.Process p : sjfProcesses) {

                System.out.printf(
                        "%-10s %-10d %-10d %-10d %-12d %-12d\n",
                        "P" + p.id,
                        p.at,
                        p.bt,
                        p.wt,
                        p.tat,
                        p.rt
                );
            }

            System.out.println(
                    "============================================================================");

            // ===================== SAVE FILE =====================
            try {

                File resultsDir = new File("results");

                if (!resultsDir.exists()) {
                    resultsDir.mkdirs();
                }

                PrintWriter writer =
                        new PrintWriter(
                                new FileWriter(
                                        "results/sjf_results.txt",
                                        true));

                writer.println(
                        "\n================ SJF RESULTS ================\n");

                writer.printf(
                        "%-10s %-10s %-10s %-10s %-12s %-12s\n",
                        "Process",
                        "Arrival",
                        "Burst",
                        "Waiting",
                        "Turnaround",
                        "Response"
                );

                for (SJF.Process p : sjfProcesses) {

                    writer.printf(
                            "%-10s %-10d %-10d %-10d %-12d %-12d\n",
                            "P" + p.id,
                            p.at,
                            p.bt,
                            p.wt,
                            p.tat,
                            p.rt
                    );
                }

                writer.close();

                System.out.println("\nSJF Results saved successfully.");

            } catch (IOException e) {

                System.out.println("Error saving file");
            }
        }

        // ===================== PRIORITY =====================
        else if (choice == 2) {

            List<PriorityScheduler.Process_Priority> processes =
                    new ArrayList<>();

            for (int i = 0; i < n; i++) {

                // ID موحد
                String id = "P" + (i + 1);

                int burst;

                while (true) {

                    System.out.print(
                            "Enter Burst Time for "
                                    + id + ": ");

                    burst = sc.nextInt();

                    if (burst > 0) {
                        break;
                    }

                    System.out.println("Invalid Burst Time!");
                }

                int priority;

                while (true) {

                    System.out.print(
                            "Enter Priority for "
                                    + id + ": ");

                    priority = sc.nextInt();

                    if (priority > 0) {
                        break;
                    }

                    System.out.println("Invalid Priority!");
                }

                int arrival;

                while (true) {

                    System.out.print(
                            "Enter Arrival Time for "
                                    + id + ": ");

                    arrival = sc.nextInt();

                    if (arrival >= 0) {
                        break;
                    }

                    System.out.println("Invalid Arrival Time!");
                }

                processes.add(
                        new PriorityScheduler.Process_Priority(
                                id,
                                burst,
                                arrival,
                                priority
                        )
                );
            }

            // تشغيل Priority
            PriorityScheduler.scheduleProcesses(processes);

            // ===================== DISPLAY =====================
            System.out.println(
                    "\n================ PRIORITY RESULTS ================\n");

            printHeader();

            for (PriorityScheduler.Process_Priority p : processes) {

                System.out.printf(
                        "%-10s %-10d %-10d %-10d %-10d %-12d %-12d\n",
                        p.processId,
                        p.priority,
                        p.arrivalTime,
                        p.burstTime,
                        p.waitingTime,
                        p.turnaroundTime,
                        p.responseTime
                );
            }

            System.out.println(
                    "============================================================================");

            // ===================== SAVE FILE =====================
            try {

                File resultsDir = new File("results");

                if (!resultsDir.exists()) {
                    resultsDir.mkdirs();
                }

                PrintWriter writer =
                        new PrintWriter(
                                new FileWriter(
                                        "results/priority_results.txt",
                                        true));

                writer.println(
                        "\n================ PRIORITY RESULTS ================\n");

                writer.printf(
                        "%-10s %-10s %-10s %-10s %-10s %-12s %-12s\n",
                        "Process",
                        "Arrival",
                        "Burst",
                        "Priority",
                        "Waiting",
                        "Turnaround",
                        "Response"
                );

                for (PriorityScheduler.Process_Priority p : processes) {

                    writer.printf(
                            "%-10s %-10d %-10d %-10d %-10d %-12d %-12d\n",
                            p.processId,
                            p.arrivalTime,
                            p.burstTime,
                            p.priority,
                            p.waitingTime,
                            p.turnaroundTime,
                            p.responseTime
                    );
                }

                writer.close();

                System.out.println(
                        "\nPriority Scheduling Results saved successfully.");

            } catch (IOException e) {

                System.out.println("Error saving file");
            }
        }

        else {

            System.out.println(
                    "Invalid choice. Please run the program again.");
        }

        sc.close();
    }
}