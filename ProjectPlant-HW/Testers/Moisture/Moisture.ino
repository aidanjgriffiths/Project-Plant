int min = 1024;
int max = 0;
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
  int moistureVal = analogRead(A1);
  if (moistureVal < min)
    min = moistureVal;

  if (moistureVal > max)
    max = moistureVal;

  Serial.print("C:");
  Serial.print(moistureVal);
  Serial.print("\tMin:");
  Serial.print(min);
  Serial.print("\tMax:");
  Serial.println(max);
  delay(500); 
  
}
