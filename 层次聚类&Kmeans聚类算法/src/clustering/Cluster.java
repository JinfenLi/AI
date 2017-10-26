package clustering;

import java.util.HashMap;
import java.util.Iterator;

public class Cluster {
	HashMap<Integer, Point> pointList = null;// �ؼ���
	Point centerPoint = null;// �����ĵ�

	public Cluster(HashMap<Integer, Point> pointList) {
		this.pointList = pointList;
		this.centerPoint = getCenterPoint(pointList);
	}

	/**
	 * ��ȡ�����ĵ�
	 * @param pointList
	 * @return �����ĵ�
	 */
	public static Point getCenterPoint(HashMap<Integer, Point> pointList) {

		double el = 0;// ��Ƭ����
		double ew = 0;// ��Ƭ���
		double fl = 0;// ���곤��
		double fw = 0;// ������

		Iterator<Integer> iterator = pointList.keySet().iterator();
		for (; iterator.hasNext();) {
			Point point = pointList.get(iterator.next());
			el += point.el;
			ew += point.ew;
			fl += point.fl;
			fw += point.fw;
		}
		return new Point(el / pointList.size(), ew / pointList.size(), fl / pointList.size(), fw / pointList.size());
	}
}

/**
 * �ص�
 * 
 * @author home
 */
class Point {
	double el;
	double ew;
	double fl;
	double fw;

	public Point(double el, double ew, double fl, double fw) {
		this.el = el;
		this.ew = ew;
		this.fl = fl;
		this.fw = fw;
	}

	public String toString() {
		return "(" + el + "," + ew + "," + fl + "," + fw + ")";
	}
}
