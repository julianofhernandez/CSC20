

public class MinMaxAccount extends BankingAccount{
    private int balance;
    private int minBalance;
    private int maxBalance;

    public MinMaxAccount (Startup s) {
        super(s);
    }
    public void debit(Debit d)	{//records the given debit
        super.debit(d);
        if (super.getBalance() < this.minBalance) {
            this.minBalance = super.getBalance();
        }
        if (super.getBalance() > this.maxBalance) {
            this.maxBalance = super.getBalance();
        }
    }
    public void credit(Credit c) {	//records the given credit
        super.credit(c);
        if (super.getBalance() < this.minBalance) {
            this.minBalance = super.getBalance();
        }
        if (super.getBalance() > this.maxBalance) {
            this.maxBalance = super.getBalance();
        }
    }
    public int getMin()	{ //returns minimum balance in pennies
        return this.minBalance;
    }
    public int getMax()	{ //returns maximum balance in pennies
        return this.maxBalance;
    }
}