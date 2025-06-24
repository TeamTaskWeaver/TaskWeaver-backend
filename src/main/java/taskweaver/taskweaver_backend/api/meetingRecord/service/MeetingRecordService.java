package taskweaver.taskweaver_backend.api.meetingRecord.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskweaver.taskweaver_backend.api.meetingRecord.controller.request.MeetingRecordRequest;
import taskweaver.taskweaver_backend.api.meetingRecord.service.response.MeetingRecordResponse;
import taskweaver.taskweaver_backend.api.meetingRecord.service.converter.MeetingRecordConverter;
import taskweaver.taskweaver_backend.common.code.ErrorCode;
import taskweaver.taskweaver_backend.common.exception.handler.BusinessExceptionHandler;
import taskweaver.taskweaver_backend.domain.meetingRecord.model.MeetingRecord;
import taskweaver.taskweaver_backend.domain.meetingRecord.repository.MeetingRecordRepository;
import taskweaver.taskweaver_backend.domain.member.model.Member;
import taskweaver.taskweaver_backend.domain.member.repository.MemberRepository;
import taskweaver.taskweaver_backend.domain.project.model.Project;
import taskweaver.taskweaver_backend.domain.project.repository.ProjectRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetingRecordService {

    private final MeetingRecordRepository meetingRecordRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public MeetingRecordResponse.MeetingCreateResponse createMeetingRecord(
            Long projectId, MeetingRecordRequest.MeetingCreateRequest request, long writer) {
        Member member = memberRepository.findById(writer)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.MEMBER_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.PROJECT_NOT_FOUND));

        MeetingRecord newMeetingRecord = MeetingRecordConverter.toMeetingRequest(request, member, project);

        meetingRecordRepository.save(newMeetingRecord);

        return MeetingRecordConverter.toCreateResponse(newMeetingRecord, member);
    }

    @Transactional
    public MeetingRecordResponse.MeetingUpdateTitleResponse updateMeetingRecordTitle(Long meetingId, MeetingRecordRequest.MeetingUpdateTitleRequest request) {
        MeetingRecord meetingRecord = meetingRecordRepository.findById(meetingId)
                        .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.MEETING_RECORD_NOT_FOUND));
        meetingRecord.updateMeetingTitle(request.getTitle());

        meetingRecordRepository.save(meetingRecord);
        return MeetingRecordConverter.toUpdateTitleResponse(meetingRecord);

    }

    @Transactional
    public MeetingRecordResponse.MeetingUpdateContentResponse updateMeetingRecordContent(Long meetingId, MeetingRecordRequest.MeetingUpdateContentRequest request) {
        MeetingRecord meetingRecord = meetingRecordRepository.findById(meetingId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.MEETING_RECORD_NOT_FOUND));
        meetingRecord.updateMeetingInfo(request.getSubTitle(), request.getContent());

        meetingRecordRepository.save(meetingRecord);
        return MeetingRecordConverter.toUpdateContentResponse(meetingRecord);
    }

    @Transactional
    public MeetingRecordResponse.MeetingUpdateContentResponse deleteMeetingRecordContent(Long meetingId) {
        MeetingRecord meetingRecord = meetingRecordRepository.findById(meetingId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.MEETING_RECORD_NOT_FOUND));
        meetingRecord.updateMeetingInfo(null, null);

        meetingRecordRepository.save(meetingRecord);
        return MeetingRecordConverter.toUpdateContentResponse(meetingRecord);
    }
}
