/**
 * 
 */
package com.manzhizhen.exception;

import java.sql.SQLException;

/**
 * @author Manzhizhen
 *
 */
public class Test {
	public static void main(String[] args) {
		SQLException e = new SQLException();
		if (e instanceof SQLException) {
			System.out.println("sdf");
          }
	}
}
