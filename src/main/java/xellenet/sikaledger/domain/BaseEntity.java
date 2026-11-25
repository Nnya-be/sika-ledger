package xellenet.sikaledger.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BaseEntity {
    LocalDateTime updatedAt;
    LocalDateTime createdAt;
}
