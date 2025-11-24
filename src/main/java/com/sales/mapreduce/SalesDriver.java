package com.sales.mapreduce;

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
 * Driver class for the Sales MapReduce job.
 * Calculates total revenue per product from CSV sales data.
 * 
 * Usage: hadoop jar sales-mapreduce-1.0-SNAPSHOT.jar com.sales.mapreduce.SalesDriver <input_path> <output_path>
 */
public class SalesDriver extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new Configuration(), new SalesDriver(), args);
        System.exit(exitCode);
    }

    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: SalesDriver <input_path> <output_path>");
            System.exit(-1);
        }

        Configuration conf = getConf();
        Job job = Job.getInstance(conf, "Sales Revenue Per Product");
        
        job.setJarByClass(SalesDriver.class);
        
        // Set Mapper and Reducer classes
        job.setMapperClass(SalesMapper.class);
        job.setReducerClass(SalesReducer.class);
        
        // Set Combiner to optimize performance
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
        
        // Wait for job completion
        boolean success = job.waitForCompletion(true);
        
        return success ? 0 : 1;
    }
}
