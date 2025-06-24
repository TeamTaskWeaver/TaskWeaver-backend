package taskweaver.taskweaver_backend.api.meetingRecord.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskweaver.taskweaver_backend.api.meetingRecord.controller.request.AgendaRequest;
import taskweaver.taskweaver_backend.api.meetingRecord.service.converter.AgendaConverter;
import taskweaver.taskweaver_backend.api.meetingRecord.service.response.AgendaResponse;
import taskweaver.taskweaver_backend.common.code.ErrorCode;
import taskweaver.taskweaver_backend.common.exception.handler.BusinessExceptionHandler;
import taskweaver.taskweaver_backend.domain.meetingRecord.model.Agenda;
import taskweaver.taskweaver_backend.domain.meetingRecord.model.MeetingRecord;
import taskweaver.taskweaver_backend.domain.meetingRecord.repository.AgendaRepository;
import taskweaver.taskweaver_backend.domain.meetingRecord.repository.MeetingRecordRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AgendaService {
    private final AgendaRepository agendaRepository;
    private final MeetingRecordRepository meetingRepository;

    @Transactional
    public AgendaResponse.CreateAgendaResponse createAgenda(Long meetingId, AgendaRequest.CreateAgendaRequest request) {
        MeetingRecord meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.MEETING_RECORD_NOT_FOUND));

        Agenda newAgenda = AgendaConverter.toCreateAgendaRequest(request, meeting);
        agendaRepository.save(newAgenda);

        return AgendaConverter.toCreateAgendaResponse(newAgenda);
    }

    @Transactional
    public void deleteAgenda(Long agendaId) {
        Agenda agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.AGENDA_NOT_FOUND)); // ErrorCode에 추가 필요

        agendaRepository.delete(agenda);
    }


}
