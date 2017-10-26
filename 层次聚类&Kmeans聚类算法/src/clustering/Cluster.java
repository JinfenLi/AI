package clustering;

import java.util.HashMap;
import java.util.Iterator;

public class Cluster {
	HashMap<Integer, Point> pointList = null;// 簇集合
	Point centerPoint = null;// 簇中心点

	public Cluster(HashMap<Integer, Point> pointList) {
		this.pointList = pointList;
		this.centerPoint = getCenterPoint(pointList);
	}

	/**
	 * 获取簇中心点
	 * @param pointList
	 * @return 簇中心点
	 */
	public static Point getCenterPoint(HashMap<Integer, Point> pointList) {

		double el = 0;// 萼片长度
		double ew = 0;// 萼片宽度
		double fl = 0;// 花瓣长度
		double fw = 0;// 花瓣宽度

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
 * 簇点
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
