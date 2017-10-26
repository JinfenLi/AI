package clustering;

import java.util.ArrayList;
import java.util.List;

public class StratifyClustering {
	
	static List<Cluster> clusters = new ArrayList<Cluster>();
	
	public static void main(String[] args) {

		//��ȡ��ǰϵͳʱ��
		long before = System.currentTimeMillis();
		Data.print(clustering(Data.getClusters(clusters),3));
		long after = System.currentTimeMillis();
		System.out.println("��ξ�������ʱ�䣺" + (after-before) + "����");
	}
	
	/**
	 * ��ξ�������㷨
	 * @param cluster ԭʼ�ؼ���
	 * @param n Ŀ��صĸ���
	 * @return ������ɵĴؼ���
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
			cluster.get(k1).pointList.putAll(cluster.get(k2).pointList);//�ѵ�k2���ظ��ƺϲ�����k1����
			cluster.get(k1).centerPoint = Cluster.getCenterPoint(cluster.get(k1).pointList);//���¼�������ĵ�
			cluster.remove(k2);
		}
		return cluster;
	}
}
