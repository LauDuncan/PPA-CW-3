import java.util.List;
import java.util.Random;

/**
 * A simple model of a cow.
 * Cows age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Cow extends Animal
{
    // Characteristics shared by all cows (class variables).

    // The age at which a cow can start to breed.
    private static final int BREEDING_AGE = 2;
    // The age to which a cow can live.
    private static final int MAX_AGE = 22;
    // The likelihood of a cow breeding.
    private static final double BREEDING_PROBABILITY = 0.12;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 1;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    
    // The cow's age.
    private int age;

    /**
     * Create a new cow. A cow may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the cow will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Cow(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }
    
    /**
     * This is what the cow does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newCows A list to return newly born cows.
     */
    public void act(List<Animal> newCows, boolean isDay)
    {
        if(isDay){
            incrementAge();
            if(isAlive()) {
                giveBirth(newCows);            
                // Try to move into a free location.
                Location newLocation = getField().freeAdjacentLocation(getLocation());
                if(newLocation != null) {
                    setLocation(newLocation);
                }
                else {
                    // Overcrowding.
                    setDead();
                }
            }
        }
        else{
            // Animal sleeps
        }
    }

    /**
     * Increase the age.
     * This could result in the cow's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this cow is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newCows A list to return newly born cows.
     */
    private void giveBirth(List<Animal> newCows)
    {
        // New cows are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Cow young = new Cow(false, field, loc);
            newCows.add(young);
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(rand.nextDouble() <= BREEDING_PROBABILITY && canBreed() && canMeet()) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A cow can breed if it has reached the breeding age.
     * @return true if the cow can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
