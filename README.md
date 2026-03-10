
For Welcome
```agsl
http://localhost:8080/
```

Post Request to generate jwt token
```
http://localhost:8080/login

{
"userName": "santosh",
"password": "password"
}
```

Once after generating jwt code using below postman endpoints verify
copy the generated token 
Add Header field in key *Authentication*, value -> *Bearer generated_jwt_token*

```agsl
http://localhost:8080/product
```


