package Gump.DataMing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureSelection {
	private static String TextPath = "./TestData";
	private static Map<String, Map<String, Double>> DataMap = new HashMap<String, Map<String, Double>>();
	private static List<String> FileList = new ArrayList<String>();
	private static List<String> filePath = new ArrayList<String>();
	private static List<Map.Entry<String, Double>> file_feature = null;
	final int Dimensionality = 20;// 设置特征词个数

	public List<Map.Entry<String, Double>> feature_select() throws Exception {
		DataComputation compute = new DataComputation();
		getTfIdf();
		getFileList();
//		compute.setAllFilePath(filePath);
		DataMap = compute.getTF_IDF(filePath);

		FileWriter fileOut = new FileWriter(new File("./ResultFeature"));
		@SuppressWarnings("resource")
		BufferedWriter bf = new BufferedWriter(fileOut);

		for (String filename : DataMap.keySet()) {
			bf.write(filename + "   ");
			Map<String, Double> wordMap = DataMap.get(filename);
			file_feature = new ArrayList<Map.Entry<String, Double>>(wordMap.entrySet());
			// 对分词的tfidf对分词进行排序
			Collections.sort(file_feature, new Comparator<Map.Entry<String, Double>>() {
				public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
					return (o1.getValue()).compareTo(o2.getValue());
				}
			});
			// 排序输出
			for (int i = file_feature.size() - 1; i >= 0; i--) {     //全部输出
//			for (int i = file_feature.size() - 1; i >= file_feature.size() - Dimensionality; i--) {     //按Dimensionality输出
				String feature = file_feature.get(i).getKey();
				Double value = file_feature.get(i).getValue();
				bf.write(feature + "   " + value + "   ");
			}
			bf.newLine();
		}
		return file_feature;
	}

	private void getFileList() {
		// TODO Auto-generated method stub
		File fileList = new File(TextPath);
		for (String file : fileList.list()) {
			if (!file.equals(".DS_Store")) {
				FileList.add(file);
			}
		}
	}

	private void getTfIdf() throws Exception {
		// TODO Auto-generated method stub
		File dirList = new File(TextPath);
		for (String dirName : dirList.list()) {
			if (dirName.equals(".DS_Store")) {
				continue;
			}
			filePath.add(TextPath + "/" + dirName);
		}
		DataComputation compute = new DataComputation();
//		compute.setAllFilePath(filePath);
		DataMap = compute.getTF_IDF(filePath);
		compute.getWordCount();
	}

}
