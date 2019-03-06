package ROTMGRoll;

import java.math.BigDecimal;
import java.math.MathContext;
import ROTMGclasses.ROTMGStatRange;
import ROTMGclasses.ROTMGStat;
import ROTMGclasses.ROTMGClass;

public class RollCalculator {
    private int sides;
    private int dice;

    private BigDecimal totalWays;
    private int maxRoll; //assumes 0 based scale, is maximum value a dice has (= sides-1) * amnt of dice
    private int minRoll = 0; //assumes 0-based scale, so is always 0

    private ROTMGClass character;
    private ROTMGStat stat;

    public RollCalculator(int nDice, ROTMGClass rotmgClass, ROTMGStat stat) {
        character = rotmgClass;
        this.stat = stat;

        ROTMGStatRange statRange = rotmgClass.getStatRange(stat);
        sides = statRange.getNSides();
        dice = nDice;
        maxRoll = dice * statRange.getNSides();

        System.out.println("Data: \nClass: " + rotmgClass.getType() + "\nStat: " + stat.toString() + "\nMaxRoll: " + maxRoll + "\nMinRoll: " + minRoll);

        totalWays = pow(new BigDecimal(sides + 1), dice);
        System.out.println("total amount of ways to reach a roll: " + totalWays);
    }

    /** Computes odds for a given roll
     *
     * The odds for a roll are computed by finding the amount of ways to reach the roll,
     * then multiplying that value with the odds of an individual sequence of dice rolls taking place
     *
     * The probability for an individual sequence is m ^ n
     *
     * The amount of ways to get the value is specified in countWays()
     *
     * @param roll the roll the user has gotten
     * @precondition roll is within the bounds of maxRoll
     * @throws IllegalArgumentException if precondition violated
     *
     * @return the probability to get the roll, (between 0 and 1)
     *
     */
    public Roll computeRollOdds(int roll) {
        roll = convertToZeroBasedScale(roll);

        if (roll > maxRoll || roll < minRoll) throw new IllegalArgumentException("Roll.Roll value of " + roll + " not possible on level " + (dice + 1));

        BigDecimal ways = countWays(roll); //amnt of ways for specifically this value
        System.out.println("ways to get this value: " + ways);
        double prob = ways.divide(totalWays, MathContext.DECIMAL128).doubleValue(); //prob for specifically this value

        BigDecimal worseWays = BigDecimal.ZERO;
        for (int i = roll - 1; i >= minRoll; i--){
            worseWays = worseWays.add(countWays(i));
        }
        System.out.println("ways to do worse: " + worseWays);
        double worseProb = worseWays.divide(totalWays, MathContext.DECIMAL128).doubleValue(); //prob for worse than this value

        BigDecimal betterWays = BigDecimal.ZERO;
        for (int i = roll + 1; i <= maxRoll; i++) {
            betterWays = betterWays.add(countWays(i));
        }
        System.out.println("ways to do better: " + betterWays);
        double betterProb = betterWays.divide(totalWays, MathContext.DECIMAL128).doubleValue(); //prob for better than this value

        return new Roll(prob, worseProb, betterProb);
    }

    /** Counts the amount of ways to reach a certain value with n, m sided dice
     *
     * The amount of ways to reach a value S with n, m sided dice is
     * https://i.imgur.com/Ynqivlw.png
     *
     * @param roll the value to count possible ways to reach for
     *             this value is on a 0-based scale like the dice formula assumes
     * @precondition roll is on a scale from 0 to max
     * @throws IllegalArgumentException if precondition violated
     *
     * @return the amount of ways to reach the value
     *
     */
    private BigDecimal countWays(int roll) {
        if (roll < 0 || roll > maxRoll) throw new IllegalArgumentException("roll in countways of value " + roll + " is outside of scale (0 - " + maxRoll + ")");

        int r; //sum variable
        int sumGuard = roll / (sides + 1); //floor (S / (m+1))
        BigDecimal sum = BigDecimal.ZERO;

        for (r = 0; r <= sumGuard; r++){
            int term = dice - 1 + roll - (r * (sides + 1)); //large term in first binomial
            //sum += pow(-1, r) * binom(term, dice -1) * binom(dice, r);
            BigDecimal intermediate = pow(new BigDecimal(-1), r);
            intermediate = intermediate.multiply(binom(new BigDecimal(term), new BigDecimal(dice - 1)));
            intermediate = intermediate.multiply(binom(new BigDecimal(dice), new BigDecimal(r)));
            sum = sum.add(intermediate);
        }

        return sum;
    }

