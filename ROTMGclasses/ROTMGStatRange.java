package ROTMGclasses;
/**
 * Represents a stat range of possible rolls
 * This class is not concerned with what kind of stat it represents
 * (RotmgClass does this)
 *
 * The stat range is preserved,
 * While also offering a way to get the amount of possible rolls (NSides)
 * for use in Roll.RollCalculator
 */
public class ROTMGStatRange {

    private int min;
    private int max;

    ROTMGStatRange(int min, int max){
        this.min = min;
        this.max = max;
    }

    public int getNSides() {
        return max - min;
    }

    public int getMax(){return max;}

    public int getMin(){return min;}

}
