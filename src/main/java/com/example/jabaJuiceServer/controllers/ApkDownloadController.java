package com.example.jabaJuiceServer.controllers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.GetObjectRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Controller
@RequestMapping("/sanny/download-apk")
public class ApkDownloadController {

    @Autowired
    private AmazonS3 amazonS3; // Autowire the Amazon S3 client

    @GetMapping
    public void downloadApk(HttpServletResponse response) throws IOException {
        String bucketName = "naturaw"; // Replace with your S3 bucket name
        String key = "apps/naturaw.apk"; // Replace with the S3 object key

        // Retrieve the APK file from S3
        S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucketName, key));
        InputStream inputStream = s3Object.getObjectContent();

        // Set response headers
        response.setContentType("application/vnd.android.package-archive");
        response.setHeader("Content-Disposition", "attachment; filename=app.apk");

        // Copy the file data to the response output stream
        try (OutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }
}

