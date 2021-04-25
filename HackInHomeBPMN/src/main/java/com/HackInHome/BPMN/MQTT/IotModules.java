package com.HackInHome.BPMN.MQTT;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.Stack;
import java.util.UUID;

public  class IotModules {
    public static MqttAsyncClient myClient;
    public static MqttDevice myCallback;
    public static ArrayList<String> RobotArmName;

    // static String serverUrl = "tcp://172.16.1.33:1883";
    static String serverUrl = "tcp://mqtt.eclipseprojects.io:1883";

    static String inDevices = "userM/devices/in";
    static String outDevices = "userM/devices/out";

    //static String inKuka= "Kuka/in";
    //static String outKuka = "Kuka/out";

    static String inWatcher = "userM/watcher/in";
    static String outWatcher = "userM/watcher/out";

    static int Qos = 0;
    static int UID = 0;

    public static  String button1Name="button1";
    public static  String RobotArm1Name;

    static public void Begin() throws Exception{
        myClient = new MqttAsyncClient(serverUrl, UUID.randomUUID().toString());
        myCallback = new MqttDevice();
        myClient.setCallback(myCallback);
        IMqttToken token = myClient.connect();
        token.waitForCompletion();
        myClient.subscribe(outDevices, Qos);
        myClient.subscribe(outWatcher, Qos);
        //myClient.subscribe(outKuka, Qos);
        RobotArmName = new ArrayList<String>();

    }

    static public  void InDevices(String message)throws Exception {
        myClient.publish(inDevices,new MqttMessage(message.getBytes()) );
    }

    static public  void InDevicesData(String Name, String data)throws Exception {
        String msg = "<Module><Name>"+Name+"</Name><UID>" + UID +
                "</UID><Data>" + data +
                "</Data><UID>111</UID></Module>";

        myClient.publish(inDevices,new MqttMessage(msg.getBytes()) );
        UID++;
    }
    static public  void InDevicesData(ModuleType moduleType, String Name, String data)throws Exception {
        String msg = "<Module><Name>"+Name+"</Name><ModuleType>"+moduleType.toString()+"</ModuleType><UID>" + UID +
                "</UID> <Data>" + data +
                "</Data><UID>111</UID></Module>";

        myClient.publish(inDevices,new MqttMessage(msg.getBytes()) );
        UID++;
    }

    static public  void InWatchCleent(String message)throws Exception {
        myClient.publish(inWatcher,new MqttMessage(message.getBytes()) );
    }

    public static void WatсherAvailable()throws Exception {
        InWatchCleent("ожидание запуска");
        myCallback.pastMessageClear();
        while(true){
            Thread.sleep(10);
            if(myCallback.pastMessage.getToken().equals(outWatcher)){
                String str= myCallback.pastMessage.getMessage();
                if(str.equals("start"))return;
            }
        }
    }

    public static boolean RollCall()throws Exception{
        myCallback.StackMessageClear();
        InDevicesData("all","status");
        Thread.sleep(500);
       if( myCallback.getStackMessage().empty())return false;

       while (!myCallback.getStackMessage().empty()){
           Message mes = myCallback.getStackMessage().pop();
           if(mes.getToken().equals(outDevices)){
               String name = findText(mes.getMessage(),"Name");
               String ModuleType = findText(mes.getMessage(),"ModuleType");
               if((name.length()>0)&&(ModuleType.length()>0)){
                   int type = Integer.parseInt(ModuleType);

                   }
               }
           }
       return false;
    }

    public static String SelectRobotArm()throws Exception{
        String nameExecutor;
        Stack<String> stackMessage = new Stack<String>();

        int numPast = IotModules.myCallback.getStackMessage().size();
        IotModules.InDevicesData(ModuleType.robotArm,"all","status");
        Thread.sleep(250);
        int newMessagesCount = IotModules.myCallback.getStackMessage().size()-numPast;
        if(newMessagesCount<=0) throw  new Exception("Robot Arm not Found");

        for(int i = 0; i < newMessagesCount ; i++){
            String mes = IotModules.myCallback.getStackMessage().pop().getMessage();
            if(mes.indexOf("Module")!=-1 ){
                String Status  = findText(mes, "Status");
                String Name  = findText(mes, "Name");
                if(Name.length()>0 && Status.equals("0")) stackMessage.add(Name);
            }
        }

        return  stackMessage.pop();
    }

    //проверить отправку статуса
    public static boolean  statusСonfirmed(String name ){
        Message outMes = myCallback.findMessage(name);
        if(outMes==null) return  false;
        if(outMes.getStatus()==0)return  true;
        else return false;
    }

    public static boolean  statusСonfirmed(String name,int howStatus ){
        Message outMes = myCallback.findMessage(name);
        if(outMes==null) return  false;
        if(outMes.getStatus()==howStatus)return  true;
        else return false;
    }
    //ожидание статуса 0 (устройство свободно для команд)
    public static void  waitingStatusСonfirmed(String name ){
        boolean status;
        do{
            status = statusСonfirmed(name);
        }while (!status);
    }
    public static boolean ButtonIsPressed() throws Exception {
        myCallback.pastMessageClear();
        while(true){
            Thread.sleep(10);
            if(myCallback.pastMessage.getToken().equals(outWatcher)){
                String str= myCallback.pastMessage.getMessage();
                if(str.equals("start"))return true;
            }
        }
    }

    public static String findText(String str, String findContext)throws  Exception
    {
        int find1 = str.indexOf("<" + findContext + ">");
        int find2 = str.indexOf("</" + findContext + ">");
        String findStr = "";
        for (int i = find1 + ("<" + findContext + ">").length(); i < find2; i++)
        {
            findStr += (char)str.getBytes()[i];
        }

        return findStr;
    }

}


