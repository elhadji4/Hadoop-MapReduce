package com.sales.mapreduce;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Reducer class for the Sales MapReduce job.
 * Sums up all revenue values for each product.
 * 
 * Input: (product, [revenue1, revenue2, ...])
 * Output: (product, total_revenue)
 */
public class SalesReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {

    private final DoubleWritable totalRevenue = new DoubleWritable();

    @Override
    protected void reduce(Text key, Iterable<DoubleWritable> values, Context context)
            throws IOException, InterruptedException {
        
        double sum = 0.0;
        
        for (DoubleWritable value : values) {
            sum += value.get();
        }
        
        totalRevenue.set(sum);
        context.write(key, totalRevenue);
    }
}
