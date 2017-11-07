/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.ui;

import com.charlware.ants.AntHome;
import com.charlware.ants.World;
import com.charlware.ants.WorldListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author CVanJaarsveldt
 */
public class StatsModel extends AbstractTableModel {

	private static String[] cols = new String[] {"Id", "Ants", "Food"};
	private World world = null;

	public StatsModel(World world) {
		this.world = world;
		
		world.addWorldListeners(new WorldListener() {
			@Override
			public void antHomeCreated(AntHome antHome) {
				int lastRow = getRowCount() - 1;
				fireTableRowsInserted(lastRow, lastRow);
			}

			@Override
			public void antHomeRemoved(AntHome antHome) {
				int removedRow = antHome.getId() - 1;
				fireTableRowsDeleted(removedRow, removedRow);
			}

			@Override
			public void stepped() {
				for(int i = 0; i < getRowCount(); i++) {
					fireTableCellUpdated(i, 1);
					fireTableCellUpdated(i, 2);
				}
			}
			
		});
	}
	
	public void setWorld(World world) {
		this.world = world;
		fireTableDataChanged();
	}
	
//	public void simulationStepped() {
////		fireTableDataChanged();
//		for(int i = 0; i < getRowCount(); i++) {
//			fireTableCellUpdated(i, 2);
//		}
//	}
	
	@Override
	public int getRowCount() {
		return world.getAntHomes().size();
	}

	@Override
	public int getColumnCount() {
		return cols.length;
	}

	@Override
	public String getColumnName(int column) {
		return cols[column];
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		AntHome home = getAntHome(rowIndex);
		switch(columnIndex) {
			case 0: return home.getId();
			case 1: return home.getAnts().size();
			case 2: return home.getFoodStorage().getCurrent();
		}
		return null;
	}
	
	public AntHome getAntHome(int row) {
		return world.getAntHomes().get(row);
	}
}
