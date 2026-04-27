import java.util.Scanner;

public class SJF {
     //Process class
    static class Process {
        int id;
        int bt;   // burst time
        int wt; 
        int tat; 
        int rt;   
    }

    // SJF scheduling (non-preemptive, all arrive at time 0)
    static void sjf(Process[] p, int n) {
        // Sort processes by burst time (bubble sort)
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (p[i].bt > p[j].bt) {
                    Process temp = p[i];
                    p[i] = p[j];
                    p[j] = temp;
                }
            }
        }

        int total_wt = 0, total_tat = 0, total_rt = 0;

        // First process: waiting time = 0, response time = 0
        p[0].wt = 0;
        p[0].rt = 0;

        // Calculate waiting time and response time
        for (int i = 1; i < n; i++) {
            p[i].wt = p[i - 1].wt + p[i - 1].bt;
            total_wt += p[i].wt;
            p[i].rt = p[i].wt;   // because arrival time = 0
            total_rt += p[i].rt;
        }
        total_rt += p[0].rt;  // add first process RT (0)

        // Calculate turnaround time
        for (int i = 0; i < n; i++) {
            p[i].tat = p[i].wt + p[i].bt;
            total_tat += p[i].tat;
        }

        float avg_wt = (float) total_wt / n;
        float avg_tat = (float) total_tat / n;
        float avg_rt = (float) total_rt / n;

        // Display results
        System.out.println("\nProcess\tBurst Time\tWaiting Time\tTurnaround Time\tResponse Time");
        System.out.println("-------------------------------------------------------------------------");
        for (int i = 0; i < n; i++) {
            System.out.printf("%d\t%d\t\t%d\t\t%d\t\t%d\n", 
                p[i].id, p[i].bt, p[i].wt, p[i].tat, p[i].rt);
        }
        System.out.printf("\nAverage Waiting Time: %.2f\n", avg_wt);
        System.out.printf("Average Turnaround Time: %.2f\n", avg_tat);
        System.out.printf("Average Response Time: %.2f\n", avg_rt);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // --- INPUT VALIDATION for number of processes ---
        int n = 0;
        while (true) {
            System.out.print("Enter the number of processes: ");
            if (sc.hasNextInt()) {
                n = sc.nextInt();
                if (n > 0) {
                    break;
                } else {
                    System.out.println("Error: Number of processes must be positive. Try again.");
                }
            } else {
                System.out.println("Error: Invalid input. Please enter an integer.");
                sc.next(); 
            }
        }

        Process[] p = new Process[n];
        System.out.println("Enter Burst Time for each process:");

        for (int i = 0; i < n; i++) {
            p[i] = new Process();
            p[i].id = i + 1;

            // --- INPUT VALIDATION for burst time ---
            int bt = 0;
            while (true) {
                System.out.print("Process " + p[i].id+ " Burst Time: ");
                if (sc.hasNextInt()) {
                    bt = sc.nextInt();
                    if (bt > 0) {
                        break;
                    } else {
                        System.out.println("Error: Burst time must be greater than zero. Try again.");
                    }
                } else {
                    System.out.println("Error: Invalid input. Please enter a positive integer.");
                    sc.next(); 
                }
            }
            p[i].bt = bt;
        }

        sc.close();
        sjf(p, n);
    }
}
