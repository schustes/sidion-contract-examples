package contracts

import org.springframework.cloud.contract.spec.Contract


Contract.make {
    description("Example messaging contract")
    // Label by means of which the output message can be triggered
    label 'book-order'
    // input to the contract
    input {
        // the contract will be triggered by a method
        triggeredBy('bookOrdered()')
    }
    // output message of the contract
    outputMessage {
        // destination to which the output message will be sent
        sentTo 'order-exchange'
        body(
                [
                "isbn": $(regex("[0-9]{3}-[0-9]{1}-[0-9]{5}-[0-9]{3}-[0-9]{1}")) ,
                "customerId": $(regex("[0-9]*"))
                ]
        )
        headers {
            messagingContentType(applicationJson())
        }
    }
}
