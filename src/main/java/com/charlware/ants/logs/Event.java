/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.logs;

import com.charlware.ants.AntHome;
import java.time.Instant;

/**
 *
 * @author charlvj
 */
public class Event {
    final int step;
    final AntHome antHome;
    final EventType eventType;
    
    public Event(int step, AntHome antHome, EventType eventType) {
        this.step = step;
        this.antHome = antHome;
        this.eventType = eventType;
    }
    
    public String toString() {
        return "[" + step + "]  - " + antHome.getId() + " - " + eventType;
    }

    public int getStep() {
        return step;
    }

    public AntHome getAntHome() {
        return antHome;
    }

    public EventType getEventType() {
        return eventType;
    }
    
    
}
