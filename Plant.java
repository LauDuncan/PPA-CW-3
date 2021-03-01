import java.util.List;
import java.util.Random;
/**
 * Write a description of class Plants here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public abstract class Plant
{
    // The plant's position in the field.
    private Location location;
    
    /**
     * Create a new plant at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Plant(Location location)
    {
        //
    }
    
    /**
     * Make this plant grow
     * @param isDay A boolean indicating whether it is daytime.
     */
    abstract public void grow(boolean isDay, Weather weather);

    /**
     * Return the plant's location.
     * @return The plant's location.
     */
    protected Location getLocation()
    {
        return location;
    }
}

