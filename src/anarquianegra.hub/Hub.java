package anarquianegra.hub;

import java.net.MalformedURLException;
import java.io.IOException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import com.beust.jcommander.JCommander;
import java.lang.NumberFormatException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

class Hub{

    //-----------------------------------
    //Constants
    //-----------------------------------
    
    private final static int DEFAULT_LIMIT = 100;
    
    //------------------------------------
    //Atributes
    //------------------------------------
    
    /**
    * Reference to the wiki url connection getter object
    */
    private static MundoHub hub;
    
    /**
    * Reference to the parsed args
    */
    private static Args args;
    
    /**
    * Limit count of results
    */
    private static int limit;

    public static void main(String[] argsv){
        printName();
        args = new Args();
        JCommander commander = JCommander.newBuilder()
            .addObject(args)
            .build();
        commander.setProgramName("Hub");
        commander.parse(argsv);
        if(argsv.length > 0 && (args.name != null || args.topic != null)){
            hub  = new MundoHub();
            limit = DEFAULT_LIMIT;
            if(args.name != null)
                hub.setQuery(args.name);
            else
                hub.setQuery("topic:"+args.topic);
            if(args.stars != null)
                hub.setStars(args.stars.intValue());
            if(args.language != null)
                hub.setLanguage(args.language);
            if(args.sort != null)
                hub.setSort(args.sort);
            if(args.limit != null)
                limit = args.limit;
            doHubSearch();
        }
        else{
            commander.usage();
        }
    }
    
    private static void doHubSearch(){
        JSONObject result = null;
        try{
           result =  hub.search();
        }
        catch(MalformedURLException e){
            System.out.println("Error - "+e.getMessage());
        }
        catch(IOException ex){
            System.out.println("Error - "+ex.getMessage());
        }
        showResultToUser(result);
    }
    
    private static void showResultToUser(JSONObject result){
        JSONArray items = result.getJSONArray("items");
        for(int i = 0; i<items.length() && i<limit; i++){
            JSONObject item = items.getJSONObject(i);
            String label = "";
            String description = "";
            try{
                label = item.getString("name");
                description =  item.getString("description");
            }
            catch(JSONException e){
            
            }
            System.out.println(i+"| "+label+" - "+description);
        }
        if(items.length() > 0){
            System.out.println("Choose a number from the list to get the repo url: ");
            String PS2 = System.getenv("PS2");
            PS2 = (PS2 == null) ? ">" : PS2;
            System.out.println(PS2);
            waitForNumber(items);
        }
    }
    
    private static void  waitForNumber(JSONArray repos){
        InputStreamReader converter = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(converter);
         
        try{
            int num =  Integer.parseInt(in.readLine());
            JSONObject repo =  repos.getJSONObject(num);
            String git_url = repo.getString("clone_url");
            
            //Copy url to clipboard
            StringSelection selection = new StringSelection(git_url);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            System.out.println("Copied to clipboard: "+git_url);
        }
        catch(NumberFormatException e){}
        catch(IOException ex){
            System.out.println("Error - "+ex.getMessage());
        }
    }
    
    private static void printName(){
        System.out.println("#_#####_#########_#####");
        System.out.println("|#|###|#|#######|#|####");
        System.out.println("|#|__#|#|#_###_#|#|#_##");
        System.out.println("|##__)|#||#|#|#||#||#\\#");
        System.out.println("|#|###|#||#|_|#||#|_)#)");
        System.out.println("|_|###|_|#\\____||____/#");
        System.out.println("#######################");
    }
}