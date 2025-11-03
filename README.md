**Quiz Management readme file** 

A Spring Boot + Thymeleaf + MySQL based online quiz platform.
Supports Admin (quiz creation, question management) and Participant (taking quizzes and viewing scores).


**Tech Stack**

Layer	      Technology

Backend	    Spring Boot, Spring MVC, Spring Data JPA

Frontend	  Thymeleaf, Bootstrap

Security	  Spring Security

DB	        MySQL / H2

Tests      	JUnit, Mockito

Deployment	Docker, Railway

 Users
| Role        | Username               | Password     |
| -------     | ------------------     | ------------ |
| Admin       | `admin@123.com`        | `123456`     |
| Participant | `participant1@123.com` | `123456`     |


**Features:**
1. User(admin & participant) Registration & Login

2. Quiz management (CRUD)

3. Question Creation as per Quiz topic management

4. Show Question with coreect answer

5. Quiz Attempt by participant

6. Participant viewing scores

7. Swagger REST API Documentation

**Entity Flow**

Admin (1) -----< Quiz >------ (n) Question
                      \
                       >------ (n) Attempt
                                 |
                                 V
                             Participant (1)

Admin creates Quizzes and Questions

Participants take quizzes → results stored in Attempts

** Railway Deployment Notes**

Push latest code to GitHub

In Railway → “New Project” → “Deploy from GitHub” : https://github.com/Jasam95/quiz-management

Add Environment Variables:

spring.datasource.url=jdbc:mysql://root:VyFkMJCPSkPuKtsRLQarIuamBGpnnbIM@shinkansen.proxy.rlwy.net:39181/railway

spring.datasource.username=root

spring.datasource.password=VyFkMJCPSkPuKtsRLQarIuamBGpnnbIM



**Deployes Railway Url:** quiz-management-production.up.railway.app


Swaggers main End Point:

| Endpoint         | Method | Description          |
| ---------------- | ------ | -------------------- |
| `/api/quizzes`   | GET    | List all quizzes     |
| `/api/questions` | POST   | Add question to quiz |
| `/api/attempts`  | GET    | Fetch user attempts  |


