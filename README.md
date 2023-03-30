# Yoga

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 14.1.0.

#

## Prerequisites (the software used on Windows)

> MySQL

> Visual Studio Code

> Spring Tool Suite

> Postman

> Lombok (jar file)

#

## How to install the MySQL database

### 1. Prepare MySQL

- Database name : test
- Host : localhost
- Port : 3306
- Username : user
- Password : 123456

### 2. Create the schema

- The [SQL script file script.sql](https://github.com/Lob2018/Lobjois_Yann_5_Savasana_032023/blob/main/ressources/sql/script.sql), is available at the root of the project (`/ressources/sql/script.sql`)

### 3. Database default content

- 1 admin account :
  - login: yoga@studio.com
  - password: test!1234
- 2 teachers

#

## How to install the app

### 1. Clone the repository

- git clone <https://github.com/OpenClassrooms-Student-Center/P5-Full-Stack-testing>

### 2. The back-end (Java)

- Is available at the root of the project (`/back`)
- Open it with Spring Tool Suite IDE
- Right click on the folder > Run As > Maven install

### 3. The front-end (Angular)

- Is available at the root of the project (`/front`)
- Open it with Visual Studio Code IDE
- With the terminal go inside `/front` > npm install

#

## How to operate the app

### 1. Start the database

### 2. Start the back-end

- With Spring Tool Suite IDE
- Boot DashBoard > Select back > Start the process associated
- Host : localhost
- Port : 8080

### 3. Start the front-end

- With Visual Studio Code IDE
- Open the terminal go inside `/front` > npm run start
- Host : localhost
- Port : 4200
- Open your browser at localhost:4200

#

## How to run the different tests

### 1. The end-to-end tests (with Cypress)

- With Visual Studio Code IDE
- Open the terminal go inside `/front` > npm run e2e
- Cypress opens > Choose a Browser > Click on Sart E2E Testing > Click on all.cy.ts for all tests

### 2. The front-end tests (with Jest)

- With Visual Studio Code IDE
- Open the terminal go inside `/front` > npm run test (or for watch mode npm run test:watch)

### 3. The back-end tests (with Spring Boot Test)

- With Spring Tool Suite IDE
- Right click on the folder > Run As > Maven test

### 4. Test manually the back-end (start the datatbase and the backend before)

- With Postman
- Import the Postman collection available at the root of the project (`/ressources/postman/yoga.postman_collection.json`)
- Login, copy the token
- update and save the new Token in authorization

#

## How to generate the different coverage reports (run the different tests before)

### 1. The end-to-end tests Cypress coverage

- With Visual Studio Code IDE
- Open the terminal go inside `/front` > npm run e2e:coverage
- Open the index.html file from the root of the project (`/front/coverage/lcov-report/index.html`)

### 2. The front-end tests Jest Coverage

- With Visual Studio Code IDE
- Open the terminal go inside `/front` > npm test -- --coverage
- Open the index.html file from the root of the project (`/front/coverage/jest/lcov-report/index.html`)

### 3. The back-end tests Jacoco coverage

- Open the index.html file from the root of the project (`/back/target/site/jacoco/index.html`)

#

## Coverage screenshots

- [PNG screenshots](https://github.com/Lob2018/Lobjois_Yann_5_Savasana_032023/tree/main/coverage)
 are available at the root of the project (`/coverage/*`)