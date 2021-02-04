package CaffeeShopProject;

public class NoMatchingOrderIDException extends Exception{
	
	public NoMatchingOrderIDException() {
		super("No matching order ID.");
	}
}
