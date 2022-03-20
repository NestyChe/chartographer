package com.example.chartographer.controller;
import com.example.chartographer.service.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.*;


@Validated
@RestController
@RequestMapping("/chartas")
public class ChartographerController {
    private final Service service;

    public ChartographerController(Service service) {
        this.service = service;
    }


    @GetMapping(value = "/{id}/", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getFragment(@PathVariable(value = "id") String id,
                                                        @RequestParam int x,
                                                        @RequestParam  int y,
                                                        @Min(0) @Max(5000) @RequestParam int width,
                                                        @Min(0) @Max(5000) @RequestParam int height) {
        try {
            return new ResponseEntity<>(service.getFragment(id, x, y, width, height), HttpStatus.OK);
        } catch (IOException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/")
    public ResponseEntity<String> createImage(@RequestParam int width, @RequestParam int height) throws IOException {
        if ((width <= 50000 & width <= 20000) & (height <= 20000 & height <=  50000)) {
            return new ResponseEntity<>(service.createImage(width, height), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/")
    public ResponseEntity<Object> saveFragment(@PathVariable(value = "id") String id, @RequestParam int x,
                                               @RequestParam int  y, @RequestParam int width, @RequestParam int height,
                                               @RequestParam("file") MultipartFile file) {
        if ((width <= 50000 & width <= 20000) & (height <= 20000 & height <=  50000)) {
            try {
                service.saveFragment(id, x, y, width, height, file);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (IOException exc) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<String> deleteImage(@PathVariable(value = "id") String id) {
        try {
            service.deleteImage(id);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (IOException exc) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
