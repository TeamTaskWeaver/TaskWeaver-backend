package taskweaver.taskweaver_backend.domain.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskweaver.taskweaver_backend.domain.team.model.Team;



public interface TeamRepository extends JpaRepository<Team, Long> {

}
