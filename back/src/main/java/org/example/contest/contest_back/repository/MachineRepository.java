package org.example.contest.contest_back.repository;

import org.example.contest.contest_back.model.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {
    // 추가적인 쿼리 메소드 정의 가능
}
