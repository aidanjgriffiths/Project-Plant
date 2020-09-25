# Hardware design notes
Please see the Fritzing diagrams (or included exported JPG's) for details on how the spaghetti or breadboard versions are connected, or how the Testers are created.

# Hardware design notes - Naked MCU version

- A standard oscillator is formed using the following parts:
    * 16MHz Crystal - Across pins 9 and 10
    * 2x 22pf ceramic capacitors - tie pins 9 and 10 to ground.

- A standard 5V+ rail is generated using the following parts:
    * 7805 5V Regulator
    * 33uF Electrolytic capacitor - directly across the input pins. 
        This is a MINIMUM value part - larger is ok.
    * 9V Battery Snap - Fed into the In and Ground pins of the 7805 

- A standard 3.3V rail is generated using the following parts:
    * LM3940 3.3V regulator - Takes input from 7805 output
    * 100nF ceramic capacitor - Across the outputs of the LM3940.
        This is a precise part - a capacitance higher or lower than this
        runs the risk of an unstable or incorrect 3.3v rail

- The ATMega328 chip must have the following:
    * A 10kOhm pullup resistor to pin 1 (the reset line)
    * A small momentary switch from pin 1 to ground 
        - This is to allow programming by holding the reset pin low until the
          code is about to be uploaded, then released. This allows the bootloader
          to operate correctly and flash the new code.
    * A 47uF capacitor on the supply pins to help smooth voltage

- The bluetooth module is connected via a 3.3v level converter
    - The HC-05 must have the following:
        - The Enable pin must be tied to ground to prevent it entering AT Mode at startup
        - The VCC and ground must be sourced via the 3.3v rails
        - The TX pin on the HC-05 must be connected via the level shifter to pin 17
        - The RX pin on the HC-05 must be connected via the level shifter to pin 16

- The LDR operates with a matched 5kOhm resistor in a voltage divider configuration
    - One side of the divider goes to ground, the other to 5V, and the signal in
      between the two devices is fed into pin 23
    - PLEASE NOTE: The unit needs to be calibrated for the range of the particular LDR/resistor
      combination. Please see the README.md under the LDR Tester folder.

- The feedback LED is connected to pin 19 and ground via a 1kOhm resistor.
    - Note that this is higher than normal for a bright LED - This keeps the current
      down and therefore the battery usage lower.

- The DHT22 temperature/humidity sensor requires the following:
    - Vcc connected to the 5V rail
    - A solid ground connection
    - The data pin connected to pin 13 of the microcontroller