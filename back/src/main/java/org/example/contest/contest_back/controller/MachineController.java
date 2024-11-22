package org.example.contest.contest_back.controller;

import org.example.contest.contest_back.service.MachineService;
import org.example.contest.contest_back.model.Machine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/machines")
@CrossOrigin(origins = "*")
public class MachineController {

    private final MachineService machineService;

    @Autowired
    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }

    @GetMapping
    public List<Machine> getAllMachines() {
        return machineService.getAllMachines();
    }

    @PostMapping("/receiveData")
    public ResponseEntity<String> receiveData(@RequestParam("Did") int deviceId,
                                              @RequestParam("val") float injectTotalValue,
                                              @RequestParam("spd") float droppingSpeed) {
        machineService.processReceivedData(deviceId, injectTotalValue, droppingSpeed);
        return ResponseEntity.ok("Data received and processed successfully");
    }

    @GetMapping("/getLatestData")
    public ResponseEntity<Map<String, Object>> getLatestData() {
        return ResponseEntity.ok(machineService.getLatestData());
    }

    @GetMapping("/dosage")
    public ResponseEntity<List<Machine>> getCurrentMachines() {
        List<Machine> machines = machineService.getAllMachines(); // 모든 기기 데이터 조회
        return ResponseEntity.ok(machines);
    }
}
