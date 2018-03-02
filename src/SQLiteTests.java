

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteTests{

    public static void main(String[] args) {

        String driversTxtPath = "/Users/ioannis/Documents/quiz/drivers.txt";
        String constructorsTxtPath = "/Users/ioannis/Documents/quiz/constructors.txt";
        String circuitsTxtPath = "/Users/ioannis/Documents/quiz/circuits.txt";
        String helmetsTxtPath = "/Users/ioannis/Documents/quiz/helmets.txt";
        String figuresTxtPath = "/Users/ioannis/Documents/quiz/figures.txt";
        String carsTxtPath = "/Users/ioannis/Documents/quiz/cars.txt";

        String[] txtFilePaths = new String[]{driversTxtPath, constructorsTxtPath, circuitsTxtPath, helmetsTxtPath, figuresTxtPath, carsTxtPath};

        String driversTable = "drivers_table";
        String constructorsTable = "constructors_table";
        String circuitsTable = "circuits_table";
        String helmetsTable = "helmets_table";
        String figuresTable = "figures_table";
        String carsTable = "cars_table";

        String[] tableNames = new String[]{driversTable, constructorsTable, circuitsTable, helmetsTable, figuresTable, carsTable};

        int[] results = createDatabase("the_quiz_update.db", txtFilePaths, tableNames);

        for(int resInt=0; resInt<results.length; resInt++){
            System.out.println(results[resInt]);
        }

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

                    String imagePath = "/Users/ioannis/Documents/quiz/" + entries[1] + ".png";

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





    private static int[] createDatabase(String databaseFile, String[] txtFilePaths, String[] tableNames){

            int[] results = null;


            int counter = 0;

           try {

            Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/ioannis/Documents/quiz/databases/"
                                                                                            + databaseFile);

            Statement statement = connection.createStatement();

            File readTxtFile;

            for(int index = 0; index < txtFilePaths.length; index++){

                readTxtFile = new File(txtFilePaths[index]);

                String createTableQuery = "create table if not exists " + tableNames[index] + " (id integer primary key autoincrement, link text not null, " +
                  "photo_path varchar(50) not null, question text not null, answer text not null, false_1 text not null, false_2 text not null, " +
                  "false_3 text not null);";

                //System.out.println(createTableQuery);

                statement.addBatch(createTableQuery);

                StringBuilder insertQueryBuilder = new StringBuilder("insert into " + tableNames[index] + " values ");

                BufferedReader reader = new BufferedReader(new FileReader(readTxtFile));

                String line;

                while ( (line = reader.readLine()) != null ){

                    String[] entries = line.split(",");

                    String link = ", '" + entries[0].replace("'", "''") + "'";
                    String photoPath = ", '" + entries[1] + ".png'";
                    String question = ", '" + entries[2] + "'";
                    String answer = ", '" + entries[3] + "'";
                    String false1 = ", '" + entries[4] + "'";
                    String false2 = ", '" + entries[5] + "'";
                    String false3 = ", '" + entries[6] + "'";

                    insertQueryBuilder.append("(null" + link + photoPath + question + answer + false1 + false2 + false3 + "),");

                }


                String insertQuery = insertQueryBuilder.substring(0, insertQueryBuilder.length() - 1) + ";";
                //System.out.println(insertQuery);

                statement.addBatch(insertQuery);
            }


            results = statement.executeBatch();

        }
        catch (SQLException sql){
            System.out.println(sql.getMessage());
            System.out.println(counter);
        }
        catch (FileNotFoundException fnf){
               System.out.println(fnf.getMessage());
        }
        catch (IOException io){
            System.out.println(io.getMessage());
        }


        return results;
    }


}
