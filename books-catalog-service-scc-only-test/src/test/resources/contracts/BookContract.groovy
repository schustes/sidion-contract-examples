package contracts

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url '/books'
        headers {
            contentType('application/json')
        }
    }
    response {
        status 200
        body([
                 ["id":$(regex("[0-9]*")), "authorFirstName":$(regex("[a-zA-Z]*")), "authorLastName":$(regex("[a-zA-Z]*"))
                  , "title": $(regex(".*")), "isbn": $(regex("[0-9]{3}-[0-9]{1}-[0-9]{5}-[0-9]{3}-[0-9]{1}")) ]
        ])
        headers {
            contentType('application/json')
        }
    }
}
