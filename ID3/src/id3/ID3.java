package id3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
/** * @author  Rachel 
@date 创建时间：2016年12月12日 上午12:19:17 * 
@version 1.0 * 
@parameter  *
 @since  * 
@return  */
public class ID3 {
    /**
     * attribute{outlook,temperature,humidity,windy,play}
     * attributevalue{{sunny, overcast, rainy},{hot, mild, cool},{high, normal},{TRUE, FALSE},{yes, no}}
     * data{[sunny,hot,high,FALSE,no],[sunny,hot,high,TRUE,no],[],......}
     */
	private ArrayList<String> attribute = new ArrayList<String>(); // 存储属性的名称
    private ArrayList<ArrayList<String>> attributevalue = new ArrayList<ArrayList<String>>(); // 存储每个属性的取值
    private ArrayList<String[]> data = new ArrayList<String[]>(); // 原始数据
    int decatt; // 决策变量在属性集中的索引
    public static final String patternString = "@attribute(.*)[{](.*?)[}]";
 
    Document xmldoc;
    Element root;
 
    public ID3() {
        xmldoc = DocumentHelper.createDocument();
        root = xmldoc.addElement("root");
        root.addElement("DecisionTree").addAttribute("value", "null");
    }
 
    public static void main(String[] args) {
        ID3 inst = new ID3();
        //首先将属性，属性值，真实数据存储起来，然后设置叶节点，然后添加非叶子节点的索引，真实数据的行的索引
        inst.readARFF(new File("D:/java_workPaces1/ID3/src/id3/weather.nominal.arff"));
        //设置叶子节点
        inst.setDec("play");
        LinkedList<Integer> ll=new LinkedList<Integer>();
        for(int i=0;i<inst.attribute.size();i++){
        	// decatt决策变量在属性集中的索引
            if(i!=inst.decatt)
            	//添加非叶子节点的索引
                ll.add(i);
        }
        ArrayList<Integer> al=new ArrayList<Integer>();
        for(int i=0;i<inst.data.size();i++){
        	//添加真实数据的行的索引
            al.add(i);
        }
        inst.buildDT("DecisionTree", "null", al, ll);
        inst.writeXML("D:/java_workPaces1/ID3/src/id3/dt.xml");
        return;
    }
 
    //读取arff文件，给attribute、attributevalue、data赋值
    public void readARFF(File file) {
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            Pattern pattern = Pattern.compile(patternString);
            while ((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                	//添加属性
                    attribute.add(matcher.group(1).trim());
                    String[] values = matcher.group(2).split(",");
                    //属性值
                    ArrayList<String> al = new ArrayList<String>(values.length);
                    for (String value : values) {
                        al.add(value.trim());
                    }
                    //添加该属性的所有属性值
                    attributevalue.add(al);
                } else if (line.startsWith("@data")) {
                    while ((line = br.readLine()) != null) {
                        if(line=="")
                            continue;
                        String[] row = line.split(",");
                        //添加真实数据
                        data.add(row);
                    }
                } else {
                    continue;
                }
            }
            br.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
 
    //设置决策变量
    public void setDec(int n) {
        if (n < 0 || n >= attribute.size()) {
            System.err.println("决策变量指定错误。");
            System.exit(2);
        }
        decatt = n;
    }
    //设置叶节点， 值为属性的Index
    public void setDec(String name) {
        int n = attribute.indexOf(name);
        setDec(n);
    }
 
    //给一个样本（数组中是各种情况的计数），计算它的熵
    public double getEntropy(int[] arr) {
    	/**             yes no
    	 * sunny      1   3
    	 * over         2   1
    	 * 
    	 * -1/4log(1/4)-3/4log(3/4)
    	 * =1/4(-1*(og1-log4)-3*(log3-log4))
    	 * =0-1*log1-3*log3+4log4
    	 */
        double entropy = 0.0;
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            entropy -= arr[i] * Math.log(arr[i]+Double.MIN_VALUE)/Math.log(2);
            //某节点的总数
            sum += arr[i];
        }
        entropy += sum * Math.log(sum+Double.MIN_VALUE)/Math.log(2);
        entropy /= sum;
        return entropy;
    }
 
