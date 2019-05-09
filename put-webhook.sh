#!/usr/bin/env bash

echo "Consumer: " $1
echo "Provider: " $2
host=$(hostname) #$(hostname -I | awk '{print $1}' | tr -d '[:space:]')
echo "host: " $host

json='{  
	"consumer": { 
		"name": "'$1'" 
	}, 
	"provider": { 
		"name": "'$2'"  
	}, 
	"request": { 
		"method": "POST",
		"url": "http://'$host':8080/generic-webhook-trigger/invoke?token='$2'",
		"headers": { 
			"Accept": "application/json" 
		}

	}, 
	"events": [{ 
		"name": "contract_content_changed" 
	}] 
}'

url=http://192.168.99.100/webhooks/provider/$1/consumer/$2
echo "======= UPDATE WEBHOOK ======="
echo "URL:" $url
echo "============= Request Body =========="
echo "$json"
echo "====================================="

curl -d "$json"  \
-H "Content-Type: application/json" \
-X PUT http://localhost/webhooks/$3 -w -i

