package main.app;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "tracks")
public class Track {
    @Id
    private String id;
    private String fileName;
    private String originalName;
    private Date uploadDate = new Date();
}
