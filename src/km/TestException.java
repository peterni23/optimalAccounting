package km;

import org.apache.log4j.Logger;  

public class TestException {
    public static final Logger logger = Logger.getLogger("logger.debug");  
    
    public static void main(String[] args) {  
        try {  
            int a = 1 / 0;  
        } catch (Exception e) {  
            logger.error("ERROR", e);  
              
            //e.printStackTrace();  
        }  
    }  
}
