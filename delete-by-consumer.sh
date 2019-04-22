#!/usr/bin/env bash
echo "Consumer: " $1
curl -X DELETE http://localhost/pacticipants/$1