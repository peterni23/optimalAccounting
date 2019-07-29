package km;

import java.util.Collections;
import java.util.List;
import java.util.Map;


public class TestException extends Thread{
    
    public static void main(String[] args) {
    	
    	for(int i=0;i<5;i++){
    		new TestException().start();
    	}
    	
    }  
        @Override
		public void run() {
        	Map<String, List<String>> map = (new getStaticParam()).getMap();
        	List<String> list = map.get("usd");
//        	System.out.println(list);
        	Collections.sort(list);
		}
	
}
