
package sadagi.utilities.validators;

/**
 * <p>
 * PuttyUtilityValidator class is a common validation class. 
 * It provides functionalities to validate the arguments of methods. 
 * 
 * @author Chandan Singh Sroniyan (Contact : +91 7827711464, Email : sadagi.chandan@gmail.com).
 */
public class PuttyUtilityValidator {
	
	 /**
	  * <p>
	  * Returns true if aText is non-null and has visible content.
	  *  
	  * @param aText It is a String variable.
	  * @return <code>true/false</code>
	  */
	  public static boolean textHasContent(String aText){
	    String EMPTY_STRING = "";
	    return (aText != null) && (!aText.trim().equals(EMPTY_STRING));
	  }
	  
	 /**
	  * <p>
	  * Returns true if aText is IP and has visible content
	  * 
	  * @param aText It is a String variable.
	  * @return <code>true/false</code>
	 */
	public static boolean textHasIp(String aText){
	    String EMPTY_STRING = "";
	    if((aText != null) && (!aText.trim().equals(EMPTY_STRING)))
	    {
	    	if(aText.split("\\.").length  == 4) {
	    		return true;
	    	} else {
	    		return false;
	    	}
	    } else {
	    	return false;
	    }
	  }
	  
	 /**
	  * <p>
	  * Returns true if object is not-null and has visible.
	  * 
	  * @param aObject It is any class object.
	  * @return <code>true/false</code>
	  */
	  public static boolean checkForNull(Object aObject) {
		    if (aObject == null) {
		      return false;
		    }else{
		    	return true;
		    }
		  }

}
