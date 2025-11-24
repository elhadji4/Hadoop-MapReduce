package com.sales.mapreduce;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Mapper class for the Sales MapReduce job.
 * Reads CSV lines and emits (product, revenue) pairs.
 * 
 * Expected CSV format: product_id,product_name,quantity,unit_price
 * Revenue is calculated as: quantity * unit_price
 */
public class SalesMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {

    private final Text productKey = new Text();
    private final DoubleWritable revenueValue = new DoubleWritable();

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        
        String line = value.toString();
        
        // Skip header line
        if (line.startsWith("product_id") || line.startsWith("Product")) {
            return;
        }
        
        // Skip empty lines
        if (line.trim().isEmpty()) {
            return;
        }
        
        try {
            String[] fields = line.split(",");
            
            if (fields.length >= 4) {
                String productName = fields[1].trim();
                int quantity = Integer.parseInt(fields[2].trim());
                double unitPrice = Double.parseDouble(fields[3].trim());
                
                double revenue = quantity * unitPrice;
                
                productKey.set(productName);
                revenueValue.set(revenue);
                
                context.write(productKey, revenueValue);
            }
        } catch (NumberFormatException e) {
            // Log and skip malformed lines
            context.getCounter("SalesMapper", "MALFORMED_LINES").increment(1);
        }
    }
}
