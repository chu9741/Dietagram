package com.example.Dietagram.service;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.Dietagram.domain.FeedImage;
import com.example.Dietagram.dto.FoodImageDTO;
import com.example.Dietagram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Service
@Transactional
public class ImageService {

    private AmazonS3 amazonS3;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Autowired
    UserRepository userRepository;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    public FoodImageDTO upload(MultipartFile foodImage, String userId, String localOrServer) throws IOException {
        String fileName = foodImage.getOriginalFilename();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(foodImage.getSize());
        objectMetadata.setContentType(foodImage.getContentType());
        try (InputStream inputStream = foodImage.getInputStream()) {
            String filepath = localOrServer + "/" + userId + "/" + fileName;    // LOCAL TO SERVER , SERVER TO LOCAL
            amazonS3.putObject(new PutObjectRequest(bucket, filepath, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            return FoodImageDTO.builder()
                    .filename(fileName)
                    .url(amazonS3.getUrl(bucket, filepath).toString()).build();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Upload failed");
        }

    }

    public void delete(String filename, String userId, String localOrServer) {
        bucket = "dietagram-image";
        bucket += "/" + localOrServer + "/" + userId;
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, filename));
        bucket = "dietagram-image";
    }

    public String getFilePath(MultipartFile file, String userId, String localOrServer) {
        return localOrServer + userId + "/" + file.getOriginalFilename();
    }


}
