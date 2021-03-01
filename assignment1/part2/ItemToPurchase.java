public class ItemToPurchase {
	private String itemName;
	private int itemPrice;
	private int itemQuantity;
	private Category category; 
	private String itemDescription;
	
	public ItemToPurchase() {
		this.itemName = "none";
		this.itemPrice = 0;
		this.itemQuantity = 0;
		this.category = Category.N;
		this.itemDescription = "none";
	}

	public ItemToPurchase(String name, String description, int price, int quantity) {
		this.itemName = name;
		this.itemPrice = price;
		this.itemQuantity = quantity;
		this.category = Category.N;
		this.itemDescription = description;
	}

	public void setDescription(String description) {
		this.itemDescription = description;
	}

	public void setName(String name) {
		this.itemName = name;
	}

	public void setPrice(int price) {
		this.itemPrice = price;
	}

	public void setQuantity(int quantity) {
		this.itemQuantity = quantity;
	}

	public void setTaxCategory(Category category) {
		this.category = category;
	}

	public String getDescription() {
		return this.itemDescription;
	}

	public String getName() {
		return this.itemName;
	}

	public int getPrice() {
		return this.itemPrice;
	}
	public int getQuantity() {
		return this.itemQuantity;
	}
	public Category getTaxCategory() {
		return this.category;
	}
	public double getTax() {
		double rate;
		if (this.category == Category.X) {
			rate = 0.08;
		} else if (this.category == Category.N) {
			rate = 0.0;
		} else {
			rate = 0.1;
		}
		return (double)this.itemQuantity*(this.itemPrice*rate);
	}

	public void printItemCost() {
		System.out.println(this.itemName + " " + this.itemQuantity + " @ $" + this.itemPrice + " = $" + this.itemPrice*this.itemQuantity);
	}
	
	public void printItemDescription() {
		System.out.println(this.itemName + ": " + this.itemDescription);
	}
}
