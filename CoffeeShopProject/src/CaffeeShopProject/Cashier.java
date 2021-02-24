package CaffeeShopProject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Cashier {
	
	public float subtotal;
	public float tax;
	public float discount;
	public float total;
	HashMap<String, ArrayList<LocalDateTime>> cart;
	Customer customer;
	
	// Those methods need to be completed!!
	public float getCartSubtotalPrice() {
		subtotal = customer.getCartTotalPrice();
		return subtotal;
	}
	
	public float getCartTax() {
		// get  cart from Customer class
		cart = customer.getCart();
		// loop through items in cart 
		// if item is taxable, add 20% of item price to tax
		if (cart.containsKey("DRINK001")) {
			tax = getCartSubtotalPrice()/5;
		} 
		tax = getCartSubtotalPrice()/5;
		return tax;
	}
	
	public float getDiscount() {
		// 3 drinks & 1 food = 20% off //
		if (cart.containsKey("DRINK001") && cart.containsKey("FOOD001")) {
			discount = getCartSubtotalPrice()/5;
		}
		// 2 drinks = 25% off //
		if (cart.containsKey("DRINK001") && cart.containsKey("PASTRY001")) {
			discount = getCartSubtotalPrice()/4;
		}
		// 1 drink & 1 food & 1 pastry = £6 //
		if (cart.containsKey("DRINK001") && cart.containsKey("FOOD001") && cart.containsKey("PASTRY001")) {
			discount = getCartSubtotalPrice() - 6;
		}
		return discount;
	}
	
	public float getCartTotalPrice() {
		total = subtotal + tax - discount;
		return total;
	}

}
