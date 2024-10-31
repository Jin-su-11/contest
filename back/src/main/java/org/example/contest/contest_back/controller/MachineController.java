package org.example.contest.contest_back.controller;

import org.example.contest.contest_back.service.MachineService;
import org.example.contest.contest_back.model.Machine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/machines")
@CrossOrigin(origins = "http://localhost:3000")
public class MachineController {

    private final MachineService machineService;

    @Autowired
    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }

    @GetMapping
    public List<Machine> getAllMachines() {
        List<Machine> machines = machineService.getAllMachines();
        machineService.saveMachinesToJson(); // JSON 파일로 저장
        return machines;
    }

    @PostMapping
    public Machine createMachine(@RequestBody Machine machine) {
        return machineService.saveMachine(machine);
    }

    @GetMapping("/saveToJson")
    public ResponseEntity<String> saveMachinesToJson() {
        machineService.saveMachinesToJson();
        return ResponseEntity.ok("Data saved to JSON successfully.");
    }
}
