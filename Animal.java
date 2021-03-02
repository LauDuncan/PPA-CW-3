import java.util.List;
import java.util.Random; 

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling (modified by Liu Jie Xi and Lau Ying Hei)
 * @version 2016.02.29
 */
public abstract class Animal
{
    // Indicates the animal's health and gender.
    private boolean alive, isMale;

    // The animal's field.
    private Field field;

    // The animal's position in the field.
    private Location location;

    // Indicates whether the animal has a disease.
    private Boolean hasDisease = null;
    private double diseaseProbability;

    // The animal's food level, which is increased by eating another animal.
    private int foodLevel;
    /**
     * Create a new animal at location in field.
     * The gender of the animal will be randomly assigned.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
        Random rd = new Random();
        isMale = rd.nextBoolean();
    }

    /**
     * Check the adjacent cells of the current animal. 
     * The animal will be able to meet and breed if the current animal's neighbor 
     * is of the same species and opposite gender.
     * 
     * @return true if the animal can meet are of opposite genders.
     */
    public boolean canMeet()
    {
        List<Location> neighbours = field.adjacentLocations(this.getLocation());
        for(Location neighbour : neighbours){ //load each neighbour's location
            if(neighbour != null){ //check the grid exist or not
                Object object = field.getObjectAt(neighbour);
                if(object != null && object.getClass().equals(this.getClass()) && ((Animal)object).isMale() != this.isMale()){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Make this animal act, their actions will alter according to the time and weather.
     * 
     * @param newAnimals A list to receive newly born animals.
     * @param isDay A boolean to indicate whether it is daytime
     * @param weather A Weather object that will influence the animal's behaviour
     */
    abstract public void act(List<Animal> newAnimals, boolean isDay, Weather weather);

    /**
     * Check whether the animal is alive or not.
     * 
     * @return true if the animal is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is then removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's location.
     * 
     * @return The animal's location.
     */
    protected Location getLocation()
    {
        return location;
    }

    /**
     * Return the boolean value of whether the animal is a male.
     * 
     * @return true if the animal is a male.
     */
    protected boolean isMale()
    {
        return this.isMale;
    }

    /**
     * Makes the animal more hungry, they will die if their food level is below 0.
     * When a animal contracted a disease, their food level will be further decremented.
     */
    protected void incrementHunger()
    {
        if(hasDisease() != null && hasDisease() == true){
            foodLevel = foodLevel - 2; 
        }
        else{
            foodLevel--;
        }

        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Return whether the animal has a disease
     * 
     * @return true if the animal has a disease
     */
    protected Boolean hasDisease()
    {
        return this.hasDisease;
    }

    /**
     * Sets the animal to have a disease
     */
    protected void setHasDisease(Boolean hasDisease)
    {
        this.hasDisease = hasDisease;
    }

    /**
     * Simulate the probability of a animal contracting the disease, and the act of 
     * spreading the disease around a diseased animal
     */
    protected void simulateDisease()
    {        
        if(this.hasDisease == null){
            Random rd = new Random();
            this.hasDisease = rd.nextDouble() < getDiseaseProbability();
        }

        if(this.hasDisease){
            List<Location> neighbours = field.adjacentLocations(this.getLocation());        
            for(Location neighbour : neighbours){ //load each neighbour's location
                if(neighbour != null){ //check the grid exist or not
                    Object object = field.getObjectAt(neighbour);
                    if(object != null && object instanceof Animal){
                        ((Animal)object).setHasDisease(true);
                    }
                }
            }
        }
    }

    /**
     * Place the animal at the new location in the given field.
     * 
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Return the animal's field.
     * 
     * @return The animal's field.
     */
    protected Field getField()
    {
        return field;
    }

    /**
     * Returns the probability of contracting the disease
     * 
     * @return The possibility of a animal contracting a disease.
     */
    protected double getDiseaseProbability()
    {
        return diseaseProbability;
    }

    /**
     * Sets the probability of the animal catching the disease
     */
    protected void setDiseaseProbability(double diseaseProbability)
    {
        this.diseaseProbability = diseaseProbability;
    }

    /**
     * Returns the current food level of the animal
     * 
     * @return The food level of the animal
     */
    protected int getFoodLevel()
    {
        return foodLevel;
    }

    /**
     * Sets the food level of the animal
     * 
     * @param foodLevel the food level to be set
     */
    protected void setFoodLevel(int foodLevel)
    {
        this.foodLevel = foodLevel;
    }
}
