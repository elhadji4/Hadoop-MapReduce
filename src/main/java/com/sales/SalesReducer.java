package com.sales;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Reducer class for Sales Revenue calculation.
 * 
 * Input: Key = product_name, Values = list of revenues
 * Output: Key = product_name, Value = total revenue
 */
public class SalesReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {

    private DoubleWritable totalRevenue = new DoubleWritable();

    @Override
    protected void reduce(Text key, Iterable<DoubleWritable> values, Context context)
            throws IOException, InterruptedException {
        
        double sum = 0.0;
        
        // Sum all revenues for this product
        for (DoubleWritable value : values) {
            sum += value.get();
        }
        
        totalRevenue.set(sum);
        context.write(key, totalRevenue);
    }
}
