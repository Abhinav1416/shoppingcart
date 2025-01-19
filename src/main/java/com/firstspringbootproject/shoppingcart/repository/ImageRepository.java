package com.firstspringbootproject.shoppingcart.repository;

import com.firstspringbootproject.shoppingcart.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
