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
    final Instant time;
    final AntHome antHome;
    final EventType eventType;
    
    public Event(AntHome antHome, EventType eventType) {
        this(Instant.now(), antHome, eventType);
    }
    
    public Event(Instant time, AntHome antHome, EventType eventType) {
        this.time = time;
        this.antHome = antHome;
        this.eventType = eventType;
    }
    
    public String toString() {
        return "[" + time + "]  - " + antHome.getId() + " - " + eventType;
    }

    public Instant getTime() {
        return time;
    }

    public AntHome getAntHome() {
        return antHome;
    }

    public EventType getEventType() {
        return eventType;
    }
    
    
}
