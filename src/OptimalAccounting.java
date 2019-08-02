import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OptimalAccounting {
	
	static int dealCount = 0;
    static 	StringBuilder output = new StringBuilder("");


	// 核心计算方法
	public static List<String> calculate(List<AccountingInput> inputs) {
		List<String> allCalculateResults = new ArrayList<String>();
		for (AccountingInput input : inputs) {
			dealCount = 0;
			output = new StringBuilder("");
			List<TradeAccount> buyerAccounts = input.getBuyerAccount();
			List<TradeAccount> sellerAccounts = input.getSellerAccount();
			while (buyerAccounts.size() > 0 && sellerAccounts.size() > 0) {
				//剪枝1--寻找买卖双方额度相同的账户 ,直接成交
				AccountingInput accountingInput = pruning(buyerAccounts,sellerAccounts);
				buyerAccounts = accountingInput.getBuyerAccount();
				sellerAccounts = accountingInput.getSellerAccount();
				for (int i = 0; i < buyerAccounts.size(); i++) {
				     for (int j = 0; j < sellerAccounts.size(); j++) {
					//剪枝2--寻找卖方账户额度之和等于买方某个账户额度，直接成交
				    if (buyerAccounts.get(i).getLimit() != 0 && sellerAccounts.get(j).getLimit() != 0
				        && (buyerAccounts.get(i).getLimit() > sellerAccounts.get(j).getLimit())) {
				       if (isContains(sellerAccounts,
				         buyerAccounts.get(i).getLimit() - sellerAccounts.get(j).getLimit(),sellerAccounts.get(j).getIndex())) {
				        dealCount++;
				        stringAppend(output,buyerAccounts.get(i).getIndex(),sellerAccounts.get(j).getIndex(),
				        		sellerAccounts.get(j).getLimit());
				        buyerAccounts.get(i).setLimit(buyerAccounts.get(i).getLimit() - sellerAccounts.get(j).getLimit());
				        sellerAccounts.get(j).setLimit(0);
				        //剪枝1
				        accountingInput = pruning(buyerAccounts,sellerAccounts);
						buyerAccounts = accountingInput.getBuyerAccount();
						sellerAccounts = accountingInput.getSellerAccount();
				       } else {

				       }
				      } else if (buyerAccounts.get(i).getLimit() != 0 && sellerAccounts.get(j).getLimit() != 0
				        && (buyerAccounts.get(i).getLimit() < sellerAccounts.get(j).getLimit())) {
				    	 //剪枝2--寻找买方账户额度之和等于卖方某个账户额度，直接成交
				       if (isContains(buyerAccounts,
				         sellerAccounts.get(j).getLimit() - buyerAccounts.get(i).getLimit(),buyerAccounts.get(i).getIndex())) {
				        dealCount++;
				        stringAppend(output,buyerAccounts.get(i).getIndex(),sellerAccounts.get(j).getIndex(),
				        		buyerAccounts.get(i).getLimit());
				        sellerAccounts.get(j)
				          .setLimit(sellerAccounts.get(j).getLimit() - buyerAccounts.get(i).getLimit());
				        buyerAccounts.get(i).setLimit(0);
				        //剪枝1
				        accountingInput = pruning(buyerAccounts,sellerAccounts);
						buyerAccounts = accountingInput.getBuyerAccount();
						sellerAccounts = accountingInput.getSellerAccount();
				       } else {

				       }
				      } else {

				      }
				     }
				    }
				//贪心策略 	每次取买卖双方最大额度的账户成交，相减并更新额度
				int maxBuyerIndex = TradeAccount.maxLimit(buyerAccounts);
				int maxSellerIndex = TradeAccount.maxLimit(sellerAccounts);
				int maxBuyerLimit = buyerAccounts.get(maxBuyerIndex).getLimit();
				int maxSellerLimit = sellerAccounts.get(maxSellerIndex)
						.getLimit();
				if (maxBuyerLimit == 0 || maxSellerLimit == 0) {
					break;
				} else {
					if (maxBuyerLimit > maxSellerLimit) {
						//买方最大额度大于卖方最大额度
						buyerAccounts.get(maxBuyerIndex).setLimit(
								maxBuyerLimit - maxSellerLimit);
						sellerAccounts.get(maxSellerIndex).setLimit(0);
						dealCount++;
						stringAppend(output, buyerAccounts.get(maxBuyerIndex).getIndex(), 
								sellerAccounts.get(maxSellerIndex).getIndex(), maxSellerLimit);

					} else if (maxBuyerLimit == maxSellerLimit) {
						///买卖双方最大额度相等
						buyerAccounts.get(maxBuyerIndex).setLimit(0);
						sellerAccounts.get(maxSellerIndex).setLimit(0);
						dealCount++;
						stringAppend(output,buyerAccounts.get(maxBuyerIndex).getIndex(),
								sellerAccounts.get(maxSellerIndex).getIndex(),maxSellerLimit);
					} else {
						//卖方最大额度大于买方最大额度
						buyerAccounts.get(maxBuyerIndex).setLimit(0);
						sellerAccounts.get(maxSellerIndex).setLimit(
								maxSellerLimit - maxBuyerLimit);
						dealCount++;
						stringAppend(output,buyerAccounts.get(maxBuyerIndex).getIndex(),
								sellerAccounts.get(maxSellerIndex).getIndex(),maxBuyerLimit);
					}
				}
			}
			String result = "";
			result += dealCount;
			result += output;
			// System.out.println(result);
			allCalculateResults.add(result);

		}
		return allCalculateResults;
	}

	
	private static  StringBuilder stringAppend(StringBuilder output,int param1,int param2,int param3) {
		// TODO Auto-generated method stub
		output.append("|");
        output.append(param1);
        output.append(",");
        output.append(param2);
        output.append(",");
        output.append(param3);
        return output;
	}


	public static void main(String[] args) {
		String filePath = "D:\\optimalAccounting\\testcase";
		File file = new File(filePath);
		File[] files = file.listFiles();
		List<File> fileList = new ArrayList<File>();// 新建一个文件集合
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {// 判断是否为文件
				fileList.add(files[i]);
			}
		}
		for (File file1 : fileList) {
			String fileName = file1.getName();
			String[] fileNameSplit = fileName.split("\\.");
			fileName = fileNameSplit[0];
			List<AccountingInput> inputs = FileUtil.readFile(file1);
			Long startTime = System.currentTimeMillis();
			List<String> calculateResults = calculate(inputs);
			System.out.println((System.currentTimeMillis() - startTime));
			FileUtil.writeResult(calculateResults, fileName);
		}

	}
	
	//剪枝1--找买卖双方额度相同的账户  直接成交
	public static  AccountingInput pruning(List<TradeAccount> buyerAccounts,List<TradeAccount> sellerAccounts){
		Collections.sort(buyerAccounts, new TradeAccountComparator());
		Collections.sort(sellerAccounts, new TradeAccountComparator());
		for (int i = 0, j = 0; i < buyerAccounts.size()
				&& j < sellerAccounts.size();) {
			if ((buyerAccounts.get(i).getLimit() == sellerAccounts.get(
					j).getLimit())) {
				if (buyerAccounts.get(i).getLimit() != 0) {
					dealCount++;
					stringAppend(output, buyerAccounts.get(i).getIndex(), sellerAccounts.get(j).getIndex(),
							buyerAccounts.get(i).getLimit());
					buyerAccounts.get(i).setLimit(0);
					sellerAccounts.get(j).setLimit(0);
				}
				i++;
				j++;
			} else if (buyerAccounts.get(i).getLimit() < sellerAccounts
					.get(j).getLimit()) {
				i++;
			} else if (sellerAccounts.get(j).getLimit() < buyerAccounts
					.get(i).getLimit()) {
				j++;
			}
		}
		AccountingInput accountingInput = new AccountingInput();
		accountingInput.setBuyerAccount(buyerAccounts);
		accountingInput.setSellerAccount(sellerAccounts);
		return accountingInput;
	}
	//这个方法要改一下,有一种情况是 传进来的卖方当前额度*2等于买方的额度。或者买卖双方反过来。所以要剔除这种情况
	public static boolean isContains(List<TradeAccount> tradeAccounts, int tradeAccountLimit,int currentIndex) {
		  for (int i = 0; i < tradeAccounts.size(); i++) {
		   if (tradeAccounts.get(i).getLimit() == tradeAccountLimit && tradeAccounts.get(i).getIndex()!=currentIndex) {
		    return true;
		   }
		  }
		  return false;
		 }
	
	
}
