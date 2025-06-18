package taskweaver.taskweaver_backend.common.validation;


import taskweaver.taskweaver_backend.common.code.ErrorCode;
import taskweaver.taskweaver_backend.common.exception.handler.BusinessExceptionHandler;
import taskweaver.taskweaver_backend.domain.team.model.Team;

public class TeamValidator {

    public static void validateTeamLeader(Team team, Long userId) {
        System.out.println(team.getTeamLeader());
        System.out.println(userId);
        if (!team.getTeamLeader().getId().equals(userId)) {
            throw new BusinessExceptionHandler(ErrorCode.NOT_TEAM_LEADER);
        }
    }

}
