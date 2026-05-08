# 🧠 SJF vs Priority Scheduling Simulator

## 📌 Overview

This project is a simulation and comparison of three CPU scheduling algorithms:

* Shortest Job First (SJF) – Non-preemptive
* Priority Scheduling – Preemptive
* Shortest Job First (SJF) – Preemptive(SRTF)

The system allows users to input processes, run both algorithms, visualize execution using Gantt charts, and compare performance metrics such as waiting time and turnaround time.

---

## 🎯 Objective

The goal of this project is to analyze how different scheduling strategies affect:

* Execution order
* Average Waiting Time (WT)
* Average Turnaround Time (TAT)
* Response Time (RT)
* Fairness and starvation

It highlights the trade-off between choosing the shortest job and prioritizing urgent tasks.

---

## ⚙️ Features

* Dynamic process input (ID, Arrival Time, Burst Time, Priority)
* Input validation handling invalid values
* Implementation of:
  
  * Preemptive SJF
  * Non-preemptive SJF
  * Preemptive Priority Scheduling
* Gantt chart visualization
* Detailed results table for each algorithm
* Performance comparison summary

---

## 🖥️ User Interface

The project includes a simple GUI built using Java Swing:

* Input panel for entering processes
* Execution button to run simulation
* Output section displaying:

  * Gantt Charts
  * Metrics tables
  * Comparison results

---

## 🗂️ Project Structure

```
src/
│
├── Main.java
│
├── model/
│   └── Process.java
│
├── scheduling/
│   ├── SJF.java
│   └── PriorityScheduling.java
│
├── gui/
│   ├── MainFrame.java
│   ├── InputPanel.java
│   ├── GanttChartPanel.java
│   └── ResultTable.java
│
├── service/
│   └── SchedulerService.java
│
└── util/
    └── Validator.java
```

---

## 📊 Metrics Calculated

For each process:

* Waiting Time (WT) = Start Time - Arrival Time
* Turnaround Time (TAT) = Finish Time - Arrival Time
* Response Time (RT) = First Execution - Arrival Time

Also calculates:

* Average WT
* Average TAT
* Average RT

---

## 🧪 Test Scenarios

### Scenario A – Basic Workload

Processes with different arrival and burst times.

### Scenario B – Conflict Case

Short job with low priority vs long job with high priority.

### Scenario C – Starvation Case

One process experiences long waiting under one algorithm.

### Scenario D – Validation Case

Invalid inputs (e.g., negative values) are handled properly.

---

## 🔍 Analysis Questions

* Which algorithm gives lower average waiting time?
* Which algorithm gives lower turnaround time?
* Does SJF favor short jobs?
* Does Priority Scheduling favor urgent processes?
* Is there any starvation or unfair delay?

---

## 🏁 Conclusion

The project demonstrates:

* SJF improves average waiting and turnaround time but may cause starvation.
* Priority Scheduling ensures urgent processes are served first but may delay shorter tasks.
* There is a trade-off between efficiency and fairness.

---

## 🚀 How to Run

1. Open the project in any Java IDE (IntelliJ, Eclipse, etc.)
2. Run `Main.java`
3. Enter processes in the format:

```
P1,0,5,2
P2,1,3,1
P3,2,8,3
```

4. Click **Run** to view results

---

## 👥 Team

* Input & Validation
* SJF Implementation
* Priority Implementation
* GUI & Visualization
* Testing & Analysis

---

## 📌 Notes

* Priority rule: smaller number = higher priority
* Tie-breaking is handled based on arrival time

---

## 💡 Final Thought

Choosing a scheduling algorithm depends on system requirements:

* For efficiency → SJF
* For urgency → Priority Scheduling

---
