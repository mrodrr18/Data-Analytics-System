/*
  Lora Send And Receive
  This sketch demonstrates how to send and receive data with the MKR WAN 1310 LoRa module.
  This example code is in the public domain.
*/

#include <MKRWAN.h>

LoRaModem modem;

#include "arduino_secrets.h"
#include <math.h>
 
const int B = 4275;               // B value of the thermistor
const int R0 = 100000;            // R0 = 100k
const int pinTempSensor = A0;     // Grove - Temperature Sensor connect to A0

String appEui = SECRET_APP_EUI;
String appKey = SECRET_APP_KEY;

void setup() {
  
  Serial.begin(115200);
  
  randomSeed(analogRead(0));
  // change this to your regional band (eg. US915, AS923, ...)
  if (!modem.begin(EU868)) {
    Serial.println("Failed to start module");
    while (1) {}
  };
  Serial.print("Your module version is: ");
  Serial.println(modem.version());
  Serial.print("Your device EUI is: ");
  Serial.println(modem.deviceEUI());

  int connected = modem.joinOTAA(appEui, appKey);
  
  if (!connected) {
    connected = modem.joinOTAA(appEui, appKey);
    Serial.println("Something went wrong; are you indoor? Move near a window and retry");
    while (1) {}
  }

  // Set poll interval to 60 secs.
  modem.minPollInterval(60);
  // NOTE: independently by this setting the modem will
  // not allow to send more than one message every 2 minutes,
  // this is enforced by firmware and can not be changed.
}


void loop() {
  // put your main code here, to run repeatedly:
  int a = analogRead(pinTempSensor);
 
    float R = 1023.0/a-1.0;
    R = R0*R;
 
  float temperature = 1.0/(log(R/R0)/B+1/298.15)-273.15; // convert to temperature via datasheet
  
  
  if (isnan(temperature) ) {
    Serial.println("Failed to read from sensor!");
    delay(1000);
    return;
  }
  Serial.print("Temperatura: ");
  Serial.println(temperature);
  
  int err;
  modem.beginPacket();
  modem.print(temperature);
  err = modem.endPacket(false);
  if (err > 0) {
    Serial.println("Message sent correctly!");
  } else {
    Serial.println("Error sending message :(");
    Serial.println("(you may send a limited amount of messages per minute, depending on the signal strength");
    Serial.println("it may vary from 1 message every couple of seconds to 1 message every minute)");
    
  }
  delay(60000);
  if (!modem.available()) {
    Serial.println("No downlink message received at this time.");
    return;
  }
  char rcv[64];
  int i = 0;
  while (modem.available()) {
    rcv[i++] = (char)modem.read();
  }
  Serial.print("Received: ");
  for (unsigned int j = 0; j < i; j++) {
    Serial.print(rcv[j] >> 4, HEX);
    Serial.print(rcv[j] & 0xF, HEX);
    Serial.print(" ");
  }


}
