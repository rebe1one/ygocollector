package com.rms.collector.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import com.rms.collector.model.Card;

public class NameCrawler {
	public static void main(String[] args) {
		
		try {
		    BufferedReader in = new BufferedReader(new FileReader("/home/andrei/ygodata"));
		    String str;
		    HashMap<String, Integer> map = new HashMap<String, Integer>();
		    while ((str = in.readLine()) != null) {
		    	if (str.trim().length() == 0) continue;
		        str = str.replaceAll("<\\s*\\w.*?>", "");
		        str = str.replaceAll("</\\s*\\w.*?>", ";");
		        String[] str2 = str.split("\\(");
		        CardDAO cardDAO = new CardDAO();
		        Card card = new Card();
				card.setName(str2[0].trim().substring(0, str2[0].length() - 2).replace("'", "''"));
				if (str2.length > 1) {
					String[] str3 = str2[1].split("[;,)]");
					for (int i = 0; i < str3.length; i++) {
						if (str3[i].trim().equals("Card Number")) {
							card.setCardNumber(Integer.valueOf(str3[i+1].trim()));
						} else if (str3[i].trim().equals("Card Type")) {
							if (map.get(str3[i+1].trim()) == null) {
								map.put(str3[i+1].trim(), 1);
							} else {
								map.put(str3[i+1].trim(), map.get(str3[i+1].trim()) + 1);
							}
						} else if (str3[i].trim().equals("Attribute")) {
							if ("Spell Card".equals(str3[i+1].trim())) {
								card.setAttribute("SPELL");
							} else if ("Trap Card".equals(str3[i+1].trim())) {
								card.setAttribute("TRAP");
							} else {
								card.setAttribute(str3[i+1].trim());
							}
						} else if (str3[i].trim().equals("Level")) {
							card.setStars(Integer.valueOf(str3[i+1].trim()));
						}
					}
				}
				card.setDescription("");
				cardDAO.insert(card);
				System.out.println(card.toString());
		    }
		    in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		CardDAO cardDAO = new CardDAO();
		Card card = new Card();
		card.setName("Drei");
		card.setSetId("DREI-0001");
		card.setCardNumber(1337);
		card.setStars(12);
		card.setAttribute("FIRE");
		card.setDescription("");
		//cardDAO.insert(card);
	}
}
