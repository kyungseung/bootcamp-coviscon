package com.coviscon.orderservice.repository;

import com.coviscon.orderservice.entity.item.Item;
import com.coviscon.orderservice.entity.item.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
