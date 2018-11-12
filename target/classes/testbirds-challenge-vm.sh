#!/bin/bash

MAC=$(cat /sys/class/net/ens3/address)
echo "MAC: $MAC"
IP=$(ip -4 r show default | awk '{print $3}')
echo "IP: $IP"
### TODO ###
firefox http://$IP:8080/api/vm/update/$MAC
############
exit 0