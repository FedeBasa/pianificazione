package it.soprasteria.pianificazione.v2.util;

import java.util.HashMap;
import java.util.Map;

public class ColnameConverter {

	private static Map<String, String> mapColname = new HashMap<String, String>();
	
	static {
	
		mapColname.put("price", "tariffa");
		mapColname.put("cons0", "consolidato_1");
		mapColname.put("cons1", "consolidato_2");
		mapColname.put("cons2", "consolidato_3");
		mapColname.put("prod0", "prodotto_1");
		mapColname.put("prod1", "prodotto_2");
		mapColname.put("prod2", "prodotto_3");
	}
	
	public static String convertColname(String colname) {
		
		return mapColname.get(colname);
	}

	public static boolean existsColname(String colname) {
		
		return mapColname.containsKey(colname);
	}
	
}