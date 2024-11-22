package org.example.contest.contest_back.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.contest.contest_back.model.Machine;
import org.example.contest.contest_back.repository.MachineRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MachineService {

    private final MachineRepository machineRepository;
    private int lastReceivedDeviceId;
    private float lastReceivedValue;
    private float lastReceivedSpeed;
    private LocalDateTime lastUpdateTime;

    @Autowired
    public MachineService(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    public void processReceivedData(int deviceId, float injectTotalValue, float droppingSpeed) {
        LocalDateTime now = LocalDateTime.now();

        Machine machine = machineRepository.findById((long) deviceId).orElse(new Machine());
        if (machine.getId() == null) {
            machine.setId((long) deviceId);
            machine.setDosageCreatedAt(now);
            machine.setMachineState(1); // 진행 중 상태로 설정
        }

        machine.setDosageClosedAt(now);
        machine.setDosageValue(injectTotalValue);
        machine.setDosageSpeed(droppingSpeed);
        machine.setMachineState(1); // 진행 중으로 업데이트
        machine.setTotalTime(calculateTotalTime(machine.getDosageCreatedAt(), machine.getDosageClosedAt()));

        machineRepository.save(machine);

        // 최신 데이터 업데이트
        lastReceivedDeviceId = deviceId;
        lastReceivedValue = injectTotalValue;
        lastReceivedSpeed = droppingSpeed;
        lastUpdateTime = now;

        saveDataToJson(deviceId, injectTotalValue, droppingSpeed);
    }

    private void saveDataToJson(int deviceId, float injectTotalValue, float droppingSpeed) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("Did", deviceId);
        dataMap.put("val", injectTotalValue);
        dataMap.put("spd", droppingSpeed);

//        try {
//            File targetDir = new File("target");
//            if (!targetDir.exists()) {
//                targetDir.mkdirs();
//            }
//            File jsonFile = new File(targetDir, "receivedData.json");
//            objectMapper.writeValue(jsonFile, dataMap);
//            log.info("Data saved to JSON successfully: " + jsonFile.getAbsolutePath());
//        } catch (IOException e) {
//            log.error("Error saving data to JSON file", e);
//        }
    }

    public Map<String, Object> getLatestData() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("Did", lastReceivedDeviceId);
        dataMap.put("val", lastReceivedValue);
        dataMap.put("spd", lastReceivedSpeed);
        return dataMap;
    }

    private LocalTime calculateTotalTime(LocalDateTime createdAt, LocalDateTime closedAt) {
        Duration duration = Duration.between(createdAt, closedAt);
        long seconds = duration.getSeconds();
        if (seconds >= 86400) {
            // 24시간이 넘는 경우 초과된 시간 처리를 위해 Duration의 각 파트를 사용합니다.
            int hours = (int) (seconds / 3600);
            int minutes = (int) ((seconds % 3600) / 60);
            int secs = (int) (seconds % 60);
            return LocalTime.of(hours % 24, minutes, secs);
        }
        return LocalTime.ofSecondOfDay(seconds);
    }


    @Scheduled(fixedRate = 10000) // 일정시간마다 실행
    @Transactional // 데이터베이스 작업이 필요한 경우 트랜잭션 적용
    public void updateMachineStates() {
        log.info("Scheduled updateMachineStates() started");

        List<Machine> machines = machineRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Machine machine : machines) {
            if (machine.getMachineState() == 1) {
                Duration duration = Duration.between(machine.getDosageClosedAt(), now);
                if (duration.toSeconds() >= 10) {
                    machine.setMachineState(0); // 종료 상태로 설정
                    machineRepository.save(machine);
                    log.info("Machine ID: {} 상태를 0으로 업데이트했습니다.", machine.getId());
                }
            }
        }

        log.info("Scheduled updateMachineStates() ended");
    }

    public List<Machine> getAllMachines() {
        return machineRepository.findAll(); // 데이터베이스에서 모든 기기 데이터 조회
    }
}
