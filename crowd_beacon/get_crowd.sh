#!/bin/bash

#folder
dir=$(pwd)
flow1=$dir"/listamea.txt"
flow2=$dir"/listamica.txt"
temp=$dir"/listatemp.txt"

#enable BLE on Edison
/usr/sbin/rfkill unblock bluetooth

#killall bluetoothd

/usr/bin/hciconfig hci0 reset
sleep 1
/usr/bin/hciconfig hci0 down
sleep 1 
/usr/bin/hciconfig hci0 up
sleep 1 

#enable BLE flow
/usr/bin/hcitool lescan --duplicate --passive > $flow1 &


COUNTER=0
while [  1 -gt 0 ]; do
 echo The counter is $COUNTER
 sleep 3
 #check if flow down
 cp -f $flow1 $flow2
 echo "" > $flow1
 cat $flow2 | sort | uniq |awk '{print $1}' | grep -io '[0-9a-f]{12}' > $temp
 
 let COUNTER=COUNTER+1
done