/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.os_project;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hp
 */
public class OS_project {

    public static void main(String[] args) {
        
        List<Process> processes = new ArrayList<>();
        
        processes.add(new Process("P1", 8, 0, 3));
        processes.add(new Process("P2", 4, 1, 1)); 
        processes.add(new Process("P3", 2, 2, 2));


        PriorityScheduler scheduler = new PriorityScheduler();
        scheduler.scheduleProcesses(processes);

        System.out.println("Process\tAT\tBT\tPrio\tCT\tTAT\tWT\tRT");
        for (Process p : processes) {
            System.out.println(p.processId + "\t" + 
                               p.arrivalTime + "\t" + 
                               p.burstTime + "\t" + 
                               p.priority + "\t" + 
                               p.completionTime + "\t" + 
                               p.turnaroundTime + "\t" + 
                               p.waitingTime + "\t" + 
                               p.responseTime);
        }
    }
}
