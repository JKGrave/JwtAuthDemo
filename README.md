JWT AUTH DEMO
=============

Compose
-------------

### 1. api
    check token is valid
### 2. client
    get client information
### 3. front
    TODO will create (with Vue or Angular)
### 4. jwtauth
    create jwt token with 'client' & 'user' module
### 5. user
    get user information
    
How to use
-------------
### 1. Before begin
1. install 'MySql'
2. create databases with named 'user', 'client' (tables will automatically generate with JPA)
3. change 'application.yml' files from all modules
    <pre>
    <code>
    ...
    spring:
        profiles:
            active: localdev
    ...
    </code>
    </pre>
4. (Optional) install 'chrome' browser. install 'postman' from 'chrome app store'

### 2. Begin
#### 2.1 run
1. run separately (TODO Fix...)
2. open 'postman'
#### 2.2 request token
3. request token to 8080 port(a.k.a. 'jwtauth' module) with 'POST' method
> http://foo:bar@localhost:8080/oauth/token

with parameters
> username: user
<br/> password: password
<br/> grant_type: password
<br/> scope: read
#### 2.3 authorization
1. receive jwt token 
2. auth with 8081(a.k.a. 'api' module) with 'GET' method with header information

> http://localhost:8081/test/

> Authorization: bearer \<jwt token value\>

#### 2.4 result
3. result will like
> api Test Success