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
    private static final int BREEDING_AGE = 2;
    // The age to which a lion can live.
    private static final int MAX_AGE = 18;
    // The likelihood of a lion breeding.

    private static final double BREEDING_PROBABILITY = 0.18;
    //The likelihood of a lion disease

    private static final double DISEASE_PROBABILITY = 0.08;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // The number of steps a lion can go before it has to eat again.
    private static final int MAX_ACTIVITY_LEVEL = 20;
    //Whether the animal will act during the night.
    private static final boolean NIGHT_ACTIVITY = false;
    
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

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
            setAge(rand.nextInt(MAX_AGE));
            setFoodLevel(rand.nextInt(MAX_ACTIVITY_LEVEL));
        }
        else {
            setAge(0);
            setFoodLevel(MAX_ACTIVITY_LEVEL);
        }
        setDiseaseProbability(DISEASE_PROBABILITY);
        setBreedingProbability(BREEDING_PROBABILITY);
        setMaxLitterSize(MAX_LITTER_SIZE);
        setBreedingAge(BREEDING_AGE);
        setMaxAge(MAX_AGE);
        setNightActivity(NIGHT_ACTIVITY);
    }

    /**
     * Look for lambs adjacent to the current location.
     * Only the first live lamb is eaten by the lion.
     * 
     * @return Where food was found, or null if it wasn't.
     */
    @Override
    protected Location findFood()
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
    @Override
    protected void giveBirth(List<Animal> newLions)
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
}
