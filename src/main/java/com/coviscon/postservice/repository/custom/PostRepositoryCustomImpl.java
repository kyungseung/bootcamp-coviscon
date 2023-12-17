package com.coviscon.postservice.repository.custom;

import static com.coviscon.postservice.entity.post.QQna.qna;
import static org.springframework.util.StringUtils.hasText;

import com.coviscon.postservice.dto.querydsl.PostSearchCondition;
import com.coviscon.postservice.dto.MemberResponseDto;
import com.coviscon.postservice.dto.response.QResponsePostDetail;
import com.coviscon.postservice.dto.response.ResponsePostDetail;
import com.coviscon.postservice.entity.post.QnaStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

public class PostRepositoryCustomImpl implements PostRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public PostRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * itemId를 기준으로 Post 조회
     */
    @Override
    public List<ResponsePostDetail> searchPostByItemId(
        Long itemId,
        MemberResponseDto memberResponseDto) {
        return queryFactory.select(new QResponsePostDetail(
                qna.id.as("qnaId"),
                qna.lastModifiedDate,
                qna.title,
                qna.content,
                qna.qnaStatus,
                qna.nickName
            ))
            .from(qna)
            .where(
                qna.lecture.id.eq(itemId)
            )
            .fetch();
    }

    /**
     * member (student), qnaId 를 통해 작성한 게시글 조회
     */
    @Override
    public ResponsePostDetail searchPost(Long qnaId, MemberResponseDto memberResponseDto) {
        return queryFactory.select(new QResponsePostDetail(
                qna.id.as("qnaId"),
                qna.lastModifiedDate,
                qna.title,
                qna.content,
                qna.qnaStatus,
                qna.nickName
            ))
            .from(qna)
            .where(
                qna.id.eq(qnaId),
                qna.memberId.eq(memberResponseDto.getMemberId())
            )
            .fetchOne();
    }

    /**
     * member (student, teacher) 를 통해 자신이 포함 된 qna List 전부 조회
     */
    @Override
    public List<ResponsePostDetail> searchAllPost(MemberResponseDto memberResponseDto) {
        return queryFactory.select(new QResponsePostDetail(
                qna.id.as("qnaId"),
                qna.lastModifiedDate,
                qna.title,
                qna.content,
                qna.qnaStatus,
                qna.nickName
            ))
            .from(qna)
            .where(
                qna.memberId.eq(memberResponseDto.getMemberId())
            )
            .fetch();
    }

    /**
     * 전체 게시글 목록에서 title/content 검색
     *  -> 전체 리스트를 가져올때 사용
     */
    @Override
    public Page<ResponsePostDetail> searchByKeyword(PostSearchCondition postSearchCondition, Pageable pageable) {
        List<ResponsePostDetail> postContent = queryFactory
            .select(new QResponsePostDetail(
                qna.id.as("qnaId"),
                qna.lastModifiedDate,
                qna.title,
                qna.content,
                qna.qnaStatus,
                qna.nickName,
                qna.lecture.id.as("itemId")
            ))
            .from(qna)
            .where(
                titleEq(postSearchCondition.getTitle()),
                contentEq(postSearchCondition.getContent()),
                qnaStatusEq(postSearchCondition.getQnaStatus())
            )
            .orderBy(qna.lastModifiedDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(qna.count())
            .from(qna)
            .where(qna.qnaStatus.eq(postSearchCondition.getQnaStatus()));
        return PageableExecutionUtils.getPage(postContent, pageable, () -> (long) countQuery.fetch().size());
    }

    @Override
    public int postTotalPage(PostSearchCondition postSearchCondition, Pageable pageable) {
        long totalSize = queryFactory
            .selectFrom(qna)
            .where(
                titleEq(postSearchCondition.getTitle()),
                contentEq(postSearchCondition.getContent()),
                qnaStatusEq(postSearchCondition.getQnaStatus())
            )
            .fetchCount();
        System.out.println("Total Size: " + totalSize);

        int pageSize = pageable.getPageSize();

        if (totalSize == 0) {
            return 0; // 게시글이 없는 경우 페이지도 없도록 수정
        }

        return (int) Math.ceil((double) totalSize / pageSize);
    }


    private BooleanExpression contentEq(String content) {
        return hasText(content) ? qna.content.contains(content) : null;
    }

    private BooleanExpression titleEq(String title) {
        return hasText(title) ? qna.title.contains(title) : null;
    }

    private BooleanExpression qnaStatusEq(QnaStatus qnaStatus) {
        return !qnaStatus.equals(QnaStatus.ALL) ? qna.qnaStatus.eq(qnaStatus) : null;
    }
}
