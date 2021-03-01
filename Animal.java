import java.util.List;
import java.util.Random; 

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Animal
{
    // Whether the animal is alive or not.
    private boolean alive, isMale;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    
    //indicate the animal has disease
    private Boolean hasDisease = null;
    private double diseaseProbability;
    
    // The animal's food level, which is increased by eating another animal.
    private int foodLevel;
    /**
     * Create a new animal at location in field.
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
     * Check the adjacent cells of the current animal and the animal will be able to meet and breed
     * if the current animal's neighbor is of the same species and opposite gender.
     * 
     * @return true if the animal can meet and breed.
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
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     * @param isDay A boolean to indicate whether it is daytime
     */
    abstract public void act(List<Animal> newAnimals, boolean isDay, Weather weather);

    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
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
     * @return The animal's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Return the boolean value of whether the animal is a male.
     * @return true if the animal is a male.
     */
    protected boolean isMale()
    {
        return this.isMale;
    }
    
    /**
     * Make this lion more hungry. This could result in the lion's death.
     */
    protected void incrementHunger()
    {
        if(hasDisease() != null && hasDisease() == true){
            foodLevel = foodLevel -2; 
        }
        else{
            foodLevel--;
        }
        
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Return if the animal has disease
     */
    protected Boolean hasDisease()
    {
        return this.hasDisease;
    }
    
    protected void setHasDisease(Boolean hasDisease)
    {
        this.hasDisease = hasDisease;
    }
    
    /**
     * Simulate disease for any animal
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
     * @return The animal's field.
     */
    protected Field getField()
    {
        return field;
    }
    
    /**
     * Get the disease probability
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
