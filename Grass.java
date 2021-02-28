import java.util.Random;
/**
 * Write a description of class Grass here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Grass extends Plant
{
    // The growth of the plant.
    private int growth;
    /**
     * Constructor for objects of class Grass
     */
    public Grass(Location location)
    {
        super(location);
        Random rd = new Random();
        growth = rd.nextInt(5);
    }

    public int getGrowth()
    {
        return growth;
    }

    public void grow(boolean isDay, Weather weather)
    {
        if(isDay && weather == null){
            growth += 1; //normal growth of the plant under sunlight
        }
        
        else if(weather != null){
            growth += weather.getGrowthEffect();
        }
    }

    /**
     * Indicate that the plant just got eaten.
     * The growth of the plant has been resetted.
     */
    protected void reset()
    {
        growth = 0;
    }

    /**
     * Check whether the plant is edible.
     * @return true if the plant is edible.
     */
    protected boolean isEdible()
    {
        return growth > 2;
    }

    protected int consume()
    {
        if (growth > 3){
            return 6;
        }
        else{
            return 3;
        }
    }
}
