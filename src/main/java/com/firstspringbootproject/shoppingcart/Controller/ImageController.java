package com.firstspringbootproject.shoppingcart.Controller;


import com.firstspringbootproject.shoppingcart.dto.ImageDto;
import com.firstspringbootproject.shoppingcart.exceptions.ResourceNotFoundException;
import com.firstspringbootproject.shoppingcart.model.Image;
import com.firstspringbootproject.shoppingcart.repository.CategoryRepository;
import com.firstspringbootproject.shoppingcart.response.ApiResponse;
import com.firstspringbootproject.shoppingcart.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")

public class ImageController {
    private final IImageService imageService;
    private final CategoryRepository categoryRepository;

    // @RequestParam does the same work as @PathVariable but the key difference lies in the location of the passed data
    // ResponseEntity can be used to create our custom HTTPS status code, custom headers and custom Response body, and it is automatically gets converted into JSON

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId) {
        try {
            List<ImageDto> imageDtos = this.imageService.saveImages(files, productId);
            return ResponseEntity.ok(new ApiResponse("Upload success!", imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload failed!", e.getMessage()));
        }
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image = this.imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +image.getFileName() + "\"")
                .body(resource);
    }

    // @RequestBody is used to represent that thr present variable contains a JSON object
    @GetMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {
        try {
            Image image = this.imageService.getImageById(imageId);
            if (image != null) {
                this.imageService.updateImage(file, imageId);
                return ResponseEntity.ok(new ApiResponse("Update success!", image));
            }
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Update failed", INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
        try {
            Image image = this.imageService.getImageById(imageId);
            if (image != null) {
                this.imageService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("Delete success!", image));
            }
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete failed", INTERNAL_SERVER_ERROR));
    }
}
