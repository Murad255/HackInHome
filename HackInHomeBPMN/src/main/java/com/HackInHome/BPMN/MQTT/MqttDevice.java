package com.HackInHome.BPMN.MQTT;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.ProcessEngine;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;



public class MqttDevice implements MqttCallback {

	Stack<Message> messages = new Stack<Message>();
	Map< String, Message> robotArmMessages = new HashMap< String, Message>();
	Map< String, Message> machineToolMessages = new HashMap< String, Message>();
	Map< String, Message> modulesMessages = new HashMap< String, Message>();


	public Message pastMessage;

	public void pastMessageClear() throws Exception {
		pastMessage = new Message("","");
	}

	public void StackMessageClear(){messages.clear(); }

	public Stack<Message> getStackMessage(){
		return  messages;
	}

	public  Message findMessage(String Name){
		int len = messages.size();
		while (len>=0){
			Message mes= messages.get(len);
			if(mes.getName().equals(Name)){
				messages.remove(mes);
				return mes;
			}
			len--;
		}
		return null;
	}


	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(topic+"\t"+message);
		Message msg= new Message(message.toString(),topic);
		messages.push(msg);
		pastMessage=msg;

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}

	
}
