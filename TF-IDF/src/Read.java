import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Read {
	
	private static List<String> dirList = new ArrayList<String>();   //保存总目录下的所有目录的路径
	private static List<String> fileList = new ArrayList<String>();  //保存目录下的文件的路径
	
	/**
	 * 根据输入的目录，获取该目录下所有目录的绝对路径
	 * @param dirPath:需要统计tf-idf的总目录
	 * @return dirList:输入目录下所有分目录的路径
	 */
	public static List<String> getDirList(String dirPath){

		
		File dir = new File(dirPath);
		if (!dir.isDirectory()) {
		    System.out.println("输入的参数应该为[文件夹名]");
		    System.out.println("dirPath: " + dir.getAbsolutePath());
		} else if (dir.isDirectory()) {
			String[] tempDirList = dir.list();        // 获取输入目录下的目录名称
		    for (int i = 0; i < tempDirList.length; i++) {
		        File readDir = new File(dirPath + "/" + tempDirList[i]);
		        if (readDir.isDirectory()) {    //如果是目录，则获取绝对路径,加入dirList中
		            dirList.add(readDir.getAbsolutePath());    
		        }
		    }
		}
        return dirList;
	}
	
	/**
	 * 根据输入的目录，获取输入目录下所有文件的绝对路径
	 * @param dirPath：目录的路径
	 * @return fileList:输入路径下所有文件的绝对路径
	 */
	public static List<String> getFileList(String dirPath){
		
		File dir = new File(dirPath);
		if (!dir.isDirectory()) {
		    System.out.println("输入的参数应该为[文件夹名]");
		    System.out.println("dirPath: " + dir.getAbsolutePath());
		} else if (dir.isDirectory()) {
			String[] tempFileList = dir.list();        // 获取输入目录下的目录名称
		    for (int i = 0; i < tempFileList.length; i++) {
		        File readfile = new File(dirPath + "/" + tempFileList[i]);
		        if (readfile.isFile()) {    //如果是文件，则获取绝对路径,加入fileList中
		            fileList.add(readfile.getAbsolutePath());    
		        }
		    }
		}
        return fileList;
	}
}
