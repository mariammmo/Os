// GanttChartPanel.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class GanttChartPanel {
    
    public static void openSJFWindow(JFrame parent, List<Process> processList, SJFResult latestSJFResult) {
        if (processList.isEmpty()) {
            JOptionPane.showMessageDialog(parent, 
                "Please add at least one process first!", 
                "No Processes", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JDialog sjfDialog = new JDialog(parent, "SJF Scheduling Algorithm", false);
        sjfDialog.setSize(1000, 750);
        sjfDialog.setLocationRelativeTo(parent);
        sjfDialog.setLayout(new BorderLayout());
        
        JPanel chartPanel = new JPanel();
        chartPanel.setBackground(Color.LIGHT_GRAY);
        chartPanel.setPreferredSize(new Dimension(0, 250));
        chartPanel.add(new JLabel("SJF GANTT CHART - PLACEHOLDER"));
        chartPanel.setBorder(BorderFactory.createTitledBorder("Gantt Chart"));
        
        String[] columns = {"Arrival Time", "Turnaround Time", "Waiting Time"};
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
            
            // ========== PLACEHOLDER FOR SJF CODE ==========
            for (Process p : processList) {
                double arrivalTime = p.getArrivalTime();
                double turnaroundTime = 0;
                double waitingTime = 0;
                
                double avgArrival = arrivalTime;
                double avgTurnaround = turnaroundTime;
                double avgWaiting = waitingTime;
                
                Object[] resultRow = {arrivalTime, turnaroundTime, waitingTime};
                tableModel.addRow(resultRow);
                
                Object[] avgRow = {
                    String.format("Avg: %.2f", avgArrival),
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
            
            JOptionPane.showMessageDialog(sjfDialog, "SJF Algorithm executed!", "SJF Complete", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JPanel topPanel = new JPanel();
        topPanel.add(btnRun);
        
        sjfDialog.add(topPanel, BorderLayout.NORTH);
        sjfDialog.add(chartPanel, BorderLayout.CENTER);
        sjfDialog.add(tableScroll, BorderLayout.SOUTH);
        
        sjfDialog.setVisible(true);
    }
    
    public static void openPriorityWindow(JFrame parent, List<Process> processList, PriorityResult latestPriorityResult) {
        if (processList.isEmpty()) {
            JOptionPane.showMessageDialog(parent, 
                "Please add at least one process first!", 
                "No Processes", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JDialog priorityDialog = new JDialog(parent, "Priority Scheduling Algorithm", false);
        priorityDialog.setSize(1000, 750);
        priorityDialog.setLocationRelativeTo(parent);
        priorityDialog.setLayout(new BorderLayout());
        
        JPanel chartPanel = new JPanel();
        chartPanel.setBackground(Color.LIGHT_GRAY);
        chartPanel.setPreferredSize(new Dimension(0, 250));
        chartPanel.add(new JLabel("PRIORITY GANTT CHART - PLACEHOLDER"));
        chartPanel.setBorder(BorderFactory.createTitledBorder("Gantt Chart"));
        
        String[] columns = {"Arrival Time", "Turnaround Time", "Waiting Time"};
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
            
            // ========== PLACEHOLDER FOR PRIORITY CODE ==========
            for (Process p : processList) {
                double arrivalTime = p.getArrivalTime();
                double turnaroundTime = 0;
                double waitingTime = 0;
                
                double avgArrival = arrivalTime;
                double avgTurnaround = turnaroundTime;
                double avgWaiting = waitingTime;
                
                Object[] resultRow = {arrivalTime, turnaroundTime, waitingTime};
                tableModel.addRow(resultRow);
                
                Object[] avgRow = {
                    String.format("Avg: %.2f", avgArrival),
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
            
            JOptionPane.showMessageDialog(priorityDialog, "Priority Algorithm executed!", "Priority Complete", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JPanel topPanel = new JPanel();
        topPanel.add(btnRun);
        
        priorityDialog.add(topPanel, BorderLayout.NORTH);
        priorityDialog.add(chartPanel, BorderLayout.CENTER);
        priorityDialog.add(tableScroll, BorderLayout.SOUTH);
        
        priorityDialog.setVisible(true);
    }
}
