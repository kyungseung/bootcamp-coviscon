package com.coviscon.postservice.repository;

import com.coviscon.postservice.entity.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
