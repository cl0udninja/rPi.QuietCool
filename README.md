# rPi.QuietCool

RaspberryPi based WiFi controller backend for controlling a QuietCool Wholehouse fan.

**IMPORTANT** This is an unsecured API endpoint to your fan, make sure that the network your RPi is connecting is secured. None of the APIs require any sort of authentication or authorization. Feel free to change it using [SpringBoot configuration](https://www.baeldung.com/spring-security-authentication-and-registration) if you want!

# Background

In northern California a Wholehouse fan is very useful. In the evening temperatures drop significantly and it's an efficient and cheap way to cool down the house for the night or pre-cool the whouse in the morning. My Quietcool wholehouse fan came with a timer switch and a speed selector / off switch. In order to make it smart and tie it into my Smartthings based home automation system. I created a RaspberryPi based solution. Here's how it looked before and after:

![Image of the UI before and after](https://github.com/cl0udninja/rPi.QuietCool/blob/main/.doc/1536443168190.jpg)

# Physical setup

There's a RPi model 3 B+ in the wall. In hindsight a 4 gang box would have been better but I was able the squeeze it in to a 3 gang box. Get the deeper one! It gets its power from the USB on the outlet. Not the most elegant way, but works. It's an easy reboot, too if things would hang. There's a 2 relay 5v board wired up to the RPi. One relay is for the high speed and one for the low speed. If both of them are off, the fan is off. The code is written in a way that it would always turn off one relay before it would turn on the other one, but I don't think it would cause damage if both of them were on. Also the relays have 2 outputs one is on if the relay is not powered the other is on when the relay is powered. Connect the fan wires to the relay outlet that's unpowered if the relayboard is off.

Connecting the relay board to the RPi:
| Relay board | Pi |
| --- | --- |
| 5V in | 5V out (ie.: PIN 2) |
| GND in | GND out (ie.: PIN 6) |
| Relay control for the low speed | GPIO 4 |
| Relay control for the high speed | GPIO 5 |


The solution is using Pi4J. See the pinout diagram for the Model 3B+ [here](https://pi4j.com/1.2/pins/model-3b-plus-rev1.html). 

![Pi4J pinout for Model 3B+](https://pi4j.com/1.2/images/j8header-3b-plus.png)

# Installation

You'll need:

* Java 8 Development Kit

    `sudo apt-get update && sudo apt-get install oracle-java8-jdk`

* Maven

    `sudo apt-get install maven`

After this it's just `java -jar target/rpi.quietcool-0.0.1-SNAPSHOT.jar`. 

If you want to make it start up when the Pi boots up, then `sudo nano /etc/rc.local` and add `java -jar target/rpi.quietcool-0.0.1-SNAPSHOT.jar &` above the last line (which is `exit 0`).

If you're getting permission errors accessing the GPIO on the RPi you'll have to run it with `sudo`

If you wan't to make any changes to the setup you can override any values in the [application.properties](src/main/resources/application.properties) at startup. I.e. `java -jar -Dserver.port=9999 ...` For settings that are not in the properties file, check out [Spring Boot's reference page](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html) about common properties.

# API

## GET `/api/fan`

Will tell the current status:  OFF, LOW, HIGH

## POST `/api/fan`

In the body simply your post either  OFF, LOW or HIGH and it will switch the fan to the desired setting

## GET `/api/pi`

Returns a general health info of the RPi including some temperatures, memory allocation, version numbers. See the [DTO returned](https://raw.githubusercontent.com/cl0udninja/rPi.QuietCool/52c3eec9d6fe1b1c1d6ec85dfc23346e96642857/src/main/java/com/cl0udninja/raspberrypi/quietcool/web/dto/SystemInfoDTO.java) for exact details.
