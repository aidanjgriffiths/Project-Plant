# DFRobot Moisture Sensor

This sensor simply returns a 0-5v signal based on the amount of moisture, with 0 being bone dry (no current between probes), and 5v, being dipped in very salty water. This sketch simply monitors and returns the current sensor reading as well as the minimum and maximum since power on. Since each sensor will behave slightly different to any other, these values are sensor-dependent and need to be found for every individual unit.

## Use

Upload the sketch to the device. Then, connect to the hardware serial UART. The device will output the current moisture reading, as well as maintain and display the minimum reading and maximum reading since poweron. Using the current reading you can test the responsiveness of the device, as well as record the minimum and maximum by moving the device from open air to a saturated salt-water solution, and using these values to set the relevant values in the main firmware sketch. (RAW_SOIL_MIN and RAW_SOIL_MAX).