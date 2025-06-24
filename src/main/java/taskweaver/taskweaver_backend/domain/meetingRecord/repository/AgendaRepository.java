package taskweaver.taskweaver_backend.domain.meetingRecord.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskweaver.taskweaver_backend.domain.meetingRecord.model.Agenda;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {
}
