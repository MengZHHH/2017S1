package HCMM17S1;

import java.text.SimpleDateFormat;

public class Validation{
	
	// Below described every step to validate each field of an member object.
	public static boolean ValidName(String name){
		if(name.matches("^[ A-Za-z]*$"))
			return true;
		else
			return false;
	}
	
	public static boolean ValidMobile(String mobile){
		if(mobile.matches("^\\d{8}$"))
			return true;
		else
			return false;
	}
	
	public static boolean ValidPass(String pass){
		if(pass.equals("Gold") || pass.equals("Silver") || pass.equals("Bronze"))
			return true;
		else 
			return false;
	}
	
	public static boolean ValidEmail(String email){
		if(email.matches("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$"))
			return true;
		else
			return false;
	}
	
	public static boolean ValidDate(String date){
		SimpleDateFormat dateFormat = null;
		try {
			dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			dateFormat.setLenient(false);
			dateFormat.parse(date);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}
	
	public static boolean ValidAddress(String addr){
		String[] strs = addr.split(", ");
		for(String str : strs){
			String[] arr = str.split(" ");
			for(String s : arr){
				if(!s.matches("^[A-Za-z]*$") && !s.matches("^[0-9]*$"))
					return false;
			}
		}
		return true;
	}
}