package com.example.chartographer.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AbstractService {
    byte[] getFragment(String id, int x, int y, int width, int height) throws IOException;
    String createImage(int width, int height) throws IOException;

    void saveFragment(String id, int x, int y, int width, int height, MultipartFile file) throws IOException;
    void deleteImage(String id) throws IOException;
}
