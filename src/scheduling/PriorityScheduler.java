import java.util.List;

/**
 *
 * @author hp
 */
public class PriorityScheduler {
    public void scheduleProcesses(List<Process> processes) {
        int currentTime = 0;
        int completed = 0;
        int n = processes.size(); //عدد ال processes
        // هيفضل شغال لحد ما كل ال processes اللي عندنا تخلص 
        while (completed < n) {
            Process bestProcess = null;
            int minPriority = Integer.MAX_VALUE;

            for (Process p : processes) {
                // process لازم تكون وصلت ومش completed 
                if (p.arrivalTime <= currentTime && !p.isCompleted) {
                    // lw rkm el priority bta3ha a2l yb2a de elly httnfz 
                    if (p.priority < minPriority) {
                        minPriority = p.priority;
                        bestProcess = p;
                    } 
                    // لو اتنين نفس الرقم يبقي اللي جت الاول تتنفذ الاول  tie Breaking
                    else if (p.priority == minPriority) {
                        if (bestProcess == null || p.arrivalTime < bestProcess.arrivalTime) {
                            bestProcess = p;
                        }
                    }
                }
            }
            // لو مفيش process يبقي ال cpu idle
            if (bestProcess == null) {
                currentTime++;
                continue;
            }
            // تحديث وقت الاستجابة (Response Time) عند أول تشغيل فقط 
            if (bestProcess.startTime == -1) {
                bestProcess.startTime = currentTime;
                bestProcess.responseTime = bestProcess.startTime - bestProcess.arrivalTime;
            }
            // ينفذ (1 unit) و يرجع يختار تاني Preemptive
            bestProcess.remainingTime--;
            currentTime++;
            //لو ال process خلصت
            if (bestProcess.remainingTime == 0) {
                bestProcess.isCompleted = true;
                completed++;
                bestProcess.completionTime = currentTime;
                
                bestProcess.turnaroundTime = bestProcess.completionTime - bestProcess.arrivalTime;
                bestProcess.waitingTime = bestProcess.turnaroundTime - bestProcess.burstTime;
            }
        }
    }
}
