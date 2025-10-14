//package iuh.fit.se.iwork4se.controller;
//
//import iuh.fit.se.iwork4se.dto.CreateEducationRequest;
//import iuh.fit.se.iwork4se.dto.UpdateEducationRequest;
//import iuh.fit.se.iwork4se.dto.profile.EducationDTO;
//import iuh.fit.se.iwork4se.service.EducationService;
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
//@RequestMapping("/api/profile/educations")
//@RequiredArgsConstructor
//public class EducationController {
//
//    private final EducationService educationService;
//
//    @GetMapping
//    public ResponseEntity<List<EducationDTO>> listMine() {
//        return ResponseEntity.ok(educationService.listMyEducations(SecurityUtil.currentUserId()));
//    }
//
//    @PostMapping
//    public ResponseEntity<EducationDTO> create(@Valid @RequestBody CreateEducationRequest req) {
//        EducationDTO created = educationService.createMyEducation(
//                SecurityUtil.currentUserId(),
//                req.school(), req.degree(), req.major(), req.startYear(), req.endYear(), req.description()
//        );
//        return ResponseEntity.created(URI.create("/api/profile/educations/" + created.getId())).body(created);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<EducationDTO> update(@PathVariable UUID id, @Valid @RequestBody UpdateEducationRequest req) {
//        EducationDTO updated = educationService.updateMyEducation(
//                SecurityUtil.currentUserId(),
//                id, req.school(), req.degree(), req.major(), req.startYear(), req.endYear(), req.description()
//        );
//        return ResponseEntity.ok(updated);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable UUID id) {
//        educationService.deleteMyEducation(SecurityUtil.currentUserId(), id);
//        return ResponseEntity.noContent().build();
//    }
//}
