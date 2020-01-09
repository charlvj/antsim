/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.logs;

import com.charlware.ants.AntHome;
import com.charlware.ants.World;
import com.charlware.ants.WorldListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author charlvj
 */
public class EventLog {
    List<Event> events = new Vector<>();
    List<EventLogListener> listeners = new ArrayList<>();
    MetricsLog metrics;
    World world;
    
    AtomicInteger steps = new AtomicInteger(0);
    
    public EventLog(World world) {
        this.world = world;
        metrics = new MetricsLog(world);
        world.addWorldListeners(new WorldListener() {
            @Override
            public void antHomeCreated(AntHome antHome) { }

            @Override
            public void antHomeRemoved(AntHome antHome) { }

            @Override
            public void stepped() {
                steps.incrementAndGet();
            }
        });
    }
    
    public void log(AntHome antHome, EventType eventType) {
        Event e = new Event(steps.get(), antHome, eventType);
        events.add(e);
        metrics.add(e);
        listeners.forEach(listener -> listener.logEntryAdded(e));
    }
 
    public void addListener(EventLogListener listener) {
        this.listeners.add(listener);
    }
    
    public int size() {
        return events.size();
    }
    
    public Event getEvent(int idx) {
        return events.get(idx);
    }
    
    public MetricsLog getMetricsLog() {
        return metrics;
    }
}
