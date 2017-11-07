/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.ant;

/**
 *
 * @author CVanJaarsveldt
 */
public abstract class AntAction {
	protected final Ant ant;
	
	public AntAction(final Ant ant) {
		this.ant = ant;
	}
	
	public abstract boolean stepAction();
}
