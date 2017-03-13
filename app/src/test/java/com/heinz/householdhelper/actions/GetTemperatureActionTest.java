package com.heinz.householdhelper.actions;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import static junit.framework.Assert.assertEquals;

/**
 * Created by Heinz on 11.03.2017.
 */

public class GetTemperatureActionTest {

    private GetTemperatureAction createTestee() {
        return new GetTemperatureAction();
    }

    @Test
    public void testCheckRealMacInvalid() {
        assertEquals(false, createTestee().checkRealMac(""));
        assertEquals(false, createTestee().checkRealMac("abcd"));
    }

    @Test
    public void testCheckRealMacValid() {
        assertEquals(true, createTestee().checkRealMac("11:11:11:11:11:11"));
        assertEquals(true, createTestee().checkRealMac("00:11:67:10:00:4C"));
        assertEquals(true, createTestee().checkRealMac("11:23:45:67:89:AB"));
        assertEquals(true, createTestee().checkRealMac("C4:A3:61:84:78:37"));
    }

    @Test
    public void testToISO8601LocalDate() {
        Calendar cal = Calendar.getInstance();
        // Test 1 simple some little 0
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.MONTH, 1);
        cal.set(Calendar.YEAR, 2010);
        assertEquals("2010-01-01T00:00:00", createTestee().toISO8601LocalDate(cal.getTime()));
        // test 2 some more realistic date
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 12);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.DAY_OF_MONTH, 12);
        cal.set(Calendar.MONTH, Calendar.AUGUST);
        cal.set(Calendar.YEAR, 2015);
        assertEquals("2015-08-12T23:12:59", createTestee().toISO8601LocalDate(cal.getTime()));
    }

}



