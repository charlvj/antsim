/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

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
    
    public int foodstore_stable_capacity = 500;
    public int foodstore_stable_refillRate = 50;
    public int foodstore_stable_refillPeriod = 100;
   
    public SimSettings() {
        // Just use the defaults
//        this("defaults.ini");
    }
    
    public void load(String filename) throws FileNotFoundException, IOException {
        Properties props = new Properties();
        props.load(new FileReader(filename));

        home_birthPeriod        = Integer.parseInt(props.getProperty("home.birth.period"));
        home_birthFoodThreshold = Integer.parseInt(props.getProperty("home.birth.foodThreshold"));
        home_foodstore_capacity = Integer.parseInt(props.getProperty("home.foodstore.capacity"));
        home_foodstore_initial  = Integer.parseInt(props.getProperty("home.foodstore.initial"));
        home_queenBirthPeriod   = Integer.parseInt(props.getProperty("home.queenBirthPeriod"));

        ant_fullTummy   = Integer.parseInt(props.getProperty("ant.fullTummy"));
        ant_hungryTummy = Integer.parseInt(props.getProperty("ant.hungryTummy"));
        ant_deadTummy   = Integer.parseInt(props.getProperty("ant.deadTummy"));
        ant_eatSpeed    = Integer.parseInt(props.getProperty("ant.eatSpeed"));
        ageLimit        = Integer.parseInt(props.getProperty("ant.ageLimit"));

        workerant_foodValue          = Integer.parseInt(props.getProperty("workerant.foodValue"));
        workerant_foodstore_capacity = Integer.parseInt(props.getProperty("workerant.foodstore.capacity"));
        workerant_food_collectSpeed  = Integer.parseInt(props.getProperty("workerant.food.collectSpeed"));
        workerant_food_storeSpeed    = Integer.parseInt(props.getProperty("workerant.food.storeSpeed"));
        
        foodstore_stable_capacity = Integer.parseInt(props.getProperty("foodstore.stable.capacity"));
        foodstore_stable_refillRate = Integer.parseInt(props.getProperty("foodstore.stable.refillRate"));
        foodstore_stable_refillPeriod = Integer.parseInt(props.getProperty("foodstore.stable.refillPeriod"));
    }
    
    public void save(String filename) throws IOException {
        try(PrintWriter writer = new PrintWriter(filename)) {
            writer.println("home.birth.period = " + home_birthPeriod);
            writer.println("home.birth.foodThreshold = " + home_birthFoodThreshold);
            writer.println("home.foodstore.capacity = " + home_foodstore_capacity);
            writer.println("home.foodstore.initial = " + home_foodstore_initial);
            writer.println("home.queenBirthPeriod = " + home_queenBirthPeriod);

            writer.println("ant.fullTummy = " + ant_fullTummy);
            writer.println("ant.hungryTummy = " + ant_hungryTummy);
            writer.println("ant.deadTummy = " + ant_deadTummy);
            writer.println("ant.eatSpeed = " + ant_eatSpeed);
            writer.println("ant.ageLimit = " + ageLimit);

            writer.println("workerant.foodValue = " + workerant_foodValue);
            writer.println("workerant.foodstore.capacity = " + workerant_foodstore_capacity);
            writer.println("workerant.food.collectSpeed = " + workerant_food_collectSpeed);
            writer.println("workerant.food.storeSpeed = " + workerant_food_storeSpeed);
            
            writer.println("foodstore.stable.capacity = " + foodstore_stable_capacity);
            writer.println("foodstore.stable.refillRate = " + foodstore_stable_refillRate);
            writer.println("foodstore.stable.refillPeriod = " + foodstore_stable_refillPeriod);
        }
    }
}


