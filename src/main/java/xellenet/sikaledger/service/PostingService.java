package xellenet.sikaledger.service;

import xellenet.sikaledger.domain.JournalEntry;
import xellenet.sikaledger.shared.domain.PostingLine;

import java.util.List;

public interface PostingService {

    public JournalEntry post(String description, List<PostingLine> lines);
}
