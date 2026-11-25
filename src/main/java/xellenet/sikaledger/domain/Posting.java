package xellenet.sikaledger.domain;


import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "postings")
public class Posting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "journal_entry_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private JournalEntry journalEntry;

    @JoinColumn(name = "account_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @Column(precision = 19, scale = 4)
    private BigDecimal amount;

    protected Posting(){

    }

    Posting(JournalEntry journalEntry, Account account, BigDecimal amount){
        this.journalEntry = journalEntry;
        this.account = account;
        this.amount = amount;
    }
}
