import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;




public class JodaTimeTests {


    public static void main(String[] args) {


        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        try{

            DateTime current = new DateTime().toDateTime(DateTimeZone.UTC);
            System.out.println(current.getYear() + "-" + current.getMonthOfYear() + "-" + current.getDayOfMonth() + " " + current.getHourOfDay() + ":" + current.getMinuteOfHour() + ":" + current.getSecondOfMinute());


            BufferedReader br = new BufferedReader(new FileReader(new File("/home/ioannis/Documents/dates.txt")));

            String line;

            while ( (line = br.readLine()) != null){

                DateTime dt = formatter.parseDateTime(line);
                DateTime utc = dt.toDateTime(DateTimeZone.UTC);

            }

        }
        catch (FileNotFoundException fnf){
            System.out.println(fnf.getMessage());
        }
        catch (IOException io){
            System.out.println(io.getMessage());
        }


    }




    private ArrayList<String> getRaceDates(String uri){

        try{

            URL url = new URL(uri);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000);

            if(con.getResponseCode() == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String line;

                while ((line = br.readLine()) != null) {

                    sb.append(line);

                }

                br.close();
                con.disconnect();

                JSONObject root = new JSONObject(sb.toString());

                return getData(root);
            }
            else {
                System.out.println("Not responding");
            }
        }
        catch (MalformedURLException urlEx){
            System.out.println(urlEx);
        }
        catch (JSONException je){
            System.out.println(je.getMessage());
        }
        catch (IOException ioe){
            System.out.println(ioe.getMessage());
        }

        return null;
    }



    private ArrayList<String> getData(JSONObject root){

        try{

            JSONObject mrData = root.getJSONObject("MRData");
            JSONObject raceTable = mrData.getJSONObject("RaceTable");

            JSONArray datesArray = raceTable.getJSONArray("Races");

            ArrayList<String> results = new ArrayList<>();

            for(int raceIndex=0; raceIndex < datesArray.length(); raceIndex++){

                JSONObject race = datesArray.getJSONObject(raceIndex);

                String date = race.getString("date");
                String time = race.getString("time");

                results.add(date + " " + time.substring(0, date.length() - 2));

            }

            return results;
        }
        catch (JSONException jse){
            System.out.println(jse.getMessage());
        }

        return null;

    }



}
