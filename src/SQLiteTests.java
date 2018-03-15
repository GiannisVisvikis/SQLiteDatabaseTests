

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLiteTests{

    public static void main(String[] args){

        //Uncomment to check missing files
        /*
        findMissingPics(getJSONInfo("http://ergast.com/api/f1/constructors.json?limit=1000&offset=0"), "Constructors", "ConstructorTable", "constructorId",
                                                                                                                                            "constructors", ".gif");
        */

        //Uncomment in any case

        String driversTxtPath = "/home/ioannis/Documents/quiz/drivers.txt";
        String constructorsTxtPath = "/home/ioannis/Documents/quiz/constructors.txt";
        String circuitsTxtPath = "/home/ioannis/Documents/quiz/circuits.txt";
        String helmetsTxtPath = "/home/ioannis/Documents/quiz/helmets.txt";
        String figuresTxtPath = "/home/ioannis/Documents/quiz/figures.txt";
        String carsTxtPath = "/home/ioannis/Documents/quiz/cars.txt";


        //Uncomment to check quiz entries
        /*
        checkEntries(driversTxtPath);
        checkEntries(constructorsTxtPath);
        checkEntries(circuitsTxtPath);
        checkEntries(figuresTxtPath);
        checkEntries(helmetsTxtPath);
        checkEntries(carsTxtPath);
        */

        //Uncomment to create database
        /*
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
        */

    }




    /*
    private static ArrayList<String> findMissingPics(String jsonString, String dataName, String dataTabled, String dataId, String dataFolder, String fileExtension){

        ArrayList<String> result = new ArrayList<>();

        try{

            JSONObject root = new JSONObject(jsonString);
            JSONObject mrData= root.getJSONObject("MRData");
            JSONObject dataTable = mrData.getJSONObject(dataTabled);

            JSONArray dataArray = dataTable.getJSONArray(dataName);

            for(int index=0; index<dataArray.length(); index++){

                JSONObject dataObject = dataArray.getJSONObject(index);

                String id = dataObject.getString(dataId);

                if( !(new File("/home/ioannis/Downloads/" + dataFolder + "/" + id + fileExtension)).exists() ) {
                    System.out.println(id + " file is missing");
                    result.add(id);
                }

            }


        }
        catch (JSONException je){
            System.out.println(je.getMessage());
        }

        System.out.println(result);
        return result;
    }

    */





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
                    System.out.println("Error in line " + counter);
                else {

                    String imagePath = "/home/ioannis/Documents/quiz/" + entries[1];

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

        System.out.println(txtFilePath + " checked");
    }





    private static int[] createDatabase(String databaseFile, String[] txtFilePaths, String[] tableNames){

            int[] results = null;


            int counter = 0;

           try {

            Connection connection = DriverManager.getConnection("jdbc:sqlite:/home/ioannis/Documents/quiz/databases/"
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
                    String photoPath = ", '" + entries[1] + "'";
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

        System.out.println("Database done...");
        return results;
    }



    private static String getJSONInfo(String apiUrl){

        StringBuilder sb = null;

        try{

            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setConnectTimeout(4000);

            sb = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;

            while ( (line = reader.readLine()) != null ){
                sb.append(line);
            }

            con.disconnect();
            reader.close();

        }catch (MalformedURLException mue){
            System.out.println(mue);
        }catch (IOException io){
            System.out.println(io.getMessage());
        }

        return sb.toString();
    }




}
