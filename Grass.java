import java.util.Random;
/**
 * Grass is a subclass of Plant.
 * Grass can grow and be consumed, the growth rate of the grass 
 * will be influenced by the weather conditions of the field.
 *
 * @author Liu Jie Xi and Lau Ying Hei
 * @version 2021.02.20
 */
public class Grass extends Plant
{
    // The growth of the plant represented as an integer.
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
    
    /**
     * Return the current growth value of the plant
     * 
     * @return The growth value of the plant
     */
    public int getGrowth()
    {
        return growth;
    }

    /**
     * Simulates the growth of the plant, which can be affected by the time of day and weather.
     * The plant will grow when there is sunlight, but this is not guaranteed due to the type of weather.
     * 
     * @param isDay A boolean indicating whether it is day time
     * @param weather A weather object that will vary the plant's growth
     */
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
     * Indicates that the plant just got eaten.
     * The growth of the plant will reset.
     */
    protected void reset()
    {
        growth = 0;
    }

    /**
     * Check whether the plant is edible, judging by its growth.
     * 
     * @return true if the plant is edible by animals.
     */
    protected boolean isEdible()
    {
        return growth > 2;
    }

    /**
     * Returns the amount of food level that will be incremented after the animal consumes this plant.
     * 
     * @return A int that will represent the food value of this plant.
     */
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
