package apriori;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class Apriori {

	private static final int SUPPORTVALVE = 185;//支持度阈值
	private static boolean endTag = false;//循环状态
	static List<List<String>> record = new ArrayList<List<String>>();//数据集
	
	/**
	 * 算法开始
	 */
	public static void apriori() {
		record = getRecord();
		
		//************获取候选1项集**************
		List<List<String>> candidateItemset = findFirstCandidate();
		System.out.println("1-候选集,共" + candidateItemset.size() + "项:");
		print(candidateItemset);
		
		//************获取频繁1项集***************
		List<List<String>> frequentItemset = getSupprotedItemset(candidateItemset);
		System.out.println("1-频繁集，共" + frequentItemset.size() + "项:");
		print(frequentItemset);
		
		//***************迭代过程**************
		while(endTag!=true){
			//**********连接操作****由k-1-频繁集  获取 候选k-项集**************
			List<List<String>> nextCandidateItemset = getNextCandidate(frequentItemset);		
			System.out.println(nextCandidateItemset.get(0).size() + "-候选集，共" + nextCandidateItemset.size() + "项:");
			print(nextCandidateItemset);
			
			//**************减枝操作***由候选k项集       获取     频繁k项集****************
			List<List<String>> nextFrequentItemset = getSupprotedItemset(nextCandidateItemset);			
			if(!nextFrequentItemset.isEmpty())
				System.out.println(nextFrequentItemset.get(0).size() + "-频繁集,共" + nextFrequentItemset.size() + "项:");
			print(nextFrequentItemset);
			
			//*********如果循环结束，输出最大模式**************
			if(endTag == true){
				System.out.println("最大频繁集,为" + frequentItemset.get(0).size() + "-项集，共" + frequentItemset.size() + "项:");
			print(frequentItemset);
			}
			
			//****************下一次循环初值********************
			candidateItemset = nextCandidateItemset;
			frequentItemset = nextFrequentItemset;
		}
	}
	
	/**
	 * 获取xls表格的数据集，存入List容器内
	 * @return
	 */
	public static List<List<String>> getRecord() {

		Workbook readWb = null;
		
		try {
			// 构建Workbook的输入流对象
			InputStream inputStream = new FileInputStream("lib//basketdata.xls");
			readWb = Workbook.getWorkbook(inputStream);
			
			// 获取第一张Sheet表
			Sheet readSheet = readWb.getSheet(0);
			// 获取Sheet表中所包含的总行数
			int rsRows = readSheet.getRows();
			
			for(int i = 2; i < rsRows; i++){
				record.add(getRowSet(readSheet,i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return record;
	}
	
	/**
	 * 获取sheet表第row行的数据集
	 * @param row,sheet
	 * @return
	 */
	public static List<String> getRowSet(Sheet sheet,int row) {
		
		List<String> list = new ArrayList<String>();
		
		// 获取Sheet表中所包含的总列数
		int rsColumns = sheet.getColumns();
		
		for(int j = 1; j < rsColumns; j++){
			Cell cell = sheet.getCell(j,row);
			if(cell.getContents().equals("T")){
				switch(j){
				case 1:
					list.add("饮料");break;
				case 2:
					list.add("冲饮食品");break;
				case 3:
					list.add("乳制冲饮");break;
				case 4:
					list.add("罐头食品");break;
				case 5:
					list.add("即食主食");break;
				case 6:
					list.add("中式挂面/通心粉");break;
				case 7:
					list.add("酱油");break;
				case 8:
					list.add("醋");break;
				case 9:
					list.add("调味品");break;
				case 10:
					list.add("饼干");break;
				case 11:
					list.add("中式糕点");break;
				case 12:
					list.add("膨化食品");break;
				case 13:
					list.add("休闲小食品");break;
				case 14:
					list.add("炒货食品");break;
				case 15:
					list.add("糖果巧克力");break;
				case 16:
					list.add("进口食品");break;
				case 17:
					list.add("香烟");break;
				case 18:
					list.add("啤酒");break;
				}
			}
		}
		return list;
	}
	
	/**
	 * 打印list集合
	 * @param list
	 */
	public static void print(List<List<String>> list) {
		for (int i = 0; i < list.size(); i++) {
			System.out.print("{");
			for (int j = 0; j < list.get(i).size(); j++) {
				System.out.print(list.get(i).get(j) + ",");
			}
			System.out.print("}");
			System.out.println();
		}
		System.out.println();
	}
	
	/**
	 * 获得1-候选集
	 * @return
	 */
	private static List<List<String>> findFirstCandidate() {

		List<List<String>> tableList = new ArrayList<List<String>>();
		HashSet<String> hs  = new HashSet<String>();
		for (int i = 0; i<record.size(); i++){  
			for(int j=0;j<record.get(i).size();j++){
				hs.add(record.get(i).get(j));
			}
		}	
		Iterator<String> itr = hs.iterator();
		while(itr.hasNext()){
			List<String>  tempList = new ArrayList<String>();
			String Item = (String) itr.next();
			tempList.add(Item);
			tableList.add(tempList);
		}
		return tableList;
	}
	
	/**
	 * 用当前频繁项集自连接求下一次候选集
	 * @param FrequentItemset
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<List<String>> getNextCandidate(List<List<String>> FrequentItemset) {
		List<List<String>> nextCandidateItemset = new ArrayList<List<String>>();
		for (int i=0; i<FrequentItemset.size(); i++){
			
			HashSet<String> hsSet = new HashSet<String>();
			HashSet<String> hsSettemp = new HashSet<String>();
			for (int k=0; k< FrequentItemset.get(i).size(); k++)//获得频繁集第i行
				hsSet.add(FrequentItemset.get(i).get(k));
			int hsLength_before = hsSet.size();//添加前长度
			hsSettemp=(HashSet<String>) hsSet.clone();
			for(int h=i+1; h<FrequentItemset.size(); h++){//频繁集第i行与第j行(j>i)连接   每次添加且添加一个元素组成新的频繁项集的某一行，   
				hsSet=(HashSet<String>) hsSettemp.clone();//做连接的hasSet保持不变
				for(int j=0; j< FrequentItemset.get(h).size();j++)
					hsSet.add(FrequentItemset.get(h).get(j));
				int hsLength_after = hsSet.size();			
				if(hsLength_before+1 == hsLength_after && isSubsetOf(hsSet,record)==1 && isnotHave(hsSet,nextCandidateItemset)){
					//如果不相等，表示添加了1个新的元素，再判断其是否为record某一行的子集若是则其为候选集中的一项
					Iterator<String> itr = hsSet.iterator();
					List<String>  tempList = new ArrayList<String>();
					while(itr.hasNext()){
						String Item = (String) itr.next();
						tempList.add(Item);
					}
					nextCandidateItemset.add(tempList);
				}
			}
		}
		return nextCandidateItemset;
	}
	
	/**
	 * 判断新添加元素形成的候选集是否在  新的候选集中
	 * @param hsSet
	 * @param nextCandidateItemset
	 * @return
	 */
	private static boolean isnotHave(HashSet<String> hsSet,
		List<List<String>> nextCandidateItemset) {
		List<String>  tempList = new ArrayList<String>();
		Iterator<String> itr = hsSet.iterator();
		while(itr.hasNext()){
			String Item = (String) itr.next();
			tempList.add(Item);
		}
		for(int i=0; i<nextCandidateItemset.size();i++)
			if(tempList.equals(nextCandidateItemset.get(i)))
				return false;
		return true;
	}

	/**
	 * 判断hsSet是不是record2中的某一记录子集
	 * @param hsSet
	 * @param record2
	 * @return
	 */
	private static int isSubsetOf(HashSet<String> hsSet,
			List<List<String>> record2) {
		//hsSet转换成List
		List<String>  tempList = new ArrayList<String>();
		Iterator<String> itr = hsSet.iterator();
		while(itr.hasNext()){
			String Item = (String) itr.next();
			tempList.add(Item);
		}		
		
		for(int i=0;i<record.size();i++){
			List<String>  tempListRecord = new ArrayList<String>();
			for(int j=0;j<record.get(i).size();j++)
				tempListRecord.add(record.get(i).get(j));
			if(tempListRecord.containsAll(tempList))
				return 1;
			}
		return 0;
	}

	/**
	 * 由k项候选集剪枝得到k项频繁集
	 * @param CandidateItemset
	 * @return
	 */
	private static List<List<String>> getSupprotedItemset(List<List<String>> CandidateItemset) {
		
		boolean end = true;
		List<List<String>> supportedItemset = new ArrayList<List<String>>();
		
		for (int i = 0; i < CandidateItemset.size(); i++){
			int count = countFrequent(CandidateItemset.get(i));//统计记录数
			if (count >= SUPPORTVALVE){	
				supportedItemset.add(CandidateItemset.get(i));
				end = false;
			}
		}
		endTag = end;//存在频繁项集则不会结束
		if(endTag==true)
			System.out.println("候选项集支持度技术小于" + SUPPORTVALVE + "，停止连接");
		return supportedItemset;
	}

	/**
	 * 统计record中出现list集合的个数
	 * @param list
	 * @return
	 */
	private static int countFrequent(List<String> list) {

		int count = 0;
		for(int i = 0; i<record.size(); i++) {
			boolean notHaveThisList = false;
			for (int k=0; k < list.size(); k++){//判断record.get(i)是否包含list
				boolean thisRecordHave = false;
				for(int j=0; j<record.get(i).size(); j++){
					if(list.get(k).equals(record.get(i).get(j)))//list.get(k)在record。get(i)中能找到
						thisRecordHave = true;
				}
				if(!thisRecordHave){//只要有一个list元素找不到，则退出其余元素比较,进行下一个record。get(i)比较
					notHaveThisList = true;
					break;
				}
			}
			if(notHaveThisList == false)
				count++;
		}
		return count;
	}
	
	public static void main(String[] args) {
		apriori();
	}

}
