/** Sort phonebook by first name */
public void bubbleSort(){
	int n = this.contacts.size();
	for (int i = 0; i < n-1; i++) {
		for (int j = 0; j < n-i-1; j++) {
			if (this.contacts.get(j).getFirstName().compareTo(this.contacts.get(j+1).getFirstName()) > 0)
			{
				// swap arr[j+1] and arr[j]
				Contact temp = this.contacts.get(j);
				this.contacts.set(j, this.contacts.get(j+1));
				this.contacts.set(j+1, temp);
			}
		}
	}
}
