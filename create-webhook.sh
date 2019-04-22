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
		"url": "http://127.0.0.1:8084/job/'$2'/build", 
		"headers": { 
			"Accept": "application/json" 
		} 
	}, 
	"events": [{ 
		"name": "contract_content_changed" 
	}] 
}'

echo "$json"

curl -d "$json"  \
-H "Content-Type: application/json" \
-X POST http://localhost/webhooks/provider/book-catalog-service/consumer/books-client-catalog-rest-consumer

