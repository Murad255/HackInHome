package com.HackInHome.BPMN;

import com.HackInHome.BPMN.MQTT.IotModules;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

///Проверка оборудования (отклик)
@Component
public class CheckingHealth implements JavaDelegate {


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println( "Hello World!" );
        IotModules.Begin();

        IotModules.InDevicesData(IotModules.button1Name,"<Led>1</Led>");
        Thread.sleep(500);
        IotModules.InDevicesData(IotModules.button1Name,"<Led>0</Led>");
        Thread.sleep(500);
        IotModules.InDevicesData(IotModules.button1Name,"<Led>1</Led>");
        Thread.sleep(500);
        IotModules.InDevicesData(IotModules.button1Name,"<Led>0</Led>");
        Thread.sleep(500);

        String statusButton = "1";//IotModules.ButtonGetStatus();
        if(statusButton.equals("1")) return;
        else throw  new BpmnError("noConnectionError");

    }
}
