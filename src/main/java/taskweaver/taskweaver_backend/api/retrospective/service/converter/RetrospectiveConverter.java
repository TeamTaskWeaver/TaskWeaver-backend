package taskweaver.taskweaver_backend.api.retrospective.service.converter;

import org.springframework.stereotype.Component;
import taskweaver.taskweaver_backend.api.meetingRecord.controller.request.MeetingRecordRequest;
import taskweaver.taskweaver_backend.api.meetingRecord.service.response.MeetingRecordResponse;
import taskweaver.taskweaver_backend.api.retrospective.controller.request.RetrospectiveRequest;
import taskweaver.taskweaver_backend.api.retrospective.service.response.RetrospectiveResponse;
import taskweaver.taskweaver_backend.domain.meetingRecord.model.MeetingRecord;
import taskweaver.taskweaver_backend.domain.member.model.Member;
import taskweaver.taskweaver_backend.domain.project.model.Project;
import taskweaver.taskweaver_backend.domain.retrospective.model.Retrospective;
import taskweaver.taskweaver_backend.domain.retrospective.model.RetrospectiveComment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RetrospectiveConverter {
    public static Retrospective toRetrospectiveRequest(RetrospectiveRequest.RetrospectiveCreateRequest request, Member writer, Project project) {
        return Retrospective.builder()
                .project(project)
                .title(request.getTitle())
                .writer(writer)
                .build();
    }

    public static RetrospectiveResponse.RetrospectiveCreateResponse toCreateResponse(Retrospective retrospective, Member writer) {
        return new RetrospectiveResponse.RetrospectiveCreateResponse(
                retrospective.getId(),
                writer.getId(),
                retrospective.getTitle(),
                retrospective.getCreatedAt()
        );
    }

    public static RetrospectiveResponse.RetrospectiveDetailResponse toRetrospectiveDetailResponse(
            Retrospective retrospective, RetrospectiveResponse.ReviewInfo reviewInfo, List<RetrospectiveComment> comments) {

        List<RetrospectiveResponse.CommentInfo> commentInfos = convertToHierarchy(comments);

        return RetrospectiveResponse.RetrospectiveDetailResponse.builder()
                .retrospectiveId(retrospective.getId())
                .title(retrospective.getTitle())
                .writerNickname(retrospective.getWriter().getNickname())
                .createdAt(retrospective.getCreatedAt())
                .review(reviewInfo)
                .comments(commentInfos)
                .build();
    }

    // 댓글 엔티티 리스트를 계층 구조 DTO 리스트로 변환
    private static List<RetrospectiveResponse.CommentInfo> convertToHierarchy(List<RetrospectiveComment> comments) {
        Map<Long, RetrospectiveResponse.CommentInfo> map = comments.stream()
                .map(RetrospectiveConverter::toCommentInfo)
                .collect(Collectors.toMap(RetrospectiveResponse.CommentInfo::getCommentId, c -> c));

        List<RetrospectiveResponse.CommentInfo> rootComments = new ArrayList<>();

        comments.forEach(comment -> {
            if (comment.getParent() != null) {
                RetrospectiveResponse.CommentInfo parentDto = map.get(comment.getParent().getId());
                if (parentDto != null) {
                    parentDto.getChildren().add(map.get(comment.getId()));
                }
            } else {
                rootComments.add(map.get(comment.getId()));
            }
        });
        return rootComments;
    }

    // 단일 댓글 엔티티를 DTO로 변환
    private static RetrospectiveResponse.CommentInfo toCommentInfo(RetrospectiveComment comment) {
        return RetrospectiveResponse.CommentInfo.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .writerNickname(comment.getMember().getNickname())
                .writerId(comment.getMember().getId())
                .createdAt(comment.getCreatedAt())
                .isDeleted(comment.isSoftDeleted())
                .children(new ArrayList<>()) // 자식 리스트 초기화
                .build();
    }
}
