import java.sql.*;

public class SQLiteTests{

    public static void main(String[] args) {

        try {

            Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/ioannis/Documents/databases/tests.db");


            DatabaseMetaData metadata = connection.getMetaData();

            ResultSet tables = metadata.getTables("tests.db", null, "%", null);

            while (tables.next()){
                String tableName = tables.getString(3);

                System.out.println(tableName);

                String sqlQuery = "select name from " + tableName + " where id%2 = 0;";

                Statement statement = connection.createStatement();

                ResultSet results = statement.executeQuery(sqlQuery);

                while (results.next()){

                    System.out.println(results.getString(1));

                }




            }

        }
        catch (SQLException sql){
            System.out.println(sql.getMessage());
        }

    }







}
