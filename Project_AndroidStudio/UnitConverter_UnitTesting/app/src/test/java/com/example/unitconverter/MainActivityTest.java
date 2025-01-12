package com.example.unitconverter;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class MainActivityTest extends TestCase {


    @Test
    public void testUnitConverter() throws Exception{

        MainActivity MainActivity = new MainActivity();

        double result_fuel = MainActivity.UnitConverter("Km/Liter",
                "Miles/Gallon", 10.0);
        assertEquals(23.5215, result_fuel, 0.01);

        double result_temp = MainActivity.UnitConverter("Celsius",
                "Fahrenheit", 10.0);
        assertEquals(50.0, result_temp, 0.01);


    }
}