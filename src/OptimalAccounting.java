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
				//剪枝2 利用k-sum
				 //将当前买卖双方不为0的账户 额度放到数组里 作为k-sum的输入
				List<Integer> buyerLimitsNotZero = new ArrayList<>();
				for (int i = 0; i < buyerAccounts.size(); i++) {
					 if(buyerAccounts.get(i).getLimit()!=0){
						 buyerLimitsNotZero.add(buyerAccounts.get(i).getLimit());
					 }
				}
				int[] numsBuyerLimits = new int[buyerLimitsNotZero.size()];
				for(int i=0;i<buyerLimitsNotZero.size();i++){
					numsBuyerLimits[i] = buyerLimitsNotZero.get(i);
				}
				List<Integer> sellerLimitsNotZero = new ArrayList<>();
				for (int i = 0; i < sellerAccounts.size(); i++) {
					 if(sellerAccounts.get(i).getLimit()!=0){
						 sellerLimitsNotZero.add(sellerAccounts.get(i).getLimit());
					 }
				}
				int[] numsSellerLimits = new int[sellerLimitsNotZero.size()];
				for(int i=0;i<sellerLimitsNotZero.size();i++){
					numsSellerLimits[i] = sellerLimitsNotZero.get(i);
				}
				//数据组装完毕
				//循环 以买方账户每个非0值为target 卖方账户为 nums 针对每个target找到最小的k
				int buyer_kmin = sellerLimitsNotZero.size();//买方kmin
				List<Integer> dealableSellerAccounts = new ArrayList<Integer>();//卖方账户额度列表
				for (int i = 0; i < buyerAccounts.size(); i++) {
				  TradeAccount BuyerAccount=null;
				  if(buyerAccounts.get(i).getLimit()==0) {
					  continue;
					  }
				  else{
					  List<List<Integer>> resultList;
					  //找最小的k
					  int k_min=sellerLimitsNotZero.size();
					  List<Integer> ksum_result=null;
					  boolean flag = false;
					  TradeAccount dealableBuyerAccount;//买方可成交的额度
					  for(int k=sellerLimitsNotZero.size();k>=2;k--){
						  resultList = new K_sum().sum(numsSellerLimits,buyerAccounts.get(i).getLimit(),k); 
						  if(resultList!=null && resultList.size()>0) {
							  k_min=k;
							  //如何根据结果集中的卖方额度定位到卖方账户编号??
							  ksum_result = resultList.get(0);
							  BuyerAccount = buyerAccounts.get(i);
							  flag = true;
						  }
					  }
					  if(flag ==true){
						  if(k_min<buyer_kmin){
							  buyer_kmin = k_min;
							  dealableSellerAccounts = ksum_result;
							  dealableBuyerAccount = BuyerAccount;
							  System.out.println("买方额度:"+dealableBuyerAccount.getLimit()+",k:"+buyer_kmin);
							  System.out.println(dealableSellerAccounts);
						  }
					  }
				  }	
				}
				//买方进行成交？
				//
				//TODO 卖方逻辑
				//
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
		String filePath = "D:\\optimalAccounting\\testcase2";
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
			System.out.println("耗时："+(System.currentTimeMillis() - startTime));
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
	
	public static boolean isContains(List<TradeAccount> tradeAccounts, int tradeAccountLimit) {
		  for (int i = 0; i < tradeAccounts.size(); i++) {
		   if (tradeAccounts.get(i).getLimit() == tradeAccountLimit) {
		    return true;
		   }
		  }
		  return false;
		 }
	
	//根据额度获取账户编号
	public int getIndexByLimit(){
		return 0;
	}
	
}
