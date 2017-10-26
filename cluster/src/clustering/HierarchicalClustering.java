package clustering;

import java.util.ArrayList;
import java.util.List;

public class HierarchicalClustering {
	
	static List<Cluster> clusters = new ArrayList<Cluster>();
	
//	public static void main(String[] args) 
	{

		//获取当前系统时间
		long before = System.currentTimeMillis();
		Data.print(clustering(Data.getClusters(clusters),3));
		long after = System.currentTimeMillis();
		System.out.println("层次聚类运行时间：" + (after-before) + "毫秒");
	}
	
	/**
	 * 层次聚类核心算法
	 * @param cluster 原始簇集合
	 * @param n 目标簇的个数
	 * @return 聚类完成的簇集合
	 */
	public static List<Cluster> clustering(List<Cluster> cluster ,int n) {
		while(cluster.size() > n) {
			int k1 = 0, k2 = 1;
			double dis = Data.sim_distance(cluster.get(k1), cluster.get(k2));
			for(int i = 0; i < cluster.size(); i++) {
				for(int j = i + 1; j < cluster.size(); j++) {
					if(dis > Data.sim_distance(cluster.get(i), cluster.get(j))) {
						dis = Data.sim_distance(cluster.get(i), cluster.get(j));
						k1 = i;
						k2 = j;
					}
				}
			}
			cluster.get(k1).pointList.putAll(cluster.get(k2).pointList);//把第k2个簇复制合并到第k1个簇
			cluster.get(k1).centerPoint = Cluster.getCenterPoint(cluster.get(k1).pointList);//重新计算簇中心点
			cluster.remove(k2);
		}
		return cluster;
	}
}
