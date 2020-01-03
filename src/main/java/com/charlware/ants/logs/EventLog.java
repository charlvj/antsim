/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.logs;

import com.charlware.ants.AntHome;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author charlvj
 */
public class EventLog {
    List<Event> events = new Vector<>();
    EventLogListener listener = null;
    
    public void log(AntHome antHome, EventType eventType) {
        Event e = new Event(antHome, eventType);
        events.add(e);
        if(listener != null) listener.logEntryAdded(e);
    }
 
    public void setListener(EventLogListener listener) {
        this.listener = listener;
    }
    
    public int size() {
        return events.size();
    }
    
    public Event getEvent(int idx) {
        return events.get(idx);
    }
}
