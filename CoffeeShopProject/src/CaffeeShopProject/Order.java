package CaffeeShopProject;


public class Order {
	
	private String name;
	private String description;
	private String category;
	private String identifier;
	private float cost;
	
	
	public Order(String name, String description, String category, float cost, String identifier) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.cost = cost;
        this.identifier = identifier;
    }
	
	// Generic get methods 
	public String getName() {
        return name;
    }
	
	public String getDescription() {
        return description;
    }
	
	public String getCategory() {
        return category;
    }
	
	public String getIdentifier() {
        return identifier;
    }
	
	public float getCost() {
		return cost;
	}
	
	
	
	
	

}
