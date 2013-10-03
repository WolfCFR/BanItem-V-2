package com.abcalvin.BanItem;

import java.util.ArrayList;

public class itemcheck {
	int number;
	int Id;
	byte Data;
	String Reason;
	public static main plugin;

	public itemcheck(main instance) {
		plugin = instance;
	}
	public itemcheck(ArrayList<String> ls, int id, byte data){
		for(int x=0;x<ls.size(); x++){
			String string = ls.get(x);
			String[] seperated = string.split(":");
			Id = Integer.parseInt(seperated[0]);
			Data = Byte.parseByte(seperated[1]);
			if(Id == id){
				if(Data == data){
					number = 1;
					if(seperated.length>2 && number ==1){
						Reason = seperated[2].toString();
						}
					break;
				}else if(Data == -1){
					number = 1;
					Reason = seperated[2].toString();
					break;
				}else{
					number = 0;
				}
			}else{
				number = 0;
			}
			
		}
		
	}
}
