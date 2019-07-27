package km;

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
import java.util.Collections;
import java.util.List;

public class CopyOfOptimalAccounting4 {

	int totalAmount;//
	List<Integer> purchaserLimits;
	List<Integer> sellerLimits;

	public CopyOfOptimalAccounting4() {
	}

	public CopyOfOptimalAccounting4(int totalAmount, List<Integer> purchaserLimits,
			List<Integer> sellerLimits) {
		super();
		this.totalAmount = totalAmount;
		this.purchaserLimits = purchaserLimits;
		this.sellerLimits = sellerLimits;
	}

	public static List<CopyOfOptimalAccounting4> readFile(File file) {
		List<CopyOfOptimalAccounting4> inputList = new ArrayList<CopyOfOptimalAccounting4>();
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
				CopyOfOptimalAccounting4 optimalAccounting = new CopyOfOptimalAccounting4(
						0, new ArrayList<Integer>(), new ArrayList<Integer>());
				optimalAccounting.setTotalAmount(Integer.parseInt(inputs[0]));
				String[] purchaserLimitStr = inputs[1].split(",");
				String[] sellerLimitStr = inputs[2].split(",");
				for (String purchaserLimit : purchaserLimitStr) {
					optimalAccounting.getpurchaserLimits().add(
							Integer.parseInt(purchaserLimit));
				}
				for (String sellerLimit : sellerLimitStr) {
					optimalAccounting.getsellerLimits().add(
							Integer.parseInt(sellerLimit));
				}
				inputList.add(optimalAccounting);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputList;
	}

	public static List<String> calculate(List<CopyOfOptimalAccounting4> inputs) {
		List<String> allCalculateResults = new ArrayList<String>();
		for (CopyOfOptimalAccounting4 input : inputs) {
			int dealCount = 0;
			StringBuilder output = new StringBuilder("");
			List<Integer> purchaserLimits = input.getpurchaserLimits();
			List<Integer> sellerLimits = input.getsellerLimits();
			while (true) {
				int purchaserMaxLimit = Collections.max(purchaserLimits);
				int sellerMaxLimit = Collections.max(sellerLimits);
				if (purchaserMaxLimit == 0 || sellerMaxLimit == 0) {
					break;
				}
				int[][] graph = new int[Math.max(purchaserLimits.size(), sellerLimits.size())][Math.max(purchaserLimits.size(), sellerLimits.size())];
				for(int i=0;i<graph.length;i++){
					for (int j = 0; j < graph[0].length; j++) {
						graph[i][j] = 0;
					}
				}
				for (int i = 0; i < purchaserLimits.size(); i++) {
					for (int j = 0; j < sellerLimits.size(); j++) {
						graph[i][j] = Math.min(purchaserLimits.get(i),
								sellerLimits.get(j));
					}
				}
				int[] match = new km(graph).km();
				for (int i = 0; i < match.length; i++) {
					if (match[i]<purchaserLimits.size()&&i<sellerLimits.size()&&purchaserLimits.get(match[i]) != 0
							&& sellerLimits.get(i) != 0) {
						if (purchaserLimits.get(match[i]) > sellerLimits.get(i)) {
							dealCount++;
							output.append("|");
							output.append(match[i] + 1);
							output.append(",");
							output.append(i + 1);
							output.append(",");
							output.append(sellerLimits.get(i));
							purchaserLimits.set(match[i],
									purchaserLimits.get(match[i])
											- sellerLimits.get(i));
							sellerLimits.set(i, 0);
						} else if (purchaserLimits.get(match[i]) == sellerLimits
								.get(i)) {
							dealCount++;
							output.append("|");
							output.append(match[i] + 1);
							output.append(",");
							output.append(i + 1);
							output.append(",");
							output.append(sellerLimits.get(i));
							purchaserLimits.set(match[i], 0);
							sellerLimits.set(i, 0);
						} else {
							dealCount++;
							output.append("|");
							output.append(match[i] + 1);
							output.append(",");
							output.append(i + 1);
							output.append(",");
							output.append(purchaserLimits.get(match[i]));
							sellerLimits.set(i, sellerLimits.get(i)
									- purchaserLimits.get(match[i]));
							purchaserLimits.set(match[i], 0);
						}
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

	public static void writeResult(List<String> results, String fileName) {
		String filePath = "D:\\optimalAccounting\\output\\" + fileName
				+ ".result.txt";
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					new FileOutputStream(filePath));
			BufferedWriter bufferedWriter = new BufferedWriter(
					outputStreamWriter);
			for (String result : results) {
				bufferedWriter.write(result);
				bufferedWriter.write("\n");
			}
			bufferedWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<Integer> getpurchaserLimits() {
		return purchaserLimits;
	}

	public void setpurchaserLimits(List<Integer> purchaserLimits) {
		this.purchaserLimits = purchaserLimits;
	}

	public List<Integer> getsellerLimits() {
		return sellerLimits;
	}

	public void setsellerLimits(List<Integer> sellerLimits) {
		this.sellerLimits = sellerLimits;
	}

	public static void main(String[] args) {
		Long startTime = System.currentTimeMillis();
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
			List<CopyOfOptimalAccounting4> inputs = readFile(file1);
			List<String> calculateResults = calculate(inputs);
			writeResult(calculateResults, fileName);
		}
		System.out.println((System.currentTimeMillis() - startTime));

	}

}
