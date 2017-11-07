/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants;

/**
 *
 * @author CVanJaarsveldt
 */
public interface WorldListener {
	void antHomeCreated(AntHome antHome);
	void antHomeRemoved(AntHome antHome);
	void stepped();
}
