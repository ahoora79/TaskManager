![image](https://github.com/user-attachments/assets/20a9abfb-52bf-4052-bdf8-9d39b471735e)# TaskManager
A simple website for managing tasks by users.
TaskManager
A simple website for managing tasks by users.

**0.** Make sure you have installed Java and maven . (you can use below link to download java and install it (OpenJDK19U-jdk_x64_windows_hotspot_19.0.2_7.msi)). also make sure .mvn directory placed on root directory:
```
https://github.com/adoptium/temurin19-binaries/releases/tag/jdk-19.0.2%2B7
```

**1.** Run the Maven command to clean and build the project, skipping tests to speed up the process:
```
mvn clean install -DskipTests
```
**2.** Use Docker Compose to build the Docker images defined in your docker-compose.yml file:
```
docker-compose bulild
```
**3.** Use Docker Compose to start up the application and its dependencies as specified in your docker-compose.yml file:
```
docker-compose up
```
Now all services are running : 

<img width="666" alt="image" src="https://github.com/user-attachments/assets/ca051636-2541-48ac-98f2-d63cd48966ca">

Finally, access the user page at (I created a task , then updating):

![alt text](image.png)

I also wrote a number of tests to verify the functionality of the web application, including unit tests (**UserServiceTest**):
![image](https://github.com/user-attachments/assets/757e093d-50f1-4fc0-abde-9015d8baba8f)

and also Integration tests (**TaskManagerApplicationTests**):  
![image](https://github.com/user-attachments/assets/d34e93be-a680-4e50-8970-b6793099d3c1)
