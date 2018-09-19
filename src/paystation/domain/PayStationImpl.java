package paystation.domain;

import java.util.HashMap;

/**
 * Implementation of the pay station.
 *
 * Responsibilities:
 *
 * 1) Accept payment; 2) Calculate parking time based on payment; 3) Know
 * earning, parking time bought; 4) Issue receipts; 5) Handle buy and cancel
 * events.
 *
 * This source code is from the book "Flexible, Reliable Software: Using
 * Patterns and Agile Development" published 2010 by CRC Press. Author: Henrik B
 * Christensen Computer Science Department Aarhus University
 *
 * This source code is provided WITHOUT ANY WARRANTY either expressed or
 * implied. You may study, use, modify, and distribute it for non-commercial
 * purposes. For any commercial use, see http://www.baerbak.com/
 */
public class PayStationImpl implements PayStation {

    private int insertedSoFar;
    private int timeBought;
    private int nNickels = 0;
    private int nDimes = 0;
    private int nQuarters = 0;

    @Override
    public void addPayment(int coinValue)
            throws IllegalCoinException {
        switch (coinValue) {
            case 5:
                nNickels++;
                break;
            case 10:
                nDimes++;
                break;
            case 25:
                nQuarters++;
                break;
            default:
                throw new IllegalCoinException("Invalid coin: " + coinValue);
        }
        insertedSoFar += coinValue;
        timeBought = insertedSoFar / 5 * 2;
    }

    @Override
    public int readDisplay() {
        return timeBought;
    }

    @Override
    public Receipt buy() {
        Receipt r = new ReceiptImpl(timeBought);
        reset();
        return r;
    }

    /**
     * Cancel the present transaction. Resets the paystation for a new
     * transaction.
     *
     * @return A Map defining the coins returned to the user. 
     * The key is the coin type and the associated value is the number of these 
     * coins that are returned. 
     * The Map object is never null even if no coins are returned. 
     * The Map will only contain only keys for coins to be returned.
     * The Map will be cleared after a cancel or buy.
     */
    
    @Override
    public HashMap<Integer, Integer> cancel() {
        Integer Zero = new Integer(0);
        Integer Five = new Integer(5);
        Integer Ten = new Integer(10);
        Integer Twentyfive = new Integer(25);

        HashMap<Integer, Integer> coinsInChange;
        coinsInChange = new HashMap<Integer, Integer>();

        // Calculate number of quarters returned
        //Integer nQuarters = new Integer(insertedSoFar/25);
        //insertedSoFar = insertedSoFar % 25;
        // Calculate number of dimes returned
        //Integer nDimes = new Integer(insertedSoFar/10);
        //insertedSoFar = insertedSoFar % 10;
        // Calculate number of nickels returned
        //Integer nNickels = new Integer(insertedSoFar/5);// Assume DNE 0 < insertedSoFar < 5
        // Return correct number of each coin in Map
        coinsInChange.put(Five, nNickels);
        coinsInChange.put(Ten, nDimes);
        coinsInChange.put(Twentyfive, nQuarters);

        reset();

        return coinsInChange;
    }

    private void reset() {
        timeBought = insertedSoFar = nNickels = nDimes = nQuarters = 0;
    }

    @Override
    public int empty() {
        int collected = insertedSoFar;
        reset();
        return collected;
    }
}
