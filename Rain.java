
/**
 * Write a description of class Rain here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Rain extends Weather
{
    // The effect that will have on the plant's growth rate.
    private final int GROWTH_EFFECT = 2;
    // Whether this weather will have an effect on the animal's movement.
    private final boolean MOVEMENT_RESTRICTION = false;
    // Whether this weather will have an effect on the animal's food finding.
    private final boolean HUNT_RESTRICTION = false;
    

    /**
     * Constructor for objects of class Rain
     */
    public Rain()
    {
        //
    }

    /**
     * 
     */
    public int getGrowthEffect()
    {
        return GROWTH_EFFECT; // Increments the growth of the plant by 2
    }
    
    public boolean getMovementRestriction()
    {
        return MOVEMENT_RESTRICTION;
    }
    
    public boolean getHuntRestriction()
    {
        return HUNT_RESTRICTION;   
    }
}
