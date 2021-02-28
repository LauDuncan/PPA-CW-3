import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing lions, wolves, tigers, lambs, and cows.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;

    private static final double GRASS_CREATION_PROBABILITY = 0.5;
    // The probability that a lion will be created in any given grid position.
    private static final double LION_CREATION_PROBABILITY = 0.01;
    // The probability that a tiger will be created in any given grid position.
    private static final double TIGER_CREATION_PROBABILITY = 0.015;
    // The probability that a wolf will be created in any given grid position.
    private static final double WOLF_CREATION_PROBABILITY = 0.03;
    // The probability that a cow will be created in any given grid position.
    private static final double COW_CREATION_PROBABILITY = 0.1;    
    // The probability that a lamb will be created in any given grid position.
    private static final double LAMB_CREATION_PROBABILITY = 0.15;    

    // The probability that there will be a weather event triggered.
    private static final double WEATHER_TRIGGER_PROBABILITY = 0.25;

    // List of animals in the field.
    private List<Animal> animals;
    // List of weather possible of triggering
    private List<Weather> weatherList;
    // The current state of the field.
    private Field field;

    // The current step of the simulation.
    private int step;
    //The current time of the simulation.
    private boolean isDay;

    // A graphical view of the simulation.
    private SimulatorView view;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        animals = new ArrayList<>();
        weatherList = new ArrayList<>();
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Lion.class, Color.YELLOW);
        view.setColor(Tiger.class, Color.ORANGE);
        view.setColor(Wolf.class, Color.DARK_GRAY);
        view.setColor(Lamb.class, Color.GREEN);
        view.setColor(Cow.class, Color.LIGHT_GRAY);
        // Setup a valid starting point.
        reset();
    }

    /**
     * Run the simulation from its current state for a reasonably long period.
     */
    public void runLongSimulation()
    {
        simulate(500);
    }

    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
            delay(60);   // uncomment this to run more slowly
        }
    }

    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * animal.
     */
    public void simulateOneStep()
    {
        Random rand = new Random();
        String timeOutput = "";
        step++;

        // Changing the time of day according to the number of steps
        if(step % 2 == 0){
            isDay = false;
            timeOutput = " Night";
        }
        else{
            isDay = true;
            timeOutput = " Day";
        }

        // Randomizes the probability of a weather event
        Weather currentWeather = null;
        if (rand.nextDouble() <= WEATHER_TRIGGER_PROBABILITY){
            currentWeather = weatherList.get(rand.nextInt(weatherList.size()));;
            System.out.println(currentWeather.getClass());
        }

        for(Plant plant : field.getPlants().values()){
            plant.grow(isDay, currentWeather);
        }


        // Provide space for newborn animals.
        List<Animal> newAnimals = new ArrayList<>();  
        
        // Let all animals act.
        for(Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
            Animal animal = it.next();
            animal.act(newAnimals, isDay, currentWeather);
            if(! animal.isAlive()) {
                it.remove();
            }
        }

        // Add the newly born animals to the main lists.
        animals.addAll(newAnimals);

        view.showStatus(step, timeOutput, field);
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 1;
        isDay = true;
        initializeWeather();
        animals.clear();
        populate();

        // Show the starting state in the view.
        view.showStatus(step, " Day", field);
    }

    /**
     * Randomly populate the field with lambs,tigers, wolves, cows, and lambs.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Location location = new Location(row, col);
                Grass grass = new Grass(location);
                field.getPlants().put(location, grass);

                if(rand.nextDouble() <= LION_CREATION_PROBABILITY) {
                    Lion lion = new Lion(true, field, location);
                    animals.add(lion);
                }
                else if(rand.nextDouble() <= TIGER_CREATION_PROBABILITY) {
                    Tiger tiger = new Tiger(true, field, location);
                    animals.add(tiger);
                }
                else if(rand.nextDouble() <= WOLF_CREATION_PROBABILITY) {
                    Wolf wolf = new Wolf(true, field, location);
                    animals.add(wolf);
                }
                else if(rand.nextDouble() <=  COW_CREATION_PROBABILITY) {
                    Cow cow = new Cow(true, field, location);
                    animals.add(cow);
                }
                else if(rand.nextDouble() <= LAMB_CREATION_PROBABILITY) {
                    Lamb lamb = new Lamb(true, field, location);
                    animals.add(lamb);
                }
                // else leave the location empty.
            }
        }
    }
    
    private void initializeWeather()
    {
        Rain rain = new Rain();
        Snow snow = new Snow();
        weatherList.add(rain);
        weatherList.add(snow);
    }

    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
}
