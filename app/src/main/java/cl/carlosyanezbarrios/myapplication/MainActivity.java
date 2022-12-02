package cl.carlosyanezbarrios.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class MainActivity extends AppCompatActivity {

    private MqttAndroidClient clientemqtt;
    private final MemoryPersistence persistencia = new MemoryPersistence();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MqttAndroidClient mqtt = new MqttAndroidClient(this.getApplicationContext(), "tcp://broker.emqx.io:1883", "Carloslechuga", persistencia);
        mqtt.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Connection was lost!");
            }
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println("Message Arrived!: " + topic + ": " + new String(message.getPayload()));
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println("Delivery Complete!");
            }
        });
        MqttConnectOptions mqtto = new MqttConnectOptions();
        mqtto.setCleanSession(true);
        try {
            clientemqtt.connect(mqtto, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("Connection Success!");
                    try {
                        System.out.println("Subscribing to Carloslechuga/house/piso1");
                        clientemqtt.subscribe("Carloslechuga/house/piso1", 0);
                        System.out.println("Subscribed to Carloslechuga/house/piso1");
                        System.out.println("publicando un mensaje nuevo...");
                        clientemqtt.publish("Carlos/house/piso1", new MqttMessage("este mensaje es de prueba".getBytes()));
                    }catch (MqttException ex) {
                        System.out.println(ex.toString());
                    }
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.out.println("La conexión a fallado");
                    System.out.println("throwable: " + exception.toString());
                }
            });
        } catch (MqttException ex) {
            System.out.println(ex.toString());
        }
    }

}