#!/bin/bash

cat raw | sort | uniq > temp

echo -n '[' > list_computed.txt

fl=0
while read line
do
    if [ "$fl" -gt 0 ]; then
     echo -n ',' >> list_computed.txt
    fi
    fl=1
    mac=$(echo $line | awk '{print $1}')
    echo -n '"'$mac'"' >> list_computed.txt
done <temp

echo -n ']' >> list_computed.txt