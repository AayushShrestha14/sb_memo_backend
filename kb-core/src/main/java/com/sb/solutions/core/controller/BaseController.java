package com.sb.solutions.core.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sb.solutions.core.constant.FilePath;

/**
 * @author Elvin Shrestha on 7/7/2020
 */
public abstract class BaseController<E, I> {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected abstract Logger getLogger();

    @PostMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestBody String path) {
        File file = new File(FilePath.getOSPath() + path);
        final InputStreamResource resource;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            logger.error("File not found {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
            String.format("attachment; filename=\"%s\"", file.getName()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok().headers(headers)
            .contentLength(file.length()).contentType(
                MediaType.parseMediaType("application/txt")).<Object>body(resource);
    }

}
