package com.houoy.www.gongxing.util;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

/**
 * Uses an callback based interface to MQTT. Callback based interfaces are
 * harder to use but are slightly more efficient.
 */
public class MQTTListener {

    public static void main(String[] args) throws Exception {
        start();
    }

    public static void start() throws Exception {
        String user = MqttConstants.user;
        String password = MqttConstants.password;
        String host = MqttConstants.host;
        int port = MqttConstants.port;
        final String destination = MqttConstants.destination;
//
//        String user = env("APOLLO_USER", "admin");
//        String password = env("APOLLO_PASSWORD", "password");
//        String host = env("APOLLO_HOST", "localhost");
//        int port = Integer.parseInt(env("APOLLO_PORT", "61613"));
//        final String destination = arg(args, 0, "/topic/1/OOOOOOOOOO/aaaa");

        MQTT mqtt = new MQTT();
        mqtt.setHost(host, port);
        mqtt.setUserName(user);
        mqtt.setPassword(password);
        mqtt.setKeepAlive((short) 30);
        // mqtt.setCleanSession(false);
        // mqtt.setClientId("aaaa");

        final CallbackConnection connection = mqtt.callbackConnection();

        connection.listener(new Listener() {
            public void onConnected() {
            }

            public void onDisconnected() {
            }

            public void onFailure(Throwable value) {
                value.printStackTrace();
                System.exit(-2);
            }

            public void onPublish(UTF8Buffer topic, Buffer msg, Runnable ack) {
                String body = msg.utf8().toString();
                System.out.println(body);
                ack.run();
            }
        });
        connection.connect(new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {
                Topic[] topics = {new Topic(destination, QoS.AT_LEAST_ONCE)};
                connection.subscribe(topics, new Callback<byte[]>() {
                    public void onSuccess(byte[] qoses) {
                        System.out.println("connected...");
                    }

                    public void onFailure(Throwable value) {
                        value.printStackTrace();
                        System.exit(-2);
                    }
                });
            }

            @Override
            public void onFailure(Throwable value) {
                value.printStackTrace();
                System.exit(-2);
            }
        });
        // Wait forever..
        synchronized (MQTTListener.class) {
            while (true) {
                MQTTListener.class.wait();
            }
        }
    }


    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if (rc == null) {
            return defaultValue;
        }
        return rc;
    }

    private static String arg(String[] args, int index, String defaultValue) {
        if (index < args.length) {
            return args[index];
        } else {
            return defaultValue;
        }
    }
}
