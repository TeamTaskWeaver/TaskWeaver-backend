package taskweaver.taskweaver_backend.domain.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taskweaver.taskweaver_backend.domain.team.model.TeamMember;


@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

}
