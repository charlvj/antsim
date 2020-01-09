/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.logs;

import java.time.Instant;

/**
 *
 * @author charlvj
 */
public class Metric {
    final int steps;
    final int antsBorn;
    final int antsDied;
    final int antHomesFounded;
    final int antHomesEnded;

    public Metric(int steps, int antsBorn, int antsDied, int antHomesFounded, int antHomesEnded) {
        this.steps = steps;
        this.antsBorn = antsBorn;
        this.antsDied = antsDied;
        this.antHomesFounded = antHomesFounded;
        this.antHomesEnded = antHomesEnded;
    }

    public int getSteps() {
        return steps;
    }

    public int getAntsBorn() {
        return antsBorn;
    }

    public int getAntsDied() {
        return antsDied;
    }

    public int getAntHomesFounded() {
        return antHomesFounded;
    }

    public int getAntHomesEnded() {
        return antHomesEnded;
    }

    @Override
    public String toString() {
        return "Metric{" + "steps=" + steps + ", antsBorn=" + antsBorn + ", antsDied=" + antsDied + ", antHomesFounded=" + antHomesFounded + ", antHomesEnded=" + antHomesEnded + '}';
    }
    
    
}
