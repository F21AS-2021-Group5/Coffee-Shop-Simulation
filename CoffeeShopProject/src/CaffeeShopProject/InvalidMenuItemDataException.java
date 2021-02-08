package CaffeeShopProject;

public class InvalidMenuItemDataException extends Exception {
	
	public InvalidMenuItemDataException(){
        super("An item of data was invalid.\n");
    }

}
