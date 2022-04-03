package com.ljz.util;

import java.io.File;

public class TestUtil {
	
	public static void main(String[] args) {
		
		delete();
	}
	
	public static void copy(){
		
	}
	
	public static void delete(){
		String path = "D://ljz//maven//repository//com//google";
		action(path);
		System.out.println("end");
	}
	
	public static void action(String path){
		File filePath = new File(path); 
		File[] listFiles = filePath.listFiles();
		for(File file :listFiles){
			
			if(file.isDirectory()){
				/*if(file.getName().startsWith("1.")||file.getName().startsWith("2.")){
					if(!file.getName().equals("2.4.10")){
						file.delete();
					}else{
						//System.out.println(file.getName());
					}
				}else{
					action(file.getAbsolutePath());
				}*/
				action(file.getAbsolutePath());
			}else if(file.isFile()){
				if(file.getName().endsWith(".jar")||file.getName().endsWith(".pom")||file.getName().endsWith(".sha1"))
					continue;
				file.delete();
				System.out.println(file.getName());
			}
		}
		
	}

}
