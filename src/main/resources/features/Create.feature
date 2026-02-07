Feature: variable post request

  Scenario: Product Scenario
    Given url 'https://fakestoreapi.com/products'
    * def requestPayload = {}
    * requestPayload.id = 0
    * requestPayload.title = 'string'
    * requestPayload.price = 0.1
    * requestPayload.description = 'string'
    * requestPayload.category = 'string'
    * requestPayload.image = 'http://example.com'
    And request requestPayload
    When method post
    Then status 201
    And match response.title == 'string'

  Scenario: Product Scenario1
    Given url 'https://fakestoreapi.com/products'
    * def requestPayload = read('payload/CreatePayload.json')
    * requestPayload.title = 'Product from JSON File'
    And request requestPayload
    When method post
    Then status 201
    And match response.title == 'Product from JSON File'

   Scenario: Create sample Json Object with variable
     * set jsonObjectPayload
       | path  | value    |
       | id    | 12       |
       | title | 'Sample' |
     * print jsonObjectPayload


  Scenario: Create sample Json Object with variable
    * def dataFaker = Java.type('net.datafaker.Faker')
    * def dataFakerObject = new dataFaker()
    * def idValue = dataFakerObject.number().numberBetween(1000, 9999)
    * def titleValue = dataFakerObject.commerce().productName()
    * set jsonObjectPayload
      | path  | value      |
      | id    | idValue    |
      | title | titleValue |
    * print jsonObjectPayload