#!/bin/bash
# Script to run the Sales MapReduce job
# Usage: ./run_job.sh [hdfs_input_dir] [hdfs_output_dir]

HDFS_INPUT_DIR=${1:-"/user/$(whoami)/sales/input"}
HDFS_OUTPUT_DIR=${2:-"/user/$(whoami)/sales/output"}
JAR_PATH="target/sales-mapreduce-1.0-SNAPSHOT.jar"

echo "=== Running Sales MapReduce Job ==="
echo "Input directory: $HDFS_INPUT_DIR"
echo "Output directory: $HDFS_OUTPUT_DIR"

# Check if JAR file exists
if [ ! -f "$JAR_PATH" ]; then
    echo "Error: JAR file not found. Please build the project first with 'mvn clean package'"
    exit 1
fi

# Remove output directory if it exists (MapReduce requires output dir to not exist)
echo "Cleaning output directory..."
hdfs dfs -rm -r -f "$HDFS_OUTPUT_DIR" 2>/dev/null

# Run the MapReduce job
echo "Starting MapReduce job..."
hadoop jar "$JAR_PATH" com.sales.mapreduce.SalesDriver "$HDFS_INPUT_DIR" "$HDFS_OUTPUT_DIR"

# Check job status
if [ $? -eq 0 ]; then
    echo "=== Job completed successfully ==="
    echo "Output files:"
    hdfs dfs -ls "$HDFS_OUTPUT_DIR/"
    echo ""
    echo "Results preview:"
    hdfs dfs -cat "$HDFS_OUTPUT_DIR/part-r-00000"
else
    echo "=== Job failed ==="
    exit 1
fi
