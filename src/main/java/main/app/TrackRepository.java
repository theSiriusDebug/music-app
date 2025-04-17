package main.app;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrackRepository extends MongoRepository<Track, String> {
}
