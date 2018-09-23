/**
 * Testcases for the Pay Station system.
 *
 * This source code is from the book "Flexible, Reliable Software: Using
 * Patterns and Agile Development" published 2010 by CRC Press. Author: Henrik B
 * Christensen Computer Science Department Aarhus University
 *
 * This source code is provided WITHOUT ANY WARRANTY either expressed or
 * implied. You may study, use, modify, and distribute it for non-commercial
 * purposes. For any commercial use, see http://www.baerbak.com/
 */
package paystation.domain;

import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class PayStationImplTest {
   
    Integer Zero = new Integer(0);
    Integer Five = new Integer(5);
    Integer Ten = new Integer(10);
    Integer Twentyfive = new Integer(25);
    Integer numQuarters, numDimes, numNickels;
    int nQuarters, nDimes, nNickels;

    PayStation ps;

    @Before
    public void setup() {
        ps = new PayStationImpl();
    }

    /**
     * Entering 5 cents should make the display report 2 minutes parking time.
     */
    @Test
    public void shouldDisplay2MinFor5Cents()
            throws IllegalCoinException {
        ps.addPayment(5);
        assertEquals("Should display 2 min for 5 cents",
                2, ps.readDisplay());
    }

    /**
     * Entering 25 cents should make the display report 10 minutes parking time.
     */
    @Test
    public void shouldDisplay10MinFor25Cents() throws IllegalCoinException {
        ps.addPayment(25);
        assertEquals("Should display 10 min for 25 cents",
                10, ps.readDisplay());
    }

    /**
     * Verify that illegal coin values are rejected.
     */
    @Test(expected = IllegalCoinException.class)
    public void shouldRejectIllegalCoin() throws IllegalCoinException {
        ps.addPayment(17);
    }

    /**
     * Entering 10 and 25 cents should be valid and return 14 minutes parking
     */
    @Test
    public void shouldDisplay14MinFor10And25Cents()
            throws IllegalCoinException {
        ps.addPayment(10);
        ps.addPayment(25);
        assertEquals("Should display 14 min for 10+25 cents",
                14, ps.readDisplay());
    }

    /**
     * Buy should return a valid receipt of the proper amount of parking time
     */
    @Test
    public void shouldReturnCorrectReceiptWhenBuy()
            throws IllegalCoinException {
        ps.addPayment(5);
        ps.addPayment(10);
        ps.addPayment(25);
        Receipt receipt;
        receipt = ps.buy();
        assertNotNull("Receipt reference cannot be null",
                receipt);
        assertEquals("Receipt value must be 16 min.",
                16, receipt.value());
    }

    /**
     * Buy for 100 cents and verify the receipt
     */
    @Test
    public void shouldReturnReceiptWhenBuy100c()
            throws IllegalCoinException {
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(25);
        ps.addPayment(25);

        Receipt receipt;
        receipt = ps.buy();
        assertEquals(40, receipt.value());
    }

    /**
     * Verify that the pay station is cleared after a buy scenario
     */
    @Test
    public void shouldClearAfterBuy()
            throws IllegalCoinException {
        ps.addPayment(25);
        ps.buy(); // I do not care about the result
        // verify that the display reads 0
        assertEquals("Display should have been cleared",
                0, ps.readDisplay());
        // verify that a following buy scenario behaves properly
        ps.addPayment(10);
        ps.addPayment(25);
        assertEquals("Next add payment should display correct time",
                14, ps.readDisplay());
        Receipt r = ps.buy();
        assertEquals("Next buy should return valid receipt",
                14, r.value());
        assertEquals("Again, display should be cleared",
                0, ps.readDisplay());
    }

    /**
     * Verify that cancel clears the pay station
     */
    @Test
    public void shouldClearAfterCancel()
            throws IllegalCoinException {
        ps.addPayment(10);
        ps.cancel();
        assertEquals("Cancel should clear display",
                0, ps.readDisplay());
        ps.addPayment(25);
        assertEquals("Insert after cancel should work",
                10, ps.readDisplay());
    }
    
    /**
     * Verify empty() method exists
     */
    @Test
    public void emptyMethodExists() {
        ps.empty();
    }
    
    /**
     * Empty returns total amount of money collected
     */
    @Test
    public void emptyMeterShouldReturnZero() {
        ps.empty();
        int collected = ps.empty();
        assertEquals(collected, 0);
    }
    
    /**
     * Verify that a meter with money inside returns content.
     * Verify that meter is empty after emptied.
     *
     * Assigned Test Case: empty resets money to zero
     */
    @Test
    public void fullMeterShouldReturnInputValueGoToZero() throws IllegalCoinException {
        ps.empty();
        ps.addPayment(5);
        ps.buy();
        int collected = ps.empty();
        assertEquals(collected, 5);
        int leftInMeter = ps.empty();
        assertEquals(leftInMeter, 0);
    }

    /**
     * Verify that entire collection is returned from empty
     *
     * Assigned test case: empty returns total amount entered.
     */
    @Test
    public void shouldReturnBothTransactions() throws IllegalCoinException {
        ps.addPayment(25);
        ps.buy();
        ps.addPayment(25);
        ps.buy();

        int collected = ps.empty();
        assertEquals("Should contain 50, sum of two transactions.", 50, collected);
    }
/**
     * Verify that entire collection is returned from empty
     *
     * Assigned test case: canceled transaction doesn't increase total
     */
    @Test
    public void shouldSumOnlyFirstTransaction() throws IllegalCoinException {
        ps.addPayment(25);
        ps.buy();
        ps.addPayment(5);
        ps.cancel();

        int collected = ps.empty();
        assertEquals("Should contain 25, only first transaction.", 25, collected);
    }

    /**
     * Verify that return from Cancel is Map
     */
    @Test
    public void shouldReturnMapFromCancel() {
        Object gotFromCancel = ps.cancel();
        assertNotNull(gotFromCancel);
        assertTrue(gotFromCancel instanceof Map);
    }
    
    /**
     * Verify Map of no coins is contains no keys, and is not null
     */
//    @Test inconsistent with requirements, test SHOULD fail
    @Test
    public void shouldReturnZeroesMapFromEmptyMachine() {
        ps.empty();
        Map coins = ps.cancel();
        assertFalse("is Null",coins==null);
        assertTrue("is Empty",coins.isEmpty());
    }
    
    /**
     * Verify that correct change is returned for one nickel
     *
     * Assigned Test Case: Return map containing the one coin entered
     */
    @Test
    public void shouldReturnCorrectChangeIn5Case() throws IllegalCoinException {
        ps.empty();
        ps.addPayment(5);
        Map<Integer, Integer> change = ps.cancel();
        Integer numNickels = change.get(Five);
        int nNickels = numNickels.intValue();
        
        assertEquals("One Nickel should be represented in the map returned", 1, nNickels);
    }
    
    /**
     * Verify that correct change is returned for one quarter.
     */
//    @Test don't bother testing, just another one ocin case
    @Test
    public void shouldReturnCorrectChangeIn25Case() throws IllegalCoinException {
        ps.empty();
        ps.addPayment(25);
        Map<Integer, Integer> change = ps.cancel();
        numQuarters = change.get(Twentyfive);
        nQuarters = numQuarters.intValue();
        
        assertEquals("One Quarter should be represented in the map returned", 1, nQuarters);
        
    }
    
    /**
     * Verify correct change for 30 cents
     *
     * Assigned Test Case: Call to cancel returns map with mixture of coins
     */
    @Test
    public void shouldReturnCorrectChangeIn30Case() throws IllegalCoinException {
        
        ps.addPayment(25);
        ps.addPayment(5);
        Map<Integer, Integer> change = ps.cancel();

        numQuarters = change.get(Twentyfive);
//        numDimes = change.get(Ten);
        numNickels = change.get(Five);
        
        nQuarters = numQuarters.intValue();
//        nDimes = numDimes.intValue();
        nNickels = numNickels.intValue();
        
        assertEquals("Thirty cents should have one nickel", nNickels, 1);
//        assertEquals("Thirty cents should have one quarter", nDimes, 0);
        assertEquals("Thirty cents should have one quarter", nQuarters, 1);
        
    }

    /**
     * Verify Map returned doesn't contain key for coins not inserted
     * event though quarter is the fastest change for total value.
     *
     * Assigned Test Case: Map does not contain key for coin entered.
     */
    @Test
    public void shouldReturnMapWithoutKeyForAbsentCoins() throws IllegalCoinException {
        for(int i = 0; i < 5; i++) {
            ps.addPayment(5);
        }
        Map<Integer, Integer> change = ps.cancel();

        assertFalse(change.containsKey(Twentyfive));
    }

    /**
     * Verify correct change for 35 cents
     */
//    @Test don't bother running -- other tests covered all assigned cases.
    @Test
    public void shouldReturnCorrectChangeIn35Case() throws IllegalCoinException {
        // 
        ps.addPayment(25);
        ps.addPayment(5);
        ps.addPayment(5);
        Map<Integer, Integer>change = ps.cancel();
        
        numQuarters = change.get(Twentyfive);
        numNickels = change.get(Five);
        
        nQuarters = numQuarters.intValue();
        nNickels = numNickels.intValue();
        
        assertEquals("Thirty cents should have no nickel", nNickels, 2);
        assertEquals("Thirty cents should have one quarter", nQuarters, 1);
    }

    /**
     * Verify that canceled transactions don't increase count of coins available.
     *
     * Assigned Test Case: call to cancel clears map
     */
    @Test
    public void shouldDisregardCanceledCoin() throws IllegalCoinException {
        ps.addPayment(25);
        ps.cancel();

        for(int i = 0; i < 5; i++) {
            ps.addPayment(5);
        }
        Map<Integer, Integer> change = ps.cancel();
        assertFalse("No quarters should be returned", change.containsKey(Twentyfive));
    }

    /**
     * Verify that cancel doesn't return credit from last transaction.
     *
     * Assigned Test Case: call to buy resets map
     */
    @Test
    public void shouldNotCreditLastTransaction() throws IllegalCoinException {
        ps.addPayment(25);
        ps.addPayment(10);
        ps.addPayment(5);
        ps.buy();

        Map<Integer, Integer> change = ps.cancel();
        assertTrue("Map is empty because nothing inserted since last buy.", change.isEmpty());
    }
}
