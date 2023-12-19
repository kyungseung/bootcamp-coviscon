package com.coviscon.itemservice.repository.custom;

import com.coviscon.itemservice.dto.querydsl.BookSearchCondition;
import com.coviscon.itemservice.dto.response.QResponseBookList;
import com.coviscon.itemservice.dto.response.ResponseBookList;
import com.coviscon.itemservice.entity.item.Category;
import com.coviscon.itemservice.repository.custom.BookRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.coviscon.itemservice.entity.item.QBook.*;
import static com.coviscon.itemservice.entity.item.QItem.item;
import static com.coviscon.itemservice.entity.item.QLecture.lecture;
import static org.springframework.util.StringUtils.hasText;


@Slf4j
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BookRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ResponseBookList> searchByCondition(BookSearchCondition bookSearchCondition, Pageable pageable) {
            log.info("[BookRepositoryCustomImpl] BookSearchCondition : {}", bookSearchCondition);

            List<ResponseBookList> books = queryFactory
                    .select(new QResponseBookList(
                            book.title,
                            book.author,
                            book.press,
                            book.url,
                            book.imageFileName,
                            book.category
                    ))
                    .from(book)
                    .where(
                            categoryEq(bookSearchCondition.getCategory()),
                            titleEq(bookSearchCondition.getTitle()),
                            authorEq(bookSearchCondition.getAuthor())
                    )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            log.info("[BookRepositoryCustomImpl] books.size : {}", books.size());

            JPAQuery<Long> countQuery = queryFactory
                    .select(book.count())
                    .from(book)
                    .where(book.category.eq(bookSearchCondition.getCategory()));
            return PageableExecutionUtils.getPage(books, pageable, () -> (long) countQuery.fetch().size());
    }

    @Override
    public int bookTotalPage(BookSearchCondition bookSearchCondition, Pageable pageable) {
        int totalSize = queryFactory
                .selectFrom(book)
                .where(
                        categoryEq(bookSearchCondition.getCategory()),
                        titleEq(bookSearchCondition.getTitle()),
                        authorEq(bookSearchCondition.getAuthor())
                )
                .fetch().size();
        int pageSize = pageable.getPageSize();
        return totalSize % pageable.getPageSize() == 0 ? totalSize / pageSize : (totalSize / pageSize) + 1;
    }

    private BooleanExpression categoryEq(Category category) {
        return !category.equals(Category.NONE) ? book.category.eq(category) : null;
    }

    private BooleanExpression authorEq(String author) {
        return hasText(author) ? book.author.contains(author) : null;
    }

    private BooleanExpression titleEq(String title) {
        return hasText(title) ? book.title.contains(title) : null;
    }
}
