package com.heinz.householdhelper.bt;

import com.heinz.householdhelper.bt.FreetecBLECallback;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Heinz on 10.03.2017.
 */

public class FreetecBLECallbackTest {

    public FreetecBLECallbackTest(){

    }

    private FreetecBLECallback createTestee() {
        return new FreetecBLECallback(null, null);
    }

    @Test
    public void checkDecimalTemperatureToStringTemperature() {
        assertEquals("21.13", createTestee().decimalTemperatureToStringTemperature(2113));
        assertEquals("9.00", createTestee().decimalTemperatureToStringTemperature(900));
        assertEquals("9.05", createTestee().decimalTemperatureToStringTemperature(905));
        assertEquals("21.10", createTestee().decimalTemperatureToStringTemperature(2110));
    }

    @Test
    public void testparsePackage() {
        byte[] bytes = new byte[] {0x02, 0x01, 0x04, 0x09, 0x09, 0x38, 0x33, 0x38, 0x37, 0x33, 0x42, 0x34, 0x43, 0x07, 0x16, 0x09, 0x18, (byte)0xA0, 0x07, 0x00, (byte)0xFE, 0x04, 0x16, 0x0F, 0x18, 0x64};
        FreetecMeasure freetecMeasure = createTestee().parsePackage(bytes);
        assertEquals("83873B4C", freetecMeasure.getName());
        assertEquals("19.52", freetecMeasure.getTemperature());
        assertEquals(24, freetecMeasure.getBattery());
    }
}
