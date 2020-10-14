package com.cl0udninja.raspberrypi.quietcool.web;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cl0udninja.raspberrypi.quietcool.service.RaspberryPiStatusService;
import com.cl0udninja.raspberrypi.quietcool.web.dto.SystemInfoDTO;

@RestController()
@RequestMapping("/api/pi")
public class RaspberryPiStatusController {

  @Autowired
  private RaspberryPiStatusService svc;

  @GetMapping
  public ResponseEntity<SystemInfoDTO> getStatus()
      throws UnsupportedOperationException, IOException, InterruptedException, ParseException {
    return ResponseEntity.ok(svc.compileStatusInformation());
  }

}
