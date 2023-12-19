package com.coviscon.itemservice.repository;

import com.coviscon.itemservice.entity.item.Video;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    @Query("select v from Video v join fetch v.lecture l where l.id = :itemId")
    Video findByItemFetch(@Param("itemId") Long itemId);

//    @EntityGraph(attributePaths = { "lecture" })
//    @Query("select v from Video v where v.lecture.id = :itemID")
//    Video findByItemFetch2(@Param("itemId") Long itemId);
}
