package anarquianegra.hub;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.net.URL;
import java.io.InputStreamReader;
import org.json.*;
import java.net.ProtocolException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.io.UnsupportedEncodingException;

class MundoHub{
    //---------------------------------
    //Constants
    //---------------------------------
    
    private final static String USER_AGENT = "wiki_command_line_interface_java_based_program";
    
    private final static String HUB_QUERY = "https://api.github.com/search/repositories?";
    
    private final static String DEFAULT_ORDER = "desc";
    
    private final static String ACCEPT_HEADER = "vnd.github.mercy-preview+json";
    
    //------------------------
    //Atributes
    //---------------------------------
    
    /**
    * HttpURLConnection object
    */
    private HttpsURLConnection connection;
    
    /**
    * The url String to connect to
    */
    private String urlString;
    
    //--------------------------------
    //Constructor
    //-------------------------------
    public MundoHub(){
        urlString = HUB_QUERY;
    }
    
    //---------------------------------
    //Methods
    //---------------------------------
    
    public void setQuery(String q){
        urlString += "q="+q;
    }
    
    public void setStars(int stars){
        urlString += "+stars:>="+stars;
    }
    
    public void setLanguage(String language){
        urlString += "+language:"+language;
    }
    
    public void setSort(String sort){
        urlString += "&sort="+sort;
    }
    
    public JSONObject search() throws MalformedURLException, IOException{
        urlString += "&order="+DEFAULT_ORDER;
        connection = (HttpsURLConnection)new URL(urlString).openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setRequestProperty("Accept", "application/"+ACCEPT_HEADER);
        connection.connect();
        JSONObject result = null;
        try{
            System.out.println("Getting results from: ");
            System.out.println(urlString);
            System.out.println("-----------------------------------------");
            result = getResults();
        }
        catch(UnsupportedEncodingException e){
            System.out.println("Error - "+e.getMessage());
        }
        catch(ProtocolException ex){
            System.out.println("Error - "+ex.getMessage());
        }
        return result;
    }
    
    private JSONObject getResults() throws ProtocolException, UnsupportedEncodingException, MalformedURLException, IOException{
        StringBuilder sb = new StringBuilder();
        int HttpResult = connection.getResponseCode();
        JSONObject jsonObject = null;

        if (HttpResult == HttpsURLConnection.HTTP_OK) {
            InputStreamReader ir = new InputStreamReader(connection.getInputStream(), "utf-8");
            BufferedReader br = new BufferedReader(ir);
            String line = null;
            while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
            }
            br.close();
            jsonObject = (JSONObject) new JSONTokener(sb.toString()).nextValue();
        }
        return jsonObject;
    }
}