import java.util.*;

public class ShoppingCart {
	private String customerName;
	private String currentDate;
	private ArrayList<ItemToPurchase> cartItems;
	private CouponCode couponCode;

	public ShoppingCart() {
		this.customerName = "none";
		this.currentDate = "January 1, 2020";
		this.cartItems = new ArrayList<ItemToPurchase>();
	}

	public ShoppingCart(String name, String date) {
		this.customerName = name;
		this.currentDate = date;
		this.cartItems = new ArrayList<ItemToPurchase>();
	}

	public void addItem(ItemToPurchase newItem) {
		this.cartItems.add(newItem);
	}

	public void removeItem(String itemName) {
		for(int i=0; i<this.cartItems.size(); i++) {
			if(this.cartItems.get(i).getName().equals(itemName)) {
				cartItems.remove(i);
				return;
			}
			System.out.println("Item not found in cart. Nothing removed.");
		}
	}

	public void modifyItem(ItemToPurchase item) {
		for(int i=0; i<this.cartItems.size(); i++) {
			if(this.cartItems.get(i).getName().equals(item.getName())) {
				if (item.getPrice() != 0) {
					cartItems.get(i).setPrice(item.getPrice());
				}
				if (!item.getDescription().equals("none")) {
					cartItems.get(i).setDescription(item.getDescription());
				}
				if (item.getQuantity() != 0) {
					cartItems.get(i).setQuantity(item.getQuantity());
				}
				return;
			}
			System.out.println("Item not found in cart. Nothing modified.");
		}
	}

	public int getNumItemsInCart() {
		return this.cartItems.size();
	}

	public double getCostOfCart() {	
		double totalCost = 0.0;
		for(int i=0; i<this.cartItems.size(); i++) {
			totalCost += (this.cartItems.get(i).getQuantity()*this.cartItems.get(i).getPrice());
		}
		return totalCost;
	}

	public int applyCouponCode() {
		if (this.getCostOfCart() > 150.0) {
			this.couponCode = CouponCode.OFF30;
			return 30;
		} else if (this.getCostOfCart() > 100.0) {
			this.couponCode = CouponCode.OFF20;
			return 20;
		} else if (this.getCostOfCart() > 50) {
			this.couponCode = CouponCode.OFF10;
			return 10;
		} else {
			// No coupon can by applied
			this.couponCode = CouponCode.NONE;
			return 0;
		}
	}

	public void printTotal() {
		if (this.cartItems.size() == 0) {
			System.out.println("SHOPPING CART IS EMPTY");
			return;
		} else if (!(this.couponCode == CouponCode.OFF30 || this.couponCode == CouponCode.OFF20 || this.couponCode == CouponCode.OFF10)) {
			System.out.println("INVALID CODE");
		}
		System.out.println(this.customerName + "'s Shopping Cart - " + this.currentDate);
		System.out.println("Number of Items: " + this.cartItems.size() + "\n");
		for(int i=0; i<this.cartItems.size(); i++) {
			this.cartItems.get(i).printItemCost();
		}
		int discount = this.applyCouponCode();
		System.out.println("Discount (" + this.couponCode + "): -$" + discount);
		System.out.printf("Total: $%.2f", (this.getCostOfCart() - discount));
	}

	public void printItemDescriptions() {
		System.out.println(this.customerName + "'s Shopping Cart - " + this.currentDate + "\n");
		System.out.println("Item Descriptions");
		for(int i=0; i<this.cartItems.size(); i++) {
			System.out.println(this.cartItems.get(i).getName() + ": " + this.cartItems.get(i).getDescription());
		}
	}
}
