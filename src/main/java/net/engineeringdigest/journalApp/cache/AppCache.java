package net.engineeringdigest.journalApp.cache;


import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.ConfigJournalAppEntity;
import net.engineeringdigest.journalApp.repository.ConfigJournalAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AppCache {

    public Map<String, String> APP_CACHE;

    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;

    @PostConstruct
    public void init() {
        APP_CACHE = new HashMap<>();
        List<ConfigJournalAppEntity> all = configJournalAppRepository.findAll();
        if (all != null){

        for(ConfigJournalAppEntity configJournalAppEntity: all){
            APP_CACHE.put(configJournalAppEntity.getKey(), configJournalAppEntity.getValue());
        }
        }
        else{
            log.info("NULLLLLLLLL");
            throw new NullPointerException("App Cache Null pointer");
        }
    }
}
