/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.logs;

/**
 *
 * @author charlvj
 */
public enum EventType {
    ANTHOME_FOUNDED,
    ANTHOME_ENDED,
    ANT_BORN,
    ANT_DIED;
    
    public String toString() {
        switch(this) {
            case ANTHOME_FOUNDED: return "Ant Home Founded";
            case ANTHOME_ENDED: return "Ant Home Ended";
            case ANT_BORN: return "An Ant was born";
            case ANT_DIED: return "An Ant died";
        }
        return "Danger Will Robinson";
    }
}
