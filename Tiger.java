
import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a tiger.
 * Tigers age, move, eat lambs, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Tiger extends Animal
{
    // Characteristics shared by all tigers (class variables).
    
    // The age at which a tiger can start to breed.
    private static final int BREEDING_AGE = 3;
    // The age to which a tiger can live.
    private static final int MAX_AGE = 15;
    // The likelihood of a tiger breeding.
    private static final double BREEDING_PROBABILITY = 0.15;
    //The likelihood of a tiger disease
    private static final double DISEASE_PROBABILITY = 0.04;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of a single lamb. In effect, this is the
    // number of steps a tiger can go before it has to eat again.
    private static final int LAMB_FOOD_VALUE = 9;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // The tiger's age.
    private int age;

    /**
     * Create a tiger. A tiger can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the tiger will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Tiger(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            setFoodLevel(rand.nextInt(LAMB_FOOD_VALUE));
        }
        else {
            age = 0;
            setFoodLevel(LAMB_FOOD_VALUE);
        }
        setDiseaseProbability(DISEASE_PROBABILITY);
    }
    
    /**
     * This is what the tiger does most of the time: it hunts for
     * lambs. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newTigers A list to return newly born tigers.
     */
    public void act(List<Animal> newTigers, boolean isDay, Weather weather)
    {
        if(isDay){
            simulateDisease();
            incrementAge();
            incrementHunger();
            if(isAlive()) {
                giveBirth(newTigers);            
                routine(weather);
            }
        }
        else{
            // Animal sleeps
            incrementHunger();
        }
    }

    private void routine(Weather weather)
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
     * Increase the age. This could result in the tiger's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    

    /**
     * Look for lambs adjacent to the current location.
     * Only the first live lamb is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Lamb) {
                Lamb lamb = (Lamb) animal;
                if(lamb.isAlive()) { 
                    lamb.setDead();
                    setFoodLevel(getFoodLevel() + LAMB_FOOD_VALUE) ;
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this tiger is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newTigers A list to return newly born tigers.
     */
    private void giveBirth(List<Animal> newTigers)
    {
        // New tigers are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Tiger young = new Tiger(false, field, loc);
            newTigers.add(young);
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
     * A tiger can breed if it has reached the breeding age.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
