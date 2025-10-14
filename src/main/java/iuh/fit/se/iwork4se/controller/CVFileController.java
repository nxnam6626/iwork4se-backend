//package iuh.fit.se.iwork4se.controller;
//
//import iuh.fit.se.iwork4se.dto.CreateCVFileRequest;
//import iuh.fit.se.iwork4se.dto.profile.CVFileDTO;
//import iuh.fit.se.iwork4se.service.CVFileService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.net.URI;
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/cv-files")
//@RequiredArgsConstructor
//public class CVFileController {
//
//    private final CVFileService cvFileService;
//
//    @GetMapping
//    public ResponseEntity<List<CVFileDTO>> listMine() {
//        return ResponseEntity.ok(cvFileService.listMyCVFiles(SecurityUtil.currentUserId()));
//    }
//
//    @PostMapping
//    public ResponseEntity<CVFileDTO> create(@Valid @RequestBody CreateCVFileRequest req) {
//        CVFileDTO created = cvFileService.createMyCVFile(
//                SecurityUtil.currentUserId(),
//                req.resumeId(), req.objectKey(), req.fileName(), req.mimeType(), req.size()
//        );
//        return ResponseEntity.created(URI.create("/api/cv-files/" + created.getId())).body(created);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<CVFileDTO> get(@PathVariable UUID id) {
//        return ResponseEntity.ok(cvFileService.getMyCVFile(SecurityUtil.currentUserId(), id));
//    }
//}
