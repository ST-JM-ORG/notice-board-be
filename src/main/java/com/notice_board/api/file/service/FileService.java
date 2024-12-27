package com.notice_board.api.file.service;

import com.notice_board.api.file.dto.FileDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileDto saveFile(MultipartFile file, String filePathType);

    void ExtCheck(MultipartFile[] files, String type);

    ResponseEntity<byte[]> imageDisplay(String filePathType, String uuid);
}
