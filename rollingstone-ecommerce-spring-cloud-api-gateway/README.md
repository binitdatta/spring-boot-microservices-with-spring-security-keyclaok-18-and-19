# Spring Cloud API Gateway 

## How to Test

## First get an Authorization Code

``` 
- 1. Open a new Postman Tab
- 2. Enter GET as HTTP Method
- 3. URL as http://localhost:8080/auth/realms/binitdattarealm/protocol/openid-connect/auth
- 4. Request Parameters as shown below
```


![Alt text](images/2.png?raw=true "Title")

## Then Copy the full URL from Postman and Open a Browser to Fire the URL
## If asked to login, user your Realm username and password
## Copy the value for the "code"

![Alt text](images/1.png?raw=true "Title")





## Paste the value for the "code" in the new Postman tab below and Click Send
## Then Copy the Access Token Value

![Alt text](images/5.png?raw=true "Title")


## Copy the Access Token Value and Open a new Postman Tab
## Click on the Headers Tab
## Enter new Header as Authorization as Key and "Bearer ...." [... replace the ... with your token value]


## Now change the method to GET
## URL as http://localhost:8092/api/category/
## Navigate to the Headers Tab and enter two Headers
## Accept and Content-Type both as application/json




## Press Send


![Alt text](images/6.png?raw=true "Title")

## Now change the method to POST
## Navigate to the body tab and enter the following body

``` 
{
    "categoryName": "Travel Luggage",
    "categoryDescription": "Travel Luggage"
}
```



![Alt text](images/7.png?raw=true "Title")

# Results of POST Method
![Alt text](images/8.png?raw=true "Title")

## Verify the Newly Created Category with GET Method


![Alt text](images/9.png?raw=true "Title")

## Update the Category with PUT Method

![Alt text](images/10.png?raw=true "Title")



# Verify the Newly Updated Category with GET Method

![Alt text](images/11.png?raw=true "Title")

# Delete the Newly Created Category
![Alt text](images/12.png?raw=true "Title")

# Change the path to /api/product/ and Click Send

![Alt text](images/13.png?raw=true "Title")

# 
![Alt text](images/14.png?raw=true "Title")

# 
![Alt text](images/15.png?raw=true "Title")

# Change the Method to POST
## Use the Following Body
```
{
    "productCode": "P09062001",
    "productName": "Samsung 160 inch TV",
    "shortDescription": "",
    "longDescription": "160 inch Samsung Television with Retina Display",
    "canDisplay": true,
    "productPrice": 21.99,
    "productDiscountPercentage": 10.3,
    "parentCategory": {
        "id": 3,
        "categoryName": "Electronics",
        "categoryDescription": "Electronics"
    },
    "category": {
        "id": 4,
        "categoryName": "Televison",
        "categoryDescription": "Televison"
    },
    "deleted": false,
    "automotive": false,
    "international": true
}
```


![Alt text](images/16.png?raw=true "Title")

# Verify the Newly Created Product with GET
![Alt text](images/17.png?raw=true "Title")


# Update the NEwly Created Product with PUT
## Use The Following Body


``` 
{
    "id":10,
    "productCode": "P09062001",
    "productName": "Samsung 160 inch TV Updated",
    "shortDescription": "",
    "longDescription": "160 inch Samsung Television with Retina Display",
    "canDisplay": true,
    "productPrice": 21.99,
    "productDiscountPercentage": 10.3,
    "parentCategory": {
        "id": 3,
        "categoryName": "Electronics",
        "categoryDescription": "Electronics"
    },
    "category": {
        "id": 4,
        "categoryName": "Televison",
        "categoryDescription": "Televison"
    },
    "deleted": false,
    "automotive": false,
    "international": true
}
```
![Alt text](images/18.png?raw=true "Title")

# Verify the Update with GET
![Alt text](images/19.png?raw=true "Title")

# Delete the Newly Created / Updated Product
![Alt text](images/20.png?raw=true "Title")

# 21
![Alt text](images/21.png?raw=true "Title")



