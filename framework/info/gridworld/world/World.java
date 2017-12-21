/* 
 * AP(r) Computer Science GridWorld Case Study:
 * Copyright(c) 2005-2006 Cay S. Horstmann (http://horstmann.com)
 *
 * This code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * @author Cay Horstmann
 */

package info.gridworld.world;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import info.gridworld.actor.Actor;
import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import info.gridworld.gui.WorldFrame;

/**
 * 
 * A <code>World</code> is the mediator between a grid and the GridWorld GUI. <br />
 * This class is not tested on the AP CS A and AB exams.
 */
public class World<T> {
	private Grid<T> gr;
	private Set<String> occupantClassNames;
	private Set<String> gridClassNames;
	private String message;
	private WorldFrame<T> frame;
	private boolean hasShown = false;

	private static Random generator = new Random();

	private static final int DEFAULT_ROWS = 10;
	private static final int DEFAULT_COLS = 10;

	public World() {
		this(new BoundedGrid<T>(DEFAULT_ROWS, DEFAULT_COLS));
		message = null;
	}

	public World(Grid<T> g) {
		gr = g;
		gridClassNames = new TreeSet<String>();
		occupantClassNames = new TreeSet<String>();
		addGridClass("info.gridworld.grid.BoundedGrid");
		addGridClass("info.gridworld.grid.UnboundedGrid");
	}

	/**
	 * Constructs and shows a frame for this world.
	 */
	public void show() {
		if (frame == null) {
			hasShown = true;
			frame = new WorldFrame<T>(this);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
			frame.setVisible(true);
		} else
			frame.repaint();
	}

	/**
	 * Gets the grid managed by this world.
	 * 
	 * @return the grid
	 */
	public Grid<T> getGrid() {
		return gr;
	}

	/**
	 * Sets the grid managed by this world.
	 * 
	 * @param newGrid
	 *            the new grid
	 */
	public void setGrid(Grid<T> newGrid) {
		gr = newGrid;
		repaint();
	}

	/**
	 * Sets the message to be displayed in the world frame above the grid.
	 * 
	 * @param newMessage
	 *            the new message
	 */
	public void setMessage(String newMessage) {
		message = newMessage;
		repaint();
	}

	/**
	 * Gets the message to be displayed in the world frame above the grid.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * This method is called when the user clicks on the step button, or when
	 * run mode has been activated by clicking the run button.
	 */
	public void step() {
		repaint();
	}

	/**
	 * This method is called when the user clicks on a location in the
	 * WorldFrame.
	 * 
	 * @param loc
	 *            the grid location that the user selected
	 * @return true if the world consumes the click, or false if the GUI should
	 *         invoke the Location->Edit menu action
	 */
	public boolean locationClicked(Location loc) {
		return false;
	}

	/**
	 * This method is called when a key was pressed. Override it if your world
	 * wants to consume some keys (e.g. "1"-"9" for Sudoku). Don't consume plain
	 * arrow keys, or the user loses the ability to move the selection square
	 * with the keyboard.
	 * 
	 * @param description
	 *            the string describing the key, in <a href=
	 *            "http://java.sun.com/javase/6/docs/api/javax/swing/KeyStroke.html#getKeyStroke(java.lang.String)"
	 *            >this format</a>.
	 * @param loc
	 *            the selected location in the grid at the time the key was
	 *            pressed
	 * @return true if the world consumes the key press, false if the GUI should
	 *         consume it.
	 */
	public boolean keyPressed(String description, Location loc) {
		return false;
	}

	/**
	 * Gets a random empty location in this world.
	 * 
	 * @return a random empty location
	 */
	public Location getRandomEmptyLocation() {
		Grid<T> gr = getGrid();
		int rows = gr.getNumRows();
		int cols = gr.getNumCols();

		if (rows > 0 && cols > 0) // bounded grid
		{
			// get all valid empty locations and pick one at random
			ArrayList<Location> emptyLocs = new ArrayList<Location>();
			for (int i = 0; i < rows; i++)
				for (int j = 0; j < cols; j++) {
					Location loc = new Location(i, j);
					if (gr.isValid(loc) && gr.get(loc) == null)
						emptyLocs.add(loc);
				}
			if (emptyLocs.size() == 0)
				return null;
			int r = generator.nextInt(emptyLocs.size());
			return emptyLocs.get(r);
		} else
		// unbounded grid
		{
			while (true) {
				// keep generating a random location until an empty one is found
				int r;
				if (rows < 0)
					r = (int) (DEFAULT_ROWS * generator.nextGaussian());
				else
					r = generator.nextInt(rows);
				int c;
				if (cols < 0)
					c = (int) (DEFAULT_COLS * generator.nextGaussian());
				else
					c = generator.nextInt(cols);
				Location loc = new Location(r, c);
				if (gr.isValid(loc) && gr.get(loc) == null)
					return loc;
			}
		}
	}

