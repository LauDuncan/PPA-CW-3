import java.util.List;
import java.util.Random;
import java.util.Iterator;

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
    private static final int MAX_AGE = 10;
    // The likelihood of a cow breeding.
    private static final double BREEDING_PROBABILITY = 0.3;
    //The likelihood of a cow disease
    private static final double DISEASE_PROBABILITY = 0.06;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // number of steps a cow can go before it has to eat again (5 days).
    private static final int MAX_ACTIVITY_LEVEL = 10;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

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
    }

    /**
     * Look for grass cells adjacent to the current location.
     * Only one grass cell is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Plant plant = field.getPlants().get(where);
            if(plant instanceof Grass) {
                Grass grass = (Grass) plant;
                if(grass.isEdible()) { 
                    //System.out.println("Before:\n Growth: "+ grass.getGrowth() + "  Food Level:" + foodLevel);
                    setFoodLevel(getFoodLevel() + grass.consume()) ;
                    grass.reset();
                    //System.out.println("After:\n Growth: "+ grass.getGrowth() + "   Cow Level:" + foodLevel);
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * Check whether or not this cow is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newCows A list to return newly born cows.
     */
    protected void giveBirth(List<Animal> newCows)
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


}
