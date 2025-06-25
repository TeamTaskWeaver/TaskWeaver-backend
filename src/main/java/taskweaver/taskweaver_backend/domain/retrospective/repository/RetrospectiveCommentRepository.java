package taskweaver.taskweaver_backend.domain.retrospective.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskweaver.taskweaver_backend.domain.retrospective.model.RetrospectiveComment;

public interface RetrospectiveCommentRepository extends JpaRepository<RetrospectiveComment, Long> {
}