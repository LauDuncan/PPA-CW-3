
/**
 * Write a description of class Fog here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Fog extends Weather
{
    // The effect that will have on the plant's growth rate.
    private final int GROWTH_EFFECT = 1;
    // Whether this weather will have an effect on the animal's movement.
    private final boolean MOVEMENT_RESTRICTION = false;
    // Whether this weather will have an effect on the animal's food finding.
    private final boolean HUNT_RESTRICTION = true;
    

    /**
     * Constructor for objects of class Rain
     */
    public Fog()
    {
        //
    }

    /**
     * 
     */
    public int getGrowthEffect()
    {
        return GROWTH_EFFECT;
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
