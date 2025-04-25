package ru.sovcomcheck.back_end.photoservice.services;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import org.springframework.web.multipart.MultipartFile;
import ru.sovcomcheck.back_end.photoservice.dtos.FileDTO;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface FileService {
    List<FileDTO> getListObjects(String bucket);

    FileDTO uploadFile(MultipartFile file, String bucket) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    byte[] downloadFile(String filename, String bucket);

    String deleteFile(String file, String bucket);

    FileDTO moveFile(String filename, String sourceBucket, String targetBucket);
}
