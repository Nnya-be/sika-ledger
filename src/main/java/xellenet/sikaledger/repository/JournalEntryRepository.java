package xellenet.sikaledger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xellenet.sikaledger.domain.JournalEntry;

public interface JournalEntryRepository extends JpaRepository<JournalEntry,Long> {
}
