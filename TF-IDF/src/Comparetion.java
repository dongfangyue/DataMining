//package Gump.code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List; 

public class Comparetion{
	/**
	 * 读取输入文本文件
	 * 
	 * @param FilePath
	 * @return
	 * @throws Exception
	 */
	private String readFile(String FilePath) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(FilePath));
		StringBuffer word = new StringBuffer();
		String txtLine = null;
		while ((txtLine = br.readLine()) != null) {
			word.append(txtLine);
		}
		br.close();
		return word.toString();
	}
	
	/**
	 * 输入人工分词标准结果与自己设计算法的分词结果，进行对比
	 * @param rawResult 自己设计算法的分词结果
	 * @param stardResult 人工标准分词
	 * @return
	 */
	public float compareResult(List<String> rawResult, String[] stardResult){
		List<Integer> stardNum = new ArrayList<Integer>();
		List<Integer> rawNum = new ArrayList<Integer>();
		int num = 0;
		float count = 0;
		for(String stardWord:stardResult){
			num = num + stardWord.length();
			stardNum.add(num);
		}
		num = 0;
		for(String rawWord:rawResult){
			num = num + rawWord.length();
			rawNum.add(num);
		}
		for(int i=0; i<rawNum.size(); i++){
			if(stardNum.contains(rawNum.get(i))){
				count += 1;
			}
		}
		return (count/stardNum.size());
	}

	/**
	 * 与人工分词结果对比，得到准确度
	 * @param DictCutFile
	 * @param StardFile
	 * @return
	 * @throws Exception
	 */
	public float getResult(String InputFile, String StardFile) throws Exception{
		//change
		// List<String> DictCutResult = null;
		String[] StardResult = null;
		String text = null;
		float accuracyRate = 0;
		wordSplit wordSplit = new wordSplit();
		text = readFile(StardFile);
		String[] TempDictCutResult = wordSplit.WordCut(InputFile, false);
		List<String> DictCutResult = java.util.Arrays.asList(TempDictCutResult);
		StardResult = text.split(" ");  
		accuracyRate = compareResult(DictCutResult, StardResult);
//		System.out.println(DictCutResult);
//		for(int i=0; i<StardResult.length;i++){
//			System.out.print(StardResult[i]+", ");
//		}
//		System.out.print("\n");
		return accuracyRate; 
	}
	public void printAccuracyRate() throws Exception{
		String inputPath = "InputFile.txt";
		String stardPath = "StardFile.txt";

		float rate = 0;
		//Comparetion compare = new Comparetion();
		rate = getResult(inputPath, stardPath);
		System.out.print("分词准确率：");
		System.out.println(rate);
	}
}