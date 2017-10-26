package clustering;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

/**
 * 数据类，用于获取、打印，整理数据等
 */
public class Data {

	/**
	 * 获取xls表格的数据集，整理数据并保存在List容器内
	 * 
	 * @return
	 */
	public static List<Cluster> getClusters(List<Cluster> cluster) {

		Workbook readWb = null;

		try {
			// 构建Workbook的输入流对象
			InputStream inputStream = new FileInputStream("lib//fisheriris_meas.xls");
			readWb = Workbook.getWorkbook(inputStream);

			// 获取第一张Sheet表
			Sheet readSheet = readWb.getSheet(0);
			// 获取Sheet表中所包含的总行数
			int rsRows = readSheet.getRows();

			for (int i = 0; i < rsRows; i++) {
				HashMap<Integer, Point> map = new HashMap<Integer, Point>();
				map.put(Integer.valueOf(i + 1), getRowSet(readSheet, i));
				Cluster clu = new Cluster(map);
				cluster.add(clu);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cluster;
	}

	/**
	 * 获取sheet表第row行的数据点
	 * 
	 * @param
	 * @return
	 */
	public static Point getRowSet(Sheet sheet, int row) {

		double el = Double.parseDouble(sheet.getCell(0, row).getContents());
		double ew = Double.parseDouble(sheet.getCell(1, row).getContents());
		double fl = Double.parseDouble(sheet.getCell(2, row).getContents());
		double fw = Double.parseDouble(sheet.getCell(3, row).getContents());

		return new Point(el, ew, fl, fw);
	}

	/**
	 * 计算两个簇的欧几里得距离
	 * 
	 * @return 两簇的距离
	 */
	public static double sim_distance(Cluster cluster1, Cluster cluster2) {
		double temp = Math.pow((cluster1.centerPoint.el - cluster2.centerPoint.el), 2)
				+ Math.pow((cluster1.centerPoint.ew - cluster2.centerPoint.ew), 2)
				+ Math.pow((cluster1.centerPoint.fl - cluster2.centerPoint.fl), 2)
				+ Math.pow((cluster1.centerPoint.fw - cluster2.centerPoint.fw), 2);
		double distance = Math.sqrt(temp);
		return distance;
	}

	/**
	 * 打印list集合的各个簇
	 * 
	 * @param list
	 */
	public static void print(List<Cluster> list) {
		for (int i = 0; i < list.size(); i++) {
			Cluster cluster = list.get(i);
			System.out.println("簇" + (i + 1) + ":");
			System.out.println("簇元素个数：" + cluster.pointList.keySet().size());
			Iterator<Integer> iterator = cluster.pointList.keySet().iterator();
			for (; iterator.hasNext();) {
				Integer integer = iterator.next();
				System.out.println("<" + integer + "," + cluster.pointList.get(integer) + ">,");
			}
			System.out.println("中心点：" + cluster.centerPoint);
			System.out.println();
		}
	}
}
