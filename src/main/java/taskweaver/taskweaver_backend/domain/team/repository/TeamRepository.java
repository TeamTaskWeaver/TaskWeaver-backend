package taskweaver.taskweaver_backend.domain.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskweaver.taskweaver_backend.domain.team.model.Team;

import java.util.Optional;


public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByInviteLink(String inviteCode);
}
