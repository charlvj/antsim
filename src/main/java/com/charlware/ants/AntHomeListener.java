/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants;

import com.charlware.ants.ant.Ant;

/**
 *
 * @author CVanJaarsveldt
 */
public interface AntHomeListener {
	void antBorn(AntHome antHome, Ant ant);
	void antDied(AntHome antHome, Ant ant);
	void foodStorageEmpty(AntHome antHome);
	void foodStorageFull(AntHome antHome);
}
