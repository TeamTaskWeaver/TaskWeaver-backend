package taskweaver.taskweaver_backend.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
public class BaseEntity {

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;

    // 삭제
    public void deleteSoftly() {
        this.deletedAt = LocalDateTime.now();
    }

    // 확인
    public boolean isSoftDeleted() {
        return null != deletedAt;
    }

    // 삭제 취소
    public void undoDeletion(){
        this.deletedAt = null;
    }
}
