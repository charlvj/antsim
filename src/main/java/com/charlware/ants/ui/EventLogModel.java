/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.ui;

import com.charlware.ants.logs.Event;
import com.charlware.ants.logs.EventLog;
import com.charlware.ants.logs.EventLogListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author charlvj
 */
public class EventLogModel extends AbstractTableModel
        implements EventLogListener {

    private static String[] cols = new String[]{"Time", "AntHome", "Event"};
    private EventLog eventLog = null;

    public EventLogModel(EventLog eventLog) {
        this.eventLog = eventLog;
        eventLog.setListener(this);
    }

    @Override
    public int getRowCount() {
        return eventLog.size();
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }

    @Override
    public String getColumnName(int column) {
        return cols[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Event event = eventLog.getEvent(rowIndex);
        switch (columnIndex) {
            case 0:
                return event.getTime();
            case 1:
                return event.getAntHome().getId();
            case 2:
                return event.getEventType();
        }
        return null;
    }

    @Override
    public void logEntryAdded(Event event) {
        int lastRow = getRowCount() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }
}
