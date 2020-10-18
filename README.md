# Spring Boot Web-Socket demo 

This demo uses socket.io

### Instructions:
* Clone this repo   
* Build project using `mvn clean install`  
* Start server using   
  ```
  mvn spring-boot:run
  or
  java -jar target/spring-boot-websocket-demo-0.0.1-SNAPSHOT.jar  
  ```  
* Open browser, and go to [http://localhost:8080](http://localhost:8080)  
* Enter Company Id (e.g. 1) in text box and click 'Connect' button.  
* You will see periodic incoming messages from the server.  
* Open another browser in incognito, and go to [http://localhost:8080](http://localhost:8080)  
* This time enter another Company Id (e.g. 12) in text box and click 'Connect' button.  
* Now open terminal and enter following command to send data to client,  
  ```
    curl http://localhost:8080/api/data?companyId=12  
  ```
* You will see some message on browser of client with companyId = 12.  
* To stop the server, Press Ctrl + C.  