package com.notice_board.api.file.service.impl;


import com.notice_board.api.file.dto.FileDto;
import com.notice_board.api.file.service.FileService;
import com.notice_board.common.component.CommonExceptionResultMessage;
import com.notice_board.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service("fileService")
@Transactional
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${project.file.upload.path}")
    private String fileUploadPath;

    private static final List<String> EXT_WHITELIST_IMG = Arrays.asList(
            "jpg", "jpeg", "png", "gif", "bmp"// 이미지 파일
    );

    private static final List<String> MIME_WHITELIST_IMG = Arrays.asList(
            "image/jpeg", "image/pjpeg", "image/png", "image/gif", "image/bmp", "image/x-windows-bmp" // 이미지파일
    );

    @Override
    public FileDto saveFile(MultipartFile file, String filePathType) throws IOException {
        if (file == null) throw new CustomException(CommonExceptionResultMessage.FILE_UPLOAD_FAIL);

        FileDto fileDto = new FileDto();
        String originalFilename = file.getOriginalFilename(); // 원본 파일명
        String fileName = originalFilename.substring(0, originalFilename.lastIndexOf('.')); // 파일명
        String fileExt = originalFilename.substring(originalFilename.lastIndexOf('.') + 1); // 확장자

        fileDto.setFileName(fileName); // 파일명
        fileDto.setExt(fileExt); // 확장자

        String dirPath = fileUploadPath + File.separator + filePathType;
        String uuid = UUID.randomUUID().toString(); // uuid
        String filePath = dirPath + File.separator + uuid;
        fileDto.setFilePath(File.separator + filePathType + File.separator + uuid); // 저장 경로 고정에서 유동적으로
        fileDto.setFileSize(file.getSize()); // 파일 사이즈
        fileDto.setUuid(uuid); // uuid

        makeFolder(dirPath); // 폴더 생성
        Path savePath = Paths.get(filePath);// path 지정
        file.transferTo(savePath); // 파일 저장
        return fileDto;
    }

    @Override
    public Boolean ExtCheck(MultipartFile[] files, String type) throws IOException {
        if (files != null) {
            for (MultipartFile file : files) {
                String originalFilename = file.getOriginalFilename(); // 원본 파일명
                String fileExt = originalFilename.substring(originalFilename.lastIndexOf('.') + 1); // 확장자
                if (StringUtils.equals(type, ("image"))) {
                    if (!EXT_WHITELIST_IMG.contains(fileExt.toLowerCase())) {
                        return false;
                    }
                } else {
                    return false;
                }
                InputStream inputStream = file.getInputStream();
                if (!validMime(inputStream, type)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public ResponseEntity<byte[]> imageDisplay(String filePathType, String uuid) {
        byte[] defaultImageByteArray = null;
        try (InputStream inputStream = getClass().getResourceAsStream("/static/assets/images/no_image.png")) {
            defaultImageByteArray = IOUtils.toByteArray(inputStream);
        } catch (IOException e) { // 기본 이미지를 읽을 수 없는 경우 예외 처리
            e.printStackTrace();
            return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String path = fileUploadPath + File.separator + filePathType + File.separator + uuid;
        try {
            byte[] imageByteArray = Files.readAllBytes(Paths.get(path));
            return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<byte[]>(defaultImageByteArray, HttpStatus.OK);
        }
    }

    private void makeFolder(String dirPath) {
        File uploadPath = new File(dirPath);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }
    }

    private boolean validMime(InputStream inputStream, String type) throws IOException {
        Tika tika = new Tika();
        List<String> validTypeList;
        if (StringUtils.equals(type, ("image"))) {
            validTypeList = MIME_WHITELIST_IMG;
        } else {
            return false;
        }
        String mimeType = tika.detect(inputStream);
        return validTypeList.stream().anyMatch(notValidType -> notValidType.equalsIgnoreCase(mimeType));
    }

}