	/**
	 * Adds an occupant at a given location.
	 * 
	 * @param loc
	 *            the location
	 * @param occupant
	 *            the occupant to add
	 */
	public void add(Location loc, T occupant) {
		getGrid().put(loc, occupant);
		repaint();
	}

	/**
	 * Removes an occupant from a given location.
	 * 
	 * @param loc
	 *            the location
	 * @return the removed occupant, or null if the location was empty
	 */
	public T remove(Location loc) {
		T r = getGrid().remove(loc);
		repaint();
		return r;
	}

	/**
	 * Adds a class to be shown in the "Set grid" menu.
	 * 
	 * @param className
	 *            the name of the grid class
	 */
	public void addGridClass(String className) {
		gridClassNames.add(className);
	}

	/**
	 * Adds a class to be shown when clicking on an empty location.
	 * 
	 * @param className
	 *            the name of the occupant class
	 */
	public void addOccupantClass(String className) {
		occupantClassNames.add(className);
	}

	/**
	 * Gets a set of grid classes that should be used by the world frame for
	 * this world.
	 * 
	 * @return the set of grid class names
	 */
	public Set<String> getGridClasses() {
		return gridClassNames;
	}

	/**
	 * Gets a set of occupant classes that should be used by the world frame for
	 * this world.
	 * 
	 * @return the set of occupant class names
	 */
	public Set<String> getOccupantClasses() {
		return occupantClassNames;
	}

	private void repaint() {
		if (frame != null)
			frame.repaint();
	}

	/**
	 * Returns a string that shows the positions of the grid occupants.
	 */
	public String toString() {
		String s = "";
		Grid<?> gr = getGrid();

		int rmin = 0;
		int rmax = gr.getNumRows() - 1;
		int cmin = 0;
		int cmax = gr.getNumCols() - 1;
		if (rmax < 0 || cmax < 0) // unbounded grid
		{
			for (Location loc : gr.getOccupiedLocations()) {
				int r = loc.getRow();
				int c = loc.getCol();
				if (r < rmin)
					rmin = r;
				if (r > rmax)
					rmax = r;
				if (c < cmin)
					cmin = c;
				if (c > cmax)
					cmax = c;
			}
		}

		for (int i = rmin; i <= rmax; i++) {
			for (int j = cmin; j < cmax; j++) {
				Object obj = gr.get(new Location(i, j));
				if (obj == null)
					s += " ";
				else
					s += obj.toString().substring(0, 1);
			}
			s += "\n";
		}
		return s;
	}

	/**
	 * Returns a string of all the actors on the grid formatted to be saved
	 * 
	 * @author Ben Eisner, Jake Balfour, Shon Kaganovich
	 */
	private String getActorString() {
		Grid<?> gr = getGrid();
		ArrayList<Location> locs = gr.getOccupiedLocations();

		String code = "";
		String actorName;
		int i = 1;
		code += "\n\t\tActorWorld world = new ActorWorld();";
		code += "\n\t\tworld.setGrid(new BoundedGrid<Actor>(" + gr.getNumRows()
				+ "," + gr.getNumCols() + "));";

		for (Location a : locs) {
			String name1 = gr.get(a).getClass().getName();
			name1 = name1.replace(".", " ");
			String[] s = name1.split(" ");
			String actorType = s[s.length - 1];
			actorName = actorType + i;
			i++;
			code += "\n\t\t" + actorType + " " + actorName + " = new "
					+ actorType + "();";

			Color c = ((Actor) gr.get(a)).getColor();
			int dir = ((Actor) gr.get(a)).getDirection();
			if (c == null)
				code += "\n\t\t" + actorName + ".setColor(null);";
			else
				code += "\n\t\t" + actorName + ".setColor(new Color("
						+ c.getRed() + "," + c.getGreen() + "," + c.getBlue()
						+ "));";
			code += "\n\t\t" + actorName + ".setDirection(" + dir + ");";
			code += "\n\t\tworld.add(new Location(" + a.getRow() + ","
					+ a.getCol() + ")," + actorName + ");";

		}
		code += "\n\t\tworld.show();";
		return code;
	}

