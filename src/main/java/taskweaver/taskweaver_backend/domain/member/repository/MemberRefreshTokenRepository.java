package taskweaver.taskweaver_backend.domain.member.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taskweaver.taskweaver_backend.domain.member.model.MemberRefreshToken;

import java.util.Optional;

@Repository
public interface MemberRefreshTokenRepository extends JpaRepository<MemberRefreshToken, Long> {
    Optional<MemberRefreshToken> findByMemberIdAndReissueCountLessThan(Long id, Long count);

}
