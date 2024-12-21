package com.notice_board.api.file.service;

import com.notice_board.api.file.dto.FileDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    FileDto saveFile(MultipartFile file, String filePathType) throws IOException;

    Boolean ExtCheck(MultipartFile[] files, String type) throws IOException;

    ResponseEntity<byte[]> imageDisplay(String filePathType, String uuid);
}