	/**
	 * Writes save output to a .java file
	 * 
	 * @author Ben Eisner
	 */
	private void writeFile(String actorString) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("/H"));
		int retrival = chooser.showSaveDialog(null);
		if (retrival == JFileChooser.APPROVE_OPTION) {
			try {
				String fileName = chooser.getSelectedFile() + "";
				if (!fileName.substring(fileName.length() - 5,
						fileName.length()).equals(".java"))
					fileName = fileName + ".java";
				FileWriter fw = new FileWriter(fileName);
				String s = chooser.getSelectedFile().getName();
				if (s.length() > 5
						&& s.substring(s.length() - 5, s.length()).equals(
								".java"))
					s = s.substring(0, s.length() - 5);
				actorString = "/*Save Written By: Ben Eisner, Shon Kaganovich, Jake Balfour ©2015*/\n\nimport info.gridworld.actor.Actor;\nimport info.gridworld.actor.ActorWorld;\nimport info.gridworld.actor.Bug;\nimport info.gridworld.actor.Flower;\nimport info.gridworld.actor.Rock;\nimport info.gridworld.actor.Critter;\nimport info.gridworld.grid.BoundedGrid;\nimport info.gridworld.grid.Grid;\nimport info.gridworld.grid.Location;\nimport java.awt.Color;\n\n"
						+ "public class "
						+ s
						+ "{\n\n\tpublic static void main(String[]args){"
						+ actorString + "\n\t}\n}";

				fw.write(actorString);
				System.out.println("Saved " + fileName);
				JOptionPane.showMessageDialog(null, "Saved Runner to "
						+ fileName + "!");
				fw.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	/**
	 * Copies save output to clipboard
	 * 
	 * @author Ben Eisner
	 */
	private void clipboardFile(String actorString) {
		actorString = "/*Save Written By: Ben Eisner, Shon Kaganovich, Jake Balfour ©2015*/\n\nimport info.gridworld.actor.Actor;\nimport info.gridworld.actor.ActorWorld;\nimport info.gridworld.actor.Bug;\nimport info.gridworld.actor.Flower;\nimport info.gridworld.actor.Rock;\nimport info.gridworld.actor.Critter;\nimport info.gridworld.grid.BoundedGrid;\nimport info.gridworld.grid.Grid;\nimport info.gridworld.grid.Location;\nimport java.awt.Color;\n\n"
				+ "public class Runner/*Change 'Runner' to your class name*/"
				+ "{\n\n\tpublic static void main(String[]args){"
				+ actorString
				+ "\n\t}\n}";

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		StringSelection strSel = new StringSelection(actorString);
		clipboard.setContents(strSel, null);
		JOptionPane.showMessageDialog(null,
				"Runner saved to clipboard!\nUse Control+V to paste.");
	}

	/**
	 * Similar to getActorString(), but will also save custom classes with
	 * parameters
	 * 
	 * @author Ben Eisner
	 */
	private String getActorStringWithConstructors() {
		Grid<?> gr = getGrid();
		ArrayList<Location> locs = gr.getOccupiedLocations();

		String code = "";
		String actorName;
		int i = 1;
		code += "\n\t\tActorWorld world = new ActorWorld();";
		code += "\n\t\tworld.setGrid(new BoundedGrid<Actor>(" + gr.getNumRows()
				+ "," + gr.getNumCols() + "));";

		for (Location a : locs) {
			String name1 = gr.get(a).getClass().getName();
			name1 = name1.replace(".", " ");
			String[] s = name1.split(" ");
			String actorType = s[s.length - 1];
			actorName = actorType + i;
			i++;
			String[] constr = { actorType, actorName };
			String parenString = getConstructorString(
					getConstructor(constr, actorType, a),
					getConstructorValues(a), a, actorType);
			if (parenString == null)
				code += "\n\t\t" + actorType + " " + actorName + " = new "
						+ actorType + "();";
			else
				code += "\n\t\t" + actorType + " " + actorName + " = new "
						+ actorType + parenString;
			Color c = ((Actor) gr.get(a)).getColor();
			int dir = ((Actor) gr.get(a)).getDirection();
			if (c == null)
				code += "\n\t\t" + actorName + ".setColor(null);";
			else
				code += "\n\t\t" + actorName + ".setColor(new Color("
						+ c.getRed() + "," + c.getGreen() + "," + c.getBlue()
						+ "));";
			code += "\n\t\t" + actorName + ".setDirection(" + dir + ");";
			code += "\n\t\tworld.add(new Location(" + a.getRow() + ","
					+ a.getCol() + ")," + actorName + ");";

		}
		code += "\n\t\tworld.show();";
		return code;
	}

	/**
	 * Returns a string of the final constructor output which will be saved
	 * 
	 * @author Ben Eisner
	 */
	private String getConstructorString(Object[] constructorObjects,
			Object[] constValues, Location a, String actorType) {
		if (constructorObjects != null && constValues != null) {
			String selectedConstructor = (String) constructorObjects[0];
			Class<?>[] dataTypes = (Class<?>[]) constructorObjects[1];
			@SuppressWarnings("unchecked")
			ArrayList<Object> constructorValues = (ArrayList<Object>) constValues[0];
			@SuppressWarnings("unchecked")
			ArrayList<Object> constructorValuesNum = (ArrayList<Object>) constValues[1];
			String[] showOps = new String[constructorValues.size()];
			String[] fOps = new String[constructorValues.size()];
			String inParenthesis = "(";
			ArrayList<String> finalString = new ArrayList<String>();
			for (int i = 0; i < showOps.length; i++) {
				showOps[i] = constructorValues.get(i).toString();
				fOps[i] = constructorValuesNum.get(i).toString();

			}
			for (int x = 0; x < dataTypes.length; x++) {
				String optionDialog = "The constructor " + selectedConstructor
						+ " for the " + actorType + " at " + a
						+ " has been selected.\n";
				optionDialog += "What " + dataTypes[x]
						+ " do you want to save in parameter " + (x + 1)
						+ " out of " + dataTypes.length + "?";
				if (fOps.length > 1) {
					int choice = JOptionPane.showOptionDialog(null,
							optionDialog, "GridWorld Save",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, fOps, null);
					if (choice != -1)
						finalString.add(showOps[choice]);
				} else {
					finalString.add(showOps[0]);

				}
			}
			for (String f : finalString) {
				inParenthesis += f + ",";
			}
			inParenthesis = inParenthesis.substring(0,
					inParenthesis.length() - 1) + ");";
			return inParenthesis;
		}
		return null;
	}

	/**
	 * Returns an Object[] with all the Constructor Values at the given location
	 * 
	 * @author Ben Eisner
	 */
	private Object[] getConstructorValues(Location loc) {
		Actor a = ((Actor) getGrid().get(loc));
		// Class<?> c = a.getClass();
		// Field[] fields = c.getDeclaredFields();
		Object[] obj = new Object[2];
		ArrayList<Object> fieldList = new ArrayList<Object>();
		ArrayList<Object> visualList = new ArrayList<Object>();
		try {
			for (Field field : a.getClass().getDeclaredFields()) {
				Field currentField = a.getClass().getDeclaredField(
						field.getName());
				currentField.setAccessible(true);
				visualList.add(field.getName() + "(" + currentField.get(a)
						+ ")");
				fieldList.add(currentField.get(a));
			}
			obj[0] = fieldList;
			obj[1] = visualList;
			return obj;
		} catch (NoSuchFieldException e) {
			System.out.println("ERROR");
			return null;
		} catch (IllegalArgumentException e) {
			System.out.println("ERROR");
			return null;
		} catch (IllegalAccessException e) {
			System.out.println("ERROR");
			return null;
		}

	}

	/**
	 * Returns an Object[] with all the Constructor Types at the given location
	 * 
	 * @author Ben Eisner
	 */
	private Object[] getConstructor(String[] className, String actorType,
			Location loc) {
		try {
			Class<?> c = Class.forName(className[0]);
			Constructor<?>[] constructors = c.getConstructors();
			if (constructors.length > 1) {
				String[] conOptions = new String[constructors.length];
				for (int x = 0; x < constructors.length; x++) {
					conOptions[x] = constructors[x].toString();
				}
				int selectedConstructor = JOptionPane.showOptionDialog(null,
						"What constructor do you want to use to save the "
								+ actorType + " at " + loc + "?",
						"GridWorld Save", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, conOptions, null);
				if (selectedConstructor != -1) {
					Class<?>[] types = constructors[selectedConstructor]
							.getParameterTypes();
					Object[] objects = new Object[2];
					objects[0] = conOptions[selectedConstructor];
					objects[1] = types;
					if (types.length == 0)
						return null;
					return objects;
				}
				return null;
			}
			Object[] objects = new Object[2];
			objects[0] = constructors[0].toString();
			objects[1] = constructors[0].getParameterTypes();
			return objects;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	/**
	 * Save to a file or clipboard
	 * 
	 * @author Ben Eisner, Jake Balfour, Shon Kaganovich
	 */

	public void save() {

		String actorString = getActorString();
		// String actorString = getActorString();
		String[] options = { "Save Runner as .java File",
				"Copy Runner to Clipboard" };
		int choice = JOptionPane.showOptionDialog(null,
				"Runner successfully generated. What would You Like To Do?",
				"GridWorld Save", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, null);
		if (choice == 0)
			writeFile(actorString);
		else if (choice == 1)
			clipboardFile(actorString);

	}

}