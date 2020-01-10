/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.ui.charts;

import com.charlware.ants.World;
import com.charlware.ants.logs.EventLog;
import com.charlware.ants.logs.EventLogListener;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

/**
 *
 * @author charlvj
 */
public abstract class AntSimChart implements EventLogListener {
    protected final World world;
    protected XYChart chart;
    protected final EventLog eventLog;
    
    private List<ChartListener> listeners = new ArrayList<>();

    private String title = "";
    private String xAxisTitle = "Steps";
    private String yAxisTitle = "Count";
    
    public AntSimChart(World world) {
        this.world = world;
        this.eventLog = world.getEventLog();
        this.eventLog.addListener(this);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setxAxisTitle(String xAxisTitle) {
        this.xAxisTitle = xAxisTitle;
    }

    public void setyAxisTitle(String yAxisTitle) {
        this.yAxisTitle = yAxisTitle;
    }
    
    protected abstract void setupSeries();
    
    public void addChartListener(ChartListener listener) {
        listeners.add(listener);
    }
    
    public void removeChartListener(ChartListener listener) {
        listeners.remove(listener);
    }
    
    public void fireChartListener() {
        listeners.forEach(listener -> listener.chartUpdated(chart));
    }
    
    public XYChart createChart() {
        chart = new XYChartBuilder()
                .title(title)
                .xAxisTitle(xAxisTitle)
                .yAxisTitle(yAxisTitle)
                .build();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);

        setupSeries();
        
        return chart;
    }
}
