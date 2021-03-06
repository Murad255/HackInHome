#include <Arduino.h>

#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <WiFi.h>
// extern "C" {
//   #include "freertos/FreeRTOS.h"
//   #include "freertos/timers.h"
// }
#include <AsyncMqttClient.h>
//#include <OneWire.h>
//#include <DallasTemperature.h>
const int butPin = 16;

//const char *ssid = "Keenetic-6756"; // Имя  точки доступа
//const char *pass = "03403blo";		// Пароль от точки доступа

const char *ssid = "Robotavr";		 // Имя  точки доступа
const char *pass = "Qaz1234Wsx5678"; // Пароль от точки доступа

const char *mqtt_server = "mqtt.eclipseprojects.io"; // Имя сервера MQTT
const int mqtt_port = 1883;							 // Порт для подключения к серверу MQTT
const char *mqtt_user = "Login";					 // Логи от сервер
const char *mqtt_pass = "Pass";						 // Пароль от сервера
const char *deviceName = "button1";
#define BUFFER_SIZE 100

bool LedState = false;
bool pinState = false;

WiFiClient wclient;
PubSubClient client(wclient, mqtt_server, mqtt_port);

String inDevices = "userM/devices/in";
String outDevices = "userM/devices/out";

enum EventRequest
{
	byRequest,
	change,
	changeUp,
	continueTransmin
};

EventRequest receiveEvent = change;

// Функция получения данных от сервера
void callback(const MQTT::Publish &pub);
void PrintButtonStatus(bool pinState);
void TempSend();
String findText(String str, String findContext);

void setup()
{
	pinMode(LED_BUILTIN, 1);
	digitalWrite(LED_BUILTIN, 1);
	delay(500);
	digitalWrite(LED_BUILTIN, 0);
	delay(500);
	digitalWrite(LED_BUILTIN, 1);
	delay(500);
	digitalWrite(LED_BUILTIN, 0);
	delay(500);

	Serial.begin(9600);
	delay(10);
	Serial.println();
	Serial.println();
	pinMode(butPin, 0);
}

void loop()
{
	// подключаемся к wi-fi
	if (WiFi.status() != WL_CONNECTED)
	{
		Serial.print("Connecting to ");
		Serial.print(ssid);
		Serial.println("...");
		WiFi.begin(ssid, pass);

		if (WiFi.waitForConnectResult() != WL_CONNECTED)
			return;
		Serial.println("WiFi connected");
	}

	// подключаемся к MQTT серверу
	if (WiFi.status() == WL_CONNECTED)
	{
		if (!client.connected())
		{
			Serial.println("Connecting to MQTT server");
			if (client.connect(MQTT::Connect("arduinoClient2")
								   .set_auth(mqtt_user, mqtt_pass)))
			{
				Serial.println("Connected to MQTT server");
				client.set_callback(callback);
				client.subscribe(inDevices);
			}
			else
			{
				Serial.println("Could not connect to MQTT server");
			}
		}

		if (client.connected())
		{
			client.loop();
			TempSend();
		}
	}
}

// Функция отправки показаний
void TempSend()
{
	static bool pastPinState = false;
	pinState = !digitalRead(butPin);
	if (receiveEvent == continueTransmin)
	{
		//client.publish(getData1, String(pinState));
		Serial.println(pinState);
		delay(100);
	}
	else if ((receiveEvent == change) && (pastPinState != pinState))
	{
		delay(20);
		if (pastPinState != pinState)
		{
			pastPinState = pinState;
			PrintButtonStatus(pinState);
			Serial.println(pinState);
		}
	}
	else if ((receiveEvent == changeUp) && (pastPinState != pinState))
	{
		delay(20);
		if (pastPinState != pinState)
		{

			pastPinState = pinState;
			if (pinState)
			{
				PrintButtonStatus(pinState);
				Serial.println(pinState);
			}
		}
	}
}

// Функция получения данных от сервера
void callback(const MQTT::Publish &pub)
{
	Serial.print(pub.topic());
	Serial.print(" => ");
	Serial.println(pub.payload_string());

	String payload = pub.payload_string();
	payload.trim();
	String name = findText(payload, "Name");
	if (name.equals("all") || name.equals(deviceName))
	{
		String data = findText(payload, "Data");
		String led = findText(data, "Led");

		if (led.length()>0)
		{
			Serial.println("Led:\t"+led);
			digitalWrite(LED_BUILTIN,led.toInt());
		}
		else if (data == "status")
		{
PrintStatus();
		}
		if (data == "changeEvent")
		{
			receiveEvent = change;
			Serial.println("changeEvent OK");
		}
		else if (data == "changeUpEvent")
		{
			receiveEvent = changeUp;
			Serial.println("changeUpEvent OK");
		}
		else if (data == "continueTransmin")
		{
			receiveEvent = continueTransmin;
			Serial.println("continueTransmin OK");
		}
		else
		{
			//client.publish(getStatus, "OK");
			Serial.println("OK");
		}
	}

	// }
}

int UID = 0;

void PrintButtonStatus(bool pinState)
{
	String data = "<Module>\
	<Name>" + String(deviceName) +
				  "</Name>\
	<ModuleType>2</ModuleType>\
	<UID>" + String(UID) +
				  "</UID>\
	<Data><Pin16>" +
				  String(pinState) +
				  "</Pin16></Data>\
	<Status>1</Status>\
	</Module>";
	client.publish(outDevices, data);

	UID++;
}

void PrintStatus()
{
	String data = "<Module>\
	<Name>" + String(deviceName) +
				  "</Name>\
	<ModuleType>2</ModuleType>\
	<UID>" + String(UID) +
				  "</UID>\
	<Status>1</Status>\
	</Module>";
	client.publish(outDevices, data);

	UID++;
}userM/devices/in

String findText(String str, String findContext)
{

	int find1 = str.indexOf("<" + findContext + ">");
	int find2 = str.indexOf("</" + findContext + ">");
	if ((find1 < 1) || (find2 < 1))
		return "";
	String findStr = "";
	for (int i = find1 + ("<" + findContext + ">").length(); i < find2; i++)
	{
		findStr += str[i];
	}

	return findStr;
}