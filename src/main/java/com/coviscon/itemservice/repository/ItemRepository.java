package com.coviscon.itemservice.repository;

import com.coviscon.itemservice.entity.item.Item;
import com.coviscon.itemservice.entity.item.Lecture;
import com.coviscon.itemservice.repository.custom.ItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    Lecture findIsDeleteById(Long itemId);
}
