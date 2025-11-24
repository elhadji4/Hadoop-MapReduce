#!/bin/bash
# Script to ingest sales data into HDFS
# Usage: ./ingest_data.sh [local_csv_path] [hdfs_input_dir]

LOCAL_CSV_PATH=${1:-"data/sales.csv"}
HDFS_INPUT_DIR=${2:-"/user/$(whoami)/sales/input"}

echo "=== HDFS Data Ingestion Script ==="
echo "Local CSV path: $LOCAL_CSV_PATH"
echo "HDFS input directory: $HDFS_INPUT_DIR"

# Check if local file exists
if [ ! -f "$LOCAL_CSV_PATH" ]; then
    echo "Error: Local CSV file not found: $LOCAL_CSV_PATH"
    exit 1
fi

# Create HDFS directory if it doesn't exist
echo "Creating HDFS directory: $HDFS_INPUT_DIR"
hdfs dfs -mkdir -p "$HDFS_INPUT_DIR"

# Remove existing file if present
hdfs dfs -rm -f "$HDFS_INPUT_DIR/sales.csv" 2>/dev/null

# Copy local file to HDFS
echo "Copying $LOCAL_CSV_PATH to HDFS..."
hdfs dfs -put "$LOCAL_CSV_PATH" "$HDFS_INPUT_DIR/"

# Verify upload
echo "Verifying upload..."
hdfs dfs -ls "$HDFS_INPUT_DIR/"

echo "=== Data ingestion complete ==="
