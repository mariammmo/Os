/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Maya sameh
 */
// ResultTable.java
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ResultTable {
    
    private JDialog comparisonDialog;
    private DefaultTableModel comparisonModel;
    private OS_Project_Main.SJFResult latestSJFResult;
    private OS_Project_Main.PriorityResult latestPriorityResult;
    
    public ResultTable(OS_Project_Main.SJFResult sjfResult, OS_Project_Main.PriorityResult priorityResult) {
        this.latestSJFResult = sjfResult;
        this.latestPriorityResult = priorityResult;
    }
    
    public void openComparisonWindow(JFrame parent) {
        comparisonDialog = new JDialog(parent, "Algorithm Comparison - 5 Scenarios", false);
        comparisonDialog.setSize(1000, 600);
        comparisonDialog.setLocationRelativeTo(parent);
        
        String[] columns = {"Scenario", "SJF", "Priority", "Better Algorithm"};
        comparisonModel = new DefaultTableModel(columns, 0);
        JTable comparisonTable = new JTable(comparisonModel);
        comparisonTable.setFillsViewportHeight(true);
        comparisonTable.setRowHeight(50);
        comparisonTable.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JTableHeader header = comparisonTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        
        comparisonTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        comparisonTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        comparisonTable.getColumnModel().getColumn(2).setPreferredWidth(250);
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
            
            String sjfWaiting = (latestSJFResult != null) ? 
                String.format("%.2f", latestSJFResult.avgWaitingTime) : "Not calculated";
            String priorityWaiting = (latestPriorityResult != null) ? 
                String.format("%.2f", latestPriorityResult.avgWaitingTime) : "Not calculated";
            
            Object[][] scenarios = {
                {"1. Short job has low priority"," " , "", ""},
                {"2. Long job has high priority", "", "", ""},
                {"3. Average WT / TAT", sjfWaiting, priorityWaiting, ""},
                {"4. Urgent processes", "", "", ""},
                {"5. Fairness", "", "", ""}
            };
            
            for (Object[] row : scenarios) {
                comparisonModel.addRow(row);
            }
        }
    }
    
    public void openConclusionWindow(JFrame parent) {
        JDialog conclusionDialog = new JDialog(parent, "Conclusion", false);
        conclusionDialog.setSize(600, 400);
        conclusionDialog.setLocationRelativeTo(parent);
        
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
                conclusion.append("- SJF is best for minimizing average waiting time.\n");
            } else if (latestPriorityResult.avgWaitingTime < latestSJFResult.avgWaitingTime) {
                conclusion.append("- Priority scheduling is best for handling urgent/important processes.\n");
            }
            
            if (latestSJFResult.avgTurnaroundTime < latestPriorityResult.avgTurnaroundTime) {
                conclusion.append("- SJF is best for minimizing average turnaround time.\n");
            }
            
            conclusion.append("- Both algorithms may cause starvation under certain conditions.\n");
        }
        
        conclusionArea.setText(conclusion.toString());
        conclusionArea.setCaretPosition(0);
        
        conclusionDialog.setLayout(new BorderLayout());
        conclusionDialog.add(scrollPane, BorderLayout.CENTER);
        
        conclusionDialog.setVisible(true);
    }
}