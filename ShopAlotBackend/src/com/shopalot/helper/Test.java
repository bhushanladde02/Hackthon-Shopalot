package com.shopalot.helper;

public class Test {
	public static void main(String[] args) {
		String variable1="55,58,64,63,0,333~~55,58,64,63,0,333~~55,56,64,63,0,333~~56,56,65,63,0,333~~55,56,65,63,0,333~~53,58,64,67,0,333~~55,58,64,67,0,333~~";
		
		String[] signals = variable1.split("~~");
		
		for (int i = 0; i < signals.length; i++) {
		String[] out =  signals[i].split(",");
		
		int one = Integer.parseInt(out[0]);
		int two = Integer.parseInt(out[1]);
		int three = Integer.parseInt(out[2]);
		int four = Integer.parseInt(out[3]);
		int five = Integer.parseInt(out[4]);
		
		String trainingID = out[5].replaceAll("~~", "");
		
		System.out.println(one);
		System.out.println(five);
		System.out.println(out[5]);
		System.out.println(trainingID);
	}
}
}