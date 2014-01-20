cyril-basic-mongodb-and-rest
============================

MongoDB and REST 


To Auth with OAuth2 with CURL:

1- Request Token (using password)
curl -X POST -d "client_id=cyrril&client_secret=secret&grant_type=password&username=marissa&password=koala" http://localhost:8080/services/oauth/token
--> {"additionalInformation":{"entry":[]},"expiration":"2014-01-21T03:08:43.267","expiresIn":43199,"refreshToken":{},"scope":["read","write"],"tokenType":"bearer","value":"f413b59c-af06-4eea-b6d6-46c0e8df72f6"}

2- Get resources with the token in header
curl -X GET  --header "Authorization: Bearer f413b59c-af06-4eea-b6d6-46c0e8df72f6" http://localhost:8080/services/post
curl -X GET  --header "Authorization: Bearer f413b59c-af06-4eea-b6d6-46c0e8df72f6" http://localhost:8080/services/post/52d3e54b6a0865d5ab58c060

3- Unauthorize access
curl -X GET  http://localhost:8080/services/post/52d3e54b6a0865d5ab58c060
--> {"error":"unauthorized","error_description":"An Authentication object was not found in the SecurityContext"}



