package taskweaver.taskweaver_backend.api.meetingRecord.service.response;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskweaver.taskweaver_backend.api.meetingRecord.controller.request.MeetingRecordRequest;
import taskweaver.taskweaver_backend.api.meetingRecord.service.MeetingRecordResponse;
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

        // 4. Converter를 사용하여 생성 결과를 DTO로 변환하여 반환
        return MeetingRecordConverter.toCreateResponse(newMeetingRecord, member);
    }
}
