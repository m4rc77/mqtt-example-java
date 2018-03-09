#!/usr/bin/env bash
java -cp "$HOME/.m2/repository/org/eclipse/paho/org.eclipse.paho.client.mqttv3/1.2.0/org.eclipse.paho.client.mqttv3-1.2.0.jar:target/mqtt-example-java-1.0-SNAPSHOT.jar" ch.m4rc77.mqttexample.MqttExample your/topic/prefix/here/
