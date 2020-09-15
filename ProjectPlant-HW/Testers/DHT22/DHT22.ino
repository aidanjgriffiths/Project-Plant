#include <DHT.h>; //Adafruit DHT library
DHT dht(7, DHT22);

float humidity;
float temperature;

void setup()
{
  Serial.begin(9600);
  dht.begin();
  delay(2000); //This is required to give the DHT time to start/stabilise
}

void loop()
{
    humidity    = dht.readHumidity();
    temperature = dht.readTemperature();
   
    Serial.print("Humidity: ");
    Serial.print(humidity);
    Serial.print(" Temperature: ");
    Serial.println(temperature);
    delay(2000); 
}

   
