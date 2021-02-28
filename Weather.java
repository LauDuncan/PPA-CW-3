import java.util.ArrayList;
import java.util.Random; 
/**
 * Write a description of class Weather here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Weather
{   
    // The probability that there will be a weather event triggered.
    private final double WEATHER_TRIGGER_PROBABILITY = 0.2;
    
    private static ArrayList<Weather> weatherList;
    private boolean trigger;
    

    /**
     * Constructor for objects of class Weather
     */
    public Weather()
    {
        Random rand = new Random();
        weatherList = new ArrayList<>();
    }

    /**
     * 
     */
    public void initialize()
    {
        Rain rainWeather = new Rain();
        Snow snowWeather = new Snow();
        weatherList.add(rainWeather);
        weatherList.add(snowWeather);
    }
    
    public ArrayList<Weather> getWeatherList()
    {
        return weatherList;
    }
    
    public Weather getRandomWeather()
    {
        Random rand = new Random();
        
        //System.out.println("List size: " + weatherList.size());
        //System.out.println("Rand int: " + rand.nextInt(weatherList.size()));
        
        return weatherList.get(rand.nextInt(weatherList.size()));
    }
    
    public void resetTrigger()
    {
        Random rand = new Random();
        
        if (rand.nextDouble() <= WEATHER_TRIGGER_PROBABILITY){
            trigger = true;
        }
        else{
            trigger = false;
        }
    }
    
    public int getGrowthEffect()
    {
        return 0; // this method will be overriden by subclasses
    }
    
    public void getAnimalEffect()
    {
        // Method to be overriden by subclasses
    }
    
    public boolean getTrigger()
    {
        return trigger;
    }
}
