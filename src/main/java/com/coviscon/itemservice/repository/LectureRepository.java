package com.coviscon.itemservice.repository;

import com.coviscon.itemservice.entity.item.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
