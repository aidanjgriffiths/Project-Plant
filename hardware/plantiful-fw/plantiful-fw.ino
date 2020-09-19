#include <DHT.h> //Adafruit DHT library
#include <SoftwareSerial.h>
#define VERSION "0.2"
//Comment this out when NOT debugging the hardware.
//#define DEBUG 1
#define BLUETOOTH_RX 11
#define BLUETOOTH_TX 10
#define BLUETOOTH_BAUD 9600
#define DHT_PIN 7
#define RAW_SOIL_MIN 0
#define RAW_SOIL_MAX 780
#define RAW_LIGHT_MIN 320
#define RAW_LIGHT_MAX 1017
#define ADJ_SOIL_MIN 0
#define ADJ_SOIL_MAX 100
#define ADJ_LIGHT_MIN 0
#define ADJ_LIGHT_MAX 120000
#define FEEDBACK_LED_PIN 13

DHT dht(DHT_PIN, DHT22);
SoftwareSerial bluetooth(BLUETOOTH_RX, BLUETOOTH_TX);

float humidity;
float temperature;
unsigned long light;
int moisture;
int rawLight;
int rawMoisture;

char buf[30];


void setup() {
  #ifdef DEBUG
    Serial.begin(9600);
    Serial.println("Debug Enabled");
  #endif
  
  pinMode(FEEDBACK_LED_PIN, OUTPUT);
  digitalWrite(FEEDBACK_LED_PIN, LOW);
  
  bluetooth.begin(BLUETOOTH_BAUD);
  dht.begin();
  delay(2000); //This is required to give the DHT time to start/stabilise
}

void loop() {
    humidity    = dht.readHumidity();
    temperature = dht.readTemperature();
    rawLight = analogRead(A0);
    rawMoisture = analogRead(A1);
    rawLight = max(RAW_LIGHT_MIN, rawLight);
    rawLight = min(RAW_LIGHT_MAX, rawLight);
    light = map(rawLight, RAW_LIGHT_MIN, RAW_LIGHT_MAX, ADJ_LIGHT_MIN, ADJ_LIGHT_MAX);
    moisture = map(rawMoisture, RAW_SOIL_MIN, RAW_SOIL_MAX, ADJ_SOIL_MIN, ADJ_SOIL_MAX);

    if (bluetooth.available())
    {
      char c = bluetooth.read();
      c = tolower(c);
      if(c == 'v')
      {
        strcpy(buf, VERSION);
        strcat(buf, "\r\n");
        bluetooth.print(buf);
      }
      else if (c == 'r')
      {
        digitalWrite(FEEDBACK_LED_PIN, HIGH);
        delay(100);
        digitalWrite(FEEDBACK_LED_PIN, LOW);
        char numbers[5];

        temperature = 26;
        humidity = 70;
        light = 600;
        moisture = 80;
        
        strcpy(buf, "T");
        strcat(buf, itoa(temperature, numbers, 10));
        strcat(buf, "H");
        strcat(buf, itoa(humidity, numbers, 10));
        strcat(buf, "L");
        strcat(buf, ltoa(light, numbers, 10));
        strcat(buf, "M");
        strcat(buf, itoa(moisture, numbers, 10));
        
        #ifdef DEBUG
          Serial.println(buf);
        #endif
        
        bluetooth.println(buf);
      }
    }
}
