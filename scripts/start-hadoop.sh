#!/bin/bash

# Start SSH service
service ssh start

# Start HDFS
$HADOOP_HOME/sbin/start-dfs.sh

# Start YARN
$HADOOP_HOME/sbin/start-yarn.sh

# Wait for services to be ready
sleep 10

# Create HDFS directories
hdfs dfs -mkdir -p /user/root
hdfs dfs -mkdir -p /input

echo "Hadoop cluster started successfully!"
echo "HDFS NameNode UI: http://localhost:9870"
echo "YARN ResourceManager UI: http://localhost:8088"

# Keep container running
tail -f /dev/null
