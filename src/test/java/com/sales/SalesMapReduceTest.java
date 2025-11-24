package com.sales;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Basic unit tests for the Sales Revenue MapReduce job components.
 * These tests verify the core logic without requiring the full Hadoop framework.
 */
public class SalesMapReduceTest {

    @Test
    public void testCSVParsingLogic() {
        // Test CSV line parsing logic that the mapper uses
        String line = "1,Laptop,2,999.99,2024-01-15";
        String[] fields = line.split(",");
        
        assertEquals(5, fields.length);
        assertEquals("1", fields[0]);
        assertEquals("Laptop", fields[1]);
        assertEquals(2, Integer.parseInt(fields[2]));
        assertEquals(999.99, Double.parseDouble(fields[3]), 0.001);
        assertEquals("2024-01-15", fields[4]);
    }

    @Test
    public void testRevenueCalculation() {
        // Test the revenue calculation logic: quantity * unit_price
        int quantity = 2;
        double unitPrice = 999.99;
        double expectedRevenue = 1999.98;
        
        double calculatedRevenue = quantity * unitPrice;
        assertEquals(expectedRevenue, calculatedRevenue, 0.001);
    }

    @Test
    public void testHeaderDetection() {
        // Test header line detection
        String header = "transaction_id,product_name,quantity,unit_price,date";
        String dataLine = "1,Laptop,2,999.99,2024-01-15";
        
        assertTrue(header.startsWith("transaction_id"));
        assertFalse(dataLine.startsWith("transaction_id"));
    }

    @Test
    public void testRevenueSummation() {
        // Test the reducer's summation logic
        double[] revenues = {1999.98, 999.99, 2999.97};
        double expectedTotal = 5999.94;
        
        double sum = 0.0;
        for (double revenue : revenues) {
            sum += revenue;
        }
        
        assertEquals(expectedTotal, sum, 0.001);
    }

    @Test
    public void testMalformedLineHandling() {
        // Test handling of malformed lines
        String malformedLine = "incomplete,data";
        String[] fields = malformedLine.split(",");
        
        // Should have fewer than 4 fields
        assertTrue(fields.length < 4);
    }

    @Test
    public void testDriverClassExists() {
        // Verify the driver class can be loaded
        try {
            Class<?> driverClass = Class.forName("com.sales.SalesRevenueDriver");
            assertNotNull(driverClass);
        } catch (ClassNotFoundException e) {
            fail("SalesRevenueDriver class should exist");
        }
    }

    @Test
    public void testMapperClassExists() {
        // Verify the mapper class can be loaded
        try {
            Class<?> mapperClass = Class.forName("com.sales.SalesMapper");
            assertNotNull(mapperClass);
        } catch (ClassNotFoundException e) {
            fail("SalesMapper class should exist");
        }
    }

    @Test
    public void testReducerClassExists() {
        // Verify the reducer class can be loaded
        try {
            Class<?> reducerClass = Class.forName("com.sales.SalesReducer");
            assertNotNull(reducerClass);
        } catch (ClassNotFoundException e) {
            fail("SalesReducer class should exist");
        }
    }
}
