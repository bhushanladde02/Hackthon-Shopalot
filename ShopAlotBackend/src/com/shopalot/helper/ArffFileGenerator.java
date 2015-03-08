package com.shopalot.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import au.com.bytecode.opencsv.CSVReader;

public class ArffFileGenerator {
	
	//this will keep number of sample records for each location - used for averaging cosine similarity score  
	public Map<String,Integer> locationSampleCount = new HashMap<String, Integer>();
	
	public String convertToArffFile(String filepath,String trainingID){
		CSVReader csvReader = null;
		List<String[]> lines = null;
		try {
			csvReader = new CSVReader(new FileReader(new File(filepath)));
			lines = csvReader.readAll();
			csvReader.close();
			
			String arffStart = "@relation t\n\n@attribute wifi1 numeric\n@attribute wifi2 numeric\n@attribute wifi3 numeric\n@attribute wifi4 numeric\n@attribute wifi5 numeric";
			
			String getGroupString = "@attribute group "+ getNominalNamesForLocations(lines);
			
			List<String> writeLines = new ArrayList<String>();
			writeLines.add(arffStart);
			writeLines.add(getGroupString);
			writeLines.add("@data");
			
			List<String> rLines = FileUtils.readLines(new File(filepath));
			writeLines.addAll(rLines);
			
			FileUtils.writeLines(new File("/home/data/"+"data"+trainingID+".arff"), writeLines);
//			FileUtils.writeLines(new File("D:\\data"+trainingID+".arff"), writeLines);
			
			return "data"+trainingID+".arff";
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//something failed, handle later todo
		return null;
	}

	private String getNominalNamesForLocations(List<String[]> lines) {
		// TODO Auto-generated method stub
		Set<String> locations= new HashSet<String>();
		for (int i = 0; i < lines.size(); i++) {
			String location = lines.get(i)[5];
			if(locationSampleCount.containsKey(location))
			{
				locationSampleCount.put(location, locationSampleCount.get(location)+1);
			}else{
				locationSampleCount.put(location, 1);
			}
			locations.add(location);  //we have location name at index 5
		}
		String location = "";
		Iterator<String> iterator = locations.iterator();
		while(iterator.hasNext()){
			location+=iterator.next()+",";
		}
		location=location.substring(0,location.length()-1);
		
		return "{"+location+"}";
	}
	
	public static void main(String[] args) {
		new ArffFileGenerator().convertToArffFile("D:\\hackthonworkspace\\ShopAlotBackend\\data.csv1000", "1000");
	}
}
