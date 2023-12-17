package com.coviscon.postservice.repository;

import com.coviscon.postservice.entity.post.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Image searchBySaveImageName(String saveImageName);
}
