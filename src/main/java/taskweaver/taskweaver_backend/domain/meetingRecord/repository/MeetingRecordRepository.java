package taskweaver.taskweaver_backend.domain.meetingRecord.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taskweaver.taskweaver_backend.domain.meetingRecord.model.MeetingRecord;

import java.util.Optional;

@Repository
public interface MeetingRecordRepository extends JpaRepository<MeetingRecord, Long> {

}