import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a lamb.
 * Lambs age, move, breed, and die.
 * 
 * @author Liu Jie Xi and Lau Ying Hei
 * @version 2021.02.20
 */
public class Lamb extends Animal {
    // Characteristics shared by all lambs (class variables).

    // The age at which a lamb can start to breed.
    private static final int BREEDING_AGE = 3;
    // The age to which a lamb can live.
    private static final int MAX_AGE = 15;
    // The likelihood of a lamb breeding.
    private static final double BREEDING_PROBABILITY = 0.4;
    //The likelihood of a lamb disease
    private static final double DISEASE_PROBABILITY = 0.05;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // number of steps a lamb can go before it has to eat again (5 days).
    private static final int MAX_ACTIVITY_LEVEL = 10;
    //Whether the animal will act during the night.
    private static final boolean NIGHT_ACTIVITY = false;

    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    /**
     * Create a new lamb. A lamb may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the lamb will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Lamb(boolean randomAge, Field field, Location location) {
        super(field, location);
        if (randomAge) {
            setAge(rand.nextInt(MAX_AGE));
            setFoodLevel(rand.nextInt(MAX_ACTIVITY_LEVEL));
        } else {
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
     * Look for grass cells adjacent to the current location.
     * Only one grass cell is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    @Override
    protected Location findFood() {
        Field field = getField();
        List < Location > adjacent = field.adjacentLocations(getLocation());
        Iterator < Location > it = adjacent.iterator();
        while (it.hasNext()) {
            Location where = it.next();
            Plant plant = field.getPlants().get(where);
            if (plant instanceof Grass) {
                Grass grass = (Grass) plant;
                if (grass.isEdible()) {
                    setFoodLevel(getFoodLevel() + grass.consume());
                    grass.reset();
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * Check whether or not this lamb is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newLambs A list to return newly born lambs.
     */
    @Override
    protected void giveBirth(List < Animal > newLambs) {
        // New lambs are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List < Location > free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for (int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Lamb young = new Lamb(false, field, loc);
            newLambs.add(young);
        }
    }
}