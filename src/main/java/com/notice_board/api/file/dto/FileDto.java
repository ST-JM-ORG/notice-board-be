package com.notice_board.api.file.dto;

import lombok.Data;

@Data
public class FileDto {
    private String fileName;
    private Long fileSize;
    private String ext;
    private String filePath;
    private String uuid;
}
