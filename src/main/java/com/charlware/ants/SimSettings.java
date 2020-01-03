/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author charlvj
 */
public class SimSettings {
    
    // And Home settings
    public int home_birthPeriod = 200;
    public int home_birthFoodThreshold = 500;
    public int home_foodstore_capacity = 100_000;
    public int home_foodstore_initial  = 1_000;
    public int home_queenBirthPeriod   = 10;
    
    // Ant settings
    public int ant_fullTummy = 50;
    public int ant_hungryTummy = 0;
    public int ant_deadTummy = -100;
    public int ant_eatSpeed = 4;
    /*
	Give the ant a long life. This should help to get the size of the
	colony more proportional to the amount of food available. Age helps
	to "recycle" the ants when the colony reaches its limit, ensuring 
	that queens keeps coming forth.
     */

    public int ageLimit = 20_000; // steps
    
    public int workerant_foodValue = 500;
    public int workerant_foodstore_capacity = 75;
    public int workerant_food_collectSpeed = 7;
    public int workerant_food_storeSpeed = 20;
   
    public SimSettings() {
        // Just use the defaults
//        this("defaults.ini");
    }
    
    public SimSettings(String filename) {
        try {
            Properties props = new Properties();
            props.load(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SimSettings.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SimSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}


