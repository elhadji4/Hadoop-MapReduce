FROM eclipse-temurin:8-jdk

LABEL maintainer="Sales Revenue MapReduce"
LABEL description="Hadoop Single Node Cluster for MapReduce Jobs"

# Set environment variables
ENV HADOOP_VERSION=3.3.6
ENV HADOOP_HOME=/opt/hadoop
ENV HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
ENV PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
ENV JAVA_HOME=/opt/java/openjdk
ENV HDFS_NAMENODE_USER=root
ENV HDFS_DATANODE_USER=root
ENV HDFS_SECONDARYNAMENODE_USER=root
ENV YARN_RESOURCEMANAGER_USER=root
ENV YARN_NODEMANAGER_USER=root

# Install necessary packages
RUN apt-get update && apt-get install -y \
    wget \
    ssh \
    rsync \
    && rm -rf /var/lib/apt/lists/*

# Download and install Hadoop
RUN wget -q https://archive.apache.org/dist/hadoop/common/hadoop-${HADOOP_VERSION}/hadoop-${HADOOP_VERSION}.tar.gz \
    && tar -xzf hadoop-${HADOOP_VERSION}.tar.gz -C /opt \
    && mv /opt/hadoop-${HADOOP_VERSION} ${HADOOP_HOME} \
    && rm hadoop-${HADOOP_VERSION}.tar.gz

# Configure SSH for passwordless access
RUN ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa \
    && cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys \
    && chmod 600 ~/.ssh/authorized_keys

# Copy Hadoop configuration files
COPY config/core-site.xml $HADOOP_CONF_DIR/core-site.xml
COPY config/hdfs-site.xml $HADOOP_CONF_DIR/hdfs-site.xml
COPY config/mapred-site.xml $HADOOP_CONF_DIR/mapred-site.xml
COPY config/yarn-site.xml $HADOOP_CONF_DIR/yarn-site.xml

# Set JAVA_HOME in Hadoop env
RUN echo "export JAVA_HOME=${JAVA_HOME}" >> $HADOOP_CONF_DIR/hadoop-env.sh

# Format HDFS namenode
RUN $HADOOP_HOME/bin/hdfs namenode -format -force

# Create working directories
RUN mkdir -p /app/data /app/output /app/jars

# Copy application files
COPY target/sales-revenue-mapreduce-1.0.0.jar /app/jars/
COPY data/sales.csv /app/data/
COPY scripts/run-job.sh /app/

RUN chmod +x /app/run-job.sh

WORKDIR /app

# Expose ports
EXPOSE 9870 9864 8088 8042

# Start script
COPY scripts/start-hadoop.sh /start-hadoop.sh
RUN chmod +x /start-hadoop.sh

CMD ["/start-hadoop.sh"]
