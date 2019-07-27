import java.util.List;

//交易账户
public class TradeAccount {
	int index;// 账户序号
	int limit;// 账户限额

	public TradeAccount(int index, int limit) {
		super();
		this.index = index;
		this.limit = limit;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public static int maxLimit(List<TradeAccount> tradeAccounts) {
		int max = 0;
		int index = 0;
		for (int i = 0; i < tradeAccounts.size(); i++) {
			if (tradeAccounts.get(i).getLimit() > max) {
				max = tradeAccounts.get(i).getLimit();
				index = i;
			}
		}
		return index;
	}

}
