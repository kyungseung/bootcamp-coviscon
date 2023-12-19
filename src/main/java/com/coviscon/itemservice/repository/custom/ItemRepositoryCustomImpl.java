package com.coviscon.itemservice.repository.custom;

import com.coviscon.itemservice.dto.response.MemberResponseDto;
import com.coviscon.itemservice.dto.response.QResponseLectureList;
import com.coviscon.itemservice.dto.response.ResponseLectureList;
import com.coviscon.itemservice.entity.item.Category;
import com.coviscon.itemservice.dto.querydsl.LectureSearchCondition;
import com.coviscon.itemservice.repository.custom.ItemRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.coviscon.itemservice.entity.item.QLecture.lecture;
import static com.coviscon.itemservice.entity.item.QVideo.video;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ResponseLectureList> searchByCondition(
            LectureSearchCondition lectureSearchCondition,
            Pageable pageable) {
        log.info("[ItemRepositoryCustomImpl] LectureSearchCondition : {}", lectureSearchCondition);

        List<ResponseLectureList> items = queryFactory
                .select(new QResponseLectureList(
                        lecture.id,
                        lecture.title,
                        lecture.price,
                        lecture.isDelete,
                        lecture.teacherName,
                        lecture.likeCnt,
                        video.thumbnailFileName,
                        video.savedPath
                ))
                .from(video)
                .leftJoin(video.lecture, lecture)
                .where(
                        categoryEq(lectureSearchCondition.getCategory()),
                        titleEq(lectureSearchCondition.getTitle()),
                        contentEq(lectureSearchCondition.getContent()),
                        lecture.isDelete.eq(false)
                )
                .orderBy(lecture.lastModifiedDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        log.info("[ItemRepositoryCustomImpl] items.size : {}", items.size());

        JPAQuery<Long> countQuery = queryFactory
                .select(lecture.count())
                .from(lecture)
                .where(lecture.category.eq(lectureSearchCondition.getCategory()));
        return PageableExecutionUtils.getPage(items, pageable, () -> (long) countQuery.fetch().size());
    }

    @Override
    public List<ResponseLectureList> tSearchByCondition(LectureSearchCondition lectureSearchCondition, MemberResponseDto member) {
        log.info("[ItemRepositoryCustomImpl] LectureSearchCondition : {}", lectureSearchCondition);

        List<ResponseLectureList> items = queryFactory
                .select(new QResponseLectureList(
                        lecture.id,
                        lecture.title,
                        lecture.price,
                        lecture.isDelete,
                        lecture.teacherName,
                        lecture.likeCnt,
                        video.thumbnailFileName,
                        video.savedPath
                ))
                .from(video)
                .leftJoin(video.lecture, lecture)
                .where(
                        titleEq(lectureSearchCondition.getTitle()),
                        contentEq(lectureSearchCondition.getContent()),
                        lecture.teacherId.eq(member.getMemberId())
                )
                .orderBy(lecture.lastModifiedDate.desc())
                .fetch();

        log.info("[ItemRepositoryCustomImpl] items.size : {}", items.size());

        return items;
    }

    @Override
    public int lectureTotalPage(LectureSearchCondition lectureSearchCondition, Pageable pageable) {
        int totalSize = queryFactory
                .selectFrom(lecture)
                .where(
                        categoryEq(lectureSearchCondition.getCategory()),
                        titleEq(lectureSearchCondition.getTitle()),
                        contentEq(lectureSearchCondition.getContent())
                )
                .fetch().size();
        int pageSize = pageable.getPageSize();
        return totalSize % pageable.getPageSize() == 0 ? totalSize / pageSize : (totalSize / pageSize) + 1;
    }

    private BooleanExpression categoryEq(Category category) {
        return !category.equals(Category.NONE) ? lecture.category.eq(category) : null;
    }

    private BooleanExpression contentEq(String content) {
        return hasText(content) ? lecture.content.contains(content) : null;
    }

    private BooleanExpression titleEq(String title) {
        return hasText(title) ? lecture.title.contains(title) : null;
    }
}
