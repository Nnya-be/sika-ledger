package xellenet.sikaledger.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name = "accounts")
@Getter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 3)
    private String currency;
    @Column(insertable = false, updatable = false)
    private Instant createdAt;

    public Account(String name, String currency) {
        this.name = name;
        this.currency = currency;
    }
    protected Account() {

    }
}
