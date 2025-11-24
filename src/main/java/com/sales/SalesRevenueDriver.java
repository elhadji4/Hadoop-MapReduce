package com.sales;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Driver class for the Sales Revenue MapReduce job.
 * 
 * This job calculates the total revenue per product from a CSV sales file.
 * 
 * Usage: hadoop jar sales-revenue-mapreduce-1.0.0.jar com.sales.SalesRevenueDriver <input> <output>
 */
public class SalesRevenueDriver extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: SalesRevenueDriver <input path> <output path>");
            System.exit(-1);
        }

        Configuration conf = getConf();
        Job job = Job.getInstance(conf, "Sales Revenue Per Product");

        job.setJarByClass(SalesRevenueDriver.class);
        
        // Set Mapper and Reducer classes
        job.setMapperClass(SalesMapper.class);
        job.setReducerClass(SalesReducer.class);
        
        // Set Combiner (optimization - same as reducer for sum operation)
        job.setCombinerClass(SalesReducer.class);

        // Set output key and value types
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        // Set input and output formats
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        // Set input and output paths
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // Run the job and wait for completion
        boolean success = job.waitForCompletion(true);
        
        return success ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new Configuration(), new SalesRevenueDriver(), args);
        System.exit(exitCode);
    }
}
