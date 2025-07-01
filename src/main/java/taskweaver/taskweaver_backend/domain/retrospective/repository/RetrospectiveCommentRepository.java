package taskweaver.taskweaver_backend.domain.retrospective.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskweaver.taskweaver_backend.domain.retrospective.model.Retrospective;
import taskweaver.taskweaver_backend.domain.retrospective.model.RetrospectiveComment;

import java.util.List;

public interface RetrospectiveCommentRepository extends JpaRepository<RetrospectiveComment, Long> {
    List<RetrospectiveComment> findByRetrospectiveOrderByCreatedAtAsc(Retrospective retrospective);
}