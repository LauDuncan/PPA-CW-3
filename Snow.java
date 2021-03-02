
/**
 * A simple model of the rain weather.
 * Rain will cause plants to have an increased growth rate.
 * Rain will not cause any movement and hunt restrictions on animals.
 *
 * @author Liu Jie Xi and Lau Ying Hei
 * @version 2021.02.20
 */
public class Snow extends Weather
{
    // The effect that will have on the plant's growth rate.
    private final int GROWTH_EFFECT = 0;
    // Whether this weather will have an effect on the animal's movement.
    private final boolean MOVEMENT_RESTRICTION = true;
    // Whether this weather will have an effect on the animal's food finding.
    private final boolean HUNT_RESTRICTION = false;
    

    /**
     * Constructor for objects of class Rain
     */
    public Snow()
    {
        //
    }

    /**
     * Returns a integer to increment the growth value of the plant by.
     * 
     * @return A int that will indicate the growth of the plant
     */
    public int getGrowthEffect()
    {
        return GROWTH_EFFECT;
    }
    
    /**
     * Returns a boolean to indicate whether rain can restrict the animal from moving
     * 
     * @return true, since snow will cause movement restriction
     */
    public boolean getMovementRestriction()
    {
        return MOVEMENT_RESTRICTION;
    }
    
    /**
     * Returns a boolean to indicate whether rain can restrict the animal from hunting
     * 
     * @return false, since snow will not cause hunting restriction
     */
    public boolean getHuntRestriction()
    {
        return HUNT_RESTRICTION;   
    }
}
