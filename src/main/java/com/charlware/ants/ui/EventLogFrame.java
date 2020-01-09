/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.ui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

/**
 *
 * @author charlvj
 */
public class EventLogFrame extends javax.swing.JFrame {

    private EventLogModel eventLogModel = null;

    /**
     * Creates new form EventLogFrame
     */
    public EventLogFrame(SimAnt simAnt) {
        initComponents();
        eventLogModel = new EventLogModel(simAnt.getWorld().getEventLog());
        tblEvents.setModel(eventLogModel);
//        stats = new StatsChartAccumulator(simAnt.getWorld());
//        stats = new MetricsXYDataset(simAnt.getWorld());
//        JFreeChart chart = ChartFactory.createStackedXYAreaChart("Metrics", "Steps", "Y-Axis", stats, PlotOrientation.VERTICAL, true, true, false);
////        JFreeChart chart = createChart(stats);
//        ChartPanel chartPanel = new ChartPanel(chart);
//        pnlCharts.add(chartPanel, BorderLayout.NORTH);

        MetricsChart metricsChart = new MetricsChart(simAnt.getWorld());
        final JPanel chartPanel = new XChartPanel<XYChart>(metricsChart.createChart());
        metricsChart.addChartListener(new MetricsChartListener() {
            @Override
            public void chartUpdated(XYChart chart) {
                SwingUtilities.invokeLater(() -> {
                   chartPanel.revalidate();
                   chartPanel.repaint();
                });
            }
        });
        pnlCharts.add(chartPanel, BorderLayout.CENTER);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblEvents = new javax.swing.JTable();
        pnlCharts = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Event Log");

        tblEvents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblEvents);

        pnlCharts.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCharts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 12, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(pnlCharts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlCharts;
    private javax.swing.JTable tblEvents;
    // End of variables declaration//GEN-END:variables
}

//class LabelGenerator implements XYItemLabelGenerator {
//
//        @Override
//        public String generateLabel(XYDataset dataset, int series, int item) {
//            StatsChartAccumulator labelSource = (StatsChartAccumulator) dataset;
//            return labelSource.getLabel(series, item);
//        }
//
//    }