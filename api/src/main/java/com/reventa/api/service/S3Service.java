package com.reventa.api.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {
    
    @Value("${aws.access.key.id}") private String accessKey;
    @Value("${aws.secret.access.key}") private String secretKey;
    @Value("${aws.region}") private String region;
    @Value("${aws.s3.bucket.name}") private String bucketName;
    
    private S3Client s3Client;
    
    @PostConstruct
    public void init() {
        this.s3Client = S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)))  
            .build();
        }

    public String subirPdf(MultipartFile file) throws IOException {
        // Validación estricta del tipo de archivo
        if (!"application/pdf".equals(file.getContentType())) {
            throw new IllegalArgumentException("El archivo debe ser un formato PDF válido.");
        }
        
        // Generar un nombre único para el archivo en el bucket
        String nombreArchivo = "entradas/" + UUID.randomUUID().toString() + ".pdf";
        
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(nombreArchivo)
            .contentType(file.getContentType())
            .build();

        s3Client.putObject(putObjectRequest,
            RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        
        // Construir y retornar la URL pública del objeto alojado en S3
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, nombreArchivo);
    }
}