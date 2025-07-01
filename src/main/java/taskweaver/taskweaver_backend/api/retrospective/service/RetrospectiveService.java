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
import taskweaver.taskweaver_backend.domain.retrospective.model.RetrospectiveComment;
import taskweaver.taskweaver_backend.domain.retrospective.model.RetrospectiveReview;
import taskweaver.taskweaver_backend.domain.retrospective.model.RetrospectiveReviewType;
import taskweaver.taskweaver_backend.domain.retrospective.repository.RetrospectiveCommentRepository;
import taskweaver.taskweaver_backend.domain.retrospective.repository.RetrospectiveRepository;
import taskweaver.taskweaver_backend.domain.retrospective.repository.RetrospectiveReviewRepository;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RetrospectiveService {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final RetrospectiveRepository retrospectiveRepository;
    private final RetrospectiveReviewRepository reviewRepository;
    private final RetrospectiveCommentRepository commentRepository;
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


    public RetrospectiveResponse.RetrospectiveDetailResponse getRetrospectiveDetails(Long retrospectiveId, Long currentMemberId) {
        Retrospective retrospective = retrospectiveRepository.findRetrospectiveWriterById(retrospectiveId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.RETROSPECTIVE_NOT_FOUND));

        long likeCount = reviewRepository.countByRetrospectiveAndReviewType(retrospective, RetrospectiveReviewType.LIKE);
        long dislikeCount = reviewRepository.countByRetrospectiveAndReviewType(retrospective, RetrospectiveReviewType.DISLIKE);

        RetrospectiveReviewType myReviewType = reviewRepository
                .findByRetrospectiveAndMember(retrospective, Member.builder().id(currentMemberId).build())
                .map(RetrospectiveReview::getReviewType)
                .orElse(null);

        RetrospectiveResponse.ReviewInfo reviewInfo = RetrospectiveResponse.ReviewInfo.builder()
                .likeCount(likeCount)
                .dislikeCount(dislikeCount)
                .myReviewType(myReviewType)
                .build();

        List<RetrospectiveComment> comments = commentRepository.findByRetrospectiveOrderByCreatedAtAsc(retrospective);

        return RetrospectiveConverter.toRetrospectiveDetailResponse(retrospective, reviewInfo, comments);
    }
}
