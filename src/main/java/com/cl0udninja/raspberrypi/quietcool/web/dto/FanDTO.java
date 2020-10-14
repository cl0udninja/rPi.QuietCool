package com.cl0udninja.raspberrypi.quietcool.web.dto;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Validated
@Data
public class FanDTO {

  public enum FanSpeed {
    OFF, LOW, HIGH
  }

  @JsonProperty
  @NotNull
  private FanSpeed fanSpeed;

}
