/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Maya sameh
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GanttChartPanel {

    // ===================== GANTT DRAWING =====================

    private static final Color[] PROCESS_COLORS = {
        new Color(100, 149, 237),
        new Color(144, 238, 144),
        new Color(255, 182, 193),
        new Color(255, 218, 128),
        new Color(216, 191, 216),
        new Color(135, 206, 235),
        new Color(240, 128, 128),
        new Color(152, 251, 152),
    };

    static void openPriorityWindow(MainFrame aThis, List<Process> processList, PriorityResult latestPriorityResult) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    static class GanttEntry {
        String processId;
        int startTime, endTime;
        GanttEntry(String pid, int s, int e) {
            processId = pid; startTime = s; endTime = e;
        }
    }

    static class GanttDrawPanel extends JPanel {
        private List<GanttEntry> entries = new ArrayList<>();
        private Map<String, Color> colorMap = new HashMap<>();

        GanttDrawPanel() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(900, 100));
        }

        void setEntries(List<GanttEntry> entries) {
            this.entries = entries;
            colorMap.clear();
            int idx = 0;
            for (GanttEntry e : entries) {
                if (!colorMap.containsKey(e.processId)) {
                    colorMap.put(e.processId, PROCESS_COLORS[idx % PROCESS_COLORS.length]);
                    idx++;
                }
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (entries.isEmpty()) return;

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int L = 20, BAR_Y = 10, BAR_H = 50;
            int drawW = getWidth() - L - 20;
            int lastTime = entries.stream().mapToInt(e -> e.endTime).max().orElse(1);

            for (GanttEntry entry : entries) {
                int x = L + (int)((double) entry.startTime / lastTime * drawW);
                int w = Math.max(2, (int)((double)(entry.endTime - entry.startTime) / lastTime * drawW));

                g2.setColor(colorMap.getOrDefault(entry.processId, PROCESS_COLORS[0]));
                g2.fillRect(x, BAR_Y, w, BAR_H);
                g2.setColor(new Color(80, 80, 80));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRect(x, BAR_Y, w, BAR_H);

                if (w > 20) {
                    g2.setFont(new Font("Arial", Font.BOLD, 11));
                    g2.setColor(Color.BLACK);
                    FontMetrics fm = g2.getFontMetrics();
                    String lbl = entry.processId;
                    if (fm.stringWidth(lbl) > w - 4) lbl = "";
                    int tx = x + (w - fm.stringWidth(lbl)) / 2;
                    int ty = BAR_Y + (BAR_H + fm.getAscent() - fm.getDescent()) / 2;
                    g2.drawString(lbl, tx, ty);
                }
            }

            // Time axis
            int axisY = BAR_Y + BAR_H;
            g2.setColor(new Color(80, 80, 80));
            g2.setStroke(new BasicStroke(1f));
            g2.drawLine(L, axisY, L + drawW, axisY);
            g2.setFont(new Font("Arial", Font.PLAIN, 10));
            FontMetrics fm = g2.getFontMetrics();

            java.util.Set<Integer> times = new java.util.TreeSet<>();
            for (GanttEntry e : entries) {
                times.add(e.startTime);
                times.add(e.endTime);
            }
            int prevX = -999;
            for (int t : times) {
                int x = L + (int)((double) t / lastTime * drawW);
                g2.drawLine(x, axisY, x, axisY + 4);
                String lbl = String.valueOf(t);
                int lx = x - fm.stringWidth(lbl) / 2;
                if (lx > prevX + 5) {
                    g2.drawString(lbl, lx, axisY + 14);
                    prevX = lx + fm.stringWidth(lbl);
                }
            }
        }
    }

    // ===================== SJF WINDOW =====================

    public static void openSJFWindow(java.awt.Window parent, List<Process> processList) {
        if (processList.isEmpty()) {
            JOptionPane.showMessageDialog(parent,
                "Please add at least one process first!",
                "No Processes", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog sjfDialog = new JDialog((Frame) parent, "SJF Scheduling Algorithm", false);
        sjfDialog.setSize(1000, 750);
        sjfDialog.setLocationRelativeTo(parent);
        sjfDialog.setLayout(new BorderLayout(0, 10));

        // Gantt panel
        GanttDrawPanel ganttDraw = new GanttDrawPanel();
        JPanel ganttWrapper = new JPanel(new BorderLayout());
        ganttWrapper.setBackground(Color.WHITE);
        ganttWrapper.setBorder(BorderFactory.createTitledBorder("Gantt Chart — SJF"));
        ganttWrapper.add(new JScrollPane(ganttDraw), BorderLayout.CENTER);
        ganttWrapper.setPreferredSize(new Dimension(0, 150));

        // Results table
        String[] columns = {"Process ID", "Arrival Time", "Burst Time", "Waiting Time", "Turnaround Time", "Response Time"};
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

            int n = processList.size();

            // بناء SJF processes من الـ processList
            SJF.Process[] sjfArr = new SJF.Process[n];
            for (int i = 0; i < n; i++) {
                Process p = processList.get(i);
                sjfArr[i] = new SJF.Process();
                sjfArr[i].id = i + 1;
                sjfArr[i].at = p.getArrivalTime();
                sjfArr[i].bt = p.getBurstTime();
                sjfArr[i].rem = p.getBurstTime();
                sjfArr[i].done = false;
                sjfArr[i].startedOnce = false;
            }

            // تشغيل SJF
            SJF.SJF(sjfArr, n);

            // بناء الـ Gantt entries
            // SJF non-preemptive: كل process بتتنفذ من startTime لـ startTime + bt
            List<GanttEntry> entries = new ArrayList<>();
            // نحسب الـ start time من الـ algorithm
            // الـ rt = currentTime - at عند بداية التنفيذ
            // يعني startTime = at + rt
            for (int i = 0; i < n; i++) {
                SJF.Process p = sjfArr[i];
                int start = p.at + p.rt;
                int end   = start + p.bt;
                entries.add(new GanttEntry(
                    processList.get(i).getId(),
                    start,
                    end
                ));
            }
            // رتبي الـ entries بالـ start time عشان الرسم يكون صح
            entries.sort((a, b) -> a.startTime - b.startTime);
            ganttDraw.setEntries(entries);

            // املي الجدول
            double totalWT = 0, totalTAT = 0, totalRT = 0;
            for (int i = 0; i < n; i++) {
                SJF.Process p = sjfArr[i];
                tableModel.addRow(new Object[]{
                    processList.get(i).getId(),
                    p.at,
                    p.bt,
                    p.wt,
                    p.tat,
                    p.rt
                });
                totalWT  += p.wt;
                totalTAT += p.tat;
                totalRT  += p.rt;
            }

            // صف المتوسطات
            tableModel.addRow(new Object[]{
                "Average", "—", "—",
                String.format("%.2f", totalWT  / n),
                String.format("%.2f", totalTAT / n),
                String.format("%.2f", totalRT  / n)
            });

            styleTable(outputTable, tableModel);
        });

        JPanel topPanel = new JPanel();
        topPanel.add(btnRun);

        sjfDialog.add(topPanel,      BorderLayout.NORTH);
        sjfDialog.add(ganttWrapper,  BorderLayout.CENTER);
        sjfDialog.add(tableScroll,   BorderLayout.SOUTH);
        sjfDialog.setVisible(true);
    }

    // ===================== PRIORITY WINDOW =====================

    public static void openPriorityWindow(java.awt.Window parent, List<Process> processList) {
        if (processList.isEmpty()) {
            JOptionPane.showMessageDialog(parent,
                "Please add at least one process first!",
                "No Processes", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // تأكدي إن كل الـ processes عندها priority
        for (Process p : processList) {
            if (p.getPriority() == null) {
                JOptionPane.showMessageDialog(parent,
                    "All processes must have a priority value!",
                    "Missing Priority", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        JDialog priorityDialog = new JDialog((Frame) parent, "Priority Scheduling Algorithm", false);
        priorityDialog.setSize(1000, 750);
        priorityDialog.setLocationRelativeTo(parent);
        priorityDialog.setLayout(new BorderLayout(0, 10));

        // Gantt panel
        GanttDrawPanel ganttDraw = new GanttDrawPanel();
        JPanel ganttWrapper = new JPanel(new BorderLayout());
        ganttWrapper.setBackground(Color.WHITE);
        ganttWrapper.setBorder(BorderFactory.createTitledBorder("Gantt Chart — Priority"));
        ganttWrapper.add(new JScrollPane(ganttDraw), BorderLayout.CENTER);
        ganttWrapper.setPreferredSize(new Dimension(0, 150));

        // Results table
        String[] columns = {"Process ID", "Arrival Time", "Burst Time", "Priority", "Waiting Time", "Turnaround Time", "Response Time"};
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

            // بناء Priority processes من الـ processList
            List<PriorityScheduler.Process_Priority> prList = new ArrayList<>();
            for (Process p : processList) {
                prList.add(new PriorityScheduler.Process_Priority(
                    p.getId(),
                    p.getBurstTime(),
                    p.getArrivalTime(),
                    p.getPriority()
                ));
            }

            // بناء الـ timeline للـ Gantt قبل تشغيل الـ algorithm
            List<GanttEntry> entries = new ArrayList<>();
            String gLastId = null;
            int gBlockStart = 0;

            // نشغل الـ algorithm يدوياً عشان نبني الـ timeline
            int gTime = 0, gCompleted = 0;
            int gn = prList.size();
            // نعمل copy عشان نشغل مرتين (مرة للـ Gantt ومرة للـ metrics)
            List<PriorityScheduler.Process_Priority> copyList = new ArrayList<>();
            for (Process p : processList) {
                copyList.add(new PriorityScheduler.Process_Priority(
                    p.getId(),
                    p.getBurstTime(),
                    p.getArrivalTime(),
                    p.getPriority()
                ));
            }

            while (gCompleted < gn) {
                PriorityScheduler.Process_Priority best = null;
                int minPri = Integer.MAX_VALUE;
                for (PriorityScheduler.Process_Priority pp : copyList) {
                    if (pp.arrivalTime <= gTime && !pp.isCompleted) {
                        if (pp.priority < minPri) {
                            minPri = pp.priority;
                            best = pp;
                        } else if (pp.priority == minPri && best != null) {
                            if (pp.arrivalTime < best.arrivalTime) best = pp;
                        }
                    }
                }
                if (best == null) { gTime++; continue; }

                String currentId = best.processId;
                if (!currentId.equals(gLastId)) {
                    if (gLastId != null) {
                        entries.add(new GanttEntry(gLastId, gBlockStart, gTime));
                    }
                    gBlockStart = gTime;
                    gLastId = currentId;
                }

                best.remainingTime--;
                gTime++;

                if (best.remainingTime == 0) {
                    best.isCompleted = true;
                    gCompleted++;
                    entries.add(new GanttEntry(gLastId, gBlockStart, gTime));
                    gLastId = null;
                }
            }

            ganttDraw.setEntries(entries);

            // تشغيل الـ algorithm الأصلي للـ metrics
            PriorityScheduler.scheduleProcesses(prList);

            // املي الجدول
            int n = prList.size();
            double totalWT = 0, totalTAT = 0, totalRT = 0;
            for (PriorityScheduler.Process_Priority p : prList) {
                tableModel.addRow(new Object[]{
                    p.processId,
                    p.arrivalTime,
                    p.burstTime,
                    p.priority,
                    p.waitingTime,
                    p.turnaroundTime,
                    p.responseTime
                });
                totalWT  += p.waitingTime;
                totalTAT += p.turnaroundTime;
                totalRT  += p.responseTime;
            }

            // صف المتوسطات
            tableModel.addRow(new Object[]{
                "Average", "—", "—", "—",
                String.format("%.2f", totalWT  / n),
                String.format("%.2f", totalTAT / n),
                String.format("%.2f", totalRT  / n)
            });

            styleTable(outputTable, tableModel);
        });

        JPanel topPanel = new JPanel();
        topPanel.add(btnRun);

        priorityDialog.add(topPanel,      BorderLayout.NORTH);
        priorityDialog.add(ganttWrapper,  BorderLayout.CENTER);
        priorityDialog.add(tableScroll,   BorderLayout.SOUTH);
        priorityDialog.setVisible(true);
    }

    // ===================== HELPER =====================

    private static void styleTable(JTable table, DefaultTableModel model) {
        int lastRow = model.getRowCount() - 1;
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                if (row == lastRow) {
                    c.setBackground(new Color(173, 216, 230));
                    c.setForeground(Color.BLACK);
                    setFont(new Font("Arial", Font.BOLD, 12));
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                    c.setForeground(Color.BLACK);
                    setFont(new Font("Arial", Font.PLAIN, 12));
                }
                return c;
            }
        });
    }
}