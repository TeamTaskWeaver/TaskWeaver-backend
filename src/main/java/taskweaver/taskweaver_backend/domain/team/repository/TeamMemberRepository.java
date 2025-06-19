package taskweaver.taskweaver_backend.domain.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import taskweaver.taskweaver_backend.domain.member.model.Member;
import taskweaver.taskweaver_backend.domain.team.model.Team;
import taskweaver.taskweaver_backend.domain.team.model.TeamMember;

import java.util.List;
import java.util.Optional;


@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    @Query("SELECT tm FROM TeamMember tm JOIN FETCH tm.team t WHERE tm.member.id = :memberId")
    List<TeamMember> findByUserIdWithTeam(@Param("memberId") Long memberId);
    boolean existsByTeamAndMember(Team team, Member member);

    @Query("SELECT tm FROM TeamMember tm JOIN FETCH tm.member WHERE tm.team.id = :teamId")
    List<TeamMember> findAllByTeamIdWithMember(@Param("teamId") Long teamId);

    boolean existsByTeamIdAndMemberId(Long teamId, Long memberId);

    Optional<TeamMember> findByTeamIdAndMemberId(Long teamId, Long memberId);



}
