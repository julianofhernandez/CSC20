public class ItemToPurchase {
	private String itemName;
	private int itemPrice;
	private int itemQuantity;
	private Category category; 

	public ItemToPurchase() {
		this.itemName = "none";
		this.itemPrice = 0;
		this.itemQuantity = 0;
		this.category = Category.N;
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

	public String toString() {
		return this.itemName + " " + this.itemQuantity + " @ $" + this.itemPrice + " = $" + this.itemPrice*this.itemQuantity;
	}
}
