package CaffeeShopProject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*; 

public class Cashier {
	
	public float subtotal;
	public float tax;
	public float discount;
	public float total;
	public int taxable;
	HashMap<String, ArrayList<LocalDateTime>> cart;
	Customer customer;
	

	public float getCartSubtotalPrice() {
		subtotal = customer.getCartTotalPrice();
		return subtotal;
	}
	
	public float getCartTax() {
		// get  cart from Customer class
		cart = customer.getCart();
		// loop through items in cart
		// if item is taxable, add 20% of item price to tax 
		taxable = 1;
		float itemTax = 0;
		tax = 0;
		while (taxable == 1) {
			for (Map.Entry mapElement : cart.entrySet()) { 
				if (CoffeeShop.menu.get(mapElement).getCategory() == "drink") {
					tax = tax + CoffeeShop.menu.get(mapElement).getCost();
				}
			}
		}
		return tax;
	}
	
	public float getDiscount() {
		// discount 1 = 3 drinks & 1 food = 20% off //
		// discount 2 = 2 drinks = 25% off //
		// discount 3 = 1 drink & 1 food & 1 pastry = £6 //
		float drinkdiscount = 0;
		float fooddiscount = 0;
		float pastrydiscount = 0;
		int drinks = 0;
		int foods = 0;
		int pastries = 0;
		for (Map.Entry mapElement : cart.entrySet()) { 
			if (CoffeeShop.menu.get(mapElement).getCategory() == "drink") {
				drinkdiscount = drinkdiscount + CoffeeShop.menu.get(mapElement).getCost();
				drinks = drinks + 1;
				
			} else if (CoffeeShop.menu.get(mapElement).getCategory() == "food") {
				fooddiscount = fooddiscount + CoffeeShop.menu.get(mapElement).getCost();
				foods = foods + 1;
				
			} else if (CoffeeShop.menu.get(mapElement).getCategory() == "pastry") {
				pastrydiscount = pastrydiscount + CoffeeShop.menu.get(mapElement).getCost();
				pastries = pastries + 1;
				
			}
			if (drinks >= 2) {
				discount = discount + drinkdiscount/4;
				drinks = drinks - 2;
				drinkdiscount = 0;
			}
			if (drinks >= 3 && foods >= 1) {
				discount = discount + drinkdiscount/5 + fooddiscount/5;
				drinks = drinks - 3;
				foods = foods - 1;
				drinkdiscount = 0;
				fooddiscount = 0;
			}
			if (drinks >= 1 && foods >= 1 && pastries >= 1) {
				discount = discount + (drinkdiscount + fooddiscount + pastrydiscount - 6);
				drinks = drinks - 1;
				foods = foods - 1;
				pastries = pastries - 1;
				drinkdiscount = 0;
				fooddiscount = 0;
				pastrydiscount = 0;
			}
        } 
		return discount;
	}
	
	public float getCartTotalPrice() {
		total = subtotal + tax - discount;
		return total;
	}

}
