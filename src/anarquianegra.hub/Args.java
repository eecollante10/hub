package anarquianegra.hub;

import com.beust.jcommander.Parameter;

public class Args {
    @Parameter(names =  "-stars" , description = "Minimum number of stars")
    public Integer stars;

    @Parameter(names = "-topic", description = "Search repository topics")
    public String topic;

    @Parameter(names = "-name", description = "Search repository names")
    public String name;
    
    @Parameter(names = { "-l", "-language"}, description = "Search repositories with given programming language")
    public String language;
   
    @Parameter(names = { "-r", "-results"}, description = "Set result amount limit")
    public Integer limit;
   
    @Parameter(names = { "-s", "-sort" }, description = "Set sort field. One of stars, forks, or updated")
    public String sort;
}
