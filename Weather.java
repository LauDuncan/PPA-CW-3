import java.util.ArrayList;
import java.util.Random;
/**
 * A class representing shared characteristics of weather.
 *
 * @author Liu Jie Xi and Lau Ying Hei
 * @version 2021.02.20
 */
public abstract class Weather {
    // The probability that there will be a weather event triggered.

    /**
     * Constructor for objects of class Weather
     */
    public Weather() {}

    abstract public int getGrowthEffect();

    abstract public boolean getMovementRestriction();

    abstract public boolean getHuntRestriction();
}