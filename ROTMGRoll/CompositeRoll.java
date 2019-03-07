package ROTMGRoll;

import java.util.ArrayList;

/**
 * Data object for a composite roll
 * Used for holding multiple rolls
 *
 * Holds any relevant field related to the calculated roll
 * By way of multiplying probabilities of all held Rolls together
 *
 * Values such as 'betterPct' and 'worsePct' only consider <i>objectively</i> worse rolls.
 * That is, rolls for which every single field is worse/better than what it is compared against.
 */
public class CompositeRoll extends AbstractRoll {

    private ArrayList<AbstractRoll> rolls;

    /**
     * Note that an empty compositeRoll returns 1 for all params (false for isBadRoll).
     * Probability of getting nothing from empty set == 100%
     */
    public CompositeRoll(){
        rolls = new ArrayList<>();
    }

    public void addRoll(Roll r){
        rolls.add(r);
    }

    public void removeRoll(){
        rolls.remove(rolls.size()-1);
    }

    public void clearRolls(){
        rolls.clear();
    }

    public boolean isEmpty(){
        return rolls.isEmpty();
    }

    public double getPct() {
        double pct = 1;
        for (AbstractRoll roll : rolls){ //pct is represented as a percentage, div by 100 to get probability value
            pct *= (roll.getPct() / 100.0);
        }
        return pct * 100;
    }

    public double getOneInX() {
        double oneInX = 1;
        for (AbstractRoll roll : rolls){
            oneInX *= roll.getOneInX();
        }
        return oneInX;
    }

    /*
    A roll can have a value of 0 for some fields (e.g. 0% chance to do better than max roll),
    resulting in a returned 0, even if a composite can bee better
    (e.g. max hp roll but min mp roll. A better roll exists (max hp + any roll that is not min mp)
    but the stat does not represent this.

    Solution: A better roll is equal in the 'best' field but better in others.
    This is done by returning pct rather than worse/betterPct for a value where worse/better is 0.

    This also means one needs to account for a roll where all fields are 0,
    in which case the worse/better pct is indeed 0.

    I can smell the spaghetti from here. Reader, I am sorry for doing this.
     */
    public double getWorsePct() {
        double worsePct = 1;
        boolean allZero = true;
        for (AbstractRoll roll : rolls){
            double intermediate = (roll.getWorsePct() / 100.0);
            if (intermediate == 0){
                intermediate = (roll.getPct() / 100.0);
            } else allZero = false; //there exists a non-zero value in the composite
            worsePct *= intermediate;
        }

        if (allZero) return 0; //there exists no worse roll. Also holds for empty set.
        return worsePct * 100;
    }

    public double getWorseOneInX() {
        double worseOneInX = 1;
        for (AbstractRoll roll : rolls) {
            worseOneInX *= roll.getWorseOneInX();
        }
        return worseOneInX;
    }

    public double getBetterPct() {
        double betterPct = 1;
        boolean allZero = true;
        for (AbstractRoll roll : rolls) {
            double intermediate = (roll.getBetterPct() / 100);
            if (intermediate == 0){
                intermediate = (roll.getPct() / 100.0);
            } else allZero = false; //there exists a non-zero value in the composite
            betterPct *= intermediate;
        }

        if (allZero) return 0; //there exists no better roll. Also holds for empty set.
        return betterPct * 100;
    }

    public double getBetterOneInX() {
        double betterOneInX = 1;
        for (AbstractRoll roll : rolls){
            betterOneInX *= roll.getBetterOneInX();
        }
        return betterOneInX;
    }

    //A roll is 'bad' if one was more likely to get a better roll than a worse one: worseOneInX > betterOneInX
    public boolean isBadRoll(){
        return getWorseOneInX() > getBetterOneInX();
    }
}
