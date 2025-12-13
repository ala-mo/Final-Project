package com.classproject;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * ClockService
 * ------------
 * Runs a background thread that updates a clock label every second.
 * Demonstrates multithreading in a simple and visible way.
 */
public class ClockService {

    private Label clockLabel;
    private Thread clockThread;

    public ClockService(Label clockLabel) {
        this.clockLabel = clockLabel;
    }

    public void start() {

        clockThread = new Thread(() -> {

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("hh:mm:ss a");

            while (true) {
                String time = LocalTime.now().format(formatter);

                Platform.runLater(() ->
                        clockLabel.setText("Current Time: " + time)
                );

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        clockThread.setDaemon(true);
        clockThread.start();
    }
}
