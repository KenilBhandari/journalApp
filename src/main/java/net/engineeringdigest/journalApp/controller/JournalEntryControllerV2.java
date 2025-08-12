package net.engineeringdigest.journalApp.controller;


import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")


public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<JournalEntry> allEntries = user.getJournalEntries();
        if (allEntries != null && !allEntries.isEmpty()) {
            return new ResponseEntity<>(allEntries, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            if (username != null) {
                journalEntryService.saveEntry(myEntry, username);
                return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(myEntry, HttpStatus.UNAUTHORIZED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{entryId}")
    public ResponseEntity<JournalEntry> viewEntry(@PathVariable ObjectId entryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(entry -> entry.getId().equals(entryId)).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Optional<JournalEntry> fetchedEntry = Optional.ofNullable(journalEntryService.findById(entryId));
            if (fetchedEntry.isPresent()) {
                return new ResponseEntity<>(fetchedEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id/{entryId}")
    public ResponseEntity<?> updateEntry(@PathVariable ObjectId entryId, @RequestBody JournalEntry updatedEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUsername(username);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(entry -> entry.getId().equals(entryId)).collect(Collectors.toList());

        if (!collect.isEmpty()) {
            Optional<JournalEntry> fetchedEntry = Optional.ofNullable(journalEntryService.findById(entryId));
            if (fetchedEntry.isPresent()) {
                JournalEntry freshUpdatedEntry = journalEntryService.updateEntry(entryId, updatedEntry);
                return new ResponseEntity<>(freshUpdatedEntry, HttpStatus.OK);

            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }


        JournalEntry oldEntry = journalEntryService.findById(entryId);
        if (oldEntry != null) {
            JournalEntry freshUpdatedEntry = journalEntryService.updateEntry(entryId, updatedEntry);
            return new ResponseEntity<>(freshUpdatedEntry, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/id/{entryId}")
    public ResponseEntity<?> deleteEntry(@PathVariable ObjectId entryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Boolean isDeleted = journalEntryService.deleteEntry(entryId, username);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
