package Mgmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MgmtMain {

	public static void main(String[] args) {
		try {
			Connection connection = DriverManager.getConnection
					("jdbc:mysql://localhost:3306/BarMgmt", "root", "password");
			System.out.println("Connected!");
			
			PreparedStatement ps = connection.prepareStatement("alter table food add column category varchar(15) after foodid");
			
//			ps = connection.prepareStatement("insert into Food values(?)");
//			ps.setString(1, "1"); // Add data
			ps.executeUpdate(); // Execute query
			System.out.println("data inserted");
		} catch (SQLException e) {
			System.out.println("Error while connecting to the database: " + e);
		}
	}

	}

}
