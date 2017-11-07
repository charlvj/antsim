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
public enum AntState {
	Wandering,
	CollectingFood,
	GoingHomeWithMarkers,
	StoringFood,
	FollowingMarker,
	GoingHomeNoMarkers,
	Eating,
	Dead,
	DesperateForFood
}
