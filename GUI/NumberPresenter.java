package GUI;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Takes values (int, double, etc.) and makes them presentable in String format:
 *
 * This involves turning exponents into trailing 0's in either direction,
 * as well as inserting commas in large values (e.g. 123,456,789)
 * and also scrubbing values with large decimal appendages down to a few significant numbers.
 */
class NumberPresenter {

    /**
     * Makes doubles look presentable,
     * by making a case distinction on the double,
     * then delegating the double to the correct method
     *
     * This method makes a distinction for doubles and modifies them
     * if deemed necessary.
     *
     * Case 1: Positive exponent
     * If a positive exponent is present in the double,
     * it is converted to display it as a value without one.
     * A ',' is inserted every 3rd digit to improve readability as well.
     *
     * Case 2: Negative exponent
     * If a negative exponent is present in the double,
     * it is converted to display as trailing zeroes
     * with some significant digits at the end.
     *
     * Case 3: No exponent
     * If no exponent is present in the double,
     * A check is executed for the amount of decimal digits
     * If it is deemed there are too many,
     * the amount of decimal digits is reduced.
     *
     * @param d the double to make presentable
     *
     * @return a presentable edition of the double in String format
     */
    String improveDoubleLooks(double d) {
        String value = Double.toString(d);

        int exponentPosition = value.indexOf('E');
        if (exponentPosition == -1) {
            //no exponent, only a (potentially) long decimal
            return decimalScrubber(d, 4); //scrubs decimal length down to 3
        } else {
            //has an exponent (== val is at least 7 orders of magnitude), positive or negative
            if (value.charAt(exponentPosition+1) == '-'){ //negative exponent
                return negativeExponentRemover(value, exponentPosition);
            } else {
                String fullValue = new BigDecimal(d).toString(); //lazy and quick conversion to full representation of value
                return tripleDigitCommaInsert(fullValue);
            }
        }
    }

    /**
     * Makes decimal strings on double values a specified maximum length
     * also correctly rounds based on the specified + 1-th decimal.
     * If there are more non-decimal digits than maxLen, no decimal digits are returned instead (also rounded)
     *
     * @param maxLen maximum length to allow
     * @param value the double value whose decimal to scrub
     *
     * @return the double value with a specified amount of decimal digits
     */
    String decimalScrubber(double value, int maxLen) {
        String strValue = Double.toString(value);
        int decimalDotPos = strValue.indexOf('.'); // any double contains a dot
        int offset = strValue.substring(0, decimalDotPos).length();
        BigDecimal theValue = BigDecimal.valueOf(value);
        if (offset >= maxLen){ //more non-decimal digits than we care for
            //hence both scrubbing as well as comma-inserting is needed
            theValue = theValue.setScale(0, RoundingMode.HALF_UP);
            return tripleDigitCommaInsert(theValue.toString());
        } else {
            theValue = theValue.setScale(maxLen - offset, RoundingMode.HALF_UP);
            //check if decimal is all zeroes (divide by 1.0)
            if (theValue.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
                theValue = theValue.setScale(1, BigDecimal.ROUND_UNNECESSARY);
            }
            return theValue.toString();
        }
    }

    /**
     * Split a large value in thousands by inserting ','
     *
     * @param value the large value to split
     * @return the same value, but with a , after every 3rd digit (except the 0th, and last if applicable)
     */
    private String tripleDigitCommaInsert(String value) {
        //remove decimal dot if applicable, not relevant for large values
        int decimalPos = value.indexOf('.');
        if (decimalPos != -1) {
            value = value.substring(0, decimalPos);
        }

        StringBuilder toReturn = new StringBuilder(value);

        toReturn.reverse(); //reverse for ease of use
        //insert a ',' every 3rd digit
        int i = 0;
        while (i < toReturn.length()){
            if (i % 4 == 0){
                toReturn.insert(i, ',');
            }
            i++;
        }
        //a ',' was placed at the very start, get rid of it
        return toReturn.reverse().toString().substring(0, toReturn.length()-1);
    }

    /**
     * Removes the negative exponent at the end of the string,
     * by shifting the decimal dot,
     * and adding an adequate amount of 0's if necessary
     *
     * @param value the value to have its exponent removed
     * @param exponentPos the position of the `E` exponent sign in the string
     *
     * @precondition value.contains("E");
     * @throws IllegalArgumentException if precondition violated
     *
     * @return the same value represented as decimal without an exponent
     */
    private String negativeExponentRemover(String value, int exponentPos) {
        if (! value.contains("E")) throw new IllegalArgumentException("String does not contain Exponent!");

        int exponentAmount = Integer.parseInt(value.substring(exponentPos + 1)) * -1; //amount of shifts to perform
        value = value.substring(0, exponentPos); //value without exponent
        value = decimalScrubber(Double.parseDouble(value), 3); //cut down on decimal amount
        //value is now 6 significant digits
        value = value.replace(".",""); //remove dot
        value = '.' + value; //acts as first shift
        exponentAmount--;

        while(exponentAmount > 0){
            value = ".0" + value.substring(1); //inserting 0s (shifting the decimal dot effectively)
            exponentAmount--;
        }
        return '0' + value;
    }

}
