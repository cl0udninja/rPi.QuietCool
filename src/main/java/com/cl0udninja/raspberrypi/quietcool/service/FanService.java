package com.cl0udninja.raspberrypi.quietcool.service;

import javax.validation.Valid;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.cl0udninja.raspberrypi.quietcool.web.dto.FanDTO;
import com.cl0udninja.raspberrypi.quietcool.web.dto.FanDTO.FanSpeed;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import lombok.extern.slf4j.Slf4j;

@Service
@Validated
@Slf4j
public class FanService implements InitializingBean, DisposableBean {

  public static final Pin LOW_PIN = RaspiPin.GPIO_04;
  public static final Pin HIGH_PIN = RaspiPin.GPIO_05;
  public static final String LOW_NAME = "LOW";
  public static final String HIGH_NAME = "HIGH";

  private GpioController gpio;
  private GpioPinDigitalOutput lowPin;
  private GpioPinDigitalOutput highPin;

  @Override
  public void afterPropertiesSet() throws Exception {
    gpio = GpioFactory.getInstance();
    lowPin = gpio.provisionDigitalOutputPin(LOW_PIN, LOW_NAME, PinState.HIGH);
    highPin = gpio.provisionDigitalOutputPin(HIGH_PIN, HIGH_NAME, PinState.HIGH);
    // set shutdown state for this pin
    lowPin.setShutdownOptions(true, PinState.LOW);
    highPin.setShutdownOptions(true, PinState.LOW);
  }

  @Override
  public void destroy() throws Exception {
    gpio.shutdown();
  }

  public void toggleFan(@Valid FanDTO toggleFanDTO) {
    log.debug(String.format("Changing FAN state to %s", toggleFanDTO.getFanSpeed()));
    switch (toggleFanDTO.getFanSpeed()) {
      case OFF:
        this.turnOff();
        break;
      case HIGH: {
        if (!PinState.LOW.equals(this.highPin.getState())) {
          this.turnOff();
          this.highPin.setState(PinState.LOW);
        }
        break;
      }
      case LOW: {
        if (!PinState.LOW.equals(this.lowPin.getState())) {
          this.turnOff();
          this.lowPin.setState(PinState.LOW);
        }
        break;
      }
      default: {
        this.turnOff();
        break;
      }
    }
  }

  public FanDTO getFanState() {
    FanDTO fanDto = new FanDTO();
    if (PinState.LOW.equals(this.lowPin.getState())) {
      fanDto.setFanSpeed(FanSpeed.LOW);
    } else if (PinState.LOW.equals(this.highPin.getState())) {
      fanDto.setFanSpeed(FanSpeed.HIGH);
    } else {
      fanDto.setFanSpeed(FanSpeed.OFF);
    }
    log.debug(String.format("Fan state is %s", fanDto));
    return fanDto;
  }

  private void turnOff() {
    lowPin.setState(PinState.HIGH);
    highPin.setState(PinState.HIGH);
  }
}
