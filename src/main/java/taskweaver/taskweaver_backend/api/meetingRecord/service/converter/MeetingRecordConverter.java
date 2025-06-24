package taskweaver.taskweaver_backend.api.meetingRecord.service.converter;

import org.springframework.stereotype.Component;
import taskweaver.taskweaver_backend.api.meetingRecord.controller.request.MeetingRecordRequest;
import taskweaver.taskweaver_backend.api.meetingRecord.service.MeetingRecordResponse;
import taskweaver.taskweaver_backend.api.team.controller.request.TeamRequest;
import taskweaver.taskweaver_backend.api.team.service.response.TeamResponse;
import taskweaver.taskweaver_backend.domain.meetingRecord.model.MeetingRecord;
import taskweaver.taskweaver_backend.domain.member.model.Member;
import taskweaver.taskweaver_backend.domain.project.model.Project;
import taskweaver.taskweaver_backend.domain.team.model.Team;

import java.time.LocalDateTime;

@Component
public class MeetingRecordConverter {
    public static MeetingRecord toMeetingRequest(MeetingRecordRequest.MeetingCreateRequest request, Member writer, Project project) {
        return MeetingRecord.builder()
                .project(project)
                .title(request.getTitle())
                .writer(writer)
                .build();
    }

    public static MeetingRecordResponse.MeetingCreateResponse toCreateResponse(MeetingRecord meetingRecord, Member writer) {
        return new MeetingRecordResponse.MeetingCreateResponse(
                meetingRecord.getId(),
                writer.getId(),
                meetingRecord.getTitle(),
                meetingRecord.getCreatedAt()
        );
    }

    public static MeetingRecordResponse.MeetingUpdateResponse toUpdateResponse(MeetingRecord meetingRecord) {
        return new MeetingRecordResponse.MeetingUpdateResponse(
                meetingRecord.getId(),
                meetingRecord.getTitle(),
                meetingRecord.getModifiedAt()
        );
    }
}
