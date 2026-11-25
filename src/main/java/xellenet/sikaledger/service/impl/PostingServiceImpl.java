package xellenet.sikaledger.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xellenet.sikaledger.domain.Account;
import xellenet.sikaledger.domain.JournalEntry;
import xellenet.sikaledger.repository.AccountRepository;
import xellenet.sikaledger.repository.JournalEntryRepository;
import xellenet.sikaledger.service.PostingService;
import xellenet.sikaledger.shared.domain.Money;
import xellenet.sikaledger.shared.domain.PostingLine;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostingServiceImpl implements PostingService {

    private final JournalEntryRepository journalEntryRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public JournalEntry post(String description, List<PostingLine>lines){
        if(lines.size() < 2){
            throw new IllegalArgumentException("There should be at least 2 lines");
        }

        Money total = new Money(BigDecimal.ZERO, lines.getFirst().amount().currency());

        for(PostingLine line: lines){
            total = total.plus(line.amount());
        }

        if(!total.isZero()){
            throw new IllegalArgumentException("Amount total must be 0");
        }
        JournalEntry entry = new JournalEntry(description);

        for(PostingLine line: lines){
            Account account = accountRepository.findById(line.accountId()).orElseThrow(
                    ()-> new IllegalArgumentException("Account " + line.accountId() + "not found")
            );
            if (!account.getCurrency().equals(line.amount().currency().getCurrencyCode())) {
                throw new IllegalArgumentException("Account " + account.getName() +
                        " doesn't have the same currency as " + account.getCurrency());
            }
            entry.addPosting(account, line.amount().amount());
        }

        return journalEntryRepository.save(entry);
    }
}
