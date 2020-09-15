int min = 1024;
int max = 0;
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
  int lightVal = analogRead(A0);
  if (lightVal < min)
    min = lightVal;

  if (lightVal > max)
    max = lightVal;

  Serial.print("C:");
  Serial.print(lightVal);
  Serial.print("\tMin:");
  Serial.print(min);
  Serial.print("\tMax:");
  Serial.println(max);
  delay(500); 
  
}
