import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	// 读取输入
	public static List<AccountingInput> readFile(File file) {
		List<AccountingInput> inputList = new ArrayList<AccountingInput>();
		List<String> inputData = new ArrayList<>();
		// File file = new File(filePath);
		InputStreamReader inputStreamReader;
		try {
			inputStreamReader = new InputStreamReader(new FileInputStream(file));
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				inputData.add(line);
			}
			bufferedReader.close();
			for (String data : inputData) {
				String[] inputs = data.split("\\|");
				AccountingInput accountingInput = new AccountingInput(0,
						new ArrayList<TradeAccount>(),
						new ArrayList<TradeAccount>());
				accountingInput.setTotalAmount(Integer.parseInt(inputs[0]));
				String[] purchaserLimitStr = inputs[1].split(",");
				String[] sellerLimitStr = inputs[2].split(",");
				for (int i = 0; i < purchaserLimitStr.length; i++) {
					TradeAccount tradeAccount = new TradeAccount(i + 1,
							Integer.parseInt(purchaserLimitStr[i]));
					accountingInput.getBuyerAccount().add(tradeAccount);
				}
				for (int i = 0; i < sellerLimitStr.length; i++) {
					TradeAccount tradeAccount = new TradeAccount(i + 1,
							Integer.parseInt(sellerLimitStr[i]));
					accountingInput.getSellerAccount().add(tradeAccount);
				}
				inputList.add(accountingInput);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputList;
	}

	// 将计算结果写入文件
	public static void writeResult(List<String> results, String fileName) {
		String filePath = "D:\\optimalAccounting\\output\\" + fileName
				+ ".result3.txt";
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					new FileOutputStream(filePath));
			BufferedWriter bufferedWriter = new BufferedWriter(
					outputStreamWriter);
			for (String result : results) {
				bufferedWriter.write(result);
				bufferedWriter.write("\r\n");
			}
			bufferedWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
