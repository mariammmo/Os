import java.util.Scanner;

public class sjf {

    static class Process {
        int id;
        int at;   
        int bt;   
        int rem;  // remaining time (used during scheduling, though non-preemptive)
        int wt;  
        int tat;  
        int rt;   // response time 
        boolean done;
        boolean startedOnce;  
    }

    static void sjf(Process[] p, int n) {

        int currentTime = 0;   
        int completed = 0;      
        while (completed < n) {

            // find the shortest available job
            int shortest = -1;

            for (int i = 0; i < n; i++) {
                // process must have arrived AND not finished
                if (p[i].at <= currentTime && !p[i].done) {

                    if (shortest == -1) {
                        shortest = i;
                    } else if (p[i].bt < p[shortest].bt) {
                        shortest = i;
                    } else if (p[i].bt == p[shortest].bt
                            && p[i].at < p[shortest].at) {
                        // tie-break but here earlier arrival wins
                        shortest = i;
                    }
                }
            }

            // if nothing is ready, CPU is idle
            if (shortest == -1) {
                currentTime++;
                continue;
            }

            // run the chosen process to completion
            Process cur = p[shortest];

            // response time = first moment the CPU works on this process
            cur.rt = currentTime - cur.at;
            currentTime += cur.bt;

            // compute metrics on completion
            cur.tat = currentTime - cur.at;   // TAT = finish - arrival
            cur.wt = cur.tat - cur.bt;        // WT  = TAT - burst
            cur.done = true;
            completed++;
        }

        // compute averages
        float total_wt = 0, total_tat = 0, total_rt = 0;
        for (int i = 0; i < n; i++) {
            total_wt += p[i].wt;
            total_tat += p[i].tat;
            total_rt += p[i].rt;
        }
        float avg_wt = total_wt / n;
        float avg_tat = total_tat / n;
        float avg_rt = total_rt / n;

        // display results table
        System.out.println("\nProcess  Arrival  Burst  Waiting  Turnaround  Response");
        System.out.println("─────────────────────────────────────────────────────────");
        for (int i = 0; i < n; i++) {
            System.out.printf(
                    "P%-7d %-8d %-6d %-8d %-11d %-8d%n",
                    p[i].id, p[i].at, p[i].bt,
                    p[i].wt, p[i].tat, p[i].rt
            );
        }
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.printf("Average Waiting Time    : %.2f%n", avg_wt);
        System.out.printf("Average Turnaround Time : %.2f%n", avg_tat);
        System.out.printf("Average Response Time   : %.2f%n", avg_rt);
    }

    /** Read a positive integer from the user with a clear error loop */
    static int readPositiveInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                int v = sc.nextInt();
                if (v > 0) return v;
                System.out.println("  ✗  Must be greater than zero. Try again.");
            } else {
                System.out.println("  ✗  Invalid input — please enter an integer.");
                sc.next();
            }
        }
    }

    /** Read a non‑negative integer (0 is allowed for arrival time) */
    static int readNonNegInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                int v = sc.nextInt();
                if (v >= 0) return v;
                System.out.println("  ✗  Must be 0 or greater. Try again.");
            } else {
                System.out.println("  ✗  Invalid input — please enter an integer.");
                sc.next();
            }
        }
    } 
}
