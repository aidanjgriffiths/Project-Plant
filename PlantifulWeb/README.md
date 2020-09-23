# Plantiful Web Application
The Plantiful Web Application is a Web Interface to the Plantiful Monitoring System, which allows a User to View their Data, and Analyse Statistics based on their Monitoring Activities and Health Patterns

## Hardware Infrastructure
### MySQL Cloud Database
This is used to centralise all user data for access from the Web Application. User Data is propagated from the Android Application to the Cloud Database, through the Web API. This is a one way connection between the Web Application and Android Device. The data which is propagated to the Cloud Database is never deleted, and acts as a history of the Activities of the User and the Plantiful System.

### Web Server 
The Web Server is an _Amazon Lightsail Instance_, which hosts a LAMP Stack of Web Technology. The LAMP Stack consists of the following Technologies:

+ Apache HTTP Server
+ PHP Server-Side Processing Language
+ Linux Operating System
+ MySQL Default Database (Redundant due to manual MySQL Cloud Database)

The Web Server hosts the _Hypertext Documents and Resources for the Website_, which is retrieved by a Client when requested by a Browser.

The Web Server also hosts the _Web API_ for access via the Android Application, for simple propagation of data to the Data Source.

### Client
The Client in the Computational Device which supports access the Web Application, via a Web Browser. The Client makes HTTPS requests for the Web Application from the Web Server, which supplies the Web Pages and Resources.

The Client hosts several support modules, which reduces development time. The following support modules are in the latest commit of the Web Application:

+ QR Module for Generating and Displaying QR Codes based on a generated String ID
+ JQuery Module for simpler Document Object Model (DOM) traversal
+ Chart.JS for efficient and effective Graohs and Visualisations of Data

The AJAX (Asynchronous Javascript and XML) approach is used to create Asynchronous calls to the Web Server, to retrieve certain data from the Data Source. This provides a dynamic experience for the user, and allows for changes to be made to the Web Application, based on modifications of the Data Source by other devices (for example, the Android Aplication).

### Android Device
The Android Device hosts the Android Application, which provides the functions for Monitoring Plants and Creating Plant Profiles for the User's Garden. This Data is propagated to the Database via a Web Communications Module in the Android Application. This Communciations Module interfaces with the database via Web API Calls to the database. This layer of abstraction between the Android Device and the Database allows for middle-man processing, and erroneous / currupt data prevention.

## Software Infrastructure
The following Software Stack was used to process and display the data and User Interface. 

### Web Front-End and Back-End Technologies

+ HTML5 for structuring and designing the framework and content of the Web Application
+ CSS3 for formatting and styling the Web Application 
+ PHP for Server-Side processing and access to the Data Source
+ JavaScript for client-side processing and asynchronous requests for resources from the Web Server

## Security
### SSL/ TLS Let's Encrypt
The Web Application is certified via _Let's Encrypt_ for Secure Connections. This means the Android Device and Client (Web Browser) Users can connect to the Web Server via HTTPS (Hypertext Transfer Protocol Secure), which ensures that the bidirectional communications between the Web Server and Client are encrypted, therefore preventing a large degree of Man-in-the-Middle attacks. 
