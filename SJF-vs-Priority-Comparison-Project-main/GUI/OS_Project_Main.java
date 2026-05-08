import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
 
public class OS_Project_Main extends JFrame {
 
    // ===================== COLORS =====================
    private static final Color BG_DARK       = new Color(13, 17, 31);
    private static final Color BG_CARD       = new Color(22, 28, 48);
    private static final Color BG_INPUT      = new Color(30, 38, 62);
    private static final Color ACCENT_BLUE   = new Color(64, 156, 255);
    private static final Color ACCENT_GREEN  = new Color(52, 211, 153);
    private static final Color ACCENT_ORANGE = new Color(251, 146, 60);
    private static final Color ACCENT_PURPLE = new Color(139, 92, 246);
    private static final Color TEXT_PRIMARY  = new Color(226, 232, 240);
    private static final Color TEXT_MUTED    = new Color(100, 116, 139);
    private static final Color BORDER_COLOR  = new Color(45, 55, 80);
 
    // ===================== STATE =====================
    private List<Process> processList           = new ArrayList<>();
    private SJFResult      latestSJFResult      = null;  // kept for internal use
    private PriorityResult latestPriorityResult = null;
    private SRTFResult     latestSRTFResult     = null;  // NEW: store SRTF results
 
    private DefaultTableModel processTableModel = null;
    private DefaultTableModel comparisonModel   = null;
    private JDialog processTableDialog = null;
    private JDialog comparisonDialog   = null;
    private JLabel  statusLabel;
 
    // ===================== CONSTRUCTOR =====================
    public OS_Project_Main() {
        initializeMainWindow();
    }
 
