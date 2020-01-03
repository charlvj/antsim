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
    public int fullTummy = 50;
    public int hungryTummy = 0;
    public int deadTummy = -100;
    public int eatSpeed = 4;
    public int ageLimit = 20_000; // steps
   
    public SimSettings() {
        this("defaults.ini");
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


