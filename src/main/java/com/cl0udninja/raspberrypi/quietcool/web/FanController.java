package com.cl0udninja.raspberrypi.quietcool.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cl0udninja.raspberrypi.quietcool.service.FanService;
import com.cl0udninja.raspberrypi.quietcool.web.dto.FanDTO;

@RestController
@RequestMapping("/api/fan")
@Validated
public class FanController {

  @Autowired
  private FanService fanService;

  @PostMapping
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void setLEDState(@RequestBody @Valid FanDTO toggleFanDto) {
    this.fanService.toggleFan(toggleFanDto);
  }

  @GetMapping
  public ResponseEntity<FanDTO> getFanState() {
    return ResponseEntity.ok(fanService.getFanState());
  }
}
