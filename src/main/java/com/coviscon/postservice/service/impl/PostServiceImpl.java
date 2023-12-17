package com.coviscon.postservice.service.impl;

import com.coviscon.postservice.dto.querydsl.PostSearchCondition;
import com.coviscon.postservice.dto.request.RequestPostEdit;
import com.coviscon.postservice.dto.MemberResponseDto;
import com.coviscon.postservice.dto.response.ResponsePostDetail;
import com.coviscon.postservice.dto.response.ResponsePostEdit;
import com.coviscon.postservice.entity.item.Category;
import com.coviscon.postservice.entity.item.Item;
import com.coviscon.postservice.entity.item.Lecture;
import com.coviscon.postservice.entity.post.Image;
import com.coviscon.postservice.entity.post.Qna;
import com.coviscon.postservice.entity.post.QnaStatus;
import com.coviscon.postservice.exception.CustomException;
import com.coviscon.postservice.exception.ErrorCode;
import com.coviscon.postservice.repository.ImageRepository;
import com.coviscon.postservice.repository.ItemRepository;
import com.coviscon.postservice.repository.PostRepository;
import com.coviscon.postservice.dto.request.RequestPostCreate;
import com.coviscon.postservice.service.PostService;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final ItemRepository itemRepository;

    @PostConstruct
    @Transactional
    public void init() {
        for (int i = 0; i < 30; i++) {
            Lecture lecture = Lecture.addLecture("spring 입문" + i, "springBoot" + i, 1000, Category.SPRING, "수파덕");
            itemRepository.save(lecture);

            Qna qna = Qna.addQna("질문이 있어요! " + i, "spring을 잘하고 싶어요" + i, 10L, "코딩으로우주정복", lecture);
            postRepository.save(qna);
        }
    }

    /*
    *   [커뮤니티] 모든 질문 보기
    */
    @Override
    public List<ResponsePostDetail> getAllPostList() {
        List<Qna> postList = postRepository.findAll();

        return postList.stream().map(
            qna -> new ResponsePostDetail(
                qna.getId(), qna.getLastModifiedDate(), qna.getTitle(), qna.getContent(), qna.getQnaStatus(),
                qna.getNickName(), qna.getLecture().getId())
            )
            .collect(Collectors.toList());
    }

    /*
    *   [특정 강의에 대해] 질문 작성하기
    */
    @Override
    @Transactional
    public ResponsePostDetail createPost(
        MemberResponseDto member,
        RequestPostCreate requestPostCreate,
        HttpSession session) {

        /* image 정보 가져오기 */
        Item item = getItem(requestPostCreate.getItemId());

        requestPostCreate.setQnaStatus(QnaStatus.INCOMPLETE);
        Qna post = Qna.createPost(member, requestPostCreate);

        // Set postItem if item is not null
        if (item != null) {
            post.setPostItem(item);
        }

        Qna savedPost = postRepository.save(post);
        processImages(session, savedPost);

        return convertToQnaDto(savedPost);
    }

    private Item getItem(Long itemId) {
        return (itemId != null) ?
            itemRepository.findById(itemId)
                .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND)) : null;
    }

    private void processImages(HttpSession session, Qna savedPost) {
        Enumeration<String> names = session.getAttributeNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if (!name.equals("member")) {
                Image image = (Image) session.getAttribute(name);
                Image findImage = imageRepository.findById(image.getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
                findImage.setQna(savedPost);
                session.removeAttribute(name);
            }
        }
    }

    /*
     * 제목 / 내용 / 제목 + 내용 검색
     */
    @Override
    public Page<ResponsePostDetail> searchByKeyword(
        String search, String keyword, String qnaStatus, Pageable pageable) {
        PostSearchCondition postSearchCondition = setPostSearchCondition(search, keyword, QnaStatus.valueOf(qnaStatus));

        Page<ResponsePostDetail> responsePostDetails = postRepository.searchByKeyword(
            postSearchCondition, pageable);

        return responsePostDetails;
    }

    @Override
    public int postTotalPage(String search, String keyword, String qnaStatus, Pageable pageable) {
        PostSearchCondition postSearchCondition = setPostSearchCondition(search, keyword, QnaStatus.valueOf(qnaStatus));

        return postRepository.postTotalPage(postSearchCondition, pageable);
    }

    @Override
    public PostSearchCondition setPostSearchCondition(String search, String keyword, QnaStatus qnaStatus) {
        PostSearchCondition postSearchCondition = new PostSearchCondition();

        postSearchCondition.setQnaStatus(qnaStatus);

        /* 검색어를 통한 title, content 설정 */
        if (search.equals("title")) {
            postSearchCondition.setTitle(keyword);

        } else if (search.equals("content")) {
            postSearchCondition.setContent(keyword);
        }

        return postSearchCondition;
    }

    @Override
    public ResponsePostDetail getPostById(Long qnaId, Long itemId) {
        Qna qna = postRepository.findById(qnaId)
            .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));

        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));

        ResponsePostDetail responsePostDetail = new ResponsePostDetail();
        responsePostDetail.goPost(qna);
        responsePostDetail.setItemTitle(item.getTitle());

        return responsePostDetail;
    }

    @Override
    public ResponsePostDetail getCommunityPostById(Long qnaId) {
        Qna qna = postRepository.findById(qnaId)
            .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));

        ResponsePostDetail responsePostDetail = new ResponsePostDetail();
        responsePostDetail.goPost(qna);

        return responsePostDetail;
    }

    @Override
    public ResponsePostEdit modifyPostById(Long qnaId) {
        Qna qna = postRepository.findById(qnaId)
            .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));

        return setResponsePostEdit(qna);
    }

    @Override
    @Transactional
    public void modifyPost(Long qnaId, RequestPostEdit requestPostEdit) {
        Qna qna = this.postRepository.findById(qnaId)
            .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));

        qna.updatePost(requestPostEdit);
    }

    @Override
    @Transactional
    public void deletePost(Long qnaId) {
        Qna qna = postRepository.findById(qnaId)
            .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
        postRepository.delete(qna);
    }

    @Override
    @Transactional
    public void qnaStatusUpdate(Long qnaId) {
        Qna qna = postRepository.findById(qnaId)
            .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));

        qna.setQnaStatus(QnaStatus.COMPLETE);
        postRepository.save(qna);
    }

    @Override
    public List<ResponsePostDetail> searchAllPost(MemberResponseDto memberResponseDto) {
        return postRepository.searchAllPost(memberResponseDto);
    }

    @Override
    public ResponsePostDetail searchPost(Long qnaId, MemberResponseDto memberResponseDto) {
        return postRepository.searchPost(qnaId, memberResponseDto);
    }

    private ResponsePostDetail convertToQnaDto(Qna qna) {
        return ResponsePostDetail.builder()
            .qnaId(qna.getId())
            .title(qna.getTitle())
            .content(qna.getContent())
            .qnaStatus(qna.getQnaStatus())
            .build();
    }

    private ResponsePostEdit setResponsePostEdit(Qna qna) {
        return ResponsePostEdit.builder()
            .qnaId(qna.getId())
            .title(qna.getTitle())
            .content(qna.getContent())
            .qnaStatus(qna.getQnaStatus())
            .build();
    }

}
