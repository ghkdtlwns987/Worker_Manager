#!/bin/bash
echo start worker-0
nohup java -DFILE_NAME=log0 -jar ./target/worker-1.0-SNAPSHOT-jar-with-dependencies.jar 0 &

echo start worker-1
nohup java -DFILE_NAME=log1 -jar ./target/worker-1.0-SNAPSHOT-jar-with-dependencies.jar 1 &

echo start worker-2
nohup java -DFILE_NAME=log2 -jar ./target/worker-1.0-SNAPSHOT-jar-with-dependencies.jar 2 &

echo start worker-3
nohup java -DFILE_NAME=log3 -jar ./target/worker-1.0-SNAPSHOT-jar-with-dependencies.jar 3 &

echo start worker-4
nohup java -DFILE_NAME=log4 -jar ./target/worker-1.0-SNAPSHOT-jar-with-dependencies.jar 4 &

echo start worker-5
nohup java -DFILE_NAME=log5 -jar ./target/worker-1.0-SNAPSHOT-jar-with-dependencies.jar 5 &

echo start worker-6
nohup java -DFILE_NAME=log6 -jar ./target/worker-1.0-SNAPSHOT-jar-with-dependencies.jar 6 &

echo start worker-7
nohup java -DFILE_NAME=log7 -jar ./target/worker-1.0-SNAPSHOT-jar-with-dependencies.jar 7 &
