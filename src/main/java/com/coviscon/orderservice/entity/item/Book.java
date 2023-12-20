package com.coviscon.orderservice.entity.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 추천도서, 판매 안함
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = "book")
@Entity
public class Book extends Item {

//    @Column(nullable = false)
    private String author; // 저자
//    @Column(nullable = false)
    private String press; // 출판사
//    @Column(nullable = false)
    private String url; // 구매 링크
    private String imageFileName;

    public static Book addBook(String title, String author, String press, String url, String imageFileName) {
        // 파일 업로드 추가될 시 파라미터 늘어날 예정
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPress(press);
        book.setUrl(url);
        book.setImageFileName(imageFileName);

        return book;
    }
}
