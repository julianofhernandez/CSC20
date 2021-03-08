import java.util.*;

public class ShoppingCart {
	private String customerName;
	private String currentDate;
	private ArrayList<ItemToPurchase> cartItems;
	private CouponCode couponCode;

	// 
	public ShoppingCart() {
		this.customerName = "none";
		this.currentDate = "January 1, 2020";
		this.cartItems = new ArrayList<ItemToPurchase>();
		this.couponCode = CouponCode.NONE;
	}

	public ShoppingCart(String name, String date) {
		this.customerName = name;
		this.currentDate = date;
		this.cartItems = new ArrayList<ItemToPurchase>();
		this.couponCode = CouponCode.NONE;
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

	public void applyCouponCode(String code) {
		if (code.equals("OFF30") && this.getCostOfCart() > 150) {
			this.couponCode = CouponCode.OFF30;
			System.out.println("APPLIED COUPON");
		} else if (code.equals("OFF20") && this.getCostOfCart() > 100) {
			this.couponCode = CouponCode.OFF20;
			System.out.println("APPLIED COUPON");
		} else if (code.equals("OFF10") && this.getCostOfCart() > 50) {
			this.couponCode = CouponCode.OFF10;
			System.out.println("APPLIED COUPON");
		} else {
			System.out.println("INVALID CODE");
			this.couponCode = CouponCode.NONE;
		}
	}

	public void printTotal() {
		if (this.cartItems.size() == 0) {
			System.out.println("SHOPPING CART IS EMPTY");
			return;
		}
		System.out.println(this.customerName + "'s Shopping Cart - " + this.currentDate);
		System.out.println("Number of Items: " + this.cartItems.size() + "\n");
		for(int i=0; i<this.cartItems.size(); i++) {
			this.cartItems.get(i).printItemCost();
		}
		int discount = 0;
		if (this.couponCode == CouponCode.OFF30) {
			discount = 30;
		}
		if (this.couponCode == CouponCode.OFF20) {
			discount = 30;
		}
		if (this.couponCode == CouponCode.OFF10) {
			discount = 10;
		}
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
