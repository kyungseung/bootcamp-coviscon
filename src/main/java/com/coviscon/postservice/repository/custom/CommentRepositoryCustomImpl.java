package com.coviscon.postservice.repository.custom;

import static com.coviscon.postservice.entity.post.QComment.comment;

import com.coviscon.postservice.dto.response.ResponseCommentList;
import com.coviscon.postservice.entity.post.Comment;
import com.coviscon.postservice.entity.post.QComment;
import com.coviscon.postservice.entity.post.Qna;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;

public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public CommentRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 특정 게시글(qnaId)의 댓글 가져오기
     */
    @Override
    public List<Comment> findCommentByQnaId(Long qnaId) {
        return queryFactory.selectFrom(comment)
            .leftJoin(comment.parent)
            .fetchJoin()
            .where(comment.qna.id.eq(qnaId))
            .orderBy(
                comment.parent.id.asc().nullsFirst(),
                comment.createDate.asc()
            ).fetch();
    }

    @Override
    public Optional<Comment> findCommentByIdWithParent(Long commentId) {
        return Optional.ofNullable(queryFactory.selectFrom(comment)
            .leftJoin(comment.parent)
            .fetchJoin()
            .where(comment.id.eq(commentId))
            .fetchOne()
        );
    }

}
