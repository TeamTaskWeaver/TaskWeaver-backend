package taskweaver.taskweaver_backend.domain.retrospective.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskweaver.taskweaver_backend.domain.member.model.Member;
import taskweaver.taskweaver_backend.domain.retrospective.model.Retrospective;
import taskweaver.taskweaver_backend.domain.retrospective.model.RetrospectiveReview;
import taskweaver.taskweaver_backend.domain.retrospective.model.RetrospectiveReviewType;

import java.util.Optional;

public interface RetrospectiveReviewRepository extends JpaRepository<RetrospectiveReview, Long> {

    // 특정 회고록에 특정 멤버가 남긴 리뷰 찾기
    Optional<RetrospectiveReview> findByRetrospectiveAndMember(Retrospective retrospective, Member member);

    // 특정 회고록의 리뷰 타입별 개수 세기
    long countByRetrospectiveAndReviewType(Retrospective retrospective, RetrospectiveReviewType reviewType);
}
