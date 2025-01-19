package com.firstspringbootproject.shoppingcart.service.image;

import com.firstspringbootproject.shoppingcart.dto.ImageDto;
import com.firstspringbootproject.shoppingcart.exceptions.ResourceNotFoundException;
import com.firstspringbootproject.shoppingcart.model.Image;
import com.firstspringbootproject.shoppingcart.model.Product;
import com.firstspringbootproject.shoppingcart.repository.ImageRepository;
import com.firstspringbootproject.shoppingcart.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ImageService implements IImageService {

    // This will inject the required Dependencies with the help of @RequiredArgsConstructor
    private final ImageRepository imageRepository;
    private final IProductService productService;

    @Override
    public Image getImageById(Long id) {
        return this.imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));
    }

    @Override
    public void deleteImageById(Long id) {
        this.imageRepository.findById(id)
                .ifPresentOrElse(this.imageRepository::delete, () -> {
                    throw new ResourceNotFoundException("No image found with id: " + id);
                });
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product product = this.productService.getProductById(productId);
        List<ImageDto> savedImageDto = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                // sets all the values except URL (In the next 4 lines)
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                // Creating URL for every new image
                String buildDownloadUrl = "/api/v1/images/image/download/";
                String downloadUrl = buildDownloadUrl + image.getId();
                // sets URL

                /* so in this case when I generate the downloadUrl for the 1st time its id is null, but after I have saved the object in my database the objects
                 actual id is not null but the already generated downloadUrl is null, so I will set the downloadUrl again as the downloadUrl for the object is
                 changed, so I will again have to save it in my database */

                image.setDownloadUrl(downloadUrl);
                Image savedImage = this.imageRepository.save(image);

                savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
                this.imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setImageId(savedImage.getId());
                imageDto.setImageName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDto.add(imageDto);
            }
            catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename()); // getOriginalFilename() will return the name of the file that the user has uploaded from his local machine
            image.setImage(new SerialBlob(file.getBytes()));
            this.imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
