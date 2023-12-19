package com.coviscon.itemservice.repository;

import com.coviscon.itemservice.entity.item.Book;
import com.coviscon.itemservice.repository.custom.BookRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom {
}
