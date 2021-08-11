package com.alie.db;

import java.sql.*;
import java.util.Scanner;

public class BoxTester {

    public static void main(String[] args) {
        /**
         *  # pre-ready:
         *    (1) create table box:
         *        (sql):CREATE TABLE box (
                        box_serial INT AUTO_INCREMENT PRIMARY KEY,
                        box_name VARCHAR(20) NOT NULL,
                        box_display_name VARCHAR(40),
                        box_length DOUBLE NOT NULL,
                        box_width DOUBLE NOT NULL,
                        box_height DOUBLE NOT NULL,
                        box_price INT NOT NULL
                        );
         *    (2) insert data to box:
         *        (sql):INSERT INTO box(box_name, box_display_name, box_length, box_width, box_height, box_price) VALUES ('box1 便利箱', '長型便利箱', 31, 22.8, 10.3, 80);
                        INSERT INTO box(box_name, box_display_name, box_length, box_width, box_height, box_price) VALUES ('box2 便利箱', '方型便利箱', 23, 18, 19, 80);
                        INSERT INTO box(box_name, box_display_name, box_length, box_width, box_height, box_price) VALUES ('box3 便利箱', '90公分便利箱', 39.5, 27.5, 23, 110);
                        INSERT INTO box(box_name, box_display_name, box_length, box_width, box_height, box_price) VALUES ('box4 便利箱', '長柱型便利箱', 10, 10, 62.5, 80);
                        INSERT INTO box(box_name, box_display_name, box_length, box_width, box_height, box_price) VALUES ('box5 便利箱', '小型便利箱', 23, 14, 13, 65);
         *
         */
        Connection conn = null;
        try {
            // 1. JDBC Driver
            Class.forName("org.mariadb.jdbc.Driver");
            // 2. connect database, URL String
            // url: jdbc:mariadb://localhost:3306/DB?user=root&password=myPassword
            conn = DriverManager.getConnection("jdbc:mariadb://localhost:3307/shop"
                                                , "joy", "joy316");

            // 3. list all 郵便箱 boxes
            System.out.println("（郵局便利箱 box）");
            String queryStr = "SELECT * FROM box ORDER BY box_name;";
            PreparedStatement preparedStatement = conn.prepareStatement(queryStr);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                System.out.println(resultSet.getString("box_name")+"\t"
                                    +resultSet.getString("box_display_name")+"\t"
                                    +resultSet.getFloat("box_length")+"*"
                                    +resultSet.getFloat("box_width")+"*"
                                    +resultSet.getFloat("box_height")+"(cm)\t"
                                    +"$"+resultSet.getString("box_price")
                );
            }
            resultSet.close();
            preparedStatement.close();
            System.out.println("----------------------------------------------");

            // 4. mapping boxes
            Scanner scanner = new Scanner(System.in);
            String num;
            // Please enter your object length:
            /*System.out.println("Please enter your object length (int or float):");
            float length = Float.parseFloat(scanner.next());*/
            do {
                System.out.print("Please enter your object length (int or float):");
                num = scanner.nextLine();
            } while (!BoxTester.isNumeric(num));
            float length = Float.parseFloat(num);

            // Please enter your object width:
            /*System.out.println("Please enter your object width (int or float):");
            float width = Float.parseFloat(scanner.next());*/
            do {
                System.out.print("Please enter your object width (int or float):");
                num = scanner.nextLine();
            } while (!BoxTester.isNumeric(num));
            float width = Float.parseFloat(num);

            // Please enter your object height:
            /*System.out.println("Please enter your object height (int or float):");
            float height = Float.parseFloat(scanner.next());*/
            do {
                System.out.print("Please enter your object height (int or float):");
                num = scanner.nextLine();
            } while (!BoxTester.isNumeric(num));
            float height = Float.parseFloat(num);

            // box name, price or no box for you
            Statement statement = conn.createStatement();
            String boxQueryStr = "SELECT * FROM box WHERE "+length+" <= box_length"
                                +" AND "+width+" <= box_width AND "+height+" <= box_height"
                                +" ORDER BY box_price, box_name;";
            ResultSet boxData = statement.executeQuery(boxQueryStr);
            if( boxData.next() ){
                boxData.previous();
                System.out.println("You can choise the following boxes:");
                while (boxData.next()){
                    System.out.println(boxData.getString("box_name")+"\t"
                                        +boxData.getString("box_display_name")+"\t"
                                        +boxData.getFloat("box_length")+"*"
                                        +boxData.getFloat("box_width")+"*"
                                        +boxData.getFloat("box_height")+"(cm)\t"
                                        +"$"+boxData.getString("box_price")
                    );
                }
            }
            else{
                System.out.println("no box for you!");
            }
            boxData.close();
            statement.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            if( conn != null ) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static boolean isNumeric(String str) {
        try {
            if( str == null )
                return  false;
            else if( str.trim().isEmpty() )
                return false;
            else {
                Double.parseDouble(str);
                return true;
            }
        } catch(NumberFormatException e){
            return false;
        }
    }
}
