package CaffeeShopProject;


public class Order {
	
	private String name;
	private String description;
	private String category;
	private String identifier;
	private float cost;
	private int quantity;
	
	
	public Order(String identifier, String name, String category, float cost, String description) {
		this.identifier = identifier;
		this.name = name;
        this.category = category;
        this.cost = cost;
        this.description = description;
    }
	
	// Generic get methods 
	public String getIdentifier() {
        return identifier;
    }
	
	public String getName() {
        return name;
    }
	
	public String getCategory() {
        return category;
    }
	
	public float getCost() {
		return cost;
	}
	
	public String getDescription() {
        return description;
    }
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
	
	

}
