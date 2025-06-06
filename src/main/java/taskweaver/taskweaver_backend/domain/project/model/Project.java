package taskweaver.taskweaver_backend.domain.project.model;
import jakarta.persistence.*;
import lombok.*;
import taskweaver.taskweaver_backend.domain.BaseEntity;
import taskweaver.taskweaver_backend.domain.team.model.Team;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 225)
    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectState projectState;

    @Column(length = 50)
    private String managerName;
}
