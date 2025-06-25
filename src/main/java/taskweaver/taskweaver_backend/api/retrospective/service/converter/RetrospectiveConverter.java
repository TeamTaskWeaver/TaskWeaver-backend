package taskweaver.taskweaver_backend.api.retrospective.service.converter;

import org.springframework.stereotype.Component;
import taskweaver.taskweaver_backend.api.meetingRecord.controller.request.MeetingRecordRequest;
import taskweaver.taskweaver_backend.api.meetingRecord.service.response.MeetingRecordResponse;
import taskweaver.taskweaver_backend.api.retrospective.controller.request.RetrospectiveRequest;
import taskweaver.taskweaver_backend.api.retrospective.service.response.RetrospectiveResponse;
import taskweaver.taskweaver_backend.domain.meetingRecord.model.MeetingRecord;
import taskweaver.taskweaver_backend.domain.member.model.Member;
import taskweaver.taskweaver_backend.domain.project.model.Project;
import taskweaver.taskweaver_backend.domain.retrospective.model.Retrospective;

@Component
public class RetrospectiveConverter {
    public static Retrospective toRetrospectiveRequest(RetrospectiveRequest.RetrospectiveCreateRequest request, Member writer, Project project) {
        return Retrospective.builder()
                .project(project)
                .title(request.getTitle())
                .writer(writer)
                .build();
    }

    public static RetrospectiveResponse.RetrospectiveCreateResponse toCreateResponse(Retrospective retrospective, Member writer) {
        return new RetrospectiveResponse.RetrospectiveCreateResponse(
                retrospective.getId(),
                writer.getId(),
                retrospective.getTitle(),
                retrospective.getCreatedAt()
        );
    }
}
