/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.monitoringenergirumah.Data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestPerangkat {
    public static void main(String[] args) {

        try {
            Connection conn = KoneksiDB.getConnection();
            Statement st = conn.createStatement();

            String sql = "SELECT * FROM perangkat";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id_perangkat"));
                System.out.println("Nama: " + rs.getString("nama_perangkat"));
                System.out.println("Status: " + rs.getString("STATUS"));
                System.out.println("Update: " + rs.getString("terakhir_update"));
                System.out.println("-------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}