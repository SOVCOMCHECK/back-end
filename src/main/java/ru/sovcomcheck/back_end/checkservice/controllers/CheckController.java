package ru.sovcomcheck.back_end.checkservice.controllers;

import lombok.RequiredArgsConstructor;
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

    @PostMapping("/process")
    public ResponseEntity<CheckProcessingResponse> processReceipt(
            @RequestParam("file") MultipartFile file) {
        CheckProcessingResponse response = checkFacade.processReceipt(file);
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
}