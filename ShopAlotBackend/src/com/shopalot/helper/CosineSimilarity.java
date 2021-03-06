package com.shopalot.helper;

import java.util.HashMap;
import java.util.Map;

public class CosineSimilarity {
        /*
         * This function computes cosine similarity score between two vectors
         */
        public static double difference(Map<String, Double> hashMap,
                        Map<String, Double> hashMap2) {
                String[] keys = new String[hashMap.size()];
                hashMap.keySet().toArray(keys);

                float ans = 0;

                for (int i = 0; i < keys.length; i++) {
                        if (hashMap2.containsKey(keys[i])) {
                                ans += hashMap.get(keys[i]) * hashMap2.get(keys[i]);
                                
                        }
                }

                float hashLength = 0;
                for (int i = 0; i < keys.length; i++) {
                        hashLength += (hashMap.get(keys[i]) * hashMap.get(keys[i]));
                }
                hashLength = (float) Math.sqrt(hashLength);

                String[] keys2 = new String[hashMap2.size()];
                hashMap2.keySet().toArray(keys2);

                float hash2Length = 0;
                for (int i = 0; i < keys2.length; i++) {

                        hash2Length += hashMap2.get(keys2[i]) * hashMap2.get(keys2[i]);

                }
                hash2Length = (float) Math.sqrt(hash2Length);
                
                if(hashLength == 0 || hash2Length == 0)
                        return 0;

                return (float) (ans / (hash2Length * hashLength));
        }
        
        public static float difference(Integer[] map1,Integer[] map2) {

        float ans = 0;

        for (int i = 0; i < map1.length; i++) {
                        ans += map1[i] * map2[i];
        }

        float hashLength = 0;
        for (int i = 0; i < map1.length; i++) {
                hashLength += map1[i] * map1[i];
        }
        hashLength = (float) Math.sqrt(hashLength);

        float hash2Length = 0;
        for (int i = 0; i < map2.length; i++) {

                hash2Length += map2[i] * map2[i];

        }
        hash2Length = (float) Math.sqrt(hash2Length);
        
        if(hashLength == 0 || hash2Length == 0)
                return 0;

        return (float) (ans / (hash2Length * hashLength));
}

        public static void main(String[] args) {
                CosineSimilarity consineSimilarity = new CosineSimilarity();
                HashMap<String, Double> hashMap = new HashMap<String,Double>();
                HashMap<String, Double> hashMap2 = new HashMap<String,Double>();
                hashMap.put("abc", 1.0);
                hashMap.put("def", 2.0);
                hashMap2.put("abc", 1.0);
                hashMap2.put("def", 2.0);
                
                
                double score = consineSimilarity.difference(hashMap, hashMap2);
                System.out.println(score);
                
                
                Integer signalA[] = {10,20,30,40,50};
                Integer signalB[] = {10,20,30,40,50};
                
                double distaince = CosineSimilarity.difference(signalA, signalB);
                System.out.println(distaince); 
        }

}