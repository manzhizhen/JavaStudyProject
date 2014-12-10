package com.manzhizhen.regex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
	public static void main(String[] args) throws IOException {
		// ^((?!word).)*$ 用于排除word这个字符串
//		 Pattern p = Pattern.compile("(0\\.[0-9]{0,4})&&(^((?!0\\.0000).)*$)");
		 
		 
		 Pattern p = Pattern.compile("([a-z]{3})://([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}):([0-9]{1,6})");
		 Matcher m = p.matcher("tcp://127.0.0.1:1314");
		 System.out.println(m.matches());
		 
		 System.out.println(m.group(0));
		 System.out.println(m.group(1));
		 System.out.println(m.group(2));
		 System.out.println(m.group(3));
		 
		 System.out.println("tcp://127.0.0.1:8080".matches("[0-9a-zA-Z]{1,32}"));
		 
//		 BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
////		 while(true) {
//			 p = Pattern .compile("0\\.\\d*[1-9]+\\d*|1|1.0|1.00|1.000|1.0000");
////			 String str = reader.readLine();
////			 p = Pattern.compile(str);
////			 System.out.println(str);
//			 Matcher m = p.matcher("1");
//			 System.out.println("1:" + m.matches());
//			 
//			 m = p.matcher("1.00");
//			 System.out.println("1.00:" + m.matches());
//			 m = p.matcher("1.01");
//			 System.out.println("1.01:" + m.matches());
//			 
//			 m = p.matcher("0.01");
//			 System.out.println("0.01:" + m.matches());
//			 
//			 m = p.matcher("0.000");
//			 System.out.println("0.000:" + m.matches());
//			 
//			 m = p.matcher("0");
//			 System.out.println("0:" + m.matches());
//		 }
		 
//		 p = Pattern.compile("10(471|100|220|531|311|351|551|210|250|571|591|898|200|771|971|270|731|791|371|891|280|230|290|851|871|931|951|991|431|240|451|999|997)(([0-9]{4}((0?[13578])|10|12)((0?[1-9])|[12][0-9]|(3[0-1]))|[0-9]{4}(02((0?[1-9])|[12][0-9]))|[0-9]{4}((0?[469])|11)((0?[1-9])|[12][0-9]|(30)))((0?[0-9]|1[0-9]|2[0-3])[0-5][0-9][0-5][0-9])([0-9]{3}))([0-9]{10})");
//		 m = p.matcher("102002013112818265204314012163231111");
//		 System.out.println(m.matches());
//		 new BigInteger("999999111111111111111111");
		 
//		 p = Pattern.compile(".*(create){1}\\s*(table){1}.*");
//		 System.out.println(p.matcher("create tadbcheck_yyyymmdd (").matches());
//		 
//		 String str = "	ID_TYPE                         char(1)                              null";
//		 String[] strs = str.split("\\s+");
//		 System.out.println(strs.length);
//		 for(int i = 0; i < strs.length ; i++) {
//			 System.out.println(strs[i].trim());
//		 }
		 
	}
}
