import java.util.List;
import java.util.Random;
/**
 * A simple model of a plant.
 * It can either grow or be consumed.
 *
 * @author Liu Jie Xi and Lau Ying Hei
 * @version 2021.02.20
 */
public abstract class Plant {
    // The plant's position in the field.
    private Location location;

    /**
     * Create a new plant at location in field.
     * 
     * @param location The location of the plant within the field.
     */
    public Plant(Location location) {
        this.location = location;
    }

    /**
     * Simulates the plant's growth
     * 
     * @param isDay A boolean indicating whether it is daytime.
     * @param weather A weather object that will alter the plant's growth
     */
    abstract public void grow(boolean isDay, Weather weather);

    /**
     * Return the plant's location.
     * 
     * @return The plant's location.
     */
    protected Location getLocation() {
        return location;
    }
}