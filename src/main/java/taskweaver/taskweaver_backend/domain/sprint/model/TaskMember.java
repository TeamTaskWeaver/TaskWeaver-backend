package taskweaver.taskweaver_backend.domain.sprint.model;
import jakarta.persistence.*;
import lombok.*;
import taskweaver.taskweaver_backend.domain.BaseEntity;
import taskweaver.taskweaver_backend.domain.member.model.Member;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TaskMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}