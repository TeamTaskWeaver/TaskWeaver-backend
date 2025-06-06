package taskweaver.taskweaver_backend.domain.retrospective.model;
import jakarta.persistence.*;
import lombok.*;
import taskweaver.taskweaver_backend.domain.BaseEntity;
import taskweaver.taskweaver_backend.domain.member.model.Member;
import taskweaver.taskweaver_backend.domain.project.model.Project;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RetrospectiveComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "retrospective_id", nullable = false)
    private Retrospective retrospective;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 대댓글을 위한 자기참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private RetrospectiveComment parent;


    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<RetrospectiveComment> children = new ArrayList<>();


    @Column(nullable = false, length = 225)
    private String content;

    private int depth;
}

