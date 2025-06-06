package taskweaver.taskweaver_backend.domain.meetingRecord.model;
import jakarta.persistence.*;
import lombok.*;
import taskweaver.taskweaver_backend.domain.BaseEntity;
import taskweaver.taskweaver_backend.domain.project.model.Project;

import java.time.LocalDateTime;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MeetingRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false, length = 200)
    private String title;

    private LocalDateTime meetingDate;

    @Lob
    private String content;

}