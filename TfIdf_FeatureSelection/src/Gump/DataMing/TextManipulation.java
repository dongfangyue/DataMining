package Gump.DataMing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于正逆双向最大匹配分词算法实现 
 * 
 */
public class TextManipulation {
	private Map<String, Integer> DictMap = new HashMap<String, Integer>();  //存储分词字典
	private List<String> StopList = new ArrayList<String>();   //存储停用词字典
	private List<String> NameList = new ArrayList<String>();   //存储姓氏字典
	private List<String> NoiseList = new ArrayList<String>();  //存储无用词字典，用于判断姓名
	
	private String DictPath = "./dictionary/dic.txt";  //分词字典路径
	private String StopWordPath = "./dictionary/stopwords.txt";   //停用词字典路径
	private String NameWordPath = "./dictionary/surname.txt";   //姓氏字典路径
	private String NoiseWordPath = "./dictionary/noise.txt";  //无用词路径
	private int MAX_LENGTH = 5;   //最大切词长度，初始化为5

	/**
	 * 构造方法中读取分词词典，并存储到map中
	 * 
	 * @throws IOException
	 */
	public TextManipulation() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(DictPath));
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] temp = line.split(" ");
			line = temp[0].trim();
			DictMap.put(line, 0);
		}
		br.close();
	}

	/**
	 * 读取停词词典，并存储到List中
	 * @throws Exception
	 */
	private void getStopWord() throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(StopWordPath));
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] temp = line.split(" ");
			line = temp[0].trim();
			StopList.add(line);
		}
		br.close();
	}

	/**
	 * 读取姓氏字典，并存储到List中
	 * @throws Exception
	 */
	private void getNameWord() throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(NameWordPath));
		String line = null;
		while ((line = br.readLine()) != null) {
			NameList.add(line);
		}
		br.close();
	}
	
	/**
	 * 读取无用词字典，并存储到List中
	 * @throws Exception
	 */
	private void getNoiseWord() throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(NoiseWordPath));
		String line = null;
		while ((line = br.readLine()) != null) {
			NoiseList.add(line);
		}
		br.close();
	}
	

	/**
	 * 读取输入文本文件
	 * @param FilePath
	 * @return String
	 * @throws Exception
	 */
	private static String readFile(String FilePath) throws Exception{
		BufferedReader br=new BufferedReader(new FileReader(FilePath)); 
		StringBuffer word = new StringBuffer();
		String txtLine = null;
		while((txtLine = br.readLine()) !=null){
			word.append(txtLine.trim().replaceAll(" ", "")+".");
		}
		br.close();
		return word.toString();
	}	


	/**
	 * 最大匹配分词算法
	 * 
	 * @param SplitStr
	 *            待切分的字符串
	 * @param leftToRight
	 *            切分方向，true为从左向右，false为从右向左
	 * @return 切分的字符串
	 */
	public List<String> Split(String SplitStr, boolean leftToRight) {
		// 如果带切分字符串为空则返回空
		if (SplitStr.isEmpty())
			return null;
		// 储存正向匹配分割字符串
		List<String> leftWords = new ArrayList<String>();
		// 储存负向匹配分割字符串
		List<String> rightWords = new ArrayList<String>();
		// 用于取切分的字串
		String word = null;
		// 取词的长度，初始化设置为最大值
		int wordLength = MAX_LENGTH;
		// 分词操作中处于字串当前位置
		int position = 0;
		// 已经处理字符串的长度
		int length = 0;
		// 去掉字符串中多余的空格
		SplitStr = SplitStr.trim().replaceAll("\\s+", "");
		// 当待切分字符串没有被切分完时循环切分
		while (length < SplitStr.length()) {
			// 如果还没切分的字符串长度小于最大值，让取词词长等于该词本身长度
			if (SplitStr.length() - length < MAX_LENGTH)
				wordLength = SplitStr.length() - length;
			// 否则取默认值
			else
				wordLength = MAX_LENGTH;
			// 如果是正向最大匹配，从SplitStr的position处开始切割
			if (leftToRight) {
				position = length;
				word = SplitStr.substring(position, position + wordLength);
			}
			// 如果是逆向最大匹配，从SplitStr末尾开始切割
			else {
				position = SplitStr.length() - length;
				word = SplitStr.substring(position - wordLength, position);
			}
			// 如果分词词典里面没有切割出来的字符串，舍去一个字符
			while (!DictMap.containsKey(word)) {
				// 如果是单字，退出循环
				if (word.length() == 1) {
					// 如果是字母或是数字要将连续的字母或者数字分在一起
					if (word.matches("[a-zA-z0-9]")) {
						// 如果是正向匹配直接循环将后续连续字符加起来
						if (leftToRight) {
							for (int i = SplitStr.indexOf(word, position) + 1; i < SplitStr
									.length(); i++) {
								if ((SplitStr.charAt(i) >= '0' && SplitStr
										.charAt(i) <= '9')
										|| (SplitStr.charAt(i) >= 'A' && SplitStr
												.charAt(i) <= 'Z')
										|| (SplitStr.charAt(i) >= 'a' && SplitStr
												.charAt(i) <= 'z')) {
									word += SplitStr.charAt(i);
								} else
									break;
							}
						} else {
							// 如果是逆向匹配，从当前位置之前的连续数字、字母字符加起来并翻转
							for (int i = SplitStr.indexOf(word, position - 1) - 1; i >= 0; i--) {
								if ((SplitStr.charAt(i) >= '0' && SplitStr
										.charAt(i) <= '9')
										|| (SplitStr.charAt(i) >= 'A' && SplitStr
												.charAt(i) <= 'Z')
										|| (SplitStr.charAt(i) >= 'a' && SplitStr
												.charAt(i) <= 'z')) {
									word += SplitStr.charAt(i);
									if (i == 0) {
										StringBuffer sb = new StringBuffer(word);
										word = sb.reverse().toString();
									}
								} else {
									// 翻转操作
									StringBuffer sb = new StringBuffer(word);
									word = sb.reverse().toString();
									break;
								}
							}
						}
					}
					break;
				}
				// 如果是正向最大匹配，舍去最后一个字符
				if (leftToRight)
					word = word.substring(0, word.length() - 1);
				// 否则舍去第一个字符
				else
					word = word.substring(1);
			}
			// 将切分出来的字符串存到指定的表中
			if (leftToRight)
				leftWords.add(word);
			else
				rightWords.add(word);
			// 已处理字符串增加
			length += word.length();
		}
		// 如果是逆向最大匹配，要把表中的字符串调整为正向
		if (!leftToRight) {
			for (int i = rightWords.size() - 1; i >= 0; i--) {
				leftWords.add(rightWords.get(i));
			}
		}
		// 返回切分结果
		return leftWords;
	}


	/**
	 * 判断两个集合是否相等
	 * 
	 * @param list1
	 *            集合1
	 * @param list2
	 *            集合2
	 * @return 如果相等则返回true，否则为false
	 */
	public boolean isEqual(List<String> list1, List<String> list2) {
		if (list1.isEmpty() && list2.isEmpty())
			return false;
		if (list1.size() != list2.size())
			return false;
		for (int i = 0; i < list1.size(); i++) {
			if (!list1.get(i).equals(list2.get(i)))
				return false;
		}
		return true;
	}

	/**
	 * 判别分词歧义函数
	 * 
	 * @param inputStr
	 *            待切分字符串
	 * @return 分词结果
	 */
	public List<String> resultWord(String inputStr) {
		// 分词结果
		List<String> result = new ArrayList<String>();
		// “左贪吃蛇”分词结果
		List<String> resultLeft = new ArrayList<String>();
		// “中贪吃蛇”（分歧部分）分词结果
		List<String> resultMiddle = new ArrayList<String>();
		// “右贪吃蛇”分词结果
		List<String> resultRight = new ArrayList<String>();
		// 正向最大匹配分词结果
		List<String> left = new ArrayList<String>();
		// 逆向最大匹配分词结果
		List<String> right = new ArrayList<String>();
		left = Split(inputStr, true);
		right = Split(inputStr, false);
		// 判断两头的分词拼接，是否已经在输入字符串的中间交汇，只要没有交汇，就不停循环
		while (left.get(0).length() + right.get(right.size() - 1).length() < inputStr
				.length()) {
			// 如果正逆向分词结果相等，那么取正向结果跳出循环
			if (isEqual(left, right)) {
				resultMiddle = left;
				break;
			}
			// 如果正反向分词结果不同，则取分词数量较少的那个，不用再循环
			if (left.size() != right.size()) {
				resultMiddle = left.size() < right.size() ? left : right;
				break;
			}
			// 如果以上条件都不符合，那么实行“贪吃蛇”算法
			// 让“左贪吃蛇”吃下正向分词结果的第一个词
			resultLeft.add(left.get(0));
			// 让“右贪吃蛇”吃下逆向分词结果的最后一个词
			resultRight.add(right.get(right.size() - 1));
			// 去掉被“贪吃蛇”吃掉的词语
			inputStr = inputStr.substring(left.get(0).length());
			inputStr = inputStr.substring(0,
					inputStr.length() - right.get(right.size() - 1).length());
			// 清理之前正逆向分词结果，防止造成干扰
			left.clear();
			right.clear();
			// 对没被吃掉的字符串重新开始分词
			left = Split(inputStr, true);
			right = Split(inputStr, false);
		}
		// 循环结束，说明要么分词没有歧义了，要么"贪吃蛇"从两头吃到中间交汇了
		// 如果是在中间交汇，交汇时的分词结果，还要进行以下判断：
		// 如果中间交汇有重叠了：
		// 正向第一个分词的长度 + 反向最后一个分词的长度 > 输入字符串总长度，就直接取正向的
		if (left.get(0).length() + right.get(right.size() - 1).length() > inputStr
				.length())
			resultMiddle = left;
		// 如果中间交汇，刚好吃完，没有重叠：
		// 正向第一个分词 + 反向最后一个分词的长度 = 输入字符串总长度，那么正反向一拼即可
		if (left.get(0).length() + right.get(right.size() - 1).length() == inputStr
				.length()) {
			resultMiddle.add(left.get(0));
			resultMiddle.add(right.get(right.size() - 1));
		}
		// 将没有歧义的分词结果添加到最终结果result中
		for (String string : resultLeft) {
			result.add(string);
		}
		for (String string : resultMiddle) {
			result.add(string);
		}
		// “右贪吃蛇”存储的分词要调整为正向
		for (int i = resultRight.size() - 1; i >= 0; i--) {
			result.add(resultRight.get(i));
		}
		return result;
	}


	/**
	 * 对初步分词的结果进行去停词
	 * @param result
	 * @return
	 */
	public List<String> cutStopWord(List<String> result){
		for(int i=result.size()-1; i >= 0; i--){
			for(String word:StopList){
				if(word.equals(result.get(i))){
					result.remove(i);
				}
			}
		}
		return result;
	}


	/**
	 * 对初步分词的结果进行去标点符号
	 * @param result
	 * @return
	 */
	private List<String> cutMark(List<String> result){
		List<String> newlist = new ArrayList<String>();
		//对初步分词结果List进行遍历
		for (int i = 0; i < result.size(); i++) {
			String temp = result.get(i);
			String flag = temp.replaceAll("[\\pP\\p{Punct}]", "");// 正则匹配标点符号
			if ((!flag.equals("")) && (!flag.equals("　"))) {
				newlist.add(flag);
			}
		}
		return newlist;
	}
	
	/**
	 * 对初步分词的结果进行姓名划分
	 * @param unNameList
	 * @return 进行了姓名划分后的List
	 */
	private List<String> getName(List<String> unNameList){
		for(int i=0; i<unNameList.size(); i++){
			for(int j=0; j<NameList.size(); j++){
				if(unNameList.get(i).equals(NameList.get(j))){ //对姓氏进行匹配
					for(int k=1; k<3; k++){
						if((i+1<unNameList.size())&&(unNameList.get(i+1).length() == 1)){
							boolean flag = false;
							for(String word:NoiseList){
								if(unNameList.get(i+1).equals(word)){
									flag = true;
								}
							}
							if(flag == true){
								break;
							}
							unNameList.set(i, unNameList.get(i)+unNameList.get(i+1)); //匹配上之后，将姓氏后面的最多两个单字组合起来变成姓名
							unNameList.remove(i+1);
						}
					}
				}
			}
		}
		return unNameList;
	}


	/**
	 * 本类主方法，对输入文件进行分词，可选是否去停词
	 * @param FilePath
	 * @param NoStop  
	 * 				true:去停词
	 *				false: 不去停词
	 * @return 最终的分词结果
	 * @throws Exception
	 */
	public List<String> WordCut(String FilePath, boolean NoStop) throws Exception{
		String wordString = readFile(FilePath);
		List<String> resultList;
		List<String> unMergeName;
		getStopWord();
		getNameWord();
		getNoiseWord();
		if(!NoStop){
			unMergeName = resultWord(wordString);
			resultList = getName(unMergeName);
		} else {
			List<String> rawList = resultWord(wordString);
			unMergeName = getName(rawList);
			resultList = cutStopWord(unMergeName);
		}
		List<String> returnList = cutMark(resultList);
		return returnList;
	}
}