    /** Calculates the binomial coefficient of n choose k
     *
     * The binomial coefficient is the amount of ways to generate a subset from a set of size n,
     * where those subsets are of size k.
     *
     * Mathematically, this is equal to:
     * <code>n! / (k! * (n - k)!)</code>
     *
     * @param n set size
     * @param k subset size
     *
     * @return n choose k
     */
    private BigDecimal binom(BigDecimal n, BigDecimal k) {
        BigDecimal numerator = factorial(n); // n!
        BigDecimal divisor = (factorial(k).multiply(factorial(n.subtract(k)))); // k! * (n - k)!
        //noinspection BigDecimalMethodWithoutRoundingCalled
        return numerator.divide(divisor); //n! / (k! * (n - k)!)
        //no scale required: will not ever throw exception; binomial coefficient is always an integer value
    }

    /** Calculates factorial (n!) of a given number n
     *
     * @precondition n is not negative
     * @throws IllegalArgumentException if precondition violated
     *
     * @param n value to compute factorial of
     * @return n!
     *
     */
    private BigDecimal factorial(BigDecimal n){
        if (n.doubleValue() < 0) throw new IllegalArgumentException("n cannot bee negative in this factorial method " + n);

        //base cases:
        if (n.doubleValue() == 0) {
            return BigDecimal.ONE;
        } else if (n.doubleValue() == 1) {
            return BigDecimal.ONE;
        }

        //recursion:
        return n.multiply(factorial(n.subtract(BigDecimal.ONE)));
    }

    /**
     * Method for calculating a ^ b
     *
     * Recursively calculates a ^ b by making use of:
     * a ^ b = a * a ^ (b/2) if b is even
     * a ^ b = a * (a ^ (b-1)) if b is odd
     *
     * @precondition b is >= 0
     * @throws IllegalArgumentException if precondition violated
     *
     * @param a base value
     * @param b exponent
     *
     * @return a to the power b
     *
     */
    private BigDecimal pow(BigDecimal a, int b) {
        //precondition
        if (b < 0) throw new IllegalArgumentException("method 'pow' cannot compute negative powers");

        //base cases:
        if (b == 0) {
            return BigDecimal.ONE; //a^0 = 1
        } else if (b == 1) {
            return a; //a^1 = a
        }

        //recursion
        boolean evenPower = b % 2 == 0;
        if (evenPower) {
            return pow(a.multiply(a), b / 2);
        } else {
            return a.multiply(pow(a, b - 1));
        }
    }

    /**
     * Converts a 'roll' by a player, represented as the amount of points a user has in that stat
     * Into a scale that the rollCalculator can use.
     * That is, a scale whose mimimum value is 0, and max is dice * sides-1
     *
     * This is done by computing how many stat points corresponds to '0' on the scale
     * The returning the difference between this and the roll.
     *
     * Not robust. (Lets other methods using this take care of it)
     *
     * @param roll the roll the user has represented as points in the stat
     * @return the difference between this roll and the mimimum possible value
     */
    private int convertToZeroBasedScale(int roll) {
        int minRollValue = character.getBaseStat(stat) + character.getStatRange(stat).getMin() * dice;
        System.out.println("Your roll to a 0-based scale result: " + (roll - minRollValue));
        return roll - minRollValue; //result = amount of points above the minimum (0)
    }
}