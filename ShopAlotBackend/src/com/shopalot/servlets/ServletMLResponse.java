package com.shopalot.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import com.shopalot.helper.ArffFileGenerator;
import com.shopalot.helper.CosineSimilarity;
import com.shopalot.helper.LoadTrainingData;
import com.shopalot.helper.Util;



/**
 * Servlet implementation class ServletML
 */
@WebServlet("/ServletMLResponse")
public class ServletMLResponse extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static Integer broadcastCounter = 0 ;
	
	//hold results from 5 signals
	private Map<String,Integer>  topLocations = new HashMap<String, Integer>();

	private Map<Integer[], String> dataMap;
	
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletMLResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	/*protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}*/

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		
		List<Integer[]> signalsInput = new ArrayList<Integer[]>();
		
		ArffFileGenerator arffGenerator = new ArffFileGenerator();

		String variable1=request.getParameter("signals");
		
		String[] signals = variable1.split("~~");
		
		System.out.println(signals[0]);
		
		for (int i = 0; i < signals.length; i++) {
		String[] out =  signals[i].split(",");
		
		int one = Integer.parseInt(out[0]);
		int two = Integer.parseInt(out[1]);
		int three = Integer.parseInt(out[2]);
		int four = Integer.parseInt(out[3]);
		int five = Integer.parseInt(out[4]);
		
		
		
		String trainingID = out[5].replaceAll("~~", "");
		DataSource source = null;
		try {
			dataMap=LoadTrainingData.getDataSet("/home/data/data.csv"+trainingID);
//			dataMap=LoadTrainingData.getDataSet("D://data.csv"+trainingID);
			//convert to Arff File
			arffGenerator.convertToArffFile("/home/data/data.csv"+trainingID, trainingID);
//			arffGenerator.convertToArffFile("D://data.csv"+trainingID, trainingID);
			source = new DataSource("/home/data/data"+trainingID+".arff");
//			source = new DataSource("D://data"+trainingID+".arff");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 Instances data = null;
		try {
			data = source.getDataSet();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 // setting class attribute if the data format does not provide this information
		 // For example, the XRFF format saves the class attribute information as well
		 if (data.classIndex() == -1)
		   data.setClassIndex(data.numAttributes() - 1);
		 
		 String[] options = null;
		try {
			options = weka.core.Utils.splitOptions("-C 0.25 -M 2");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 // create new instance of scheme
		 J48 tree = new J48();         // new instance of tree
		 try {
			tree.setOptions(options);
			tree.buildClassifier(data);   // build classifier
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}     // set the options
		 
		 /*FastVector atts = new FastVector();
    	 atts.addElement(new Attribute("wifi1"));
    	 atts.addElement(new Attribute("wifi2"));
    	 atts.addElement(new Attribute("wifi3"));
    	 atts.addElement(new Attribute("wifi4"));
    	 atts.addElement(new Attribute("wifi5"));
    	 
    	// - nominal
         FastVector attVals = new FastVector();
         attVals.addElement("kitchen");
         attVals.addElement("hall");
         attVals.addElement("bedroom");
         attVals.addElement("bedroomRoshan");
         atts.addElement(new Attribute("group", attVals));
    	 atts.addElement(new Attribute("group", (FastVector) null));*/
    	
		 signalsInput.add(new Integer[]{one,two,three,four,five});  //add them to list for consine similarity
    	 
    	 double[] valsRel = new double[data.numAttributes()];
         valsRel[0] = one;
         valsRel[1] = two;
         valsRel[2] = three;
         valsRel[3] = four;
         valsRel[4] = five;
         
        
         
         Instance instance = new Instance(1.0, valsRel);
         instance.setDataset(data);
//        double index = tree.classifyInstance(instances.instance(1));
         double index;
		try {
			index = tree.classifyInstance(instance);
			String location = data.classAttribute().value((int)index);
			if(topLocations.containsKey(location)){
	        	topLocations.put(location, topLocations.get(location)+1);
	        }else
	        {
	        	topLocations.put(location, 1);
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		//from topLocations which are given by classifier, give location with highest count
		 LinkedHashMap<String,Integer> map = sortHashMapByValuesDesc(topLocations);
		 Entry<String, Integer> first = map.entrySet().iterator().next();
		 
		 String lKey = first.getKey();
		 
		 if(lKey.equalsIgnoreCase("Microsoft")){
			 ++broadcastCounter;
		 }
		 
		 if(broadcastCounter%3==0){
			 Util.broadcast();
			 broadcastCounter=0;
		 }
		 
		 
		 response.getWriter().print(lKey);
		
        //now use consine similarity based on Ben suggestion and check the results
		 
		 
		String location = getConsineSimBasedSuggestion(dataMap,signalsInput,arffGenerator);
		
		
		//-- just disabling printing
		
        //response.getWriter().print(location);  
        
	}

	//logic modified for taking the average of score per location 
	private String getConsineSimBasedSuggestion(
			Map<Integer[], String> dataMap2, List<Integer[]> signalsInput,ArffFileGenerator arffGenerator) {
		// TODO Auto-generated method stub
		
		HashMap<String,Float> scorePerLocation = new HashMap<String, Float>();
		
		for (int i = 0; i < signalsInput.size(); i++) {
			Integer[] map1 = signalsInput.get(i);
			Iterator<Entry<Integer[], String>> iterator = dataMap2.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<java.lang.Integer[], java.lang.String> entry = (Map.Entry<java.lang.Integer[], java.lang.String>) iterator.next();
				Integer[] key = entry.getKey();
				float score = CosineSimilarity.difference(map1, key);
				/*RankSignalLabels rankSignalLabels = new RankSignalLabels(key, score);
				queue.add(rankSignalLabels);*/
				String location = entry.getValue();
				if(scorePerLocation.containsKey(location))
				{
					scorePerLocation.put(location, scorePerLocation.get(location)+score);
				}else
				{
					//it's new entry
					scorePerLocation.put(location, score);
				}
			}
			
		}
	
		//--deleted
		//find top node at priority queue, or we can modify based on top 3 nodes
		//RankSignalLabels first = queue.iterator().next();
		
		Map<String, Integer> locationSampleCount = arffGenerator.locationSampleCount;
		
		//normalize scores by taking average
		
		Iterator<Entry<String, Float>> iterator = scorePerLocation.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, Float> entry = iterator.next();
			entry.setValue(entry.getValue()/locationSampleCount.get(entry.getKey())); 
		}
		
		
		LinkedHashMap<String, Float> sortedMap = sortHashMapByValuesDescFloat(scorePerLocation);
//		HashMap<String, Float> sortedMap = scorePerLocation;
		
		
	
		
		
		String locationString = "";
		Iterator<Entry<String, Float>> iteraror = sortedMap.entrySet().iterator();
		while(iteraror.hasNext()){
			Entry<String, Float> entry = iteraror.next();
			locationString+=entry.getKey()+"--"+entry.getValue()+"--"+locationSampleCount.get(entry.getKey())+"--";
		}
		return " Cosine Sim Ave -- "+locationString;
	}
	
	class RankSignalLabels{
		Integer[] signal;
		float score;
		
		public RankSignalLabels(Integer[] signal,float score){
			this.signal=signal;
			this.score=score;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	//descending order
	public LinkedHashMap<String, Integer> sortHashMapByValuesDesc(Map<String,Integer> passedMap) {
		   List mapKeys = new ArrayList(passedMap.keySet());
		   List mapValues = new ArrayList(passedMap.values());
		   Collections.sort(mapValues,Collections.reverseOrder());
		   Collections.sort(mapKeys,Collections.reverseOrder());

		   LinkedHashMap sortedMap = new LinkedHashMap();

		   Iterator valueIt = mapValues.iterator();
		   while (valueIt.hasNext()) {
		       Object val = valueIt.next();
		       Iterator keyIt = mapKeys.iterator();

		       while (keyIt.hasNext()) {
		           Object key = keyIt.next();
		           String comp1 = passedMap.get(key).toString();
		           String comp2 = val.toString();

		           if (comp1.equals(comp2)){
		               passedMap.remove(key);
		               mapKeys.remove(key);
		               sortedMap.put((String)key, (Integer)val);
		               break;
		           }

		       }

		   }
		   return sortedMap;
		}
	
	//descending order
		public LinkedHashMap<String, Float> sortHashMapByValuesDescFloat(Map<String,Float> passedMap) {
			   List mapKeys = new ArrayList(passedMap.keySet());
			   List mapValues = new ArrayList(passedMap.values());
			   Collections.sort(mapValues,Collections.reverseOrder());
			   Collections.sort(mapKeys,Collections.reverseOrder());

			   LinkedHashMap sortedMap = new LinkedHashMap();

			   Iterator valueIt = mapValues.iterator();
			   while (valueIt.hasNext()) {
			       Object val = valueIt.next();
			       Iterator keyIt = mapKeys.iterator();

			       while (keyIt.hasNext()) {
			           Object key = keyIt.next();
			           String comp1 = passedMap.get(key).toString();
			           String comp2 = val.toString();

			           if (comp1.equals(comp2)){
			               passedMap.remove(key);
			               mapKeys.remove(key);
			               sortedMap.put((String)key, (Float)val);
			               break;
			           }

			       }

			   }
			   return sortedMap;
			}

}
