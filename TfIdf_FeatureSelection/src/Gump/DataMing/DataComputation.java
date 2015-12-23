package Gump.DataMing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataComputation {
	
	Map<String, Map<String, Double>> fileWordInfo = new HashMap<String, Map<String, Double>>(); //<文件名, <词语, 该文件中出现次数>>
	Map<String, Map<String, Double>> TF_Map = new HashMap<String, Map<String, Double>>();//<文件名, <词语，该文件中出现频率>>
	Map<String, Map<String, Double>> TfIdf_Map = new HashMap<String, Map<String, Double>>(); //<文件名, <词语, TF/IDF>>
	List<String> AllFilePath = new ArrayList<String>();//保存了全部文件名的List
	double total = 0; //统计全部分词的数目


	/**
	 * 根据输入文档对数据进行初始化为fileWordInfo
	 * @param AllFilePath
	 * @throws Exception
	 */
	public void initDataMap(List<String> AllFilePath) throws Exception{
		TextManipulation cutWord = new TextManipulation();
		for(String filePath:AllFilePath){
			List<String> resultList = cutWord.WordCut(filePath, true);
			Map<String, Double> wordInfo = new HashMap<String, Double>();//<word, totalNum>
			for(String word:resultList){  //统计每个文档中每个词语的出现频率，放到fileWordInfo中
				if(!wordInfo.containsKey(word)){
					wordInfo.put(word, 1.0);
				}	else {
					double count = 0;
					count = wordInfo.get(word);
					wordInfo.put(word, count+1);
				}
			}
			fileWordInfo.put(filePath, wordInfo);
		}
	}
	

	/**
	 * 设置当前文档的全部文件路径
	 * @param dirPath
	 */
	public void setAllFilePath(List<String> dirPath){
		this.AllFilePath = dirPath;
	}
	

	/**
	 * 计算出每个词的词频TF，并放到Map中
	 */
	public void setTFMap(){
		for(String file:fileWordInfo.keySet()){
			Map<String, Double> wordCount = fileWordInfo.get(file);
			Map<String, Double> wordTF = new HashMap<String, Double>();
			double count = 0;
			//从fileWordIndo中取出信息，统计每个词的词频TF,并放到TF_Map中
			for(String word:wordCount.keySet()){
				count += wordCount.get(word);
			}
			for(String word:wordCount.keySet()){
				wordTF.put(word, wordCount.get(word)/count);
			}
			TF_Map.put(file, wordTF); 
			total += count;  //统计总分词数
		}
	}
	
	/**
	 * 计算IDF
	 * @param totalNum 总文档数
	 * @param existNum 出现的文档总数
	 * @return
	 */
	private double getIDF(int totalNum, double existNum){
		if(existNum == 0){
			existNum = 1;
		}
		return Math.log((1+totalNum)/existNum);
	}
	
	
	/**
	 * 计算TF_IDF 
	 * @return Map<文件名，<词语，TF_IDF>>
	 * @throws Exception
	 */
	public Map<String, Map<String, Double>> getTF_IDF(List<String> filePath) throws Exception{
		setAllFilePath(filePath);
		initDataMap(AllFilePath);
		setTFMap();
		for(String file:TF_Map.keySet()){ //统计每个词所出现的文档数，然后计算TF_IDF
			Map<String, Double> wordMap = TF_Map.get(file);
			Map<String, Double> temp = new HashMap<String, Double>();
			for(String word:wordMap.keySet()){
				Double count = 0.0;
				for(String compareFile:TF_Map.keySet()){
					if(file.equals(compareFile)){
						continue;
					} else {
						Map<String, Double> compareWord = TF_Map.get(compareFile);
						if(compareWord.containsKey(word)){
							count += 1;
						}
					}
				}
				temp.put(word, wordMap.get(word)*getIDF(AllFilePath.size(), count));
			}
			TfIdf_Map.put(file, temp);
		}
		return TfIdf_Map;
	}
	
	
	/**
	 * 获取分词结果的词语数目
	 * @return 词语数目total
	 */
	public Double getWordCount(){
		return this.total;
	}
}