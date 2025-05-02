package pe.com.graduate.insights.api.infrastructure.repository.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class Auditable {

  @CreatedDate
  @Column(name = "created_date", updatable = false, insertable = false)
  private LocalDateTime createdDate;

  @LastModifiedDate
  @Column(name = "modified_date", insertable = false)
  private LocalDateTime modifiedDate;

  @PreUpdate
  public void onUpdate() {
    this.modifiedDate = LocalDateTime.now();
  }
}
