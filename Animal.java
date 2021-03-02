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
    private int breedingAge;
    private int maxAge;
    private boolean nightActivity;

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
     * Make this animal act, their actions will alter according to the time and weather.
     * 
     * @param newAnimals A list to receive newly born animals.
     * @param isDay A boolean to indicate whether it is daytime
     * @param weather A Weather object that will influence the animal's behaviour
     */

    public void act(List<Animal> newAnimals, boolean isDay, Weather weather)
    {
        if(checkActivity(isDay)){
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
     * Determines whether the animal will act given the time of day and the animal's night activity.
     * 
     * @param isDay A boolean indicating whether it is daytime
     * @return true if the the value of isDay and night activity are different.
     */
    protected boolean checkActivity(boolean isDay){
        return isDay ^ getNightActivity(); // exclusive or operator
    }

    /**
     * This is the movement routine of the animal, it will be able to hunt or move freely 
     * if there are no restrctions posed by the weather.
     * 
     * If there's no space for the animal to move, then the animal will die of overcrowding.
     * 
     * @param weather A Weather object that will change the animal's movement
     */
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
    
    /**
     * Look for food sources adjacent to the current location.
     * Only the food source is consumed by the animal.
     * 
     * @return Where food was found, or null if it wasn't.
     */
    abstract protected Location findFood();
    
    /**
     * Check whether or not this animal is able to give birth at this step.
     * New births will be made into free adjacent locations.
     * 
     * @param newAnimal A list to return newly born animals.
     */
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
     * 
     * @param hasDisease A boolean indicating whether the animal has contracted a disease
     */
    protected void setHasDisease(Boolean hasDisease)
    {
        this.hasDisease = hasDisease;
    }

    /**
     * Simulates the probability of a animal contracting the disease, and the act of 
     * spreading the disease around the diseased animal
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
     * 
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
     * An animal can breed if it has reached the breeding age.
     * 
     * @return true if the animal is old enough to breed
     */
    protected boolean canBreed()
    {
        return age >= breedingAge;
    }


    /**
     * Increase the age. This could result in the animal's death.
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
     * @return The probability of the animal contracting a disease.
     */
    protected double getDiseaseProbability()
    {
        return diseaseProbability;
    }

    /**
     * Sets the probability of the animal contracting a disease
     * 
     * @param diseaseProbability The probability of the animal contracting a disease
     */
    protected void setDiseaseProbability(double diseaseProbability)
    {
        this.diseaseProbability = diseaseProbability;
    }

    /**
     * Returns the breeding probability of the animal
     * 
     * @return The probability of the animal breeding
     */
    protected double getBreedingProbability()
    {
        return breedingProbability;
    }
    
    /**
     * Sets the probability of the animal breeding
     * 
     * @param breedingProbability The probability of the animal breeding
     */
    protected void setBreedingProbability(double breedingProbability)
    {
        this.breedingProbability = breedingProbability;
    }  
    
    /**
     * Returns the maximum number of births that the animal can have
     * 
     * @return The maximum number of births the animal can have in a step
     */
    protected int getMaxLitterSize()
    {
        return maxLitterSize;
    }
    
    /**
     * Sets the maximum number of births that the animal can have in one step
     * 
     * @param maxLitterSize The maximum number of births that an animal can have in a single step
     */
    protected void setMaxLitterSize(int maxLitterSize)
    {
        this.maxLitterSize = maxLitterSize;
    }
    
    /**
     * Returns the age of the animal
     * 
     * @return The age of the animal
     */
    protected int getAge()
    {
        return age;
    }
    
    /**
     * Sets the age of the animal
     * 
     * @param age The age of the animal
     */
    protected void setAge(int age)
    {
        this.age = age;
    }
    
    /**
     * Returns the maximum age of the animal
     * 
     * @return The maximum age of the animal
     */
    protected int getMaxAge()
    {
        return maxAge;
    }
    
    /**
     * Sets the maximum age of the animal
     * 
     * @param maxAge The maximum age of the animal
     */
    protected void setMaxAge(int maxAge)
    {
        this.maxAge = maxAge;
    }
    
    /**
     * Returns the breeding age of the animal
     * 
     * @return The breeding age of the animal
     */
    protected int getBreedingAge()
    {
        return breedingAge;
    }
    
    /**
     * Sets the age of the animal that they have to reach before they can start breeding
     */
    protected void setBreedingAge(int breedingAge)
    {
        this.breedingAge = breedingAge;
    }

    /**
     * Returns the breeding age of the animal
     * 
     * @return The breeding age of the animal
     */
    protected boolean getNightActivity()
    {
        return nightActivity;
    }
    
    /**
     * Sets the age of the animal that they have to reach before they can start breeding
     */
    protected void setNightActivity(boolean nightActivity)
    {
        this.nightActivity = nightActivity;
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
