package org.example.contest.contest_back.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // Java 8 시간 모듈 추가
import org.example.contest.contest_back.model.Machine;
import org.example.contest.contest_back.repository.MachineRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class MachineService {

    private final MachineRepository machineRepository;

    @Autowired
    public MachineService(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    public List<Machine> getAllMachines() {
        return machineRepository.findAll();
    }

    public Machine saveMachine(Machine machine) {
        return machineRepository.save(machine);
    }

    // JSON 파일로 저장하는 메소드 추가
    public void saveMachinesToJson() {
        List<Machine> machines = machineRepository.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // JavaTimeModule 등록

        File targetDir = new File("target");
        File jsonFile = new File(targetDir, "machine-data.json");

        try {
            log.info("=== saveMachines ===");

            if (!targetDir.exists()) {
                boolean dirCreated = targetDir.mkdirs();
                if (dirCreated) {
                    log.info("target directory created successfully.");
                } else {
                    log.warn("Failed to create target directory.");
                }
            }

            objectMapper.writeValue(jsonFile, machines);
            log.info("Data saved to JSON successfully: " + jsonFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Error saving machines to JSON file", e);
            e.printStackTrace();
        }
    }
}
