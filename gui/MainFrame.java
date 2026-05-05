/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Maya sameh
 */
// MainFrame.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    
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
    
    public MainFrame() {
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
        btnRunSJF.addActionListener(e -> openSJFWindow());
        gbc.gridx = 0;
        gbc.gridy = 2;
        centerPanel.add(btnRunSJF, gbc);
        
        btnRunPriority = createStyledButton(" RUN PRIORITY ALGORITHM", buttonSize, buttonFont, new Color(218, 112, 214));
        btnRunPriority.addActionListener(e -> openPriorityWindow());
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
    
    private void refreshComparisonTable() {
        if (comparisonModel != null) {
            // Optional refresh logic can be added here later.
        }
    }
    
    private void openSJFWindow() {
        GanttChartPanel.openSJFWindow(this, processList);
    }
    
    private void openPriorityWindow() {
        GanttChartPanel.openPriorityWindow(this, processList, latestPriorityResult);
    }
    
    private void openComparisonWindow() {
        ResultTable resultTable = new ResultTable(latestSJFResult, latestPriorityResult);
        resultTable.openComparisonWindow(this);
    }
    
    private void openConclusionWindow() {
        ResultTable resultTable = new ResultTable(latestSJFResult, latestPriorityResult);
        resultTable.openConclusionWindow(this);
    }
    
    public List<Process> getProcessList() {
        return processList;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}