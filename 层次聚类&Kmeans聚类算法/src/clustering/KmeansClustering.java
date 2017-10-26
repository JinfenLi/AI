package clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class KmeansClustering {
	static List<Cluster> clusters1 = new ArrayList<Cluster>();

//	public static void main(String[] args) 
	{

		// ��ȡ��ǰϵͳʱ��
		long before = System.currentTimeMillis();
		// Data.print(init(Data.getClusters(clusters1)));
		Data.print(clustering(init(Data.getClusters(clusters1)), Data.getClusters(clusters1)));
		long after = System.currentTimeMillis();
		System.out.println("Kmeans��������ʱ�䣺" + (after - before) + "����");
	}

	/**
	 * �س�ʼ���������ѡ��һ���ص㣬��ѡһ����õ������Զ�Ĵص㣬Ȼ����ѡһ����ǰ��������Զ�Ĵص㣬 ���������Ϊ�س�ʼ��
	 * 
	 * @param clusters
	 * @return
	 */
	public static List<Cluster> init(List<Cluster> clusters) {

		List<Cluster> newList = new ArrayList<Cluster>();
		Random random = new Random();
		int num = random.nextInt(clusters.size());
		int max1 = 0, max2 = 0;
		double dis1 = Data.sim_distance(clusters.get(num), clusters.get(max1));
		for (int i = 0; i < clusters.size(); i++) {
			if (i != num && dis1 < Data.sim_distance(clusters.get(num), clusters.get(i))) {
				dis1 = Data.sim_distance(clusters.get(num), clusters.get(i));
				max1 = i;
			}
		}

		double dis2 = Data.sim_distance(clusters.get(num), clusters.get(max2)) < Data.sim_distance(clusters.get(max1),
				clusters.get(max2)) ? Data.sim_distance(clusters.get(num), clusters.get(max2))
						: Data.sim_distance(clusters.get(max1), clusters.get(max2));
		for (int i = 0; i < clusters.size(); i++) {
			double dis3 = Data.sim_distance(clusters.get(num), clusters.get(i)) < Data.sim_distance(clusters.get(max1),
					clusters.get(i)) ? Data.sim_distance(clusters.get(num), clusters.get(i))
							: Data.sim_distance(clusters.get(max1), clusters.get(i));
			if (i != num && i != max1 && dis2 < dis3) {
				dis2 = dis3;
				max2 = i;
			}
		}

		HashMap<Integer, Point> newMap1 = new HashMap<Integer, Point>();
		newMap1.putAll(clusters.get(num).pointList);
		newList.add(new Cluster(newMap1));

		HashMap<Integer, Point> newMap2 = new HashMap<Integer, Point>();
		newMap2.putAll(clusters.get(max1).pointList);
		newList.add(new Cluster(newMap2));

		HashMap<Integer, Point> newMap3 = new HashMap<Integer, Point>();
		newMap3.putAll(clusters.get(max2).pointList);
		newList.add(new Cluster(newMap3));

		return newList;
	}

	/**
	 * �س�ʼ���� �ȶ�������˲�ξ��࣬���3���أ�����ÿ�������ѡ��һ������ΪK-means�ĳ�ʼ��
	 * 
	 * @return
	 */
	/*public static List<Cluster> init() { // �Ƚ��в�ξ����ȡ3���ؼ�
		List<Cluster> list = HierarchicalClustering.clustering(Data.getClusters(new ArrayList<Cluster>()), 3);
		List<Cluster> newList = new ArrayList<Cluster>();
		int size = list.size();
		for (int i = 0; i < size; i++) {
			HashMap<Integer, Point> map = list.get(i).pointList;
			Iterator<Integer> it = map.keySet().iterator();
			if (it.hasNext()) {
				Integer inte = it.next();
				HashMap<Integer, Point> newMap = new HashMap<Integer, Point>();
				newMap.put(inte, map.get(inte));
				Cluster cluster = new Cluster(newMap);
				newList.add(cluster);
			}
		}
		return newList;
	}*/

	/**
	 * K-means��������㷨
	 * 
	 * @param cluster1
	 *            ��ʼ��
	 * @param cluster2
	 *            ���д�
	 * @return
	 */
	public static List<Cluster> clustering(List<Cluster> cluster1, List<Cluster> cluster2) {
		lable: for (int i = 0; i < cluster2.size(); i++) {
			Cluster clus = cluster2.get(i);
			int k = 0;
			Integer it = null;
			double dis = Data.sim_distance(clus, cluster1.get(k));
			for (int j = 0; j < cluster1.size(); j++) {
				Iterator<Integer> iterator = cluster2.get(i).pointList.keySet().iterator();
				if (iterator.hasNext())
					it = iterator.next();
				if (cluster1.get(j).pointList.containsKey(it)) {
					continue lable;
				}
				if (dis > Data.sim_distance(clus, cluster1.get(j))) {
					dis = Data.sim_distance(clus, cluster1.get(j));
					k = j;
				}
			}
			cluster1.get(k).pointList.put(it, cluster2.get(i).pointList.get(it));// �ѵ�i���ظ��ƺϲ�����k����
			cluster1.get(k).centerPoint = Cluster.getCenterPoint(cluster1.get(k).pointList);// ���¼�������ĵ�
		}
		return cluster1;
	}
}
