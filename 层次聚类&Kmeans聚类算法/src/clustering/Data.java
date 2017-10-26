package clustering;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

/**
 * �����࣬���ڻ�ȡ����ӡ���������ݵ�
 */
public class Data {

	/**
	 * ��ȡxls�������ݼ����������ݲ�������List������
	 * 
	 * @return
	 */
	public static List<Cluster> getClusters(List<Cluster> cluster) {

		Workbook readWb = null;

		try {
			// ����Workbook������������
			InputStream inputStream = new FileInputStream("lib//fisheriris_meas.xls");
			readWb = Workbook.getWorkbook(inputStream);

			// ��ȡ��һ��Sheet��
			Sheet readSheet = readWb.getSheet(0);
			// ��ȡSheet������������������
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
	 * ��ȡsheet���row�е����ݵ�
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
	 * ���������ص�ŷ����þ���
	 * 
	 * @return ���صľ���
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
	 * ��ӡlist���ϵĸ�����
	 * 
	 * @param list
	 */
	public static void print(List<Cluster> list) {
		for (int i = 0; i < list.size(); i++) {
			Cluster cluster = list.get(i);
			System.out.println("��" + (i + 1) + ":");
			System.out.println("��Ԫ�ظ�����" + cluster.pointList.keySet().size());
			Iterator<Integer> iterator = cluster.pointList.keySet().iterator();
			for (; iterator.hasNext();) {
				Integer integer = iterator.next();
				System.out.println("<" + integer + "," + cluster.pointList.get(integer) + ">,");
			}
			System.out.println("���ĵ㣺" + cluster.centerPoint);
			System.out.println();
		}
	}
}
