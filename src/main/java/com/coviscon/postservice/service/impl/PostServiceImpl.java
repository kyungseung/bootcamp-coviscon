package com.coviscon.postservice.service.impl;

import com.coviscon.postservice.dto.querydsl.PostSearchCondition;
import com.coviscon.postservice.dto.request.RequestPostEdit;
import com.coviscon.postservice.dto.response.MemberResponseDto;
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
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
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
            String imageId,
            RequestPostCreate requestPostCreate,
            MemberResponseDto member) {

        /* image 정보 가져오기 */
        Item item = getItem(requestPostCreate.getItemId());

        requestPostCreate.setQnaStatus(QnaStatus.INCOMPLETE);
        Qna post = Qna.createPost(member, requestPostCreate);

        // Set postItem if item is not null
        if (item != null) {
            post.setPostItem(item);
        }

        Qna savedPost = postRepository.save(post);
        processImages(imageId, savedPost);

        return convertToQnaDto(savedPost);
    }

    private Item getItem(Long itemId) {
        return (itemId != null) ?
            itemRepository.findById(itemId)
                .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND)) : null;
    }

    /*
     * 제목 / 내용 / 제목 + 내용 검색
     */
    @Override
    public Page<ResponsePostDetail> searchByKeyword(
        String search, String keyword, String qnaStatus, Pageable pageable) {
        PostSearchCondition postSearchCondition = setPostSearchCondition(search, keyword, QnaStatus.valueOf(qnaStatus));

        return postRepository.searchByKeyword(postSearchCondition, pageable);
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
        responsePostDetail.setItemId(itemId);

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

    private void processImages(String imageId, Qna savedPost) {
        if (imageId != null && !imageId.trim().isEmpty()) {
            try {
                Long imageIdAsLong = Long.valueOf(imageId);
                Image foundImage = imageRepository.findById(imageIdAsLong)
                    .orElseThrow(() -> new CustomException(ErrorCode.IMAGE_NOT_FOUND));
                foundImage.setQna(savedPost);
            } catch (NumberFormatException e) {
                log.error("Invalid imageId format: {}", imageId);
            }
        }
        // imageId가 null이거나 빈 문자열인 경우에는 그냥 넘어가도록 설정
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
