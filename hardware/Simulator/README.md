# Using the simulator

## Hardware
HC-05 should be connected via a 3.3v level shifter so that RX *from the HC-05* goes to pin 10 on the Arduino and TX *from the HC-05* goes to pin 11 on the Arduino. Also note that while you can supply the HC-05 with 5v for it's supply, things tend to be a litte more stable if run from the 3.3v from the Arduino, this will be required for the level shifter at any rate.

## Software

Once a serial connection is created to the HC-05, a single character is all that is required to illicit a response, with the idea being that this communication protocol will directly mirror that of the finished design. 

### Commands

`v` - Version: Return the version of the firmware currently installed. Simulator will return `SIMx.yy`, "proper" version will return "x.yy".
`r` - Reading: Take a reading and return all the values in a single burst. In the case of the simulator, there are defaults for all of the readings, but these readings will randomly drift for each reading.