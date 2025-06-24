package taskweaver.taskweaver_backend.api.meetingRecord.service.converter;

import taskweaver.taskweaver_backend.api.meetingRecord.controller.request.AgendaRequest;
import taskweaver.taskweaver_backend.api.meetingRecord.service.response.AgendaResponse;
import taskweaver.taskweaver_backend.domain.meetingRecord.model.Agenda;
import taskweaver.taskweaver_backend.domain.meetingRecord.model.MeetingRecord;

public class AgendaConverter {
    public static Agenda toCreateAgendaRequest(AgendaRequest.CreateAgendaRequest request, MeetingRecord meeting) {
        return Agenda.builder()
                .title(request.getTitle())
                .meeting(meeting)
                .build();
    }

    public static AgendaResponse.CreateAgendaResponse toCreateAgendaResponse(Agenda agenda) {
        return AgendaResponse.CreateAgendaResponse.builder()
                .agendaId(agenda.getId())
                .title(agenda.getTitle())
                .build();
    }
}
