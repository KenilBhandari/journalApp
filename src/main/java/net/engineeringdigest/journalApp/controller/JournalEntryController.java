package net.engineeringdigest.journalApp.controller;


import net.engineeringdigest.journalApp.entity.JournalEntry;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RequestMapping("/journal")

public class JournalEntryController {

    private Map<Long, JournalEntry> journalEntryMap = new HashMap<>();

//    @GetMapping
    public List<JournalEntry> getAll(){
        return new ArrayList<>(journalEntryMap.values());
    }

//    @PostMapping
    public boolean createEntry(@RequestBody JournalEntry myEntry){
        journalEntryMap.put(Long.valueOf(myEntry.getId().getTimestamp()), myEntry);
        return true;
    }

//    @GetMapping("id/{entryId}")
    public JournalEntry viewEntry(@PathVariable long entryId){
        return journalEntryMap.get(entryId);
    }

//    @PutMapping("/update/{entryId}")
    public boolean updateEntry(@PathVariable long entryId,@RequestBody JournalEntry updatedEntry ){
        journalEntryMap.put(entryId, updatedEntry);
        return true ;

    }

//    @DeleteMapping("/delete/{entryId}")
    public boolean deleteEntry(@PathVariable long entryId){
        journalEntryMap.remove(entryId);
        return  true;
    }
}
