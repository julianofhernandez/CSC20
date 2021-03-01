import java.util.*;

public class ShoppingCartPrinter {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String itemName;
		int itemPrice;
		int itemQuantity;
		Category itemCategory;
		int counter = 1;
		ItemToPurchase item1 = new ItemToPurchase();
		ItemToPurchase item2 = new ItemToPurchase();

		while (counter <= 2) {
			try {
				System.out.println("Item " + counter);
				System.out.println("Enter the item name: ");
				itemName = input.nextLine();
				System.out.println("Enter the item price: ");
				itemPrice = Integer.parseInt(input.nextLine());
				System.out.println("Enter the item quantity: ");
				itemQuantity = Integer.parseInt(input.nextLine());
				System.out.println("Enter the tax category: ");
				String itemCategoryString = input.nextLine();
				if (itemCategoryString.equals("N")) {
					itemCategory = Category.N;
				} else if (itemCategoryString.equals("X")) {
					itemCategory = Category.X;
				} else if (itemCategoryString.equals("T")) {
					itemCategory = Category.T;
				} else {
					throw new InvalidSelectionException("No selection for category");
				}


				ItemToPurchase item;
				if (counter == 1) {
					// item1 = new ItemToPurchase();
					item1.setName(itemName);
					item1.setPrice(itemPrice);
					item1.setQuantity(itemQuantity);
					item1.setTaxCategory(itemCategory);
				} else {
					item2 = new ItemToPurchase();
					item2.setName(itemName);
					item2.setPrice(itemPrice);
					item2.setQuantity(itemQuantity);
					item2.setTaxCategory(itemCategory);
				}		
			} catch (Exception e) {
				System.out.println("There was an error in your inputs, please try again");
				continue;
			}
			counter++;
		}
		System.out.println("TOTAL COST");
		System.out.println(item1.toString());
		System.out.println(item2.toString());
		System.out.printf("Tax = $%.2f%n", (item1.getTax() + item2.getTax()));
		System.out.printf("Total: $%.2f%n", (item1.getPrice()*item1.getQuantity() + item1.getTax() + item2.getPrice()*item2.getQuantity() + item2.getPrice()));
	}
}
