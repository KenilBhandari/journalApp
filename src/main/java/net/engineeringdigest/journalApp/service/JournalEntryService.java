package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.el.PropertyNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class JournalEntryService {

    private final UserService userService;

    private final JournalEntryRepository journalEntryRepository;

    @Autowired
    public JournalEntryService(UserService userService, JournalEntryRepository journalEntryRepository){
        this.journalEntryRepository = journalEntryRepository;
        this.userService = userService;
    }


    @Transactional
    public void saveEntry(JournalEntry journalEntry, String username) {
        try {

            User user = userService.findByUsername(username);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry savedEntry = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(savedEntry);
            userService.saveUser(user);
        } catch (Exception e) {
            log.error("An error occurred while saving entry", e);
            throw new PropertyNotFoundException("An error occurred while saving entry", e);
        }
    }

    public JournalEntry viewEntry(ObjectId entryId) {
        return journalEntryRepository.findById(entryId).orElse(null);
    }

    public JournalEntry findById(ObjectId entryId) {
        return journalEntryRepository.findById(entryId).orElse(null);
    }

    public List<JournalEntry> viewAll() {
        return journalEntryRepository.findAll();
    }

    @Transactional
    public boolean deleteEntry(ObjectId entryId, String username) {
        boolean removed = false;
        try {
            User user = userService.findByUsername(username);
            removed = user.getJournalEntries().removeIf(entry -> entry.getId().equals(entryId));
            if (removed) {
                userService.saveUser(user);
                journalEntryRepository.deleteById(entryId);
            }
        } catch (Exception e) {
            log.error("An error occurred while saving entry", e);
            throw new PropertyNotFoundException("An error occurred while deleting journal entry " + e);
        }
        return removed;

    }

    public JournalEntry updateEntry(ObjectId entryId, JournalEntry updatedEntry) {
        JournalEntry existingEntry = journalEntryRepository.findById(entryId).orElseThrow(() -> new PropertyNotFoundException("Journal entry not found with id: " + entryId));

        existingEntry.setContent(
                updatedEntry.getContent() != null && !(updatedEntry.getContent().isEmpty())
                        ? updatedEntry.getContent()
                        : existingEntry.getContent());

        existingEntry.setTitle(
                !updatedEntry.getTitle().isEmpty()
                        ? updatedEntry.getTitle()
                        : existingEntry.getTitle());

        journalEntryRepository.save(existingEntry);
        return existingEntry;
    }


}
