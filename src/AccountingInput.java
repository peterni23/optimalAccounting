import java.util.List;


public class AccountingInput {
	
	int totalAmount;//
	List<TradeAccount> buyerAccount;//买方账户列表
	List<TradeAccount> sellerAccount;//卖方账户列表

	// 构造函数
	public AccountingInput() {
	}

	public AccountingInput(int totalAmount, List<TradeAccount> buyerAccount,
			List<TradeAccount> sellerAccount) {
		super();
		this.totalAmount = totalAmount;
		this.buyerAccount = buyerAccount;
		this.sellerAccount = sellerAccount;
	}
	
	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<TradeAccount> getBuyerAccount() {
		return buyerAccount;
	}

	public void setBuyerAccount(List<TradeAccount> buyerAccount) {
		this.buyerAccount = buyerAccount;
	}

	public List<TradeAccount> getSellerAccount() {
		return sellerAccount;
	}

	public void setSellerAccount(List<TradeAccount> sellerAccount) {
		this.sellerAccount = sellerAccount;
	}

	
}
