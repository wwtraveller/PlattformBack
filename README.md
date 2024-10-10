# Expert Information Portal – Final Project Version

## Overview
This repository contains the final project for the Expert Information Portal (the backend part), developed as part of a programming course. It is designed to be an information-sharing platform for experts in various fields, offering tools for publishing articles, rating content, and interacting with other experts.

## Purpose
This version of the repository was created for the final project presentation and contains the codebase developed by a team of back-end developers. Further development of the project is now continued in a private repository.

## Features
- User registration and authentication (for experts).
- Sections for publishing and reading articles.
- Comments and ratings for articles.
- Search functionality for experts and articles.
- Admin panel for content management.

## Project Setup

### Environment Variables
To run this project, you need to configure the following environment variables:

#### Database Configuration:
- `DB_HOST` – The host of your database.
- `DB_PORT` – The port of your database.
- `DB_NAME` – The name of the database to connect to.
- `DB_USERNAME` – The username to authenticate with your database.
- `DB_PASSWORD` – The password to authenticate with your database.

#### Cloud Storage Configuration (DigitalOcean/AWS S3):
- `s3_accessKey` – Your cloud storage access key.
- `s3_endpoint` – The endpoint URL for your cloud storage (e.g., DigitalOcean Spaces or AWS S3).
- `s3_region` – The region where your cloud storage bucket is hosted.
- `s3_secretKey` – Your cloud storage secret key.

Ensure that these environment variables are correctly set up in your `.env` file or your hosting environment before running the project.

## Technologies Used
- **Front-end:** TypeScript, React, React Router, React Context API, React Hook Form, Axios, Webpack, Tailwind CSS, Draft.js.
- **Back-end:** Java Spring (Spring Boot, Spring Security), BcryptPass, Postgres, Mockito, Spring Boot Test, Swagger.
- **Additional Tools:** Docker, Postman for API testing, Jenkins for CI/CD, Selenium for test automation.

## Deployment
This project has been deployed and can be viewed at: https://platform-qxs32.ondigitalocean.app/.

Please note that this is the final project version, and active development has been moved to a private repository. For inquiries, please contact shilimovapro@gmail.com.

## Team Members
- **Front-end developers:** Alena Shilimova, Alexander Varnavin-Braun, Katja Weimer.
- **Back-end developers:** Alisa Tongaliuk, Taras Chaikovskyi, Denys Kovalenko, Alena Shilimova.
- **QA engineers:** Anastasiia Hryhorenko, Akmoor Zabytakhunova.

## Known Issues
- Some browser versions may experience content display issues.
- Registration errors may occur with non-standard email domains.

## License
This project is licensed under the MIT License – see the [LICENSE](./LICENSE.md) file for details.

## Contact Information
For further questions about this version of the project, please contact: shilimovapro@gmail.com.
