#!/bin/bash

# Sales Revenue MapReduce Job Runner Script
# This script loads data into HDFS, runs the MapReduce job, and exports results

set -e

echo "======================================"
echo "Sales Revenue MapReduce Job"
echo "======================================"

# Configuration
INPUT_LOCAL="/app/data/sales.csv"
INPUT_HDFS="/input/sales.csv"
OUTPUT_HDFS="/output/sales-revenue"
OUTPUT_LOCAL="/app/output"
JAR_FILE="/app/jars/sales-revenue-mapreduce-1.0.0.jar"

# Step 1: Clean up previous output
echo ""
echo "Step 1: Cleaning up previous output..."
hdfs dfs -rm -r -f $OUTPUT_HDFS 2>/dev/null || true

# Step 2: Upload input data to HDFS
echo ""
echo "Step 2: Uploading sales data to HDFS..."
hdfs dfs -mkdir -p /input
hdfs dfs -put -f $INPUT_LOCAL $INPUT_HDFS
echo "Data uploaded to: $INPUT_HDFS"

# Step 3: Run MapReduce job
echo ""
echo "Step 3: Running MapReduce job..."
hadoop jar $JAR_FILE com.sales.SalesRevenueDriver $INPUT_HDFS $OUTPUT_HDFS

# Step 4: Display results
echo ""
echo "Step 4: Results - Total Revenue per Product:"
echo "--------------------------------------"
hdfs dfs -cat $OUTPUT_HDFS/part-r-00000

# Step 5: Export results to local filesystem (CSV format)
echo ""
echo "Step 5: Exporting results to CSV..."
mkdir -p $OUTPUT_LOCAL

# Create CSV with header
echo "product_name,total_revenue" > $OUTPUT_LOCAL/revenue_by_product.csv

# Append results (convert tab-separated to comma-separated)
hdfs dfs -cat $OUTPUT_HDFS/part-r-00000 | sed 's/\t/,/g' >> $OUTPUT_LOCAL/revenue_by_product.csv

echo "Results exported to: $OUTPUT_LOCAL/revenue_by_product.csv"

# Display exported CSV
echo ""
echo "Exported CSV content:"
echo "--------------------------------------"
cat $OUTPUT_LOCAL/revenue_by_product.csv

echo ""
echo "======================================"
echo "Job completed successfully!"
echo "======================================"
