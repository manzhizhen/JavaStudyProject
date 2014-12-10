package com.manzhizhen.reflective;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.xml.crypto.Data;

public class ReflectiveTest {
	/**
	 * 打印类的全部信息
	 * @param classStr
	 * @throws ClassNotFoundException 
	 */
	public static void printClassInfo(String classStr) throws ClassNotFoundException {
		Class c = Class.forName(classStr);
		Class superC = c.getSuperclass();
		System.out.println(Modifier.toString(c.getModifiers()) + " class " + classStr);
	
		if(superC != null && superC != Object.class) {
			System.out.println(" extends " + superC.getName());
		}
		
		System.out.println(" {\n");
		
		printAllFields(c);
		
		System.out.println();
		
		printConstructors(c);
		
		System.out.println();
		
		printMethods(c);
		
		System.out.println("}");
	}
	
	/**
	 * 打印成员属性信息
	 * @param c
	 */
	private static void printAllFields(Class c) {
		Field[] fields = c.getDeclaredFields(); // 返回其类和父类的所有域
		for(Field f: fields) {
			Class type = f.getType();
			System.out.print("    " + Modifier.toString(f.getModifiers()));
			System.out.println(" " + type.getName() + " " + f.getName() + ";");
		}
		
	}

	/**
	 * 打印构造函数信息
	 * @param c
	 */
	private static void printConstructors(Class c) {
		Constructor[] constructors = c.getDeclaredConstructors();
		for(Constructor con : constructors) {
			System.out.print("    " + Modifier.toString(con.getModifiers()));
			System.out.print(" " + con.getName() + " (");
			
			Class[] paramTypes = con.getParameterTypes();
			for(int j = 0; j < paramTypes.length; j++) {
				if(j > 0) {
					System.out.print(", ");
				}
				
				System.out.print(paramTypes[j].getName());
			}
			System.out.println(");");
		}
		
	}
	
	/**
	 * 打印成员方法信息
	 * @param c
	 */
	private static void printMethods(Class c) {
		Method[] methods = c.getDeclaredMethods();
		
		for(Method m : methods) {
			Class retType = m.getReturnType();
			
			// 返回值
			System.out.print("    " + Modifier.toString(m.getModifiers()));
			System.out.print(" " + retType.getName() + " " + m.getName() + "(");
			
			// 参数类型
			Class[] paramTypes = m.getParameterTypes();
			for(int j = 0; j < paramTypes.length; j++) {
				if(j > 0) {
					System.out.print(", ");
				}
				
				System.out.print(paramTypes[j].getName());
			}
			System.out.println(");");
		}
		
	}
	
	public static void main(String[] args) throws ClassNotFoundException {
		printClassInfo("java.lang.Double");
		
	}
}
