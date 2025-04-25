package ru.sovcomcheck.back_end.photoservice.services.impls;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.sovcomcheck.back_end.photoservice.dtos.FileDTO;
import ru.sovcomcheck.back_end.photoservice.exceptions.ImageDownloadException;
import ru.sovcomcheck.back_end.photoservice.exceptions.ImageUploadException;
import ru.sovcomcheck.back_end.photoservice.services.FileService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MinioClient minioClient;

    @Override
    public List<FileDTO> getListObjects(String bucket) {
        List<FileDTO> objects = new ArrayList<>();
        try {
            Iterable<Result<Item>> result = minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(bucket)
                    .recursive(true)
                    .build());
            for (Result<Item> item : result) {
                objects.add(FileDTO.builder()
                        .filename(item.get().objectName())
                        .size(item.get().size())
                        .url(getPreSignedUrl(item.get().objectName(), bucket))
                        .build());
            }
        } catch (Exception e) {
            throw new ImageUploadException("Error occurred when getting list of objects from Minio", e);
        }
        return objects;
    }

    public FileDTO uploadFile(MultipartFile file, String bucket) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        validateFile(file);
        checkIfBucketNotExists(bucket);
        String objectName = generateRandomFileName(file);

        try {
            if (minioClient.statObject(StatObjectArgs.builder().bucket(bucket).object(objectName).build()) != null) {
                throw new ImageUploadException("File with the same name already exists: " + objectName);
            }
        } catch (ErrorResponseException e) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());
        }

        return createFileDTO(file, objectName, bucket);
    }

    public byte[] downloadFile(String filename, String bucket) {
        try {
            if (minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucket)
                            .object(filename)
                            .build()
            ) != null) {
                return minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(bucket)
                                .object(filename)
                                .build()).readAllBytes();
            } else {
                throw new ImageDownloadException("Error occurred while downloading file: " + filename);
            }

        } catch (Exception e) {
            throw new ImageDownloadException("Requested file does not exist: " + filename);
        }
    }


    @Override
    public String deleteFile(String fileName, String bucket) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            throw new ImageUploadException("Error occurred when deleting file", e);
        }

        return "successfully deleted";
    }

    private String getPreSignedUrl(String filename, String bucket) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucket)
                        .object(filename)
                        .expiry(60 * 60 * 24) // 24 hours
                        .build());
    }

    private FileDTO createFileDTO(MultipartFile file, String objectName, String bucket) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return FileDTO.builder()
                .size(file.getSize())
                .url(getPreSignedUrl(objectName, bucket))
                .filename(objectName)
                .build();
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ImageUploadException("File is empty");
        }
    }

    private String generateRandomFileName(MultipartFile file) {
        return UUID.randomUUID() + "." + Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1];
    }

    private void checkIfBucketNotExists(String bucket) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
            throw new ImageUploadException("Bucket does not exist: " + bucket);
        }
    }

}
