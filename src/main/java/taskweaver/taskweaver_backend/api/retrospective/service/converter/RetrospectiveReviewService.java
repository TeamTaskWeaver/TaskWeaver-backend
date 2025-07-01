package taskweaver.taskweaver_backend.api.retrospective.service.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskweaver.taskweaver_backend.api.retrospective.controller.request.RetrospectiveReviewRequest;
import taskweaver.taskweaver_backend.api.retrospective.service.response.RetrospectiveReviewResponse;
import taskweaver.taskweaver_backend.common.code.ErrorCode;
import taskweaver.taskweaver_backend.common.exception.handler.BusinessExceptionHandler;
import taskweaver.taskweaver_backend.domain.member.model.Member;
import taskweaver.taskweaver_backend.domain.member.repository.MemberRepository;
import taskweaver.taskweaver_backend.domain.retrospective.model.Retrospective;
import taskweaver.taskweaver_backend.domain.retrospective.model.RetrospectiveReview;
import taskweaver.taskweaver_backend.domain.retrospective.model.RetrospectiveReviewType;
import taskweaver.taskweaver_backend.domain.retrospective.repository.RetrospectiveRepository;
import taskweaver.taskweaver_backend.domain.retrospective.repository.RetrospectiveReviewRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RetrospectiveReviewService {

    private final RetrospectiveReviewRepository reviewRepository;
    private final RetrospectiveRepository retrospectiveRepository;
    private final MemberRepository memberRepository;

    public RetrospectiveReviewResponse.ReviewResponse createReview(Long retrospectiveId,
                                                                   RetrospectiveReviewRequest.ReviewRequest request,
                                                                   Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.MEMBER_NOT_FOUND));
        Retrospective retrospective = retrospectiveRepository.findById(retrospectiveId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.RETROSPECTIVE_NOT_FOUND));

        // 기존 리뷰 확인
        Optional<RetrospectiveReview> existingReviewOpt = reviewRepository.findByRetrospectiveAndMember(retrospective, member);

        if (existingReviewOpt.isPresent()) {
            RetrospectiveReview existingReview = existingReviewOpt.get();

            if (existingReview.getReviewType() == request.getReviewType()) {
                reviewRepository.delete(existingReview);
            } else {
                existingReview.updateReviewType(request.getReviewType());
            }
        } else {
            RetrospectiveReview newReview = RetrospectiveReview.builder()
                    .retrospective(retrospective)
                    .member(member)
                    .reviewType(request.getReviewType())
                    .build();
            reviewRepository.save(newReview);
        }

        return createReviewResponse(retrospective, member);
    }

    private RetrospectiveReviewResponse.ReviewResponse createReviewResponse(Retrospective retrospective, Member member) {
        long likeCount = reviewRepository.countByRetrospectiveAndReviewType(retrospective, RetrospectiveReviewType.LIKE);
        long dislikeCount = reviewRepository.countByRetrospectiveAndReviewType(retrospective, RetrospectiveReviewType.DISLIKE);

        RetrospectiveReviewType myReviewType = reviewRepository.findByRetrospectiveAndMember(retrospective, member)
                .map(RetrospectiveReview::getReviewType)
                .orElse(null);

        return RetrospectiveReviewResponse.ReviewResponse.builder()
                .likeCount(likeCount)
                .dislikeCount(dislikeCount)
                .myReviewType(myReviewType)
                .build();
    }
}