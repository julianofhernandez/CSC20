import java.util.*;

public class ShoppingCartManager {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.print("Enter Customer's Name: ");
		String name = input.nextLine();
		System.out.print("Enter Today's Date: ");
		String date = input.nextLine();

		ShoppingCart cart = new ShoppingCart(name, date);

		System.out.println("Customer Name: " + name);
		System.out.println("Today's Date: " + date);
		printMenu(cart);
	}

	public static void printMenu(ShoppingCart cart) {
		Scanner input = new Scanner(System.in);
		boolean quit = false;
		while (!quit) {
			System.out.println("MENU");
			System.out.println("a - Add item to cart");
			System.out.println("d - Remove item from cart ");
			System.out.println("c - Change item quantity");
			System.out.println("i - Output items' descriptions ");
			System.out.println("o - Output shopping cart");
			System.out.println("p - Apply coupon code");
			System.out.println("q - Quit\n\nChoose an option: ");
			String option = input.nextLine();
			switch(option) {
				case "a":
					cart = addItemToCart(cart);
					break;
				case "d":
					cart = removeItem(cart);
					break;
				case "c":
					cart = changeItemQuantity(cart);
					break;
				case "i":
					outputItemsDescriptions(cart);
					break;
				case "o":
					outputShoppingCart(cart);
					break;
				case "p":
					cart = applyCouponCode(cart);
					break;
				case "q":
					quit = true;
					return;
				default:
					System.out.println("Invalid option, try again.");
			}
		}
	}

	public static ShoppingCart addItemToCart(ShoppingCart cart) {
		println("ADD ITEM TO CART");
		println("Enter the item name: ");
		String name = input();
		println("Enter the item description: ");
		String description = input();
		println("Enter the item price: ");
		int price = inputInt();
		println("Enter the item quantity: ");
		int quantity = inputInt();
		ItemToPurchase newItem = new ItemToPurchase(name, description, price, quantity);
		cart.addItem(newItem);
		println();
		return cart;
	}

	public static ShoppingCart removeItem(ShoppingCart cart) {
		println("REMOVE ITEM FROM CART");
		println("Enter the name of the item to remove: ");
		cart.removeItem(input());
		println();
		return cart;
	}

	public static void outputItemsDescriptions(ShoppingCart cart) {
		println("OUTPUT ITEMS' DESCRIPTIONS");
		cart.printItemDescriptions();	
		println();
	}

	public static void outputShoppingCart(ShoppingCart cart) {
		println("OUTPUT SHOPPING CART");
		cart.printTotal();
		println();
	}

	public static ShoppingCart changeItemQuantity(ShoppingCart cart) {
		println("CHANGE ITEM QUANTITY");
		println("Enter the item name: ");
		ItemToPurchase item = new ItemToPurchase();
		item.setName(input());
		println("Enter the new quantity: ");
		int quantity = inputInt();
		item.setQuantity(quantity);
		cart.modifyItem(item);
		println();
		return cart;
	}

	public static ShoppingCart applyCouponCode(ShoppingCart cart) {
		println("CHANGE ITEM QUANTITY");
		println("Enter Coupon Code: ");
		String code = input();
		cart.applyCouponCode(code);
		println();
		return cart;
	}

	public static void println() {
		System.out.println("");
	}

	public static void println(String input) {
		System.out.println(input);
	}

	public static String input() {
		Scanner input = new Scanner(System.in);
		return input.nextLine();
	}

	public static int inputInt() {
		while (true) {
			try {
				String rawInput = input();
				int intInput = Integer.parseInt(rawInput);
				return intInput;
			} catch (Exception e) {
				println("That's not an integer, try again.");
				continue;
			}
		}
	}
}
