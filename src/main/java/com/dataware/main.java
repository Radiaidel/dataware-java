package com.dataware;

import java.sql.Connection;

import com.dataware.database.DatabaseConnection;

public class main {

	public static void main(String[] args) {
		
			Connection conn = 	DatabaseConnection.getInstance().getConnection();
			
	}

}
