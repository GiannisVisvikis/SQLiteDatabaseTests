

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteTests{

    public static void main(String[] args) {

        /*
        checkEntries("/home/ioannis/Documents/quiz/cars.txt");
        checkEntries("/home/ioannis/Documents/quiz/constructors.txt");
        checkEntries("/home/ioannis/Documents/quiz/drivers.txt");
        checkEntries("/home/ioannis/Documents/quiz/figures.txt");
        checkEntries("/home/ioannis/Documents/quiz/helmets.txt");
        checkEntries("/home/ioannis/Documents/quiz/circuits.txt");
        */




    }





    private static void checkEntries(String txtFilePath){

        try{

            File txtFile = new File(txtFilePath);

            BufferedReader reader = new BufferedReader(new FileReader(txtFile));

            String line;
            int counter = 0;

            while ( (line = (reader.readLine())) != null){

                counter ++;

                String[] entries = line.split(",");

                if(entries.length != 7)
                    System.out.println("Error in line");
                else {

                    String imagePath = "/home/ioannis/Documents/quiz/" + entries[1] + ".png";

                    File imageFile = new File(imagePath);

                    if(!imageFile.getName().equalsIgnoreCase("unknown.png") && !imageFile.exists())
                        System.out.println(imageFile.getPath() + " is not there");

                }


            }


        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        catch (IOException io){
            System.out.println(io.getMessage());
        }


    }





    private void createDatabase(String databaseName, String[] tableNames){


           try {

            Connection connection = DriverManager.getConnection("jdbc:sqlite:/home/ioannis/Documents/quiz/databases/quiz_tests.db");


            String sqlQuery = "insert into friends values (null, 'gamw tin', 'trela mou', 24);";

            Statement statement = connection.createStatement();

            statement.execute(sqlQuery);


        }
        catch (SQLException sql){
            System.out.println(sql.getMessage());
        }




    }





}
