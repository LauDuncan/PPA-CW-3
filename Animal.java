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
    private static final Random RANDOM = new Random();
    // Indicates the animal's health and gender.
    private boolean alive, isMale;

    // The animal's field.
    private Field field;

    // The animal's position in the field.
    private Location location;

    // Indicates whether the animal has a disease.
    private Boolean hasDisease = null;
    private double diseaseProbability;

    private double breedingProbability;
    private int maxLitterSize;
    private double breedingAge;
    private int maxAge;

    // The animal's food level, which is increased by eating another animal.
    private int foodLevel;
    // The animal's age.
    private int age;
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
        isMale = RANDOM.nextBoolean();
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

    public void act(List<Animal> newAnimals, boolean isDay, Weather weather)
    {
        if(isDay){
            simulateDisease();
            incrementAge();
            incrementHunger();
            if(isAlive()) {                
                giveBirth(newAnimals);            
                routine(weather);
            }
        }
        else{
            // Animal sleeps
            incrementHunger();
        }
    }

    protected void routine(Weather weather)
    {      
        Location newLocation = null;
        // Checks whether the weather has a effect on the animal's food gathering behaviour.
        if(weather != null){
            if(! weather.getHuntRestriction()){
                newLocation = findFood();
            }
            else if(! weather.getMovementRestriction()){
                newLocation = getLocation();
            }
        }
        
        if(newLocation == null) { 
            // No food found - try to move to a free location.
            newLocation = getField().freeAdjacentLocation(getLocation());
        }

        // See if it was possible to move.
        if(newLocation != null) {
            setLocation(newLocation);
        }
        else {
            // Overcrowding.
            setDead();
        }
    }
    
    abstract protected Location findFood();
    
    abstract protected void giveBirth(List<Animal> newAnimal);
    
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
        if (!isAlive())
        {
            return;
        }
        
        if(this.hasDisease() == null){
            setHasDisease(RANDOM.nextDouble() < getDiseaseProbability());
        }
        
        if(this.hasDisease  ){

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
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    protected int breed()
    {
        int births = 0;
        if(RANDOM.nextDouble() <= breedingProbability && canBreed() && canMeet()) {
            births = RANDOM.nextInt(maxLitterSize) + 1;
        }
        return births;
    }

    /**
     * A lion can breed if it has reached the breeding age.
     */
    protected boolean canBreed()
    {
        return age >= breedingAge;
    }


    /**
     * Increase the age. This could result in the lion's death.
     */
    protected void incrementAge()
    {
        setAge(getAge()+1);
        if(getAge() > maxAge) {
            setDead();
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
     * Get the breeding probability
     */
    protected double getBreedingProbability()
    {
        return breedingProbability;
    }
    
    /**
     * Sets the probability of the animal breeding
     */
    protected void setBreedingProbability(double breedingProbability)
    {
        this.breedingProbability =breedingProbability;
    }  
    
    /**
     * Get the maximum number of births
     */
    protected int getMaxLitterSize()
    {
        return maxLitterSize;
    }
    
    /**
     * Sets the maximum number of births
     */
    protected void setMaxLitterSize(int maxLitterSize)
    {
        this.maxLitterSize = maxLitterSize;
    }
    
    /**
     * Get the age
     */
    protected int getAge()
    {
        return age;
    }
    
    /**
     * Sets the age
     */
    protected void setAge(int age)
    {
        this.age = age;
    }
    
    /**
     * Get the maximum age
     */
    protected int getMaxAge()
    {
        return maxAge;
    }
    
    /**
     * Sets the maximum age
     */
    protected void setMaxAge(int maxAge)
    {
        this.maxAge = maxAge;
    }
    
    /**
     * Get the breeding probability
     */
    protected double getBreedingAge()
    {
        return breedingAge;
    }
    
    /**
     * Sets the probability of the animal breeding
     */
    protected void setBreedingAge(double breedingAge)
    {
        this.breedingAge = breedingAge;
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
