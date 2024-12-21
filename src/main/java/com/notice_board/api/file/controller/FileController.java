package com.notice_board.api.file.controller;

import com.notice_board.api.file.service.FileService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
@CrossOrigin("*")
@Hidden
public class FileController {

    private final FileService fileService;

    @GetMapping(value = "/image/{filePathType}/{uuid}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> imageDisplay(@PathVariable String filePathType, @PathVariable String uuid) {
        return fileService.imageDisplay(filePathType, uuid);
    }
}
