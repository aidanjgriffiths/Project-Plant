# Plantiful
#### A Powerful Embedded Monitoring Device for plant Enthusiasts
This page aims to document the source code, software and hardware incorporated in the development and production of the Plantiful prototype.

## Software
### Android Studio  
Used for developing a mobile app that will collect sensor data and provide information to the user regarding the state of their plants. 
Many people possess Android phones. Group members have experience programming in Java on this platform. 
### Arduino IDE 
IDE provides a coding platform for writing high level C/C++ based code that can be flashed to an Arduino board. 
Accessible coding platform. Runs on Windows. Basic setup and looping format. Many well-established libraries available. USB flashing. Debugging is simple using the provided serial terminal.
### Fusion 360 
Used for developing 3D designs for the hardware enclosure (prototype and final design). 
Accessible. At least one group member has experience developing models with this software. Parts can be assembled and animated. Enable direct export of 3D printing file. Schematic drawings are available for large scale production. 
### Cura 
Enables slicing and positioning of component pieces on 3D printing platform. 
Group member is in possession of this software and has experience using it for printing prototypes. 

## Hardware
### Uno R3 
Compact micro-processing unit with I/O ports for interfacing with moisture, temperature/humidity and light sensors. Minimal processing required except to transmit sensor data serially over Bluetooth.
Accessible, cost effective, and comes with an IDE that includes simplified coding functions for reading data from sensors and communicating serially with the proposed mobile application 
### Moisture sensor 
Measures soil moisture using two metal probes acting as a variable resistance. Will be used to determines if plant requires watering or is over-hydrated. 
Cheap, accurate and interfaces well with Arduino. No external libraries are required. 
### Temperature / Humidity sensor (DHT11)
Digital temperature and humidity sensor to determine ambient environmental parameters that determine the suitability of the conditions for optimal plant development. Highly reliable and fast response. 0-50C temperature range with error of +/- 2C. Easy to interface with Arduino. No external libraries required. 
### Ambient light sensor (DFR0026) 
Measures ambient light and reflects analog voltage to Arduino controller. Will be used to determine if the plant is receiving the correct amount of sunlight. 
Simple to use and interfaces easily with Arduino. No additional libraries required.
### HS-05 Bluetooth module 
Enables Bluetooth connection with mobile device to communicate sensor data serially. 
Easy to setup and maintain a reliable connection with a mobile phone. Security is not a priority given the non-critical nature of the sensor data collected.
