/**
 * MenuItem.java - class to implement a menu item
 * 
 * @author Esther Rayssiguie 
 * @author Jake Marrocco
 * @author Karolina Judzentyte
 * @author Valerio Franchi
 * @version 0.1
 * 
 * Copyright (c) 2021 
 * All rights reserved.
 */

package CoffeeShopProjectThreaded;

public class MenuItem {
	
	private String name;
	private String description;
	private String category;
	private String identifier;
	private float cost;
	
	/**
	 * Constructor for MenuItem class
	 * @param identifier Item identifier
	 * @param name Name of item
	 * @param category Item category
	 * @param cost Item cost
	 * @param description Description of item 
	 */
	public MenuItem(String identifier, String name, String category, float cost, String description) {
		this.identifier = identifier;
		this.name = name;
        this.category = category;
        this.cost = cost;
        this.description = description;
	}

	/**
	 * @return Item identifier 
	 */
	public String getIdentifier() {
        return identifier;
    }
	
	/**
	 * @return Name of item
	 */
	public String getName() {
        return name;
    }
	
	/**
	 * @return Item category
	 */
	public String getCategory() {
        return category;
    }
	
	/**
	 * @return Cost of item 
	 */
	public float getCost() {
		return cost;
	}
	
	/**
	 * @return Item description
	 */
	public String getDescription() {
        return description;
    }
}
