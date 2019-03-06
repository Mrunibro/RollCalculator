package ROTMGRoll;

/**
 * ADT for Roll.Roll. A roll is either just itself, or a composite
 * A composite can hold composites or itself.
 */
public abstract class AbstractRoll {
    public abstract double getPct();
    public abstract double getOneInX();
    public abstract double getWorsePct();
    public abstract double getWorseOneInX();
    public abstract double getBetterPct();
    public abstract double getBetterOneInX();
    public abstract boolean isBadRoll();
}
