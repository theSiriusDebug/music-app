package main.app;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tracks")
public class TrackController {

    private final TrackRepository trackRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadTrack(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Файл пустой");
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File dest = new File(uploadDir, fileName);
        dest.getParentFile().mkdirs();
        file.transferTo(dest);

        Track track = new Track();
        track.setFileName(fileName);
        track.setOriginalName(file.getOriginalFilename());
        trackRepository.save(track);

        return ResponseEntity.ok("Файл загружен");
    }

    @GetMapping
    public List<Track> getAllTracks() {
        return trackRepository.findAll();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<FileSystemResource> download(@PathVariable String id) {
        Track track = trackRepository.findById(id).orElse(null);
        if (track == null) return ResponseEntity.notFound().build();

        File file = new File(uploadDir, track.getFileName());
        if (!file.exists()) return ResponseEntity.notFound().build();

        FileSystemResource resource = new FileSystemResource(file);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment().filename(track.getOriginalName()).build());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
