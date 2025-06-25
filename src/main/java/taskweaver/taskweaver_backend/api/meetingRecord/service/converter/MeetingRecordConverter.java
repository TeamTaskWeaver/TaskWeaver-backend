package taskweaver.taskweaver_backend.api.meetingRecord.service.converter;

import org.springframework.stereotype.Component;
import taskweaver.taskweaver_backend.api.meetingRecord.controller.request.MeetingRecordRequest;
import taskweaver.taskweaver_backend.api.meetingRecord.service.response.MeetingRecordResponse;
import taskweaver.taskweaver_backend.domain.meetingRecord.model.MeetingRecord;
import taskweaver.taskweaver_backend.domain.member.model.Member;
import taskweaver.taskweaver_backend.domain.project.model.Project;

import java.util.List;

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

    public static MeetingRecordResponse.MeetingUpdateTitleResponse toUpdateTitleResponse(MeetingRecord meetingRecord) {
        return new MeetingRecordResponse.MeetingUpdateTitleResponse(
                meetingRecord.getId(),
                meetingRecord.getTitle(),
                meetingRecord.getModifiedAt()
        );
    }

    public static MeetingRecordResponse.MeetingUpdateContentResponse toUpdateContentResponse(MeetingRecord meetingRecord) {
        return new MeetingRecordResponse.MeetingUpdateContentResponse(
                meetingRecord.getId(),
                meetingRecord.getSubTitle(),
                meetingRecord.getContent(),
                meetingRecord.getModifiedAt()
        );
    }


    public static MeetingRecordResponse.MeetingDetailsResponse toGetMeetingDetailsResponse(MeetingRecord meeting) {
        List<MeetingRecordResponse.AgendaInfo> agendaInfos = meeting.getAgendas().stream()
                .map(agenda -> MeetingRecordResponse.AgendaInfo.builder()
                        .agendaId(agenda.getId())
                        .title(agenda.getTitle())
                        .build())
                .toList();

        return MeetingRecordResponse.MeetingDetailsResponse.builder()
                .meetingId(meeting.getId())
                .title(meeting.getTitle())
                .subTitle(meeting.getSubTitle())
                .content(meeting.getContent())
                .createdAt(meeting.getCreatedAt())
                .agendas(agendaInfos)
                .build();
    }
}
