#include <SoftwareSerial.h>
#define VERSION "SIM0.2"
#define BLUETOOTH_RX 11
#define BLUETOOTH_TX 10
#define BLUETOOTH_BAUD 9600

char numbers[5];
char buf[30];
int temp = 25;
int humidity = 65;
int light = 50;
int moisture = 50;

SoftwareSerial bluetooth(BLUETOOTH_RX, BLUETOOTH_TX);

void setup()
{
  bluetooth.begin(BLUETOOTH_BAUD);
}

void adjustValue(int &adjustVariable, int adjustPressure)
{
  if (adjustPressure < 33) adjustVariable -= 2;
  else if (adjustPressure > 66) adjustVariable += 2;
}

void loop()
{
  if (bluetooth.available())
  {
    char c = bluetooth.read();
    c = tolower(c);
    switch (c)
    {
      case 'v':
        strcpy(buf, VERSION);
        strcat(buf, "\r\n");
        bluetooth.print(buf);
        break;
      case 'r':
        adjustValue(temp, random(15, 35));
        adjustValue(humidity, random(20, 80));
        adjustValue(light, random(100, 5000));
        adjustValue(moisture, random(20, 80));

        strcpy(buf, "T");
        strcat(buf, itoa(temp, numbers, 10));
        strcat(buf, "H");
        strcat(buf, itoa(humidity, numbers, 10));
        strcat(buf, "L");
        strcat(buf, itoa(light, numbers, 10));
        strcat(buf, "M");
        strcat(buf, itoa(moisture, numbers, 10));
        bluetooth.println(buf);
        break;
    }
  }
}
