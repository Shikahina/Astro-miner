# Astro miner

This project is built with Java Spring Boot. JSoup library is used for scraping utilties. 

## Use case

Astrologers find the daily planetary positions from some app or website and calculate the difference between their positions to find the speed of the planets on a given day (24 hrs). 
This Application send data to https://github.com/Shikahina/DinaKadhi which displays the end result of their repeated calculations.

## Setup

1)After cloning ,navigate to the root project directory (it's where you can find pom.xml).\
2)run `mvn clean install`\

## How to run

In the project directory, you can run:

### `mvn spring-boot:run`

Runs the app in the development mode.\
Runs on [http://localhost:8080](http://localhost:8080). 


### Live Demo
Please feel free to access https://planet-backend-application.herokuapp.com/ to get a demo of this project. \
(Don't worry your internet is not slow ,the free Platform as a Service provider([Heroku](https://www.heroku.com/)) takes sometime to spin up their server,when the website is used once in a blue moon) .

## How to consume/check 

Use [Postman](https://www.postman.com/) to consume and test the REST end points available from the project. 

![planetposdiffreq](https://user-images.githubusercontent.com/62425476/128444451-ed908115-ad6b-427e-991e-df5e0a77de96.png)