import java.util.List;

public class PriorityScheduler {

    public static class Process_Priority {

        String processId;
        int burstTime;
        int arrivalTime;
        int priority;

        boolean isCompleted = false;
        int remainingTime;
        int startTime = -1;
        int completionTime;
        int waitingTime;
        int turnaroundTime;
        int responseTime;

        public Process_Priority(String processId, int burstTime, int arrivalTime, int priority) {

            if (processId == null || processId.isEmpty()) {
                throw new IllegalArgumentException("Process ID cannot be empty");
            }
            if (burstTime <= 0) {
                throw new IllegalArgumentException("Burst time must be > 0");
            }
            if (arrivalTime < 0) {
                throw new IllegalArgumentException("Arrival time must be >= 0");
            }
            if (priority <= 0) {
                throw new IllegalArgumentException("Priority must be > 0");
            }

            this.processId = processId;
            this.burstTime = burstTime;
            this.arrivalTime = arrivalTime;
            this.priority = priority;
            this.remainingTime = burstTime;
        }
    }

    //  IMPORTANT: static
    public static void scheduleProcesses(List<Process_Priority> processes) {

        int currentTime = 0;
        int completed = 0;
        int n = processes.size();

        while (completed < n) {

            Process_Priority bestProcess = null;
            int minPriority = Integer.MAX_VALUE;

            for (Process_Priority p : processes) {

                if (p.arrivalTime <= currentTime && !p.isCompleted) {

                    if (p.priority < minPriority) {
                        minPriority = p.priority;
                        bestProcess = p;
                    }

                    else if (p.priority == minPriority) {
                        if (bestProcess == null ||
                                p.arrivalTime < bestProcess.arrivalTime) {
                            bestProcess = p;
                        }
                    }
                }
            }

            if (bestProcess == null) {
                currentTime++;
                continue;
            }

            if (bestProcess.startTime == -1) {
                bestProcess.startTime = currentTime;
                bestProcess.responseTime =
                        bestProcess.startTime - bestProcess.arrivalTime;
            }

            bestProcess.remainingTime--;
            currentTime++;

            if (bestProcess.remainingTime == 0) {
                bestProcess.isCompleted = true;
                completed++;

                bestProcess.completionTime = currentTime;

                bestProcess.turnaroundTime =
                        bestProcess.completionTime - bestProcess.arrivalTime;

                bestProcess.waitingTime =
                        bestProcess.turnaroundTime - bestProcess.burstTime;
            }
        }
    }
}
