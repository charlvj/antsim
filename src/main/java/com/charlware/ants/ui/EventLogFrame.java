/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.ui;

import com.charlware.ants.ui.charts.AntHomesChart;
import com.charlware.ants.ui.charts.ChartListener;
import com.charlware.ants.ui.charts.MetricsChart;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

/**
 *
 * @author charlvj
 */
public class EventLogFrame extends javax.swing.JFrame implements ChartListener {

    private EventLogModel eventLogModel = null;

    private Map<XYChart, JPanel> charts = new HashMap<>();
    
    /**
     * Creates new form EventLogFrame
     */
    public EventLogFrame(SimAnt simAnt) {
        initComponents();
        eventLogModel = new EventLogModel(simAnt.getWorld().getEventLog());
        tblEvents.setModel(eventLogModel);

        XYChart chart;
        JPanel chartPanel;
        
        MetricsChart metricsChart = new MetricsChart(simAnt.getWorld());
        metricsChart.setTitle("Overall Population");
        metricsChart.addChartListener(this);
        
        chart = metricsChart.createChart();
        chartPanel = new XChartPanel<XYChart>(chart);
        pnlCharts.add(chartPanel);
        charts.put(chart, chartPanel);
        
        AntHomesChart antHomesChart = new AntHomesChart(simAnt.getWorld());
        antHomesChart.setTitle("AntHome Populations");
        antHomesChart.addChartListener(this);
        
        chart = antHomesChart.createChart();
        chartPanel = new XChartPanel<XYChart>(chart);
        pnlCharts.add(chartPanel);
        charts.put(chart, chartPanel);
        
        pack();
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

        pnlCharts.setLayout(new java.awt.GridLayout(0, 1));

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

    @Override
    public void chartUpdated(XYChart chart) {
        SwingUtilities.invokeLater(() -> {
            JPanel p = charts.get(chart);
            p.revalidate();
            p.repaint();
         });
    }
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