package com.wywin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
public class AuctionImageController {

    private final Path imagePath = Paths.get("C:/wywin/auctionItem/");

    @GetMapping("/images/auction/{filename:.+}")
    public Resource serveImage(@PathVariable String filename) {
        try {
            File file = imagePath.resolve(filename).toFile();
            return new UrlResource(file.toURI());
        } catch (Exception e) {
            // 예외 처리 (예: 404 응답)
            return null; // 또는 적절한 응답을 리턴
        }
    }
}
