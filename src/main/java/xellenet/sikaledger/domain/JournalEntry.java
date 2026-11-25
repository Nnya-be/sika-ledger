package xellenet.sikaledger.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "journal_entries")
public class JournalEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.PERSIST)
    private List<Posting> postings = new ArrayList<>();

    @Column(insertable = false, updatable = false)
    private Instant createdAt;

    protected JournalEntry() {

    }

    public JournalEntry(String description){
        this.description = description;
    }

    public Posting addPosting(Account account, BigDecimal amount){
        Posting posting = new Posting(this, account, amount);
        postings.add(posting);
        return posting;
    }
}