    //给一个样本数组及样本的算术和，计算它的熵
    public double getEntropy(int[] arr, int sum) {
        double entropy = 0.0;
        for (int i = 0; i < arr.length; i++) {
            entropy -= arr[i] * Math.log(arr[i]+Double.MIN_VALUE)/Math.log(2);
        }
        entropy += sum * Math.log(sum+Double.MIN_VALUE)/Math.log(2);
        entropy /= sum;
        return entropy;
    }
 
    public boolean infoPure(ArrayList<Integer> subset) {
    	//value为subset第一行叶子节点的值
        String value = data.get(subset.get(0))[decatt];
        for (int i = 1; i < subset.size(); i++) {
            String next=data.get(subset.get(i))[decatt];
            //equals表示对象内容相同，==表示两个对象指向的是同一片内存
            if (!value.equals(next))
                return false;
        }
        return true;
    }
 
    // 给定原始数据的子集(subset中存储行号),当以第index个属性为节点时计算它的信息熵
    public double calNodeEntropy(ArrayList<Integer> subset, int index) {
    	//总行数
        int sum = subset.size();
        double entropy = 0.0;
        /**某个属性的属性值
         *                  yes    no
         * sunny
         * overcast
         * rainy
         */
        
        int[][] info = new int[attributevalue.get(index).size()][];
        for (int i = 0; i < info.length; i++)
        	//叶子节点的个数
            info[i] = new int[attributevalue.get(decatt).size()];
        //该节点某一个属性值的个数
        int[] count = new int[attributevalue.get(index).size()];
        for (int i = 0; i < sum; i++) {
        	//第i行行号
            int n = subset.get(i);
            //该行该节点的属性值
            String nodevalue = data.get(n)[index];
            //这个属性在属性值集合中的index
            int nodeind = attributevalue.get(index).indexOf(nodevalue);
            count[nodeind]++;
            //该行的叶子节点的值
            String decvalue = data.get(n)[decatt];
            //该叶子节点值的index
            int decind = attributevalue.get(decatt).indexOf(decvalue);
            //该节点的属性值和对应的叶子节点值的个数
            info[nodeind][decind]++;
        }
        for (int i = 0; i < info.length; i++) {
            entropy += getEntropy(info[i]) * count[i] / sum;
        }
        return entropy;
    }
 
    // 构建决策树
    /**
     * 
     * @param name  属性名
     * @param value  属性值
     * @param subset 行号
     * @param selatt  非叶子结点index
     */
    public void buildDT(String name, String value, ArrayList<Integer> subset,
            LinkedList<Integer> selatt) {
        Element ele = null;
        @SuppressWarnings("unchecked")
        List<Element> list = root.selectNodes("//"+name);
        Iterator<Element> iter=list.iterator();
        while(iter.hasNext()){
            ele=iter.next();
            if(ele.attributeValue("value").equals(value))
                break;
        }
        //设置叶子节点值
        if (infoPure(subset)) {
            ele.setText(data.get(subset.get(0))[decatt]);
            return;
        }
        int minIndex = -1;
        double minEntropy = Double.MAX_VALUE;
        for (int i = 0; i < selatt.size(); i++) {
            if (i == decatt)
                continue;
            double entropy = calNodeEntropy(subset, selatt.get(i));
            if (entropy < minEntropy) {
                minIndex = selatt.get(i);
                minEntropy = entropy;
            }
        }
        String nodeName = attribute.get(minIndex);
        selatt.remove(new Integer(minIndex));
        ArrayList<String> attvalues = attributevalue.get(minIndex);
        for (String val : attvalues) {
            ele.addElement(nodeName).addAttribute("value", val);
            ArrayList<Integer> al = new ArrayList<Integer>();
            for (int i = 0; i < subset.size(); i++) {
                if (data.get(subset.get(i))[minIndex].equals(val)) {
                	//只添加该属性值的行号
                    al.add(subset.get(i));
                }
            }
            buildDT(nodeName, val, al, selatt);
        }
    }
 
    // 把xml写入文件
    public void writeXML(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists())
                file.createNewFile();
            FileWriter fw = new FileWriter(file);
            OutputFormat format = OutputFormat.createPrettyPrint(); // 美化格式
            XMLWriter output = new XMLWriter(fw, format);
            output.write(xmldoc);
            output.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
