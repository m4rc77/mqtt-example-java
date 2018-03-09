package ch.m4rc77.mqttexample;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttExample {

    private static final String TOPIC = "status/angle";
    private static final String BROKER = "tcp://broker.hivemq.com:1883";
    private static int QOS = 1; // at least once ... we do not care about double delivery

    private static MqttClient client;

    private static String topicPrefix = "";

    /**
     * Runs the mqtt example ...
     * @param args pass as first argument the prefix of the topic to use.
     * @throws Exception in case of an error
     */
    public static void main(String[] args) throws Exception {
        topicPrefix = args[0];
        System.out.println("\rStart ... using prefix " + topicPrefix);

        switchTerminalToRawMode();

        setupMqtt();

        System.out.println("\rPress keys ...");

        byte[] b = new byte[3];
        boolean run = true;
        while (run) {
            int count = System.in.read(b);

            if (count  > 1) {
                byte key = b[2];
                switch (key) {
                     case 68: // left arrow
                        mqttOut(90);
                        break;
                    case 65: // up arrow
                        mqttOut(180);
                        break;
                    case 67: // right arrow
                        mqttOut(270);
                        break;
                   case 66: // down arrow
                        mqttOut(0);
                        break;
                    default:
                        System.out.println("\r" + key);
                        break;
                }
            } else {
                byte key = b[0];
                switch (key) {
                    case 113:  // q
                    case 27:  // ESC
                        client.disconnect(2);
                        System.out.println("\rbye bye ..");
                        run = false;
                        break;
                    case 107: // key k ... like kill
                        System.exit(-9);
                        break;
                    case 49: // num block 1 
                        mqttOut(45);
                        break;
                    case 52: // num block 4 
                        mqttOut(90);
                        break;
                    case 55: // num block 7 
                        mqttOut(135);
                        break;
                    case 56: // num block 8 
                        mqttOut(180);
                        break;
                    case 57: // num block 9 
                        mqttOut(225);
                        break;
                    case 54: // num block 6 
                        mqttOut(270);
                        break;
                    case 51: // num block 3 
                        mqttOut(315);
                        break;
                    case 50: // num block 2 
                        mqttOut(0);
                        break;
                    default:
                        System.out.println("\r" + key);
                        break;
                }
            }
        }

        restoreTerminalMode();

        System.out.println("\r");
        System.out.println("\r");
        System.exit(0);
    }

    private static void switchTerminalToRawMode() throws Exception {
        String[] cmd = {"/bin/sh", "-c", "stty raw </dev/tty"};
        Runtime.getRuntime().exec(cmd);
    }

    private static void restoreTerminalMode() throws Exception {
        String[] cmd2 = {"/bin/sh", "-c", "stty sane </dev/tty"};
        Runtime.getRuntime().exec(cmd2);
    }

    private static void setupMqtt() {
        try {
            String clientId = "MqttExampleJava";
            MemoryPersistence persistence = new MemoryPersistence();
            client = new MqttClient(BROKER, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setAutomaticReconnect(true);
            // configure a last will ...
            connOpts.setWill(topicPrefix + TOPIC, "Java just died".getBytes(), QOS, false);
            System.out.println("\rConnecting to broker: " + BROKER);
            client.connect(connOpts);
            System.out.println("\rConnected");
        } catch(MqttException me) {
            System.out.println("\rError on connecting to server " + me );
        }
    }

    private static void mqttOut(int val) {
        try {
            System.out.println("\rMQTT Send: " + val);
            MqttMessage message = new MqttMessage(("" + val).getBytes());
            message.setQos(QOS);
            message.setRetained(false);
            client.publish(topicPrefix + TOPIC, message);
            System.out.println("\rMessage published to " + topicPrefix + TOPIC);
        } catch(MqttException me) {
            System.out.println("\rError on publishing message " + me );
        }
    }

}
