package com.example.chartographer.service;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@org.springframework.stereotype.Service
public class Service implements AbstractService{

    private final String contentPath = "content/";
    public Service() {
    }
    @Override
    public byte[] getFragment(String id, int x, int y, int width, int height) throws IOException {
        if (x < 0 || y < 0) {
            width += x;
            height += y;
            y = 0;
            x = 0;
        }
        BufferedImage in = ImageIO.read(new File(generateFilePath(id))).getSubimage(x, y, width, height);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(in,"bmp", out);
        return out.toByteArray();
    }
    @Override
    public String createImage(int width, int height) throws IOException {
        String uuid = UUID.randomUUID().toString();
        while (!Files.exists(Path.of(generateFilePath(UUID.randomUUID().toString())))){
            uuid = UUID.randomUUID().toString();
        }
        BufferedImage off_Image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Path contentFolder = Path.of(contentPath);
        if (!Files.exists(contentFolder)) {
            Files.createDirectories(contentFolder);
        }
        File outputFile = new File(generateFilePath(uuid));
        ImageIO.write(off_Image, "bmp", outputFile);
        return uuid;
    }
    @Override
    public void saveFragment(String id, int x, int y, int width, int height, MultipartFile file) throws IOException {
        BufferedImage inputFile = ImageIO.read(file.getInputStream());
        BufferedImage changeFile = ImageIO.read(new FileInputStream(generateFilePath(id)));
        for (int wIn = 0, wOut = x; wIn < width & wOut < changeFile.getWidth(); wIn++, wOut++) {
            for (int hIn = 0, hOut = y; hIn < height & hOut < changeFile.getHeight(); hIn++, hOut++) {
                int inputPixel = inputFile.getRGB(wIn, hIn);
                changeFile.setRGB(wOut, hOut, inputPixel);
            }
        }
        File result = new File(generateFilePath(id));
        ImageIO.write(changeFile, "bmp",result );
    }
    @Override
    public void deleteImage(String id) throws IOException {
        Files.delete(Path.of(id));
    }

    public String generateFilePath(String id) {
        String extension = ".bmp";
        return contentPath + id + extension;}
}
