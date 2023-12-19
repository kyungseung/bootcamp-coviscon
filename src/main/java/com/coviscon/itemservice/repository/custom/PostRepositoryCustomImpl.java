package com.coviscon.itemservice.repository.custom;


import com.coviscon.itemservice.dto.querydsl.PostSearchCondition;
import com.coviscon.itemservice.dto.response.QResponsePostList;
import com.coviscon.itemservice.dto.response.ResponsePostList;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;


import java.util.List;

import static com.coviscon.itemservice.entity.post.QQna.qna;
import static org.springframework.util.StringUtils.hasText;

public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public PostRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ResponsePostList> searchByKeyword(PostSearchCondition postSearchCondition, Long itemId, Pageable pageable) {
        List<ResponsePostList> postContent = queryFactory
            .select(new QResponsePostList(
                qna.title,
                qna.content,
                qna.nickName,
                qna.lastModifiedDate,
                qna.id,
                qna.qnaStatus
            ))
            .from(qna)
            .where(
                titleEq(postSearchCondition.getTitle()),
                contentEq(postSearchCondition.getContent()),
                qna.lecture.id.eq(itemId)
            )
            .orderBy(qna.lastModifiedDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(qna.count())
            .from(qna)
            .where(
                titleEq(postSearchCondition.getTitle()),
                contentEq(postSearchCondition.getContent())
            );

        return PageableExecutionUtils.getPage(postContent, pageable, () -> (long) countQuery.fetch().size());
    }

    private BooleanExpression contentEq(String content) {
        return hasText(content) ? qna.content.contains(content) : null;
    }

    private BooleanExpression titleEq(String title) {
        return hasText(title) ? qna.title.contains(title) : null;
    }
}
