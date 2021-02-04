package CaffeeShopProject;

public class InvalidOrderQuantityException extends Exception{

	public InvalidOrderQuantityException() {
		super("Invalid order ID.");
	}
}
