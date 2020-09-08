# Bill of Materials

## Introduction

This document will outline the total list of parts required for each variation/version of the hardware, including testers and intermediate revisions. This document will only detail the materials required. Connection and operation will be left to the design document.

## Testers
### Bluetooth Tester

* Arduino Uno/Nano
* HC-05 Bluetooth Module
* 5v-3.3v level converter module
* M-F Dupont wires

### DHT22 Tester

* Arduino Uno/Nano
* DHT22 Humidity/Temperature Sensor
* M-F Dupont wires

### LDR Tester

* Arduino Uno/Nano
* 5Kohm Light Dependent Resistor (CdS cell)
* 5.6Kohm 5% 1/4W resistor
* M-F Dupont wires
* Light duty hookup wire

### Moisture Tester

* Arduino Uno/Nano
* DFRobot resistive moisture sensor 

## "Spaghetti" version

Note: this is the first cut of the hardware design prior to using a breadboard to begin simplifying the setup.

* Arduino Uno/Nano
* HC-05 Bluetooth Module
* 5v-3.3v level converter module
* DHT22 Humidity/Temperature Sensor
* 5Kohm Light Dependent Resistor (CdS cell)
* 5.6Kohm 5% 1/4W resistor
* DFRobot resistive moisture sensor
* M-F Dupont wires
* Light duty hookup wire

## "Breadboard" version

Note: This is the first cut of the hardware design, using a breadboard to simplify the "spaghetti" wiring first used"

* Arduino Uno/Nano
* HC-05 Bluetooth Module
* 5v-3.3v level converter module
* DHT22 Humidity/Temperature Sensor
* 5Kohm Light Dependent Resistor (CdS cell)
* 5.6Kohm 5% 1/4W resistor
* DFRobot resistive moisture sensor
* M-F Dupont wires
* Light duty hookup wire
* Small breadboard (2x power rails, 2 sides of 5x30 tie points)

## "Naked MCU" version

Note: This is the first cut of the hardware design, using a raw ATMega328 microcontroller on a breadboard. This is the most physically robust design.

* Atmel ATMega328-PU DIP Microcontroller with Arduino bootloader
* USB-to-serial adapter (or Arduino Uno with ZIF Socket for programming)
* 16MHz Crystal
* 2x 22pf ceramic capacitors
* 7805 5V Regulator
* LM3940 3.3V regulator
* 9v battery snap
* 5mm green LED
* 3.3uF 5+V electrolytic capacitor
* 2x 47uF 5+V electrolytic capacitor
* 100nF ceramic capacitor
* Solid-core breadboard wiring (will be cut into individual lengths)
* 1kohm 5% 1/4w resistor
* 10kohm 5% 1/4w resistor
* Breadboard compatible momentary switch (can use a simple jumper wire if careful for reset of MCU when programming)
* HC-05 Bluetooth Module
* DHT22 Humidity/Temperature Sensor
* 5Kohm Light Dependent Resistor (CdS cell)
* 5.6Kohm 5% 1/4W resistor
* DFRobot resistive moisture sensor
* M-F Dupont wires
* Light duty hookup wire
* Small breadboard (2x power rails, 2 sides of 5x30 tie points)