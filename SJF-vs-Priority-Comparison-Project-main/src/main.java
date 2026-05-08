import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class main {

    // ===================== TABLE HEADER =====================
    static void printHeaderSJF() {

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

    static void printHeaderPriority() {

        System.out.println(
                "======================================================================================");

        System.out.printf(
                "%-10s %-10s %-10s %-10s %-10s %-12s %-12s\n",
                "Process",
                "Arrival",
                "Burst",
                "Priority",
                "Waiting",
                "Turnaround",
                "Response"
        );

        System.out.println(
                "======================================================================================");
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int n = sc.nextInt();

        System.out.println("\nChoose the scheduling algorithm:");
        System.out.println("1. Shortest Job First (Non-Preemptive)");
        System.out.println("2. Priority Scheduling");
        System.out.println("3. Shortest Remaining Time First (Preemptive SJF)");

        int choice = sc.nextInt();

        // ===================== SJF NON PREEMPTIVE =====================
        if (choice == 1) {

            SJF.Process[] sjfProcesses = new SJF.Process[n];
            String[] processNames = new String[n];

            for (int i = 0; i < n; i++) {

                sjfProcesses[i] = new SJF.Process();

                System.out.print("Enter Process ID: ");
                processNames[i] = sc.next();

                sjfProcesses[i].id = i + 1;

                while (true) {

                    System.out.print(
                            "Enter Arrival Time for "
                                    + processNames[i] + ": ");

                    sjfProcesses[i].at = sc.nextInt();

                    if (sjfProcesses[i].at >= 0) {
                        break;
                    }

                    System.out.println("Invalid Arrival Time!");
                }

                while (true) {

                    System.out.print(
                            "Enter Burst Time for "
                                    + processNames[i] + ": ");

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

            printHeaderSJF();

            double totalWaiting = 0;
            double totalTurnaround = 0;
            double totalResponse = 0;

            for (int i = 0; i < sjfProcesses.length; i++) {

                SJF.Process p = sjfProcesses[i];

                System.out.printf(
                        "%-10s %-10d %-10d %-10d %-12d %-12d\n",
                        processNames[i],
                        p.at,
                        p.bt,
                        p.wt,
                        p.tat,
                        p.rt
                );

                totalWaiting += p.wt;
                totalTurnaround += p.tat;
                totalResponse += p.rt;
            }

            double avgWaiting =
                    totalWaiting / sjfProcesses.length;

            double avgTurnaround =
                    totalTurnaround / sjfProcesses.length;

            double avgResponse =
                    totalResponse / sjfProcesses.length;

            System.out.println(
                    "============================================================================");

            System.out.printf(
                    "\nAverage Waiting Time: %.2f\n",
                    avgWaiting
            );

            System.out.printf(
                    "Average Turnaround Time: %.2f\n",
                    avgTurnaround
            );

            System.out.printf(
                    "Average Response Time: %.2f\n",
                    avgResponse
            );

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

                for (int i = 0; i < sjfProcesses.length; i++) {

                    SJF.Process p = sjfProcesses[i];

                    writer.printf(
                            "%-10s %-10d %-10d %-10d %-12d %-12d\n",
                            processNames[i],
                            p.at,
                            p.bt,
                            p.wt,
                            p.tat,
                            p.rt
                    );
                }

                writer.println(
                        "============================================================================");

                writer.printf(
                        "\nAverage Waiting Time: %.2f\n",
                        avgWaiting
                );

                writer.printf(
                        "Average Turnaround Time: %.2f\n",
                        avgTurnaround
                );

                writer.printf(
                        "Average Response Time: %.2f\n",
                        avgResponse
                );

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

                System.out.print("Enter Process ID: ");
                String id = sc.next();

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

            printHeaderPriority();

            double totalWaiting = 0;
            double totalTurnaround = 0;
            double totalResponse = 0;

            for (PriorityScheduler.Process_Priority p : processes) {

                System.out.printf(
                        "%-10s %-10d %-10d %-10d %-10d %-12d %-12d\n",
                        p.processId,
                        p.arrivalTime,
                        p.burstTime,
                        p.priority,
                        p.waitingTime,
                        p.turnaroundTime,
                        p.responseTime
                );

                totalWaiting += p.waitingTime;
                totalTurnaround += p.turnaroundTime;
                totalResponse += p.responseTime;
            }

            double avgWaiting =
                    totalWaiting / processes.size();

            double avgTurnaround =
                    totalTurnaround / processes.size();

            double avgResponse =
                    totalResponse / processes.size();

            System.out.println(
                    "======================================================================================");

            System.out.printf(
                    "\nAverage Waiting Time: %.2f\n",
                    avgWaiting
            );

            System.out.printf(
                    "Average Turnaround Time: %.2f\n",
                    avgTurnaround
            );

            System.out.printf(
                    "Average Response Time: %.2f\n",
                    avgResponse
            );

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

                writer.println(
                        "======================================================================================");

                writer.printf(
                        "\nAverage Waiting Time: %.2f\n",
                        avgWaiting
                );

                writer.printf(
                        "Average Turnaround Time: %.2f\n",
                        avgTurnaround
                );

                writer.printf(
                        "Average Response Time: %.2f\n",
                        avgResponse
                );

                writer.close();

                System.out.println(
                        "\nPriority Scheduling Results saved successfully.");

            } catch (IOException e) {

                System.out.println("Error saving file");
            }
        }

        // ===================== SRTF PREEMPTIVE =====================
        else if (choice == 3) {

            SJF_preemptive.Process[] p =
                    new SJF_preemptive.Process[n];

            for (int i = 0; i < n; i++) {

                p[i] = new SJF_preemptive.Process();

                p[i].id = i + 1;

                System.out.println("\nProcess P" + p[i].id + ":");

                while (true) {

                    System.out.print("Arrival Time: ");

                    p[i].at = sc.nextInt();

                    if (p[i].at >= 0) {
                        break;
                    }

                    System.out.println("Invalid Arrival Time!");
                }

                while (true) {

                    System.out.print("Burst Time: ");

                    p[i].bt = sc.nextInt();

                    if (p[i].bt > 0) {
                        break;
                    }

                    System.out.println("Invalid Burst Time!");
                }

                p[i].startedOnce = false;
                p[i].done = false;
            }

            // تشغيل SRTF
            SJF_preemptive.SRTF_Preemptive(p, n);

            // ===================== SAVE FILE =====================
            try {

                File resultsDir = new File("results");

                if (!resultsDir.exists()) {
                    resultsDir.mkdirs();
                }

                PrintWriter writer =
                        new PrintWriter(
                                new FileWriter(
                                        "results/srtf_results.txt",
                                        true));

                writer.println(
                        "\n================ SRTF RESULTS ================\n");

                writer.printf(
                        "%-10s %-10s %-10s %-10s %-12s %-12s\n",
                        "Process",
                        "Arrival",
                        "Burst",
                        "Waiting",
                        "Turnaround",
                        "Response"
                );

                double totalWaiting = 0;
                double totalTurnaround = 0;
                double totalResponse = 0;

                for (int i = 0; i < p.length; i++) {

                    writer.printf(
                            "P%-9d %-10d %-10d %-10d %-12d %-12d\n",
                            p[i].id,
                            p[i].at,
                            p[i].bt,
                            p[i].wt,
                            p[i].tat,
                            p[i].rt
                    );

                    totalWaiting += p[i].wt;
                    totalTurnaround += p[i].tat;
                    totalResponse += p[i].rt;
                }

                double avgWaiting =
                        totalWaiting / p.length;

                double avgTurnaround =
                        totalTurnaround / p.length;

                double avgResponse =
                        totalResponse / p.length;

                writer.println(
                        "============================================================================");

                writer.printf(
                        "\nAverage Waiting Time: %.2f\n",
                        avgWaiting
                );

                writer.printf(
                        "Average Turnaround Time: %.2f\n",
                        avgTurnaround
                );

                writer.printf(
                        "Average Response Time: %.2f\n",
                        avgResponse
                );

                writer.close();

                System.out.println(
                        "\nSRTF Results saved successfully.");

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