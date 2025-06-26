package taskweaver.taskweaver_backend.api.retrospective.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskweaver.taskweaver_backend.api.retrospective.controller.request.RetrospectiveRequest;
import taskweaver.taskweaver_backend.api.retrospective.service.converter.RetrospectiveConverter;
import taskweaver.taskweaver_backend.api.retrospective.service.response.RetrospectiveResponse;
import taskweaver.taskweaver_backend.common.code.ErrorCode;
import taskweaver.taskweaver_backend.common.exception.handler.BusinessExceptionHandler;
import taskweaver.taskweaver_backend.domain.member.model.Member;
import taskweaver.taskweaver_backend.domain.member.repository.MemberRepository;
import taskweaver.taskweaver_backend.domain.project.model.Project;
import taskweaver.taskweaver_backend.domain.project.repository.ProjectRepository;
import taskweaver.taskweaver_backend.domain.retrospective.model.Retrospective;
import taskweaver.taskweaver_backend.domain.retrospective.repository.RetrospectiveRepository;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RetrospectiveService {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final RetrospectiveRepository retrospectiveRepository;
    @Transactional
    public RetrospectiveResponse.RetrospectiveCreateResponse createRetrospective(
            Long projectId, RetrospectiveRequest.RetrospectiveCreateRequest request, long writer) {
        Member member = memberRepository.findById(writer)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.MEMBER_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.PROJECT_NOT_FOUND));

        Retrospective retrospective = RetrospectiveConverter.toRetrospectiveRequest(request, member, project);
        retrospectiveRepository.save(retrospective);

        return RetrospectiveConverter.toCreateResponse(retrospective, member);
    }


    @Transactional
    public void deleteRetrospective(Long retroId, Long userId) {
        Retrospective retrospective = retrospectiveRepository.findById(retroId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.RETROSPECTIVE_NOT_FOUND));

        retrospective.deleteSoftly();
    }

}
