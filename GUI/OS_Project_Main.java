/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Maya sameh
 */
// OS_Project_Main.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OS_Project_Main extends JFrame {
    
    private List<Process> processList;
    private JButton btnAddProcess;
    private JButton btnViewProcessTable;
    private JButton btnRunSJF;
    private JButton btnRunPriority;
    private JButton btnViewComparison;
    private JButton btnViewConclusion;
    
    private SJFResult latestSJFResult;
    private PriorityResult latestPriorityResult;
    
    private JDialog processTableDialog;
    private DefaultTableModel processTableModel;
    private JDialog comparisonDialog;
    private DefaultTableModel comparisonModel;
    
    public OS_Project_Main() {
        processList = new ArrayList<>();
        latestSJFResult = null;
        latestPriorityResult = null;
        initializeMainWindow();
    }
    
    private void initializeMainWindow() {
        setTitle("CPU Scheduling Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("CPU SCHEDULING SIMULATOR");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        Dimension buttonSize = new Dimension(280, 60);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        
        btnAddProcess = createStyledButton(" ADD NEW PROCESS", buttonSize, buttonFont, new Color(60, 179, 113));
        btnAddProcess.addActionListener(e -> openInputWindow());
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(btnAddProcess, gbc);
        
        btnViewProcessTable = createStyledButton(" VIEW PROCESS TABLE", buttonSize, buttonFont, new Color(100, 149, 237));
        btnViewProcessTable.addActionListener(e -> openProcessTableWindow());
        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(btnViewProcessTable, gbc);
        
        btnRunSJF = createStyledButton(" RUN SJF ALGORITHM", buttonSize, buttonFont, new Color(255, 140, 0));
        btnRunSJF.addActionListener(e -> GanttChartPanel.openSJFWindow(this, processList));
        gbc.gridx = 0;
        gbc.gridy = 2;
        centerPanel.add(btnRunSJF, gbc);
        
        btnRunPriority = createStyledButton(" RUN PRIORITY ALGORITHM", buttonSize, buttonFont, new Color(218, 112, 214));
        GanttChartPanel.openPriorityWindow(this, processList, latestPriorityResult);
        gbc.gridx = 0;
        gbc.gridy = 3;
        centerPanel.add(btnRunPriority, gbc);
        
        btnViewComparison = createStyledButton(" VIEW COMPARISON", buttonSize, buttonFont, new Color(70, 130, 180));
        btnViewComparison.addActionListener(e -> openComparisonWindow());
        gbc.gridx = 0;
        gbc.gridy = 4;
        centerPanel.add(btnViewComparison, gbc);
        
        btnViewConclusion = createStyledButton(" VIEW CONCLUSION", buttonSize, buttonFont, new Color(34, 139, 34));
        btnViewConclusion.addActionListener(e -> openConclusionWindow());
        gbc.gridx = 0;
        gbc.gridy = 5;
        centerPanel.add(btnViewConclusion, gbc);
        
        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(Color.LIGHT_GRAY);
        JLabel statusLabel = new JLabel("Ready | Total Processes: 0");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
        
        new Timer(1000, e -> {
            statusLabel.setText("Ready | Total Processes: " + processList.size());
            if (processTableDialog != null && processTableDialog.isVisible() && processTableModel != null) {
                refreshProcessTable();
            }
            if (comparisonDialog != null && comparisonDialog.isVisible() && comparisonModel != null) {
                refreshComparisonTable();
            }
        }).start();
        
        setVisible(true);
    }
    
    private JButton createStyledButton(String text, Dimension size, Font font, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(size);
        button.setFont(font);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void openInputWindow() {
        JDialog inputDialog = new JDialog(this, "Add New Process", true);
        inputDialog.setSize(600, 300);
        inputDialog.setLocationRelativeTo(this);
        inputDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel lblId = new JLabel("Process ID:*");
        JTextField txtId = new JTextField(15);
        JLabel lblArrival = new JLabel("Arrival Time:*");
        JTextField txtArrival = new JTextField(15);
        JLabel lblBurst = new JLabel("Burst Time:*");
        JTextField txtBurst = new JTextField(15);
        JLabel lblPriority = new JLabel("Priority (Optional):");
        JTextField txtPriority = new JTextField(15);
        
        JButton btnSubmit = new JButton("ADD PROCESS");
        btnSubmit.setBackground(new Color(60, 179, 113));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 14));
        
        gbc.gridx = 0; gbc.gridy = 0; inputDialog.add(lblId, gbc);
        gbc.gridx = 1; gbc.gridy = 0; inputDialog.add(txtId, gbc);
        gbc.gridx = 0; gbc.gridy = 1; inputDialog.add(lblArrival, gbc);
        gbc.gridx = 1; gbc.gridy = 1; inputDialog.add(txtArrival, gbc);
        gbc.gridx = 0; gbc.gridy = 2; inputDialog.add(lblBurst, gbc);
        gbc.gridx = 1; gbc.gridy = 2; inputDialog.add(txtBurst, gbc);
        gbc.gridx = 0; gbc.gridy = 3; inputDialog.add(lblPriority, gbc);
        gbc.gridx = 1; gbc.gridy = 3; inputDialog.add(txtPriority, gbc);
        gbc.gridx = 1; gbc.gridy = 4; inputDialog.add(btnSubmit, gbc);
        
        btnSubmit.addActionListener(e -> {
            try {
                String id = txtId.getText().trim();
                String arrivalStr = txtArrival.getText().trim();
                String burstStr = txtBurst.getText().trim();
                String priorityStr = txtPriority.getText().trim();
                
                if (id.isEmpty() || arrivalStr.isEmpty() || burstStr.isEmpty()) {
                    JOptionPane.showMessageDialog(inputDialog, 
                        "Process ID, Arrival Time, and Burst Time are required!", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int arrivalTime = Integer.parseInt(arrivalStr);
                int burstTime = Integer.parseInt(burstStr);
                Integer priority = priorityStr.isEmpty() ? null : Integer.parseInt(priorityStr);
                
                Process process = new Process(id, arrivalTime, burstTime, priority);
                processList.add(process);
                
                JOptionPane.showMessageDialog(inputDialog, 
                    "Process " + id + " added successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                inputDialog.dispose();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(inputDialog, 
                    "Please enter valid numbers!", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        inputDialog.setVisible(true);
    }
    
    private void openProcessTableWindow() {
        processTableDialog = new JDialog(this, "Process Table", false);
        processTableDialog.setSize(800, 400);
        processTableDialog.setLocationRelativeTo(this);
        
        String[] columns = {"Process ID", "Arrival Time", "Burst Time", "Priority"};
        processTableModel = new DefaultTableModel(columns, 0);
        
        JTable processTable = new JTable(processTableModel);
        processTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(processTable);
        
        refreshProcessTable();
        
        processTableDialog.setLayout(new BorderLayout());
        processTableDialog.add(scrollPane, BorderLayout.CENTER);
        
        processTableDialog.setVisible(true);
    }
    
    private void refreshProcessTable() {
        if (processTableModel != null) {
            processTableModel.setRowCount(0);
            for (Process p : processList) {
                Object[] row = {p.getId(), p.getArrivalTime(), p.getBurstTime(), 
                               p.getPriority() != null ? p.getPriority() : "N/A"};
                processTableModel.addRow(row);
            }
        }
    }
    
    // ========== SJF WINDOW (with Response Time) ==========
    private void openSJFWindow() {
        if (processList.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please add at least one process first!", 
                "No Processes", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JDialog sjfDialog = new JDialog(this, "SJF Scheduling Algorithm", false);
        sjfDialog.setSize(1000, 750);
        sjfDialog.setLocationRelativeTo(this);
        sjfDialog.setLayout(new BorderLayout());
        
        JPanel chartPanel = new JPanel();
        chartPanel.setBackground(Color.LIGHT_GRAY);
        chartPanel.setPreferredSize(new Dimension(0, 250));
        chartPanel.add(new JLabel("SJF GANTT CHART - PLACEHOLDER"));
        chartPanel.setBorder(BorderFactory.createTitledBorder("Gantt Chart"));
        
        // Table columns: Response Time, Turnaround Time, Waiting Time
        String[] columns = {"Response Time", "Turnaround Time", "Waiting Time"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable outputTable = new JTable(tableModel);
        outputTable.setFillsViewportHeight(true);
        outputTable.setRowHeight(30);
        
        JTableHeader header = outputTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        
        JScrollPane tableScroll = new JScrollPane(outputTable);
        
        JButton btnRun = new JButton("RUN SJF");
        btnRun.setFont(new Font("Arial", Font.BOLD, 16));
        btnRun.setBackground(new Color(255, 140, 0));
        btnRun.setForeground(Color.WHITE);
        
        btnRun.addActionListener(e -> {
            tableModel.setRowCount(0);
            
            // Convert to internal SJF process objects
            int n = processList.size();
            SJFProcess[] procs = new SJFProcess[n];
            for (int i = 0; i < n; i++) {
                Process p = processList.get(i);
                procs[i] = new SJFProcess();
                procs[i].id = p.getId();
                procs[i].at = p.getArrivalTime();
                procs[i].bt = p.getBurstTime();
                procs[i].remaining = p.getBurstTime();
                procs[i].completed = false;
                procs[i].startTime = -1;
            }
            
            // Non-preemptive SJF
            int currentTime = 0;
            int completed = 0;
            while (completed < n) {
                int shortestIdx = -1;
                int shortestBurst = Integer.MAX_VALUE;
                for (int i = 0; i < n; i++) {
                    if (!procs[i].completed && procs[i].at <= currentTime) {
                        if (procs[i].bt < shortestBurst) {
                            shortestBurst = procs[i].bt;
                            shortestIdx = i;
                        } else if (procs[i].bt == shortestBurst && shortestIdx != -1) {
                            if (procs[i].at < procs[shortestIdx].at) {
                                shortestIdx = i;
                            }
                        }
                    }
                }
                if (shortestIdx == -1) {
                    currentTime++;
                    continue;
                }
                SJFProcess cur = procs[shortestIdx];
                if (cur.startTime == -1) {
                    cur.startTime = currentTime;
                }
                currentTime += cur.bt;
                cur.completionTime = currentTime;
                cur.turnaround = cur.completionTime - cur.at;
                cur.waiting = cur.turnaround - cur.bt;
                cur.response = cur.startTime - cur.at;
                cur.completed = true;
                completed++;
            }
            
            // Calculate averages
            double totalResponse = 0, totalTurnaround = 0, totalWaiting = 0;
            for (SJFProcess p : procs) {
                totalResponse += p.response;
                totalTurnaround += p.turnaround;
                totalWaiting += p.waiting;
            }
            double avgResponse = totalResponse / n;
            double avgTurnaround = totalTurnaround / n;
            double avgWaiting = totalWaiting / n;
            
            // Populate table: each process gets white row + blue row with averages
            for (SJFProcess p : procs) {
                Object[] resultRow = {p.response, p.turnaround, p.waiting};
                tableModel.addRow(resultRow);
                Object[] avgRow = {
                    String.format("Avg: %.2f", avgResponse),
                    String.format("Avg: %.2f", avgTurnaround),
                    String.format("Avg: %.2f", avgWaiting)
                };
                tableModel.addRow(avgRow);
            }
            
            // Blue background for average rows
            outputTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (row % 2 == 1) {
                        c.setBackground(new Color(173, 216, 230));
                        c.setForeground(Color.BLACK);
                        setFont(new Font("Arial", Font.BOLD, 12));
                    } else {
                        c.setBackground(row % 4 == 0 ? Color.WHITE : new Color(245, 245, 245));
                        c.setForeground(Color.BLACK);
                        setFont(new Font("Arial", Font.PLAIN, 12));
                    }
                    return c;
                }
            });
            
            latestSJFResult = new SJFResult();
            latestSJFResult.avgResponseTime = avgResponse;
            latestSJFResult.avgTurnaroundTime = avgTurnaround;
            latestSJFResult.avgWaitingTime = avgWaiting;
            
            JOptionPane.showMessageDialog(sjfDialog, 
                "SJF Algorithm executed!\nAverage Response Time: " + String.format("%.2f", avgResponse) +
                "\nAverage Waiting Time: " + String.format("%.2f", avgWaiting) +
                "\nAverage Turnaround Time: " + String.format("%.2f", avgTurnaround), 
                "SJF Complete", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JPanel topPanel = new JPanel();
        topPanel.add(btnRun);
        
        sjfDialog.add(topPanel, BorderLayout.NORTH);
        sjfDialog.add(chartPanel, BorderLayout.CENTER);
        sjfDialog.add(tableScroll, BorderLayout.SOUTH);
        
        sjfDialog.setVisible(true);
    }
    
    // SJF internal process class
    static class SJFProcess {
        String id;
        int at, bt, remaining, startTime, completionTime, turnaround, waiting, response;
        boolean completed;
    }
    
    // ========== PRIORITY WINDOW (with Response Time) ==========
    private void openPriorityWindow() {
        if (processList.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please add at least one process first!", 
                "No Processes", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Check priorities exist
        boolean missingPriority = false;
        for (Process p : processList) {
            if (p.getPriority() == null) {
                missingPriority = true;
                break;
            }
        }
        if (missingPriority) {
            JOptionPane.showMessageDialog(this, 
                "Priority algorithm requires all processes to have a priority value.\n" +
                "Please add or edit processes to include priority (lower number = higher priority).", 
                "Missing Priority", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JDialog priorityDialog = new JDialog(this, "Priority Scheduling Algorithm", false);
        priorityDialog.setSize(1000, 750);
        priorityDialog.setLocationRelativeTo(this);
        priorityDialog.setLayout(new BorderLayout());
        
        JPanel chartPanel = new JPanel();
        chartPanel.setBackground(Color.LIGHT_GRAY);
        chartPanel.setPreferredSize(new Dimension(0, 250));
        chartPanel.add(new JLabel("Priority GANTT CHART - PLACEHOLDER"));
        chartPanel.setBorder(BorderFactory.createTitledBorder("Gantt Chart"));
        
        String[] columns = {"Response Time", "Turnaround Time", "Waiting Time"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable outputTable = new JTable(tableModel);
        outputTable.setFillsViewportHeight(true);
        outputTable.setRowHeight(30);
        
        JTableHeader header = outputTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        
        JScrollPane tableScroll = new JScrollPane(outputTable);
        
        JButton btnRun = new JButton("RUN PRIORITY");
        btnRun.setFont(new Font("Arial", Font.BOLD, 16));
        btnRun.setBackground(new Color(218, 112, 214));
        btnRun.setForeground(Color.WHITE);
        
        btnRun.addActionListener(e -> {
            tableModel.setRowCount(0);
            
            int n = processList.size();
            PriorityProcess[] procs = new PriorityProcess[n];
            for (int i = 0; i < n; i++) {
                Process p = processList.get(i);
                procs[i] = new PriorityProcess();
                procs[i].arrivalTime = p.getArrivalTime();
                procs[i].burstTime = p.getBurstTime();
                procs[i].priority = p.getPriority();
                procs[i].remainingTime = p.getBurstTime();
                procs[i].isCompleted = false;
                procs[i].startTime = -1;
            }
            
            // Preemptive priority scheduling (lower number = higher priority)
            int currentTime = 0;
            int completed = 0;
            while (completed < n) {
                PriorityProcess bestProcess = null;
                int minPriority = Integer.MAX_VALUE;
                
                for (PriorityProcess pp : procs) {
                    if (pp.arrivalTime <= currentTime && !pp.isCompleted) {
                        if (pp.priority < minPriority) {
                            minPriority = pp.priority;
                            bestProcess = pp;
                        } else if (pp.priority == minPriority && bestProcess != null) {
                            if (pp.arrivalTime < bestProcess.arrivalTime) {
                                bestProcess = pp;
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
                    bestProcess.responseTime = bestProcess.startTime - bestProcess.arrivalTime;
                }
                bestProcess.remainingTime--;
                currentTime++;
                if (bestProcess.remainingTime == 0) {
                    bestProcess.isCompleted = true;
                    completed++;
                    bestProcess.completionTime = currentTime;
                    bestProcess.turnaroundTime = bestProcess.completionTime - bestProcess.arrivalTime;
                    bestProcess.waitingTime = bestProcess.turnaroundTime - bestProcess.burstTime;
                }
            }
            
            // Calculate averages
            double totalResponse = 0, totalTurnaround = 0, totalWaiting = 0;
            for (PriorityProcess pp : procs) {
                totalResponse += pp.responseTime;
                totalTurnaround += pp.turnaroundTime;
                totalWaiting += pp.waitingTime;
            }
            double avgResponse = totalResponse / n;
            double avgTurnaround = totalTurnaround / n;
            double avgWaiting = totalWaiting / n;
            
            // Populate table
            for (PriorityProcess pp : procs) {
                Object[] resultRow = {pp.responseTime, pp.turnaroundTime, pp.waitingTime};
                tableModel.addRow(resultRow);
                Object[] avgRow = {
                    String.format("Avg: %.2f", avgResponse),
                    String.format("Avg: %.2f", avgTurnaround),
                    String.format("Avg: %.2f", avgWaiting)
                };
                tableModel.addRow(avgRow);
            }
            
            outputTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (row % 2 == 1) {
                        c.setBackground(new Color(173, 216, 230));
                        c.setForeground(Color.BLACK);
                        setFont(new Font("Arial", Font.BOLD, 12));
                    } else {
                        c.setBackground(row % 4 == 0 ? Color.WHITE : new Color(245, 245, 245));
                        c.setForeground(Color.BLACK);
                        setFont(new Font("Arial", Font.PLAIN, 12));
                    }
                    return c;
                }
            });
            
            latestPriorityResult = new PriorityResult();
            latestPriorityResult.avgResponseTime = avgResponse;
            latestPriorityResult.avgTurnaroundTime = avgTurnaround;
            latestPriorityResult.avgWaitingTime = avgWaiting;
            
            JOptionPane.showMessageDialog(priorityDialog, 
                "Priority Algorithm executed!\nAverage Response Time: " + String.format("%.2f", avgResponse) +
                "\nAverage Waiting Time: " + String.format("%.2f", avgWaiting) +
                "\nAverage Turnaround Time: " + String.format("%.2f", avgTurnaround), 
                "Priority Complete", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JPanel topPanel = new JPanel();
        topPanel.add(btnRun);
        
        priorityDialog.add(topPanel, BorderLayout.NORTH);
        priorityDialog.add(chartPanel, BorderLayout.CENTER);
        priorityDialog.add(tableScroll, BorderLayout.SOUTH);
        
        priorityDialog.setVisible(true);
    }
    
    // Internal class for priority processes
    static class PriorityProcess {
        int arrivalTime, burstTime, priority, remainingTime, startTime, responseTime, completionTime, turnaroundTime, waitingTime;
        boolean isCompleted;
    }
    
    // ========== COMPARISON WINDOW ==========
    private void openComparisonWindow() {
        comparisonDialog = new JDialog(this, "Algorithm Comparison - 5 Scenarios", false);
        comparisonDialog.setSize(1000, 600);
        comparisonDialog.setLocationRelativeTo(this);
        
        String[] columns = {"Scenario", "SJF", "Priority", "Better Algorithm"};
        comparisonModel = new DefaultTableModel(columns, 0);
        JTable comparisonTable = new JTable(comparisonModel);
        comparisonTable.setFillsViewportHeight(true);
        comparisonTable.setRowHeight(80);
        comparisonTable.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JTableHeader header = comparisonTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        
        comparisonTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        comparisonTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        comparisonTable.getColumnModel().getColumn(2).setPreferredWidth(300);
        comparisonTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        
        JScrollPane scrollPane = new JScrollPane(comparisonTable);
        scrollPane.setBorder(null);
        
        refreshComparisonTable();
        
        comparisonDialog.setLayout(new BorderLayout());
        comparisonDialog.add(scrollPane, BorderLayout.CENTER);
        
        comparisonDialog.setVisible(true);
    }
    
    private void refreshComparisonTable() {
        if (comparisonModel != null) {
            comparisonModel.setRowCount(0);
            
            // Get numeric averages if available
            String sjfResponse = (latestSJFResult != null) ? String.format("%.2f", latestSJFResult.avgResponseTime) : "Not calculated";
            String priResponse = (latestPriorityResult != null) ? String.format("%.2f", latestPriorityResult.avgResponseTime) : "Not calculated";
            String sjfWaiting = (latestSJFResult != null) ? String.format("%.2f", latestSJFResult.avgWaitingTime) : "Not calculated";
            String priWaiting = (latestPriorityResult != null) ? String.format("%.2f", latestPriorityResult.avgWaitingTime) : "Not calculated";
            String sjfTurnaround = (latestSJFResult != null) ? String.format("%.2f", latestSJFResult.avgTurnaroundTime) : "Not calculated";
            String priTurnaround = (latestPriorityResult != null) ? String.format("%.2f", latestPriorityResult.avgTurnaroundTime) : "Not calculated";
            
            // Scenario descriptions
            String scenario1_SJF = "SJF ignores priority. A short job with low priority will still run quickly because SJF selects by CPU burst length, not priority.";
            String scenario1_Priority = "Priority scheduling may starve a short job with low priority if higher priority (smaller number) processes keep arriving.";
            String scenario1_Better = "SJF";
            
            String scenario2_SJF = "SJF may delay a long job with high priority because it always picks the shortest burst first. The high priority is ignored.";
            String scenario2_Priority = "Priority scheduling will run the long job immediately if it has the highest priority (smallest number), regardless of its burst length.";
            String scenario2_Better = "Priority ";
            
            String scenario3_SJF = sjfWaiting + sjfTurnaround;
            String scenario3_Priority = priWaiting + priTurnaround;
            String scenario3_Better = getBetterOverall();
            
            String scenario4_SJF = "SJF gives good response to short jobs but may ignore urgency. Urgent (high priority) long jobs can be delayed.";
            String scenario4_Priority = "Priority scheduling is designed exactly for urgent processes. Higher priority (smaller number) runs immediately.";
            String scenario4_Better = "Priority ";
            
            String scenario5_SJF = "SJF can cause starvation for long jobs if short jobs keep arriving.";
            String scenario5_Priority = "Priority can cause starvation for low priority processes if higher priority processes continuously arrive.";
            String scenario5_Better = "Both ";
            
            Object[][] scenarios = {
                {"1. Short job has low priority", scenario1_SJF, scenario1_Priority, scenario1_Better},
                {"2. Long job has high priority", scenario2_SJF, scenario2_Priority, scenario2_Better},
                {"3. Average WT / TAT", scenario3_SJF, scenario3_Priority, scenario3_Better},
                {"4. Urgent processes ", scenario4_SJF, scenario4_Priority, scenario4_Better},
                {"5. Fairness", scenario5_SJF, scenario5_Priority, scenario5_Better}
            };
            
            for (Object[] row : scenarios) {
                comparisonModel.addRow(row);
            }
        }
    }
    
    private String getBetterOverall() {
        if (latestSJFResult == null || latestPriorityResult == null) 
            return "Run both algorithms";
        double sjfAvg = (latestSJFResult.avgResponseTime + latestSJFResult.avgWaitingTime + latestSJFResult.avgTurnaroundTime) / 3;
        double priAvg = (latestPriorityResult.avgResponseTime + latestPriorityResult.avgWaitingTime + latestPriorityResult.avgTurnaroundTime) / 3;
        if (sjfAvg < priAvg) return "SJF better on average";
        else if (priAvg < sjfAvg) return "Priority better on average";
        else return "Equal";
    }
    
    // ========== CONCLUSION WINDOW ==========
    private void openConclusionWindow() {
        JDialog conclusionDialog = new JDialog(this, "Conclusion", false);
        conclusionDialog.setSize(600, 400);
        conclusionDialog.setLocationRelativeTo(this);
        
        JTextArea conclusionArea = new JTextArea();
        conclusionArea.setEditable(false);
        conclusionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        conclusionArea.setBackground(new Color(255, 255, 224));
        conclusionArea.setLineWrap(true);
        conclusionArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(conclusionArea);
        
        StringBuilder conclusion = new StringBuilder();
        conclusion.append("CONCLUSION:\n\n");
        
        if (latestSJFResult == null && latestPriorityResult == null) {
            conclusion.append("- No algorithm results available yet.\n");
            conclusion.append("- Please run both SJF and Priority algorithms first.\n\n");
        } else if (latestSJFResult == null) {
            conclusion.append("- Only Priority algorithm results are available.\n");
            conclusion.append("- Please run SJF algorithm for complete comparison.\n\n");
        } else if (latestPriorityResult == null) {
            conclusion.append("- Only SJF algorithm results are available.\n");
            conclusion.append("- Please run Priority algorithm for complete comparison.\n\n");
        } else {
            if (latestSJFResult.avgWaitingTime < latestPriorityResult.avgWaitingTime) {
                conclusion.append("- SJF minimizes average waiting time.\n");
                conclusion.append(String.format("  (SJF: %.2f, Priority: %.2f)\n", 
                    latestSJFResult.avgWaitingTime, latestPriorityResult.avgWaitingTime));
            } else if (latestPriorityResult.avgWaitingTime < latestSJFResult.avgWaitingTime) {
                conclusion.append("- Priority minimizes average waiting time.\n");
                conclusion.append(String.format("  (Priority: %.2f, SJF: %.2f)\n", 
                    latestPriorityResult.avgWaitingTime, latestSJFResult.avgWaitingTime));
            }
            
            if (latestSJFResult.avgTurnaroundTime < latestPriorityResult.avgTurnaroundTime) {
                conclusion.append("- SJF minimizes average turnaround time.\n");
                conclusion.append(String.format("  (SJF: %.2f, Priority: %.2f)\n", 
                    latestSJFResult.avgTurnaroundTime, latestPriorityResult.avgTurnaroundTime));
            } else if (latestPriorityResult.avgTurnaroundTime < latestSJFResult.avgTurnaroundTime) {
                conclusion.append("- Priority minimizes average turnaround time.\n");
                conclusion.append(String.format("  (Priority: %.2f, SJF: %.2f)\n", 
                    latestPriorityResult.avgTurnaroundTime, latestSJFResult.avgTurnaroundTime));
            }
            
            if (latestSJFResult.avgResponseTime < latestPriorityResult.avgResponseTime) {
                conclusion.append("- SJF gives better average response time.\n");
            } else if (latestPriorityResult.avgResponseTime < latestSJFResult.avgResponseTime) {
                conclusion.append("- Priority gives better average response time.\n");
            }
            
            conclusion.append("\n- Both algorithms may cause starvation under certain conditions.\n");
            conclusion.append("- Consider using aging to prevent starvation.\n");
        }
        
        conclusionArea.setText(conclusion.toString());
        conclusionArea.setCaretPosition(0);
        
        conclusionDialog.setLayout(new BorderLayout());
        conclusionDialog.add(scrollPane, BorderLayout.CENTER);
        
        conclusionDialog.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OS_Project_Main());
    }
}

// Data classes for storing results
class SJFResult {
    double avgResponseTime;
    double avgTurnaroundTime;
    double avgWaitingTime;
}

class PriorityResult {
    double avgResponseTime;
    double avgTurnaroundTime;
    double avgWaitingTime;
}

// UI Process class
class Process {
    private String id;
    private int arrivalTime;
    private int burstTime;
    private Integer priority;
    
    public Process(String id, int arrivalTime, int burstTime, Integer priority) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
    }
    
    public String getId() { return id; }
    public int getArrivalTime() { return arrivalTime; }
    public int getBurstTime() { return burstTime; }
    public Integer getPriority() { return priority; }
}