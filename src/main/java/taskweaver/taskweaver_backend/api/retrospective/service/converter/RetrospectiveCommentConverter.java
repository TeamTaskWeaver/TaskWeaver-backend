package taskweaver.taskweaver_backend.api.retrospective.service.converter;

import taskweaver.taskweaver_backend.api.retrospective.controller.request.RetrospectiveCommentRequest;
import taskweaver.taskweaver_backend.api.retrospective.service.response.RetrospectiveCommentResponse;
import taskweaver.taskweaver_backend.domain.member.model.Member;
import taskweaver.taskweaver_backend.domain.retrospective.model.Retrospective;
import taskweaver.taskweaver_backend.domain.retrospective.model.RetrospectiveComment;

public class RetrospectiveCommentConverter {
    public static RetrospectiveComment toRetrospectiveComment(RetrospectiveCommentRequest.CreateCommentRequest request,
                                                              Retrospective retrospective,
                                                              Member member,
                                                              RetrospectiveComment parentComment) {
        return RetrospectiveComment.builder()
                .retrospective(retrospective)
                .member(member)
                .content(request.getContent())
                .parent(parentComment)
                .depth(parentComment != null ? parentComment.getDepth() + 1 : 0) // 깊이 설정
                .build();
    }


    public static RetrospectiveCommentResponse.CreateCommentResponse toRetrospectiveCommentResponse(RetrospectiveComment comment) {
        return RetrospectiveCommentResponse.CreateCommentResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .writerNickname(comment.getMember().getNickname())
                .writerId(comment.getMember().getId())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
