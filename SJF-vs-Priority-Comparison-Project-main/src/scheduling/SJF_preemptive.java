import java.util.Scanner;

public class SJF_preemptive {
    static class Process {
        int id;
        int at;   
        int bt;   
        int rem;         // Remaining time (Crucial for Preemptive)
        int wt;  
        int tat;  
        int rt;          // Response time 
        boolean done;
        boolean startedOnce;  
    }

    static void SRTF_Preemptive(Process[] p, int n) {
        int currentTime = 0;   
        int completed = 0; 
        
        // Initialize remaining time for all processes
        for(int i=0; i<n; i++) p[i].rem = p[i].bt;

        while (completed < n) {
            int shortest = -1;
            int minRemaining = Integer.MAX_VALUE;

            // Find the process with the shortest REMAINING time at current moment
            for (int i = 0; i < n; i++) {
                if (p[i].at <= currentTime && !p[i].done) {
                    if (p[i].rem < minRemaining) {
                        minRemaining = p[i].rem;
                        shortest = i;
                    }
                    // Tie-breaker: earlier arrival time
                    else if (p[i].rem == minRemaining && p[i].at < p[shortest].at) {
                        shortest = i;
                    }
                }
            }

            if (shortest == -1) {
                currentTime++;
                continue;
            }

            Process cur = p[shortest];

            // Response Time: first time the process gets the CPU
            if (!cur.startedOnce) {
                cur.rt = currentTime - cur.at;
                cur.startedOnce = true;
            }

            // Execute for only 1 unit of time (The Preemptive part)
            cur.rem--;
            currentTime++;

            // If process finishes
            if (cur.rem == 0) {
                cur.done = true;
                completed++;
                cur.tat = currentTime - cur.at; 
                cur.wt = cur.tat - cur.bt;
            }
        }

        displayResults(p, n);
    }

    static void displayResults(Process[] p, int n) {
        float total_wt = 0, total_tat = 0, total_rt = 0;
        for (int i = 0; i < n; i++) {
            total_wt += p[i].wt;
            total_tat += p[i].tat;
            total_rt += p[i].rt;
        }

        System.out.println("\nProcess  Arrival  Burst  Waiting  Turnaround  Response");
        System.out.println("─────────────────────────────────────────────────────────");
        for (int i = 0; i < n; i++) {
            System.out.printf("P%-7d %-8d %-6d %-8d %-11d %-8d%n",
                    p[i].id, p[i].at, p[i].bt, p[i].wt, p[i].tat, p[i].rt);
        }
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.printf("Average Waiting Time    : %.2f%n", total_wt / n);
        System.out.printf("Average Turnaround Time : %.2f%n", total_tat / n);
        System.out.printf("Average Response Time   : %.2f%n", total_rt / n);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("--- Preemptive SJF (SRTF) Scheduler ---");
        
        int n = readPositiveInt(sc, "Enter number of processes: ");
        Process[] p = new Process[n];

        for (int i = 0; i < n; i++) {
            p[i] = new Process();
            p[i].id = i + 1;
            System.out.println("\nProcess P" + p[i].id + ":");
            p[i].at = readNonNegInt(sc, "  Arrival Time: ");
            p[i].bt = readPositiveInt(sc, "  Burst Time: ");
            p[i].startedOnce = false;
            p[i].done = false;
        }

        SRTF_Preemptive(p, n);
        sc.close();
    }

    static int readPositiveInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                int v = sc.nextInt();
                if (v > 0) return v;
            } else sc.next();
            System.out.println(" ✗ Invalid. Enter a positive integer.");
        }
    }

    static int readNonNegInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                int v = sc.nextInt();
                if (v >= 0) return v;
            } else sc.next();
            System.out.println(" ✗ Invalid. Enter 0 or greater.");
        }
    }
}
