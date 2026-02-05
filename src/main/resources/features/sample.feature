Feature: print hello world
  Scenario: USER Scenario
   Given url 'https://fake-json-api.mock.beeceptor.com/users/1'
   When method get
    Then status 200

  Scenario: Product Scenario
   Given url 'https://fakestoreapi.com/products'
    And request {"id":0,"title":"string","price":0.1,"description":"string","category":"string","image":"http:\/\/example.com"}
   When method post
    Then status 201
