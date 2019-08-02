package withoutpruning;
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


public class OptimalAccounting {

	int totalAmount;//
	List<Integer> purchaserLimits;
	List<Integer> sellerLimits;

	public OptimalAccounting(){}
	
	public OptimalAccounting(int totalAmount, List<Integer> purchaserLimits,
			List<Integer> sellerLimits) {
		super();
		this.totalAmount = totalAmount;
		this.purchaserLimits = purchaserLimits;
		this.sellerLimits = sellerLimits;
	}

	public static List<OptimalAccounting> readFile(File file) {
		List<OptimalAccounting> inputList = new ArrayList<OptimalAccounting>();
		List<String> inputData = new ArrayList<>();
//		File file = new File(filePath);
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
				OptimalAccounting optimalAccounting = new OptimalAccounting(0,new ArrayList<Integer>(),
						new ArrayList<Integer>());
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
	
	public static List<String> calculate(List<OptimalAccounting> inputs){
		List<String> allCalculateResults = new ArrayList<String>();
		for(OptimalAccounting input:inputs){
			int dealCount = 0;
			StringBuilder output = new StringBuilder("");
			List<Integer> purchaserLimits = input.getpurchaserLimits();
			List<Integer> sellerLimits = input.getsellerLimits();
			while(purchaserLimits.size()>0 && sellerLimits.size()>0){
			   int purchaserMaxLimit = Collections.max(purchaserLimits);
			   int sellerMaxLimit = Collections.max(sellerLimits);
			   if(purchaserMaxLimit==0 || sellerMaxLimit==0){
				   break;
			   }else{
				   int purchaserMaxIndex = purchaserLimits.indexOf(purchaserMaxLimit);
				   int sellerMaxIndex = sellerLimits.indexOf(sellerMaxLimit);
                   if(purchaserMaxLimit>sellerMaxLimit){
                	   //purchaserMaxLimit = purchaserMaxLimit - sellerMaxLimit;
                	   //purchaserLimits.remove(purchaserMaxIndex);
                	   //purchaserLimits.add(purchaserMaxLimit - sellerMaxLimit);
                	   //sellerLimits.remove(sellerMaxIndex);
                	   purchaserLimits.set(purchaserMaxIndex, purchaserMaxLimit - sellerMaxLimit);
                	   sellerLimits.set(sellerMaxIndex, 0);
                	   dealCount++;
                	   output.append("|");
                	   output.append(purchaserMaxIndex+1);
                	   output.append(",");
                	   output.append(sellerMaxIndex+1);
                	   output.append(",");
                	   output.append(sellerMaxLimit);
                   }else if(purchaserMaxLimit==sellerMaxLimit){
                	   purchaserLimits.set(purchaserMaxIndex, 0);
                	   sellerLimits.set(sellerMaxIndex, 0);
                	   dealCount++;
                	   output.append("|");
                	   output.append(purchaserMaxIndex+1);
                	   output.append(",");
                	   output.append(sellerMaxIndex+1);
                	   output.append(",");
                	   output.append(sellerMaxLimit);
                   }else{
                	   sellerLimits.set(sellerMaxIndex, sellerMaxLimit - purchaserMaxLimit);
                	   purchaserLimits.set(purchaserMaxIndex, 0);
                	   dealCount++;
                	   output.append("|");
                	   output.append(purchaserMaxIndex+1);
                	   output.append(",");
                	   output.append(sellerMaxIndex+1);
                	   output.append(",");
                	   output.append(purchaserMaxLimit);
                   }
			   }			    	
			}
			String result = "";
            result+=dealCount;
            result+=output;
            //System.out.println(result);
            allCalculateResults.add(result);
		}
		return allCalculateResults;
	}
	
	public static void writeResult(List<String> results,String fileName){
		String filePath = "D:\\optimalAccounting\\output\\"+fileName+".result.txt";
		try {			
			OutputStreamWriter outputStreamWriter= new OutputStreamWriter(new FileOutputStream(filePath));
			BufferedWriter bufferedWriter = new BufferedWriter (
					outputStreamWriter);
			for (String result:results) {
				bufferedWriter.write(result);
				bufferedWriter.write("\r\n");
			}
			bufferedWriter.close();
		}catch (FileNotFoundException e) {
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
	
	public static void main(String[] args){
		Long startTime = System.currentTimeMillis();
		String filePath = "D:\\optimalAccounting\\testcase2";
        File file = new File(filePath);
        File[] files = file.listFiles();
        List<File> fileList = new ArrayList<File>();//新建一个文件集合
        for (int i = 0; i < files.length; i++) {
           if (files[i].isFile()) {//判断是否为文件
        	   fileList.add(files[i]);
           }
        }
//		String inPutfilePath = "D:\\optimalAccounting\\testcase\\10_20.txt";
		//String outPutfilePath = "D:\\optimalAccounting\\output\\10_20_output.txt";
		for(File file1 :fileList){
			String fileName = file1.getName();
			String[] fileNameSplit = fileName.split("\\.");
			fileName = fileNameSplit[0];
			List<OptimalAccounting> inputs= readFile(file1);
			List<String> calculateResults = calculate(inputs);
			writeResult(calculateResults,fileName);
		}
		System.out.println((System.currentTimeMillis()-startTime));

	}
	
}
