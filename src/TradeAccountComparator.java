import java.util.Comparator;

public class TradeAccountComparator implements Comparator<TradeAccount> {
	// 按账户限额排序
	@Override
	public int compare(TradeAccount account1, TradeAccount account2) {
		int limit1 = account1.getLimit();
		int limit2 = account2.getLimit();
		if (limit1 < limit2) {
			return -1;
		} else if (limit1 > limit2) {
			return 1;
		} else {
			return 0;
		}
	}

}
