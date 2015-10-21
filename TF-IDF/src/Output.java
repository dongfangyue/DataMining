import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Output {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Compute compute = new Compute();
		String path = "text";
		System.out.println("-------------------------------程序开始------------------------------");
		Comparetion rate = new Comparetion();
		rate.printAccuracyRate(); //打印分词准确率测试结果
		HashMap<String, Map<String, HashMap<String, Float>>> tfIdfOfAll = compute.getTfIdfOfAll(path);//输入目录下所有目录的tfIDF向量
		HashMap<String, Float> cutWordSpeed = Compute.cutWordSpeed;
		for (String dirName:tfIdfOfAll.keySet()){
			System.out.println("-----------------------------------------------------------------");
			System.out.println("目录名称:" + dirName);
			System.out.print("分词速度：");
			System.out.println(cutWordSpeed.get(dirName));
			System.out.println("去停词后的tf-idf:");
			Map<String, HashMap<String, Float>> tempTfIdf = tfIdfOfAll.get(dirName);
			for (String fileName:tempTfIdf.keySet()){//输出每个目录下的的文件的每个词的tf-idf
				System.out.println("文件：" + fileName);
				System.out.println(tempTfIdf.get(fileName));
			}
		}
		System.out.println("------------------------------程序结束-------------------------------");
	}
}
