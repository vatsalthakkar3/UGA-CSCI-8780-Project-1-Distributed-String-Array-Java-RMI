#!/bin/bash
#using the local java copy
# downloading local java version and extracting
if [[ -n $1 ]]; then
    cd ../
    curl -X GET  https://download.oracle.com/java/17/archive/jdk-17.0.5_linux-x64_bin.tar.gz -o jdk-17.0.5_linux-x64_bin.tar.gz
    tar -xf jdk-17.0.5_linux-x64_bin.tar.gz
    cd server
fi

#start the rmiregistry
../jdk-17.0.5/bin/rmiregistry 9100 &

#compiling th fresh code
../jdk-17.0.5/bin/javac *.java
echo "starting server.."
#starting the server
../jdk-17.0.5/bin/java Server server-config.txt  2>&1
