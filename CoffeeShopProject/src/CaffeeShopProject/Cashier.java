package CaffeeShopProject;

import java.time.LocalDateTime;

public class Cashier {
	
	public float subtotal = (float) 1.0;
	public float tax = (float) 1.0;
	public float discount= (float) 1.0;
	public float total= (float) 1.0;
	public Customer currentCustomer;
	
	public Cashier() {
		
	}
	
	public void setCustomer(Customer customer) {
		this.currentCustomer = customer;
	}
	
	// Those methods need to be completed!!
	public float getCartSubtotalPrice() {
		return subtotal;
	}
	
	public float getCartTax() {
		return tax;
	}
	
	public float getDiscount() {
		return discount;
	}
	
	public float getCartTotalPrice() {
		return total;
	}

}
