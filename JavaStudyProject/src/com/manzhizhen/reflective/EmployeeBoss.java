package com.manzhizhen.reflective;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EmployeeBoss extends Employee{
	private List<Employee> employeeList = new ArrayList<Employee>();
	public int level;
	protected int wage;
	char sex;

	public List<Employee> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<Employee> employeeList) {
		this.employeeList = employeeList;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getWage() {
		return wage;
	}

	public void setWage(int wage) {
		this.wage = wage;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException, NoSuchFieldException {
		Class clazz = Class.forName("com.manzhizhen.reflective.EmployeeBoss");
		
		for(Field field : clazz.getFields()) {
			System.out.println("Fields: " + field.toString());
		}
		
		System.out.println("");
		
		for(Field field : clazz.getDeclaredFields()) {
			System.out.println("DeclaredFields: " + field.toString());
		}
		
		EmployeeBoss boss = (EmployeeBoss) clazz.newInstance();
		boss.setLevel(2);
		boss.setWage(40000);
		Field levelField = clazz.getField("level");
		Field wageField = clazz.getDeclaredField("wage");
		
		System.out.println(levelField.get(boss));
		
		wageField.setAccessible(true);	// 为了获取private的域值，需要设置访问权限为true
		System.out.println(wageField.get(boss));
		
		for(Field field : boss.getClass().getFields()) {
			System.out.println(field.getName());
		}
		
		Employee e1 = new Employee("123", "234");
		Employee e2 = new Employee("123", "234");
		System.out.println(e1.equals(e2));
		
	}
	
}
