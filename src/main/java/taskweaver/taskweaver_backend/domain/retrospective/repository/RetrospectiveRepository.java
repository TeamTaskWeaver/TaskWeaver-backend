package taskweaver.taskweaver_backend.domain.retrospective.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import taskweaver.taskweaver_backend.domain.retrospective.model.Retrospective;

import java.util.Optional;

public interface RetrospectiveRepository extends JpaRepository<Retrospective, Long> {
    @Query("SELECT r FROM Retrospective r " +
            "LEFT JOIN FETCH r.writer " +
            "WHERE r.id = :retrospectiveId")
    Optional<Retrospective> findRetrospectiveWriterById(@Param("retrospectiveId") Long retrospectiveId);
}