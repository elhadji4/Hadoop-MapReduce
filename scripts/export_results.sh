#!/bin/bash
# Script to export MapReduce results to CSV format for visualization
# Usage: ./export_results.sh [hdfs_output_dir] [local_csv_path]

HDFS_OUTPUT_DIR=${1:-"/user/$(whoami)/sales/output"}
LOCAL_CSV_PATH=${2:-"output/revenue_by_product.csv"}

echo "=== Exporting Results to CSV ==="
echo "HDFS output directory: $HDFS_OUTPUT_DIR"
echo "Local CSV path: $LOCAL_CSV_PATH"

# Create local output directory if it doesn't exist
mkdir -p "$(dirname "$LOCAL_CSV_PATH")"

# Create CSV header
echo "product_name,total_revenue" > "$LOCAL_CSV_PATH"

# Get results from HDFS and convert tab-separated to comma-separated
hdfs dfs -cat "$HDFS_OUTPUT_DIR/part-r-*" | while read line; do
    # Convert tab to comma
    echo "$line" | sed 's/\t/,/g' >> "$LOCAL_CSV_PATH"
done

# Verify export
if [ -f "$LOCAL_CSV_PATH" ]; then
    echo "=== Export complete ==="
    echo "CSV file created at: $LOCAL_CSV_PATH"
    echo ""
    echo "CSV contents:"
    cat "$LOCAL_CSV_PATH"
else
    echo "Error: Failed to create CSV file"
    exit 1
fi
