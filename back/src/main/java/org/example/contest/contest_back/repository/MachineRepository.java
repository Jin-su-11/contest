package org.example.contest.contest_back.repository;

import org.example.contest.contest_back.model.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {
    List<Machine> findByMachineState(int state);
}
