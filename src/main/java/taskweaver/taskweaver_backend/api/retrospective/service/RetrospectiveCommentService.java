package taskweaver.taskweaver_backend.api.retrospective.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskweaver.taskweaver_backend.api.retrospective.controller.request.RetrospectiveCommentRequest;
import taskweaver.taskweaver_backend.api.retrospective.service.converter.RetrospectiveCommentConverter;
import taskweaver.taskweaver_backend.api.retrospective.service.response.RetrospectiveCommentResponse;
import taskweaver.taskweaver_backend.common.code.ErrorCode;
import taskweaver.taskweaver_backend.common.exception.handler.BusinessExceptionHandler;
import taskweaver.taskweaver_backend.domain.member.model.Member;
import taskweaver.taskweaver_backend.domain.member.repository.MemberRepository;

import taskweaver.taskweaver_backend.domain.retrospective.model.Retrospective;
import taskweaver.taskweaver_backend.domain.retrospective.model.RetrospectiveComment;
import taskweaver.taskweaver_backend.domain.retrospective.repository.RetrospectiveCommentRepository;
import taskweaver.taskweaver_backend.domain.retrospective.repository.RetrospectiveRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RetrospectiveCommentService {

    private final RetrospectiveCommentRepository retrospectiveCommentRepository;
    private final MemberRepository memberRepository;
    private final RetrospectiveRepository retrospectiveRepository;

    @Transactional
    public RetrospectiveCommentResponse.CreateCommentResponse createRetrospectiveComment(Long retrospectiveId,
                                                                                  RetrospectiveCommentRequest.CreateCommentRequest request,
                                                                                  Long memberId) {

        Member writer = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.MEMBER_NOT_FOUND));

        Retrospective retrospective = retrospectiveRepository.findById(retrospectiveId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.RETROSPECTIVE_NOT_FOUND));

        RetrospectiveComment parentComment = null;
        if (request.getParentId() != null) {
            parentComment = retrospectiveCommentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.COMMENT_NOT_FOUND));

        }

        RetrospectiveComment newComment = RetrospectiveCommentConverter.toRetrospectiveComment(request, retrospective, writer, parentComment);
        retrospectiveCommentRepository.save(newComment);


        return RetrospectiveCommentConverter.toRetrospectiveCommentResponse(newComment);
    }
}