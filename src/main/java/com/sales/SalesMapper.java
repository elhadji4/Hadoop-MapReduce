package com.sales;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Mapper class for Sales Revenue calculation.
 * 
 * Input: CSV line with format: transaction_id,product_name,quantity,unit_price,date
 * Output: Key = product_name, Value = revenue (quantity * unit_price)
 */
public class SalesMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {

    private Text productName = new Text();
    private DoubleWritable revenue = new DoubleWritable();

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        
        String line = value.toString();
        
        // Skip header line - check if first field is non-numeric (header identifier)
        if (line.isEmpty()) {
            return;
        }
        
        String[] fields = line.split(",");
        if (fields.length < 4) {
            context.getCounter("SalesMapper", "INSUFFICIENT_FIELDS").increment(1);
            return;
        }
        
        // Skip header by checking if first field (transaction_id) is not a number
        try {
            Integer.parseInt(fields[0].trim());
        } catch (NumberFormatException e) {
            // First field is not a number, likely a header row
            return;
        }
        
        try {
            String product = fields[1].trim();
            int quantity = Integer.parseInt(fields[2].trim());
            double unitPrice = Double.parseDouble(fields[3].trim());
            
            // Calculate revenue for this transaction
            double transactionRevenue = quantity * unitPrice;
            
            productName.set(product);
            revenue.set(transactionRevenue);
            
            context.write(productName, revenue);
        } catch (NumberFormatException e) {
            // Skip malformed lines
            context.getCounter("SalesMapper", "MALFORMED_LINES").increment(1);
        }
    }
}
