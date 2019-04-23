#!/usr/bin/env bash

echo "Consumer: " $1
echo "Provider: " $2

json='{  
	"consumer": { 
		"name": "'$1'" 
	}, 
	"provider": { 
		"name": "'$2'"  
	}, 
	"request": { 
		"method": "POST",
		"url": "http://measurementsolutions.de:80/generic-webhook-trigger/invoke?token='$2'",
		"headers": { 
			"Accept": "application/json" 
		},
		"body": {
		    "thisPactWasPublished" : "${pactbroker.pactUrl}"
		}

	}, 
	"events": [{ 
		"name": "contract_content_changed" 
	}] 
}'

curl -d "$json"  \
-H "Content-Type: application/json" \
-X PUT http://localhost/webhooks/kh1jLTqBzAtCO7OnXOymHg -i

