package net.engineeringdigest.journalApp.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("config_journal_app")
@Getter
@Setter
@NoArgsConstructor
public class ConfigJournalAppEntity {
    private String key;
    private String value;
}
