public class FilteredAccount extends Account {
    private int zeros;
    private int total;

    public FilteredAccount(Client c) {
        super(c);
    }

    public boolean process(Transaction t) {
        this.total++;
        if (t.value == 0) {
            this.zeros++;
        } else {
            super.process(t);
        }
    }

    public double percentFiltered() {
        if (this.total == 0) {
            return 0;
        } else {
            return 100 * ((double)zeros / (double)total);
        }
    }
}