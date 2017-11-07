/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants;

import com.charlware.ants.sim.MatrixLocation;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CVanJaarsveldt
 */
public class MapUtils {
	public static List<MapDirection> plotRoute(MatrixLocation from, MatrixLocation to) {
		int dx = to.getX() - from.getX();
		int dy = to.getY() - from.getY();
		int nx = Math.abs(dx);
		int ny = Math.abs(dy);
		int sx = dx > 0 ? 1 : -1;
		int sy = dy > 0 ? 1 : -1;
		
		List<MapDirection> result = new ArrayList<>();
		MatrixLocation last = from;
		MatrixLocation p = new MatrixLocation(from.getX(), from.getY());
		for(int ix = 0, iy = 0; ix < nx || iy < ny;) {
			if((0.5 + ix) / nx < (0.5 + iy) / ny) {
				// move horizontally
				p = p.derive(sx, 0);
				ix++;
			}
			else {
				// move vertically
				p = p.derive(0, sy);
				iy++;
			}
			result.add(last.getDirectionTo(p));
		}
		return result;
	}
}
