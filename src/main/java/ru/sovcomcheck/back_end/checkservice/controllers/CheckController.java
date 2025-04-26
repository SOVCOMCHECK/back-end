package ru.sovcomcheck.back_end.checkservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.sovcomcheck.back_end.checkservice.dtos.Check;
import ru.sovcomcheck.back_end.checkservice.dtos.CheckProcessingResponse;
import ru.sovcomcheck.back_end.checkservice.enums.ProcessingStatus;
import ru.sovcomcheck.back_end.checkservice.facades.CheckFacade;

@RestController
@RequestMapping("/checks")
@RequiredArgsConstructor
public class CheckController {
    private final CheckFacade checkFacade;

    @PostMapping("/users/{userId}/process")
    public ResponseEntity<CheckProcessingResponse> processReceipt(
            @PathVariable String userId,
            @RequestParam("file") MultipartFile file) {
        CheckProcessingResponse response = checkFacade.processReceipt(userId, file);
        return response.getStatus() == ProcessingStatus.FAILED
                ? ResponseEntity.badRequest().body(response)
                : ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmCheck(
            @PathVariable String id,
            @RequestParam boolean approved) {
        checkFacade.confirmCheck(id, approved);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Check> getCheck(@PathVariable String id) {
        return ResponseEntity.ok(checkFacade.getCheckById(id));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<Page<Check>> getUserChecks(
            @PathVariable String userId,
            @PageableDefault(size = 10, sort = "processedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(checkFacade.getUserChecks(userId, pageable));
    }
}