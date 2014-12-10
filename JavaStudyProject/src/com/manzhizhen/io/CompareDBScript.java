package com.manzhizhen.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class CompareDBScript {
	private static Map<String, MyTable> leftMap = new LinkedHashMap<String, MyTable>();
	private static Map<String, MyTable> rightMap = new LinkedHashMap<String, MyTable>();
	
	public static void main(String args[]) {
		File leftFile = new File("d://生产.sql");
		File rightFile = new File("d://全网.sql");
		
		Pattern p = Pattern.compile(".*(create){1}\\s*(table){1}.*");
		try {
			BufferedReader leftBufferedReader = new BufferedReader(new FileReader(leftFile));
			BufferedReader rightBufferedReader = new BufferedReader(new FileReader(rightFile));		
			
			String leftStr = null;
			while((leftStr = leftBufferedReader.readLine()) != null ) {
				if(p.matcher(leftStr).matches()) {
					MyTable myTable = new MyTable();
					
					int tableIndex = leftStr.indexOf("table") + 5;
					int endIndex = leftStr.indexOf("(", tableIndex);
					
					myTable.setTableName(leftStr.substring(tableIndex, endIndex).trim());
//					System.out.println("生产 获取表：" +  myTable.getTableName());
					
					while((leftStr = leftBufferedReader.readLine()) != null ) {
						if(!leftStr.trim().equals(")")) {
							
							String[] strs = leftStr.trim().split("\\s+");
							
							MyColumn myColumn = new MyColumn();
							myColumn.setName(strs[0]);
							for(int i = 1; i < strs.length; i++) {
								myColumn.getParams().add(strs[i].trim());
							}
							myTable.getColumnMap().put(myColumn.getName(), myColumn);
							
							
						} else {
							leftMap.put(myTable.getTableName(), myTable);
							break;
						}
					}
				}
			}
			
			
			String rightStr = null;
			while((rightStr = rightBufferedReader.readLine()) != null ) {
				if(p.matcher(rightStr).matches()) {
					MyTable myTable = new MyTable();
					
					int tableIndex = rightStr.indexOf("table") + 5;
					int endIndex = rightStr.indexOf("(", tableIndex);
					
					myTable.setTableName(rightStr.substring(tableIndex, endIndex).trim());
//					System.out.println("生产 获取表：" +  myTable.getTableName());
					
					while((rightStr = rightBufferedReader.readLine()) != null ) {
						if(!rightStr.trim().equals(")")) {
							
							String[] strs = rightStr.trim().split("\\s+");
							
							MyColumn myColumn = new MyColumn();
							myColumn.setName(strs[0]);
							for(int i = 1; i < strs.length; i++) {
								myColumn.getParams().add(strs[i].trim());
							}
							myTable.getColumnMap().put(myColumn.getName(), myColumn);
							
							
						} else {
							rightMap.put(myTable.getTableName(), myTable);
							break;
						}
					}
				}
			}
			
			System.out.println("生产共有表： " + leftMap.size());
			System.out.println("全网产共有表： " + rightMap.size());
			
			System.out.println("开始比较：");
			
			Set<String> leftKeySet = leftMap.keySet();
			Set<String> rightKeySet = rightMap.keySet();
			Set<String> allKeySet = new HashSet<String>();
			allKeySet.addAll(leftKeySet);
			allKeySet.addAll(rightKeySet);
			for(String key : allKeySet) {
				if(leftMap.get(key) == null) {
					System.out.println("生产缺失表：" + key);
					System.out.println();
					continue ;
				}
				
				if(rightMap.get(key) == null) {
					System.out.println("全网缺失表：" + key);
					System.out.println();
					continue ;
				}
				
				compareTable(leftMap.get(key), rightMap.get(key));
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void compareTable(MyTable leftTable, MyTable rightTable) {
		System.out.println("开始比较表：" + leftTable.getTableName());
		Map<String, MyColumn> leftMap = leftTable.getColumnMap();
		Map<String, MyColumn> rightMap = rightTable.getColumnMap();
		Set<String> leftKeySet = leftMap.keySet();
		Set<String> rightKeySet = rightMap.keySet();
		Set<String> allKeySet = new HashSet<String>();
		allKeySet.addAll(leftKeySet);
		allKeySet.addAll(rightKeySet);
		
		for(String key : allKeySet) {
			if(leftMap.get(key) == null) {
				System.out.println("生产表缺失字段：" + key);
				continue ;
			}
			
			if(rightMap.get(key) == null) {
				System.out.println("全网缺失表缺失字段：" + key);
				continue ;
			}
			
			compareColumn(leftTable.getColumnMap().get(key), rightTable.getColumnMap().get(key));
		}
		
		System.out.println();
		
	}
	
	public static void compareColumn(MyColumn leftColumn, MyColumn rightColumn) {
		List<String> leftList = leftColumn.getParams();
		List<String> rightList = rightColumn.getParams();
		
		int leftSize = leftList.size();
		int rightSize = rightList.size();
		
		if(leftSize != rightSize) {
			System.out.println(leftColumn.toString() + " -- " + rightColumn.toString());
			return ;
		}
		
		for(int i = 0; i < leftSize; i++) {
			if(!leftList.get(i).trim().equalsIgnoreCase(rightList.get(i).trim())) {
				System.out.println(leftColumn.toString() + " -- " + rightColumn.toString());
				return ;
			}
		}
		
	}
}

class MyTable {
	private String tableName;
	private Map<String, MyColumn> columnMap = new LinkedHashMap<String, MyColumn>();

	public Map<String, MyColumn> getColumnMap() {
		return columnMap;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	
}

class MyColumn {
	private String name;
	private List<String> params = new ArrayList<String>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getParams() {
		return params;
	}
	
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append("[");
		for(int i = 0; i < params.size(); i++) {
			str.append(params.get(i));
			if(i != params.size() - 1) {
				str.append("  ");
			}
		}
		str.append("]");
		return str.toString();
	}
}