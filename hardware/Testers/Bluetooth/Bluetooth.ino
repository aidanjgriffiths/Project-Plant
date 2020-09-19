#include <SoftwareSerial.h>
#define BLUETOOTH_RX 11
#define BLUETOOTH_TX 10
#define BLUETOOTH_BAUD 9600

char buf[30];

SoftwareSerial bluetooth(BLUETOOTH_RX, BLUETOOTH_TX);

void setup()
{
  bluetooth.begin(BLUETOOTH_BAUD);
}

void loop()
{
  if (bluetooth.available())
  {
    char c = bluetooth.read();
    c = tolower(c);
    if(c == 'h')
    {
      strcpy(buf, "Hello!");
      strcat(buf, "\r\n");
      bluetooth.print(buf);
    }
  }
}
