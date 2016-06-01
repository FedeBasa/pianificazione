package it.soprasteria.pianificazione.v2.util;

import java.util.HashMap;
import java.util.Map;

public class ColnameConverter {

	private static Map<String, String> mapColname = new HashMap<>();
	
	static {
	
		mapColname.put("price", "tariffa");
		mapColname.put("cons0", "consolidato_1");
		mapColname.put("cons1", "consolidato_2");
		mapColname.put("cons2", "consolidato_3");
		mapColname.put("prod0", "prodotto_1");
		mapColname.put("prod1", "prodotto_2");
		mapColname.put("prod2", "prodotto_3");
		mapColname.put("currency","valuta");
		//TODO
		// eliminare accento, attenzione anche su DB
		mapColname.put("activity", "attività");
		mapColname.put("custom_desc", "desc_custom");
		mapColname.put("ferie1", "ferie_1");
		mapColname.put("ferie2", "ferie_2");
		mapColname.put("ferie3", "ferie_3");
	}

	private ColnameConverter() {
		
	}
	
	public static String convertColname(String colname) {
		
		return mapColname.get(colname);
	}

	public static boolean existsColname(String colname) {
		
		return mapColname.containsKey(colname);
	}
	
}
