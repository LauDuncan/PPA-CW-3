import java.util.ArrayList;
import java.util.Random; 
/**
 * Write a description of class Weather here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public abstract class Weather
{   
    // The probability that there will be a weather event triggered.
    

    /**
     * Constructor for objects of class Weather
     */
    public Weather()
    {
        //
    }
    
    abstract public int getGrowthEffect();
    
    abstract public boolean getMovementRestriction();
    
    abstract public boolean getHuntRestriction();
}
