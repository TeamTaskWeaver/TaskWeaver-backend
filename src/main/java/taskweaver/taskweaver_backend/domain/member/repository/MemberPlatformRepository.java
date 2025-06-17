package taskweaver.taskweaver_backend.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taskweaver.taskweaver_backend.domain.member.model.Member;
import taskweaver.taskweaver_backend.domain.member.model.MemberPlatform;
import taskweaver.taskweaver_backend.domain.member.model.ProviderType;

import java.util.Optional;

@Repository
public interface MemberPlatformRepository extends JpaRepository<MemberPlatform, Long> {
    //
    Optional<MemberPlatform> findByProviderAndProviderId(ProviderType provider, String providerId);

}

