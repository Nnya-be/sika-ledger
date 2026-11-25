package xellenet.sikaledger.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import xellenet.sikaledger.domain.Account;
import xellenet.sikaledger.domain.JournalEntry;
import xellenet.sikaledger.repository.AccountRepository;
import xellenet.sikaledger.service.PostingService;
import xellenet.sikaledger.shared.domain.Money;
import xellenet.sikaledger.shared.domain.PostingLine;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Use the same PostgreSQLContainer import as in your DatabaseConnectionTest

@SpringBootTest
@Testcontainers
class PostingServiceTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16");

    @Autowired
    PostingService postingService;
    @Autowired AccountRepository accountRepository;
    @Autowired JdbcTemplate jdbc;

    Account cash;
    Account bank;

    @BeforeEach
    void cleanDatabaseAndCreateAccounts() {
        jdbc.execute("TRUNCATE postings, journal_entries, accounts RESTART IDENTITY CASCADE");
        cash = accountRepository.save(new Account("Cash", "GHS"));
        bank = accountRepository.save(new Account("Bank", "GHS"));
    }

    @Test
    void balancedEntryIsSaved() {
        JournalEntry entry = postingService.post("Deposit", List.of(
                new PostingLine(cash.getId(), Money.of("100.00", "GHS")),
                new PostingLine(bank.getId(), Money.of("-100.00", "GHS"))));

        assertNotNull(entry.getId());
        assertEquals(1, count("journal_entries"));
        assertEquals(2, count("postings"));
    }

    @Test
    void unbalancedEntryIsRejectedAndNothingIsWritten() {
        assertThrows(IllegalArgumentException.class, () ->
                postingService.post("Broken", List.of(
                        new PostingLine(cash.getId(), Money.of("100.00", "GHS")),
                        new PostingLine(bank.getId(), Money.of("-50.00", "GHS")))));

        assertEquals(0, count("journal_entries"));
        assertEquals(0, count("postings"));
    }

    @Test
    void unknownAccountIsRejectedAndNothingIsWritten() {
        assertThrows(IllegalArgumentException.class, () ->
                postingService.post("Ghost", List.of(
                        new PostingLine(cash.getId(), Money.of("100.00", "GHS")),
                        new PostingLine(999L, Money.of("-100.00", "GHS")))));

        assertEquals(0, count("journal_entries"));
        assertEquals(0, count("postings"));
    }

    private int count(String table) {
        return jdbc.queryForObject("SELECT count(*) FROM " + table, Integer.class);
    }
}