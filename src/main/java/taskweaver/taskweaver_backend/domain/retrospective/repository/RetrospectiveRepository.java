package taskweaver.taskweaver_backend.domain.retrospective.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskweaver.taskweaver_backend.domain.project.model.Project;
import taskweaver.taskweaver_backend.domain.retrospective.model.Retrospective;

public interface RetrospectiveRepository extends JpaRepository<Retrospective, Long> {
}