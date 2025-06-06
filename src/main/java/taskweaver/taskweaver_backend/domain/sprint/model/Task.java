package taskweaver.taskweaver_backend.domain.sprint.model;
import jakarta.persistence.*;
import lombok.*;
import taskweaver.taskweaver_backend.domain.BaseEntity;
import java.time.LocalDateTime;
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id", nullable = false)
    private Sprint sprint;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskImportance importance;

    private LocalDateTime dueDate;
}