# HC-05 Bluetooth module

This tester provides basic testing of the HC-05 Bluetooth module. The SoftwareSerial library is used for communication as this allows the hardware UART on the microcontroller to be left available for easy firmware updates and in-situ serial debugging. Performance is more than adequate for this use-case.

## Use
Upload the sketch, and use a serial app on a test phone unit connected via Bluetooth to the HC-05 module to send a single 'h' character. The device should respond with a "Hello!" string if the connection is successful and working.