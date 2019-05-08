#!/usr/bin/env bash
echo "Consumer: " $1
curl -X DELETE http://192.168.99.100/pacticipants/$1 -i