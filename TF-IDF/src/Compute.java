
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Compute {
	/**
	 * 存储目录下所有文件的tf向量
	 */
	private static HashMap<String, HashMap<String, Float>> allTheTf = new HashMap<String, HashMap<String, Float>>();
    private static HashMap<String, HashMap<String, Integer>> allTheNormalTF = new HashMap<String, HashMap<String, Integer>>();

	/**
	 * 根据一个输入的分词结果的数组，统计这个数组中词的tf值
	 * @param cutWordResult：分词的结果数组
	 * @return tfMap:Map格式：HashMap<word,tf>输入数组中词语的tf值
	 */
	public static HashMap<String, Float> getTf(String[] cutWordResult){
		HashMap<String, Float> tf = new HashMap<String, Float>();   //这是词频/总词数的结果
       int wordNum = cutWordResult.length;
       for (int i = 0; i < wordNum; i++) {   //遍历cutWordResult，用数组中得每个词与数组中的其他词匹配，如果找到一个相同的，wordTf+1
           int wordTf = 0;      //每遍历完一次，就把wordTf清零
           for (int j = 0; j < wordNum; j++) {
               if (cutWordResult[i] != " " && i != j) {//判断当前的词语是否和要匹配的词语相等，如果相等wordTf+1，并把当前词置换成空格
                   if (cutWordResult[i].equals(cutWordResult[j])) {
                       cutWordResult[j] = " ";
                       wordTf++;
                   }
               }
           }
           if (cutWordResult[i] != " ") {//计算除空格外的词语的词频   公式：该词出现的次数/cutWordResult数组中的总词数
               tf.put(cutWordResult[i], (new Float(++wordTf)) / wordNum);
               cutWordResult[i] = " ";
           }
       }
       return tf;
	}

	/**
	 * 本函数实现计算cutWordResult数组中词语的出现的词数
	 * @param cutWordResult
	 * @return
	 */
	public static HashMap<String,Integer> getNormalTf(String[] cutWordResult){
		
		HashMap<String, Integer> tf = new HashMap<String, Integer>();   //这是词频/总词数的结果
       int wordNum = cutWordResult.length;
       for (int i = 0; i < wordNum; i++) {   //遍历cutWordResult，用数组中得每个词与数组中的其他词匹配，如果找到一个相同的，wordTf+1
           int wordTf = 0;      //每遍历完一次，就把wordTf清零
           for (int j = 0; j < wordNum; j++) {
               if (cutWordResult[i] != " " && i != j) {//判断当前的词语是否和要匹配的词语相等，如果相等wordTf+1，并把当前词置换成空格
                   if (cutWordResult[i].equals(cutWordResult[j])) {
                       cutWordResult[j] = " ";
                       wordTf++;
                   }
               }
           }
           if (cutWordResult[i] != " ") {//计算除空格外的词语的词频   公式：该词出现的次数/cutWordResult数组中的总词数
               tf.put(cutWordResult[i], ++wordTf);
               cutWordResult[i] = " ";
           }
       }
       return tf;
	}
	
	/**
	 * 计算输入目录下所有的文件的tf
	 * @param dir：需要计算tf的目录
	 * @throws Exception 异常
	 * @return：所有文件的tf向量，格式:Map<filename,HashMap<word,tf>>
	 */
	public static Map<String, HashMap<String, Float>> getTfOfAll(String dir) throws Exception{
		
		List<String> fileList = Read.getFileList(dir);
        for (String file : fileList) {
            HashMap<String, Float> dict = new HashMap<String, Float>();
            dict = Compute.getTf(wordSplit.WordCut(file, true));
            allTheTf.put(file, dict);
        }
        return allTheTf;
	}
	
	/**
	 * 获取目录dir下所有文件的词频向量
	 * @param dir：文档存放的目录
	 * @return：所有文件的词频向量，格式:Map<filename,HashMap<word,词频>>
	 * @throws Exception
	 */
	public static Map<String, HashMap<String, Integer>> getNormalTFOfAll(String dir) throws Exception {
		
        List<String> fileList = Read.getDirList(dir);
        for (int i = 0; i < fileList.size(); i++) {  
            HashMap<String, Integer> dict = new HashMap<String, Integer>();
            dict = Compute.getNormalTf(wordSplit.WordCut(fileList.get(i), true));
            allTheNormalTF.put(fileList.get(i), dict);
        }
        return allTheNormalTF;
    }
	/**
	 * 计算目录下的词语的出现的文档个数
	 * 公式IDF＝log((1+|D|)/|Dt|)，其中|D|表示文档总数，|Dt|表示包含关键词t的文档数量。
	 * @param dir：输入目录
	 * @return 目录下词语的idf,格式：Map<word,idf>
	 */
	public static Map<String, Float> getIdf(String dir){
		
		Map<String, Float> idf = new HashMap<String, Float>();
        List<String> located = new ArrayList<String>();

        float Dt = 1;
        float D = allTheNormalTF.size();//文档总数
        
        List<String> key = Read.getFileList(dir);//存储各个文档名的List
        Map<String, HashMap<String, Integer>> tfInIdf = allTheNormalTF;//存储各个文档tf的Map

        /**
         * 按文档遍历，依次统计文档中每个词在其他文档中出现的次数
         */
        for (int i = 0; i < D; i++) {
            HashMap<String, Integer> temp = tfInIdf.get(key.get(i));
            for (String word : temp.keySet()) {
                Dt = 1;
                if (!(located.contains(word))) {
                    for (int k = 0; k < D; k++) {
                        if (k != i) {
                            HashMap<String, Integer> temp2 = tfInIdf.get(key.get(k));
                            if (temp2.keySet().contains(word)) {
                                located.add(word);
                                Dt = Dt + 1;
                                continue;
                            }
                        }
                    }
                    idf.put(word, Compute.log((1 + D) / Dt, 10));
                }
            }
        }
        return idf;
	}
	
	/**
	 * 计算单个目录的tf-idf
	 * @param dir:目录路径
	 * @return：该目录下词语的tf-idf，格式：Map<filename,HashMap<word,tfidf>>
	 * @throws Exception
	 */
	public static Map<String, HashMap<String, Float>> getTfIdf(String dir) throws Exception {

        Map<String, Float> idf = Compute.getIdf(dir);
        Map<String, HashMap<String, Float>> tf = Compute.getTfOfAll(dir);

        for (String file : tf.keySet()) {
            Map<String, Float> singelFile = tf.get(file);
            for (String word : singelFile.keySet()) {
                singelFile.put(word, (idf.get(word)) * singelFile.get(word));
            }
        }
        return tf;
    }
	
	/**
	 * 计算总目录下所有每个目录中词语的tf-idf向量
	 * @param dir：工程data总目录
	 * @return ：总目录下每个目录词语的tf-idf，向量;格式：HashMap<dirname,Map<filename,HashMap<word,tfidf>>>
	 * @throws Exception 
	 */
	public HashMap<String, Map<String, HashMap<String, Float>>> getTfIdfOfAll(String dirPath) throws Exception{
		//File dir = new File(dirPath);
		List<String> dirList = Read.getDirList(dirPath);
		HashMap<String, Map<String, HashMap<String, Float>>> tfIdfOfAll = new HashMap<String, Map<String, HashMap<String, Float>>>();
		for (String path : dirList){
			Map<String, HashMap<String, Float>> tempMap = getTfIdf(path);
			tfIdfOfAll.put(path, tempMap);
		}
		return tfIdfOfAll;
	}
	
	/**
	 * idf的计算公式
	 * 公式IDF＝log((1+|D|)/|Dt|)，其中|D|表示文档总数，|Dt|表示包含关键词t的文档数量。
	 * @param value：
	 * @param base：
	 * @return ：公式计算的结果
	 */
	public static float log(float value, float base) {  
        return (float) (Math.log(value) / Math.log(base)); 
	}
}
