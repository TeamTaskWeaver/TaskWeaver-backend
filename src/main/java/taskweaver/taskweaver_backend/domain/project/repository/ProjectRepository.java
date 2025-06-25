package taskweaver.taskweaver_backend.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskweaver.taskweaver_backend.domain.project.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