    // ===================== MAIN WINDOW =====================
    private void initializeMainWindow() {
        setTitle("CPU Scheduling Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 620);   // slightly shorter since one button removed
        setLocationRelativeTo(null);
        setResizable(false);
 
        GradientPanel root = new GradientPanel();
        root.setLayout(new BorderLayout());
        setContentPane(root);
 
        // ---- TITLE ----
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(new EmptyBorder(30, 20, 10, 20));
 
        JLabel titleLbl = new JLabel("\u2699  CPU SCHEDULING SIMULATOR", SwingConstants.CENTER);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLbl.setForeground(TEXT_PRIMARY);
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        // CHANGE 1: Updated subtitle - removed SJF, kept SRTF · Priority
        JLabel subLbl = new JLabel("SRTF  \u00b7  Priority", SwingConstants.CENTER);
        subLbl.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        subLbl.setForeground(TEXT_MUTED);
        subLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        titlePanel.add(titleLbl);
        titlePanel.add(Box.createVerticalStrut(6));
        titlePanel.add(subLbl);
        root.add(titlePanel, BorderLayout.NORTH);
 
        // ---- BUTTONS ----
        Dimension btnSize = new Dimension(340, 52);
 
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(new EmptyBorder(10, 60, 10, 60));
 
        center.add(Box.createVerticalStrut(8));
        center.add(menuBtn("\u2795   ADD NEW PROCESS",           ACCENT_GREEN,  btnSize, e -> openInputWindow()));
        center.add(Box.createVerticalStrut(10));
        center.add(menuBtn("\ud83d\udccb   VIEW PROCESS TABLE",  ACCENT_BLUE,   btnSize, e -> openProcessTableWindow()));
        center.add(Box.createVerticalStrut(18));
 
        center.add(separator("RUN ALGORITHM"));
        center.add(Box.createVerticalStrut(10));
 
        // CHANGE 1: SJF button is REMOVED from GUI (code kept below)
        // center.add(menuBtn("\u25b6   RUN SJF (Non-Preemptive)", ACCENT_ORANGE, btnSize, e -> openSJFWindow()));
        // center.add(Box.createVerticalStrut(10));

        center.add(menuBtn("\u25b6   RUN SRTF (Preemptive SJF)", ACCENT_GREEN,  btnSize, e -> openSRTFWindow()));
        center.add(Box.createVerticalStrut(10));
        center.add(menuBtn("\u25b6   RUN PRIORITY SCHEDULING",   ACCENT_PURPLE, btnSize, e -> openPriorityWindow()));
 
        center.add(Box.createVerticalStrut(18));
        center.add(separator("RESULTS"));
        center.add(Box.createVerticalStrut(10));
 
        center.add(menuBtn("\ud83d\udcca   VIEW COMPARISON",     ACCENT_BLUE,   btnSize, e -> openComparisonWindow()));
        center.add(Box.createVerticalStrut(10));
        center.add(menuBtn("\ud83d\udcdd   VIEW CONCLUSION",     ACCENT_GREEN,  btnSize, e -> openConclusionWindow()));
        center.add(Box.createVerticalGlue());
 
        root.add(center, BorderLayout.CENTER);
 
        // ---- STATUS BAR ----
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 6));
        statusPanel.setOpaque(false);
        statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));
        statusLabel = new JLabel("Ready  |  Total Processes: 0");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(TEXT_MUTED);
        statusPanel.add(statusLabel);
        root.add(statusPanel, BorderLayout.SOUTH);
 
        new Timer(800, e -> statusLabel.setText("Ready  |  Total Processes: " + processList.size())).start();
    }
 
    // ===================== INPUT WINDOW =====================
    private void openInputWindow() {
        JDialog dlg = new JDialog(this, "Add New Process", true);
        dlg.setSize(440, 340);
        dlg.setLocationRelativeTo(this);
        dlg.setResizable(false);
 
        GradientPanel panel = new GradientPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));
        dlg.setContentPane(panel);
 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 6, 7, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
 
        JTextField txtId       = dialogField();
        JTextField txtArrival  = dialogField();
        JTextField txtBurst    = dialogField();
        JTextField txtPriority = dialogField();
 
        String[] labels = {
            "Process ID  *",
            "Arrival Time  (\u2265 0)  *",
            "Burst Time  (> 0)  *",
            "Priority  (> 0, optional)"
        };
        JTextField[] fields = { txtId, txtArrival, txtBurst, txtPriority };
 
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.4;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lbl.setForeground(TEXT_MUTED);
            panel.add(lbl, gbc);
            gbc.gridx = 1; gbc.weightx = 0.6;
            panel.add(fields[i], gbc);
        }
 
        JButton btnAdd = accentBtn("ADD PROCESS", ACCENT_GREEN);
        gbc.gridx = 0; gbc.gridy = labels.length; gbc.gridwidth = 2;
        gbc.insets = new Insets(18, 6, 6, 6);
        panel.add(btnAdd, gbc);
 
        ActionListener submit = e -> {
            // ---- Validate ID ----
            String id = txtId.getText().trim();
            if (id.isEmpty()) { err(dlg, "Process ID cannot be empty!"); return; }
            for (Process p : processList) {
                if (p.getId().equalsIgnoreCase(id)) {
                    err(dlg, "Process ID '" + id + "' already exists!"); return;
                }
            }
            // ---- Validate Arrival ----
            int arrival;
            try {
                arrival = Integer.parseInt(txtArrival.getText().trim());
                if (arrival < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                err(dlg, "Arrival Time must be an integer \u2265 0!"); return;
            }
            // ---- Validate Burst ----
            int burst;
            try {
                burst = Integer.parseInt(txtBurst.getText().trim());
                if (burst <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                err(dlg, "Burst Time must be an integer > 0!"); return;
            }
            // ---- Validate Priority (optional) ----
            Integer priority = null;
            String prStr = txtPriority.getText().trim();
            if (!prStr.isEmpty()) {
                try {
                    priority = Integer.parseInt(prStr);
                    if (priority <= 0) throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    err(dlg, "Priority must be an integer > 0!"); return;
                }
            }
 
            processList.add(new Process(id, arrival, burst, priority));
            JOptionPane.showMessageDialog(dlg,
                "Process " + id + " added successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            dlg.dispose();
        };
 
        btnAdd.addActionListener(submit);
        for (JTextField tf : fields) tf.addActionListener(submit);
        dlg.setVisible(true);
    }
 
    // ===================== PROCESS TABLE WINDOW =====================
    private void openProcessTableWindow() {
        processTableDialog = new JDialog(this, "Process Table", false);
        processTableDialog.setSize(640, 380);
        processTableDialog.setLocationRelativeTo(this);
 
        GradientPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());
        processTableDialog.setContentPane(panel);
 
        String[] cols = {"Process ID", "Arrival Time", "Burst Time", "Priority"};
        processTableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
 
        JTable table = new JTable(processTableModel);
        styleTable(table);
 
        JButton btnRemove = accentBtn("Remove Selected", new Color(239, 68, 68));
        btnRemove.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { err(processTableDialog, "Please select a row to remove!"); return; }
            String rid = (String) processTableModel.getValueAt(row, 0);
            processList.remove(row);
            processTableModel.removeRow(row);
            statusLabel.setText("Removed: " + rid + "  |  Total: " + processList.size());
        });
 
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        bot.setOpaque(false);
        bot.add(btnRemove);
 
        panel.add(darkScroll(table), BorderLayout.CENTER);
        panel.add(bot, BorderLayout.SOUTH);
 
        refreshProcessTable();
        processTableDialog.setVisible(true);
    }
 
    private void refreshProcessTable() {
        if (processTableModel == null) return;
        processTableModel.setRowCount(0);
        for (Process p : processList) {
            processTableModel.addRow(new Object[]{
                p.getId(), p.getArrivalTime(), p.getBurstTime(),
                p.getPriority() != null ? p.getPriority() : "\u2014"
            });
        }
    }
 
    // ===================== SJF ALGORITHM (CODE KEPT, NOT SHOWN IN GUI) =====================
    private void openSJFWindow() {
        if (processList.isEmpty()) {
            err(this, "Please add at least one process first!"); return;
        }
 
        int n = processList.size();
 
        int[] at  = new int[n], bt  = new int[n];
        int[] wt  = new int[n], tat = new int[n], rt = new int[n];
        boolean[] done = new boolean[n];
        List<int[]> ganttBlocks = new ArrayList<>();
 
        for (int i = 0; i < n; i++) {
            at[i] = processList.get(i).getArrivalTime();
            bt[i] = processList.get(i).getBurstTime();
        }
 
        int currentTime = 0, completed = 0;
        while (completed < n) {
            int shortest = -1;
            for (int i = 0; i < n; i++) {
                if (!done[i] && at[i] <= currentTime) {
                    if (shortest == -1 || bt[i] < bt[shortest]
                            || (bt[i] == bt[shortest] && at[i] < at[shortest])) {
                        shortest = i;
                    }
                }
            }
            if (shortest == -1) { currentTime++; continue; }
 
            rt[shortest]  = currentTime - at[shortest];
            ganttBlocks.add(new int[]{shortest, currentTime, currentTime + bt[shortest]});
            currentTime  += bt[shortest];
            tat[shortest] = currentTime - at[shortest];
            wt[shortest]  = tat[shortest] - bt[shortest];
            done[shortest] = true;
            completed++;
        }
 
        double avgWT = 0, avgTAT = 0, avgRT = 0;
        for (int i = 0; i < n; i++) { avgWT += wt[i]; avgTAT += tat[i]; avgRT += rt[i]; }
        avgWT /= n; avgTAT /= n; avgRT /= n;
 
        latestSJFResult = new SJFResult();
        latestSJFResult.avgWaitingTime    = avgWT;
        latestSJFResult.avgTurnaroundTime = avgTAT;
        latestSJFResult.avgResponseTime   = avgRT;
 
        String[] cols = {"Process ID", "Arrival", "Burst", "Waiting", "Turnaround", "Response"};
        Object[][] rows = new Object[n + 1][6];
        for (int i = 0; i < n; i++) {
            rows[i] = new Object[]{processList.get(i).getId(), at[i], bt[i], wt[i], tat[i], rt[i]};
        }
        rows[n] = new Object[]{"Average", "—", "—",
            String.format("%.2f", avgWT), String.format("%.2f", avgTAT), String.format("%.2f", avgRT)};
 
        showResultWindow("SJF (Non-Preemptive) Results", cols, rows, ganttBlocks, ACCENT_ORANGE);
    }
 
    // ===================== SRTF ALGORITHM WINDOW =====================
    private void openSRTFWindow() {
        if (processList.isEmpty()) {
            err(this, "Please add at least one process first!"); return;
        }
 
        int n = processList.size();
        int[] at   = new int[n], bt  = new int[n], rem = new int[n];
        int[] wt   = new int[n], tat = new int[n], rt  = new int[n];
        boolean[] done        = new boolean[n];
        boolean[] startedOnce = new boolean[n];
        List<int[]> ganttBlocks = new ArrayList<>();
 
        for (int i = 0; i < n; i++) {
            at[i]  = processList.get(i).getArrivalTime();
            bt[i]  = processList.get(i).getBurstTime();
            rem[i] = bt[i];
        }
 
        int currentTime = 0, completed = 0;
        int lastIdx = -1, blockStart = 0;
 
        while (completed < n) {
            int shortest = -1;
            for (int i = 0; i < n; i++) {
                if (!done[i] && at[i] <= currentTime) {
                    if (shortest == -1 || rem[i] < rem[shortest]
                            || (rem[i] == rem[shortest] && at[i] < at[shortest])) {
                        shortest = i;
                    }
                }
            }
            if (shortest == -1) {
                if (lastIdx != -1) {
                    ganttBlocks.add(new int[]{lastIdx, blockStart, currentTime});
                    lastIdx = -1;
                }
                currentTime++; continue;
            }
            if (!startedOnce[shortest]) {
                rt[shortest]         = currentTime - at[shortest];
                startedOnce[shortest] = true;
            }
            if (shortest != lastIdx) {
                if (lastIdx != -1) ganttBlocks.add(new int[]{lastIdx, blockStart, currentTime});
                blockStart = currentTime;
                lastIdx    = shortest;
            }
            rem[shortest]--;
            currentTime++;
            if (rem[shortest] == 0) {
                done[shortest] = true;
                completed++;
                tat[shortest] = currentTime - at[shortest];
                wt[shortest]  = tat[shortest] - bt[shortest];
                ganttBlocks.add(new int[]{shortest, blockStart, currentTime});
                lastIdx = -1;
            }
        }
 
        double avgWT = 0, avgTAT = 0, avgRT = 0;
        for (int i = 0; i < n; i++) { avgWT += wt[i]; avgTAT += tat[i]; avgRT += rt[i]; }
        avgWT /= n; avgTAT /= n; avgRT /= n;

        // CHANGE 2 & 3: Save SRTF results to latestSRTFResult
        latestSRTFResult = new SRTFResult();
        latestSRTFResult.avgWaitingTime    = avgWT;
        latestSRTFResult.avgTurnaroundTime = avgTAT;
        latestSRTFResult.avgResponseTime   = avgRT;
 
        String[] cols = {"Process ID", "Arrival", "Burst", "Waiting", "Turnaround", "Response"};
        Object[][] rows = new Object[n + 1][6];
        for (int i = 0; i < n; i++) {
            rows[i] = new Object[]{processList.get(i).getId(), at[i], bt[i], wt[i], tat[i], rt[i]};
        }
        rows[n] = new Object[]{"Average", "—", "—",
            String.format("%.2f", avgWT), String.format("%.2f", avgTAT), String.format("%.2f", avgRT)};
 
        showResultWindow("SRTF (Preemptive SJF) Results", cols, rows, ganttBlocks, ACCENT_GREEN);
    }
 
    // ===================== PRIORITY ALGORITHM WINDOW =====================
    private void openPriorityWindow() {
        if (processList.isEmpty()) {
            err(this, "Please add at least one process first!"); return;
        }
        for (Process p : processList) {
            if (p.getPriority() == null) {
                err(this, "All processes must have a priority value!\nPlease add priority when entering processes.");
                return;
            }
        }
 
        int n = processList.size();
        int[] at   = new int[n], bt  = new int[n], pri = new int[n];
        int[] rem  = new int[n], wt  = new int[n], tat = new int[n], rt = new int[n];
        boolean[] done        = new boolean[n];
        boolean[] startedOnce = new boolean[n];
        List<int[]> ganttBlocks = new ArrayList<>();
 
        for (int i = 0; i < n; i++) {
            at[i]  = processList.get(i).getArrivalTime();
            bt[i]  = processList.get(i).getBurstTime();
            pri[i] = processList.get(i).getPriority();
            rem[i] = bt[i];
        }
 
        int currentTime = 0, completed = 0;
        int lastIdx = -1, blockStart = 0;
 
        while (completed < n) {
            int best = -1;
            for (int i = 0; i < n; i++) {
                if (!done[i] && at[i] <= currentTime) {
                    if (best == -1 || pri[i] < pri[best]
                            || (pri[i] == pri[best] && at[i] < at[best])) {
                        best = i;
                    }
                }
            }
            if (best == -1) {
                if (lastIdx != -1) {
                    ganttBlocks.add(new int[]{lastIdx, blockStart, currentTime});
                    lastIdx = -1;
                }
                currentTime++; continue;
            }
            if (!startedOnce[best]) {
                rt[best]         = currentTime - at[best];
                startedOnce[best] = true;
            }
            if (best != lastIdx) {
                if (lastIdx != -1) ganttBlocks.add(new int[]{lastIdx, blockStart, currentTime});
                blockStart = currentTime;
                lastIdx    = best;
            }
            rem[best]--;
            currentTime++;
            if (rem[best] == 0) {
                done[best] = true;
                completed++;
                tat[best] = currentTime - at[best];
                wt[best]  = tat[best] - bt[best];
                ganttBlocks.add(new int[]{best, blockStart, currentTime});
                lastIdx = -1;
            }
        }
 
        double avgWT = 0, avgTAT = 0, avgRT = 0;
        for (int i = 0; i < n; i++) { avgWT += wt[i]; avgTAT += tat[i]; avgRT += rt[i]; }
        avgWT /= n; avgTAT /= n; avgRT /= n;
 
        latestPriorityResult = new PriorityResult();
        latestPriorityResult.avgWaitingTime    = avgWT;
        latestPriorityResult.avgTurnaroundTime = avgTAT;
        latestPriorityResult.avgResponseTime   = avgRT;
 
        String[] cols = {"Process ID", "Arrival", "Burst", "Priority", "Waiting", "Turnaround", "Response"};
        Object[][] rows = new Object[n + 1][7];
        for (int i = 0; i < n; i++) {
            rows[i] = new Object[]{processList.get(i).getId(), at[i], bt[i], pri[i], wt[i], tat[i], rt[i]};
        }
        rows[n] = new Object[]{"Average", "—", "—", "—",
            String.format("%.2f", avgWT), String.format("%.2f", avgTAT), String.format("%.2f", avgRT)};
 
        showResultWindow("Priority Scheduling Results", cols, rows, ganttBlocks, ACCENT_PURPLE);
    }
 
    // ===================== SHARED RESULT WINDOW =====================
    private void showResultWindow(String title, String[] cols, Object[][] rows,
                                  List<int[]> ganttBlocks, Color accentColor) {
 
        JDialog dlg = new JDialog(this, title, false);
        dlg.setSize(900, 620);
        dlg.setLocationRelativeTo(this);
 
        GradientPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout(0, 8));
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        dlg.setContentPane(panel);
 
        // ---- GANTT CHART ----
        GanttPanel gantt = new GanttPanel(ganttBlocks, processList, accentColor);
        gantt.setPreferredSize(new Dimension(0, 110));
        JPanel ganttWrapper = new JPanel(new BorderLayout());
        ganttWrapper.setOpaque(false);
        ganttWrapper.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(accentColor, 1, true),
            "Gantt Chart",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            accentColor));
        ganttWrapper.add(gantt, BorderLayout.CENTER);
        panel.add(ganttWrapper, BorderLayout.NORTH);
 
        // ---- TABLE ----
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Object[] row : rows) model.addRow(row);
 
        JTable table = new JTable(model);
        styleTable(table);
        int lastRow = rows.length - 1;
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                if (row == lastRow) {
                    setBackground(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 60));
                    setFont(new Font("Segoe UI", Font.BOLD, 13));
                    setForeground(TEXT_PRIMARY);
                } else {
                    setBackground(row % 2 == 0 ? BG_CARD : new Color(28, 36, 58));
                    setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    setForeground(TEXT_PRIMARY);
                }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return this;
            }
        });
 
        panel.add(darkScroll(table), BorderLayout.CENTER);
        dlg.setVisible(true);
    }
 
    // ===================== GANTT CHART PANEL =====================
    static class GanttPanel extends JPanel {
        private final List<int[]>  blocks;
        private final List<Process> processes;
        private final Color accentColor;
 
        private static final Color[] COLORS = {
            new Color(100, 149, 237), new Color(52, 211, 153),
            new Color(251, 146, 60),  new Color(139, 92, 246),
            new Color(248, 113, 113), new Color(251, 191, 36),
            new Color(34, 211, 238),  new Color(167, 139, 250)
        };
 
        GanttPanel(List<int[]> blocks, List<Process> processes, Color accentColor) {
            this.blocks      = blocks;
            this.processes   = processes;
            this.accentColor = accentColor;
            setOpaque(false);
        }
 
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (blocks.isEmpty()) return;
 
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 
            int PAD   = 20;
            int BAR_Y = 15;
            int BAR_H = 40;
            int drawW = getWidth() - PAD * 2;
            int lastTime = blocks.get(blocks.size() - 1)[2];
            if (lastTime == 0) lastTime = 1;
 
            for (int[] block : blocks) {
                int idx   = block[0];
                int start = block[1];
                int end   = block[2];
 
                int x = PAD + (int)((double) start / lastTime * drawW);
                int w = Math.max(2, (int)((double)(end - start) / lastTime * drawW));
 
                Color c = COLORS[idx % COLORS.length];
                g2.setColor(c);
                g2.fillRoundRect(x, BAR_Y, w, BAR_H, 6, 6);
                g2.setColor(new Color(0, 0, 0, 80));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(x, BAR_Y, w, BAR_H, 6, 6);
 
                if (w > 18) {
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                    g2.setColor(Color.WHITE);
                    FontMetrics fm  = g2.getFontMetrics();
                    String lbl      = (idx < processes.size()) ? processes.get(idx).getId() : "P" + idx;
                    int tx          = x + (w - fm.stringWidth(lbl)) / 2;
                    int ty          = BAR_Y + (BAR_H + fm.getAscent() - fm.getDescent()) / 2;
                    g2.drawString(lbl, tx, ty);
                }
            }
 
            g2.setColor(new Color(100, 116, 139));
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            g2.setStroke(new BasicStroke(1f));
            int axisY = BAR_Y + BAR_H;
            g2.drawLine(PAD, axisY, PAD + drawW, axisY);
 
            java.util.Set<Integer> times = new java.util.TreeSet<>();
            for (int[] b : blocks) { times.add(b[1]); times.add(b[2]); }
            FontMetrics fm = g2.getFontMetrics();
            int prevX = -99;
            for (int t : times) {
                int x = PAD + (int)((double) t / lastTime * drawW);
                g2.drawLine(x, axisY, x, axisY + 4);
                String lbl = String.valueOf(t);
                int lx = x - fm.stringWidth(lbl) / 2;
                if (lx > prevX + 4) {
                    g2.drawString(lbl, lx, axisY + 14);
                    prevX = lx + fm.stringWidth(lbl);
                }
            }
        }
    }
 
    // ===================== COMPARISON WINDOW =====================
    // CHANGE 2: Comparison is now between SRTF and Priority only
    private void openComparisonWindow() {
        comparisonDialog = new JDialog(this, "Algorithm Comparison \u2014 SRTF vs Priority", false);
        comparisonDialog.setSize(980, 460);
        comparisonDialog.setLocationRelativeTo(this);
 
        GradientPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());
        comparisonDialog.setContentPane(panel);
 
        String[] cols = {"Scenario", "SRTF (Preemptive SJF)", "Priority", "Better"};
        comparisonModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
 
        JTable table = new JTable(comparisonModel) {
            public int getRowHeight(int row) { return 58; }
        };
        styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(180);
        table.getColumnModel().getColumn(1).setPreferredWidth(310);
        table.getColumnModel().getColumn(2).setPreferredWidth(310);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
 
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                JTextArea area = new JTextArea(v != null ? v.toString() : "");
                area.setLineWrap(true);
                area.setWrapStyleWord(true);
                area.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                area.setBackground(row % 2 == 0 ? BG_CARD : new Color(28, 36, 58));
                area.setForeground(TEXT_PRIMARY);
                area.setBorder(new EmptyBorder(6, 10, 6, 10));
                return area;
            }
        });
 
        panel.add(darkScroll(table), BorderLayout.CENTER);
        refreshComparisonTable();
        comparisonDialog.setVisible(true);
    }
 
    // CHANGE 2: refreshComparisonTable now compares SRTF vs Priority
    private void refreshComparisonTable() {
        if (comparisonModel == null) return;
        comparisonModel.setRowCount(0);
 
        String srtfStr = latestSRTFResult != null
            ? String.format("Avg WT=%.2f  TAT=%.2f  RT=%.2f",
                latestSRTFResult.avgWaitingTime,
                latestSRTFResult.avgTurnaroundTime,
                latestSRTFResult.avgResponseTime)
            : "Run SRTF first";
 
        String priStr = latestPriorityResult != null
            ? String.format("Avg WT=%.2f  TAT=%.2f  RT=%.2f",
                latestPriorityResult.avgWaitingTime,
                latestPriorityResult.avgTurnaroundTime,
                latestPriorityResult.avgResponseTime)
            : "Run Priority first";
 
        comparisonModel.addRow(new Object[]{
            "1. Short job, low priority",
            "SRTF always runs the shortest remaining job \u2014 priority is completely ignored.",
            "Priority may delay the short job if high-priority longer jobs keep arriving.",
            "SRTF"
        });
        comparisonModel.addRow(new Object[]{
            "2. Long job, high priority",
            "SRTF will preempt the long job the moment a shorter job arrives.",
            "Priority keeps the high-priority long job running \u2014 urgency is respected.",
            "Priority"
        });
        comparisonModel.addRow(new Object[]{
            "3. Avg WT / TAT / RT (your data)",
            srtfStr,
            priStr,
            getBetterOverall()
        });
        comparisonModel.addRow(new Object[]{
            "4. Preemption behaviour",
            "SRTF preempts aggressively on every new arrival \u2014 optimal for minimising WT.",
            "Priority preempts based on urgency level, not burst length.",
            "Depends on goal"
        });
        comparisonModel.addRow(new Object[]{
            "5. Starvation / Fairness",
            "SRTF can starve long jobs if short jobs keep arriving continuously.",
            "Priority can starve low-priority processes if higher-priority ones keep arriving.",
            "Both risky"
        });
    }
 
    // CHANGE 2: getBetterOverall now compares SRTF vs Priority
    private String getBetterOverall() {
        if (latestSRTFResult == null || latestPriorityResult == null) return "Run both first";
        double srtf = (latestSRTFResult.avgWaitingTime + latestSRTFResult.avgTurnaroundTime + latestSRTFResult.avgResponseTime) / 3;
        double pri  = (latestPriorityResult.avgWaitingTime + latestPriorityResult.avgTurnaroundTime + latestPriorityResult.avgResponseTime) / 3;
        if (srtf < pri) return "SRTF";
        if (pri < srtf) return "Priority";
        return "Equal";
    }
 
    // ===================== CONCLUSION WINDOW =====================
    // CHANGE 3: Conclusion now compares SRTF vs Priority only
    private void openConclusionWindow() {
        JDialog dlg = new JDialog(this, "Conclusion", false);
        dlg.setSize(560, 400);
        dlg.setLocationRelativeTo(this);
 
        GradientPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(16, 20, 16, 20));
        dlg.setContentPane(panel);
 
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        area.setBackground(BG_CARD);
        area.setForeground(TEXT_PRIMARY);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(12, 14, 12, 14));
 
        StringBuilder sb = new StringBuilder("CONCLUSION\n");
        sb.append("------------------------------------------\n\n");
 
        if (latestSRTFResult == null && latestPriorityResult == null) {
            sb.append("No results yet.\nPlease run SRTF and/or Priority algorithm first.\n");
        } else {
            if (latestSRTFResult != null)
                sb.append(String.format("SRTF:      Avg WT=%.2f   TAT=%.2f   RT=%.2f%n",
                    latestSRTFResult.avgWaitingTime,
                    latestSRTFResult.avgTurnaroundTime,
                    latestSRTFResult.avgResponseTime));
            if (latestPriorityResult != null)
                sb.append(String.format("Priority:  Avg WT=%.2f   TAT=%.2f   RT=%.2f%n",
                    latestPriorityResult.avgWaitingTime,
                    latestPriorityResult.avgTurnaroundTime,
                    latestPriorityResult.avgResponseTime));
 
            sb.append("\n");
 
            if (latestSRTFResult != null && latestPriorityResult != null) {
                if (latestSRTFResult.avgWaitingTime < latestPriorityResult.avgWaitingTime)
                    sb.append("* SRTF gives lower average waiting time.\n");
                else if (latestPriorityResult.avgWaitingTime < latestSRTFResult.avgWaitingTime)
                    sb.append("* Priority gives lower average waiting time.\n");
                else
                    sb.append("* Both have equal average waiting time.\n");
 
                if (latestSRTFResult.avgTurnaroundTime < latestPriorityResult.avgTurnaroundTime)
                    sb.append("* SRTF gives lower average turnaround time.\n");
                else if (latestPriorityResult.avgTurnaroundTime < latestSRTFResult.avgTurnaroundTime)
                    sb.append("* Priority gives lower average turnaround time.\n");
                else
                    sb.append("* Both have equal average turnaround time.\n");
 
                if (latestSRTFResult.avgResponseTime < latestPriorityResult.avgResponseTime)
                    sb.append("* SRTF gives better average response time.\n");
                else if (latestPriorityResult.avgResponseTime < latestSRTFResult.avgResponseTime)
                    sb.append("* Priority gives better average response time.\n");
                else
                    sb.append("* Both have equal average response time.\n");
 
                sb.append("\n* SRTF minimises waiting time but ignores process urgency.\n");
                sb.append("* Priority respects urgency but may not minimise overall waiting time.\n");
                sb.append("* Both algorithms can cause starvation under certain conditions.\n");
                sb.append("* Consider aging to prevent starvation in real systems.\n");
            }
        }
 
        area.setText(sb.toString());
        area.setCaretPosition(0);
 
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scroll.getViewport().setBackground(BG_CARD);
        panel.add(scroll, BorderLayout.CENTER);
        dlg.setVisible(true);
    }
 
    // ===================== UI HELPERS =====================
    private JLabel separator(String text) {
        JLabel lbl = new JLabel("--  " + text + "  --", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(TEXT_MUTED);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }
 
    private JTextField dialogField() {
        JTextField tf = new JTextField(16);
        tf.setFont(new Font("Consolas", Font.PLAIN, 13));
        tf.setForeground(TEXT_PRIMARY);
        tf.setBackground(BG_INPUT);
        tf.setCaretColor(ACCENT_BLUE);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(5, 10, 5, 10)));
        return tf;
    }
 
    private JButton menuBtn(String text, Color color, Dimension size, ActionListener al) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base  = new Color(color.getRed(), color.getGreen(), color.getBlue(), 35);
                Color hover = new Color(color.getRed(), color.getGreen(), color.getBlue(), 70);
                g2.setColor(getModel().isRollover() ? hover : base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(color);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(size);
        btn.setMaximumSize(size);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 18, 0, 0));
        btn.addActionListener(al);
        return btn;
    }
 
    private JButton accentBtn(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? color.brighter() : color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 36));
        return btn;
    }
 
    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(TEXT_PRIMARY);
        table.setBackground(BG_CARD);
        table.setSelectionBackground(new Color(64, 156, 255, 60));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setGridColor(BORDER_COLOR);
        table.setRowHeight(34);
        table.setIntercellSpacing(new Dimension(0, 1));
 
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(BG_INPUT);
        header.setForeground(ACCENT_BLUE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
 
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBackground(row % 2 == 0 ? BG_CARD : new Color(28, 36, 58));
                setForeground(TEXT_PRIMARY);
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return this;
            }
        });
    }
 
    private JScrollPane darkScroll(JTable table) {
        JScrollPane sp = new JScrollPane(table);
        sp.setOpaque(false);
        sp.getViewport().setBackground(BG_CARD);
        sp.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        return sp;
    }
 
    private void err(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }
 
    // ===================== GRADIENT BACKGROUND =====================
    static class GradientPanel extends JPanel {
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setPaint(new GradientPaint(0, 0, BG_DARK, getWidth(), getHeight(), new Color(18, 10, 38)));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setPaint(new RadialGradientPaint(
                new Point2D.Float(0, 0), getWidth() * 0.65f,
                new float[]{0f, 1f},
                new Color[]{new Color(64, 156, 255, 22), new Color(0, 0, 0, 0)}));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }
 
    // ===================== INNER DATA CLASSES =====================
    public static class Process {
        private final String  id;
        private final int     arrivalTime;
        private final int     burstTime;
        private final Integer priority;
 
        public Process(String id, int arrivalTime, int burstTime, Integer priority) {
            this.id          = id;
            this.arrivalTime = arrivalTime;
            this.burstTime   = burstTime;
            this.priority    = priority;
        }
        public String  getId()          { return id; }
        public int     getArrivalTime() { return arrivalTime; }
        public int     getBurstTime()   { return burstTime; }
        public Integer getPriority()    { return priority; }
    }
 
    // Kept for internal use (SJF code still works if called programmatically)
    static class SJFResult {
        double avgResponseTime, avgTurnaroundTime, avgWaitingTime;
    }

    // NEW: SRTF result class
    static class SRTFResult {
        double avgResponseTime, avgTurnaroundTime, avgWaitingTime;
    }
 
    static class PriorityResult {
        double avgResponseTime, avgTurnaroundTime, avgWaitingTime;
    }
 
    // ===================== MAIN =====================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
            new OS_Project_Main().setVisible(true);
        });
    }
}