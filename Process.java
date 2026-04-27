/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.os_project;

/**
 *
 * @author hp
 */
public class Process {

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

    public Process(String processId, int burstTime, int arrivalTime, int priority) {
        if (processId == null || processId.isEmpty()) {
            throw new IllegalArgumentException("Process ID cannot be empty");
        }
        if (burstTime <= 0) {
            throw new IllegalArgumentException("Burst time must be > 0");
        }

        if (arrivalTime < 0) {
            throw new IllegalArgumentException("Arrival time must be >= 0");
        }

        if (priority < 0) {
            throw new IllegalArgumentException("Priority must be >= 0");
        }
        this.processId = processId;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.priority = priority;
        this.remainingTime = burstTime;
    }

}
