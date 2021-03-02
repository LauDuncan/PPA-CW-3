import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a lion.
 * Lions age, move, eat lambs, and die.
 * 
 * @author Liu Jie Xi and Lau Ying Hei
 * @version 2021.02.20
 */
public class Lion extends Animal
{
    // Characteristics shared by all lions (class variables).

    // The age at which a lion can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a lion can live.
    private static final int MAX_AGE = 20;
    // The likelihood of a lion breeding.
    private static final double BREEDING_PROBABILITY = 0.15;
    // The likelihood of a lion contracting a disease
    private static final double DISEASE_PROBABILITY = 0.03;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The number of steps a lion can go before it has to eat again.
    private static final int MAX_ACTIVITY_LEVEL = 9;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    // Individual characteristics (instance fields).
    // The lion's age.
    private int age;
    
    /**
     * Create a lion. A lion can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the lion will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Lion(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            setFoodLevel(rand.nextInt(MAX_ACTIVITY_LEVEL));
        }
        else {
            age = 0;
            setFoodLevel(MAX_ACTIVITY_LEVEL);
        }
        setDiseaseProbability(DISEASE_PROBABILITY);
    }

    /**
     * This is what the lion does most of the time: it will age, breed, and hunt,
     * but this will also be altered by the weather of the field.
     * 
     * The lion will hunt during the day and sleep during the night.
     * 
     * @param newLions A list to return newly born lions.
     * @param isDay A boolean to indicate whether it is daytime.
     * @param weather A Weather object that will alter the lion's actions.
     */
    public void act(List<Animal> newLions, boolean isDay, Weather weather)
    {
        if(isDay){
            incrementAge();
            incrementHunger();
            if(isAlive()) {
                simulateDisease();
                giveBirth(newLions);            
                routine(weather);
            }
        }
        else{
            // Animal sleeps
            incrementHunger();
        }
    }

    /**
     * This is the movement routine of the lion, it will be able to hunt or move freely 
     * if there are no restrctions posed by the weather.
     * 
     * If there's no space for the lion to move, then the lion will die of overcrowding.
     * 
     * @param weather A Weather object that will change the lion's movement
     */
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
     * Increase the age. This could result in the lion's death.
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
     * Only the first live lamb is eaten by the lion.
     * 
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
                    setFoodLevel(getFoodLevel()+MAX_ACTIVITY_LEVEL);
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * Check whether or not this lion is to give birth at this step.
     * New births will be made into free adjacent locations.
     * 
     * @param newLions A list to return newly born lions.
     */
    private void giveBirth(List<Animal> newLions)
    {
        // New lions are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Lion young = new Lion(false, field, loc);
            newLions.add(young);
        }
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * 
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
     * A lion can breed if it has reached the breeding age.
     * 
     * @return true if the lion has reached breeding age.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
