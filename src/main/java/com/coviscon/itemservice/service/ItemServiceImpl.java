package com.coviscon.itemservice.service;

import com.coviscon.itemservice.dto.querydsl.BookSearchCondition;
import com.coviscon.itemservice.dto.querydsl.PostSearchCondition;
import com.coviscon.itemservice.dto.request.RequestCreateLecture;
import com.coviscon.itemservice.dto.response.*;
import com.coviscon.itemservice.entity.item.*;
import com.coviscon.itemservice.entity.post.Qna;
import com.coviscon.itemservice.exception.CustomException;
import com.coviscon.itemservice.exception.ErrorCode;
import com.coviscon.itemservice.repository.*;
import com.coviscon.itemservice.dto.querydsl.LectureSearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final VideoRepository videoRepository;
    private final LectureRepository lectureRepository;
    private final BookRepository bookRepository;
    private final PostRepository postRepository;

    @Value("${image.save.path}")
    private String savedImage;


    @PostConstruct
    @Transactional
    public void init() {
        for (int i = 0; i < 5; i++) {
            Lecture lecture = Lecture.addLecture("spring" + i, "springBoot" + i, 1000, Category.SPRING, 101L, "수파덕 선생님");
            Video video = Video.createVideo(lecture);
            itemRepository.save(lecture);
            videoRepository.save(video);
        }

        for (int i = 0; i < 5; i++) {
            Lecture lecture = Lecture.addLecture("spring" + i, "springBoot" + i, 2000, Category.JPA, 102L, "수파덕 선생님");
            itemRepository.save(lecture);
        }

        Book book1 = Book.addBook("점프 투 스프링부트 3", "박응용", "이지스 퍼블리싱", "https://product.kyobobook.co.kr/detail/S000211685975", "spring1.jpg", Category.SPRING);
        itemRepository.save(book1);
        Book book2 = Book.addBook("그림으로 이해하는 AWS 구조와 기술", "오가사와라 시게타카", "길벗", "https://www.yes24.com/Product/Goods/102368122", "aws1.png", Category.AWS);
        itemRepository.save(book2);
//        Book book3 = Book.addBook("업무에 바로 쓰는 AWS 입문", "김성민", "한빛미디어", "https://www.yes24.com/Product/Goods/116626210", "aws2.png", Category.AWS);
//        itemRepository.save(book3);
//        Book book4 = Book.addBook("AWS 교과서", "김원일", "길벗", "https://www.yes24.com/Product/Goods/123016650", "aws3.png", Category.AWS);
//        itemRepository.save(book4);
        Book book5 = Book.addBook("Docker 도커 실전 가이드", "사쿠라이 요이치로", "영진닷컴", "https://www.yes24.com/Product/Goods/101878731", "docker1.png", Category.DOCKER);
        itemRepository.save(book5);
//        Book book6 = Book.addBook("도커, 컨테이너 빌드업!", "이현룡", "제이펍", "https://www.yes24.com/Product/Goods/105756626", "docker2.png", Category.DOCKER);
//        itemRepository.save(book6);
//        Book book7 = Book.addBook("제대로 배우는 도커", "아드리안 모트", "비제이퍼블릭", "https://product.kyobobook.co.kr/detail/S000001890811", "docker3.png", Category.DOCKER);
//        itemRepository.save(book7);
        Book book8 = Book.addBook("젠킨스 2 시작하기", "브렌트 래스터", "에이콘출판사", "https://www.yes24.com/Product/Goods/70894020", "jenkins1.png", Category.JENKINS);
        itemRepository.save(book8);
//        Book book9 = Book.addBook("초보를 위한 젠킨스 2 활용 가이드 2/e", "니킬 파타니아", "에이콘출판사", "https://www.yes24.com/Product/Goods/64691711", "jenkins2.png", Category.JENKINS);
//        itemRepository.save(book9);
//        Book book10 = Book.addBook("젠킨스 마스터", "조나단 맥앨리스터", "에이콘출판사", "https://www.yes24.com/Product/Goods/60568781", "jenkins3.png", Category.JENKINS);
//        itemRepository.save(book10);
//        Book book11 = Book.addBook("쿠버네티스 교과서", "엘튼 스톤맨", "길벗", "https://www.yes24.com/Product/Goods/121992570", "kubernetes1.png", Category.KUBERNETES);
//        itemRepository.save(book11);
//        Book book12 = Book.addBook("쿠버네티스 입문", "정원천", "동양북스", "https://www.yes24.com/Product/Goods/85578606", "kubernetes2.png", Category.KUBERNETES);
//        itemRepository.save(book12);
        Book book13 = Book.addBook("24단계 실습으로 정복하는 쿠버네티스", "이정훈", "위키북스", "https://www.yes24.com/Product/Goods/115187666", "kubernetes3.png", Category.KUBERNETES);
        itemRepository.save(book13);
    }

    @Override
    public Page<ResponseLectureList> searchLectureList(String category, String search, String keyword, Pageable pageable) {
        LectureSearchCondition lectureSearchCondition = setLectureSearchCondition(Category.valueOf(category), search, keyword);
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        return itemRepository.searchByCondition(lectureSearchCondition, pageRequest);
    }

    @Override
    public int lectureTotalPage(String category, String search, String keyword, Pageable pageable) {
        log.info("[ItemServiceImpl lectureTotalPage] search Lecture List");

        LectureSearchCondition lectureSearchCondition =
                setLectureSearchCondition(Category.valueOf(category), search, keyword);

        return itemRepository.lectureTotalPage(lectureSearchCondition, pageable);
    }

    /**
     * 강의 리스트 전체 검색(디폴트 값 설정)용 전체 페이지 수 계산, 주석 처리
     **/
//    @Override
//    public int defaultLectureTotalPage(Pageable pageable) {
//        LectureSearchCondition lectureSearchCondition = setDefaultLectureSearchCondition();
//        return itemRepository.lectureTotalPage(lectureSearchCondition, pageable);
//    }
    @Override
    public ResponseLectureDetail findLectureDetail(Long itemId) {
        log.info("[ItemServiceImpl findLectureDetail] itemId : {}", itemId);

        Lecture lectureDetails = lectureRepository.findById(itemId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));

        String thumbnailFileName = getThumbnailFileName(lectureDetails);
        String savedPath = getSavedPathInVideo(lectureDetails);
        String subVideoName = getSubVideoName(lectureDetails);

        ResponseLectureDetail lectureDetail =
                getLectureDetail(lectureDetails, savedPath, thumbnailFileName, subVideoName);

        log.info("[ItemServiceImpl findLectureDetail] thumbnailFileName: {}", thumbnailFileName);
        log.info("[ItemServiceImpl findLectureDetail] LectureDetail: {}", lectureDetail);

        return lectureDetail;
    }

    @Override
    public Page<ResponseBookList> searchBookList(String category, String search, String keyword, Pageable pageable) {
        log.info("[ItemServiceImpl searchBookList]");

        BookSearchCondition bookSearchCondition
                = setBookSearchCondition(Category.valueOf(category), search, keyword);

        log.info("[ItemServiceImpl searchBookList] BookSearchCondition: {}", bookSearchCondition);
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        return bookRepository.searchByCondition(bookSearchCondition, pageRequest);
    }

    /**
     * 책 리스트 전체 조회(디폴트 값 설정), 주석 처리
     **/
//    @Override
//    public Page<ResponseBookList> allBookList(Pageable pageable) {
//        BookSearchCondition bookSearchCondition = setDefaultBookSearchCondition();
//        log.info("BookSearchCondition: {}", bookSearchCondition);
//        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
//
//        return bookRepository.searchByCondition(bookSearchCondition, pageRequest);
//    }
    @Override
    public int bookTotalPage(String category, String search, String keyword, Pageable pageable) {
        log.info("[ItemServiceImpl bookTotalPage]");

        BookSearchCondition bookSearchCondition =
                setBookSearchCondition(Category.valueOf(category), search, keyword);

        return bookRepository.bookTotalPage(bookSearchCondition, pageable);
    }

    /**
     * 책 리스트 전체 검색(디폴트 값 설정)용 전체 페이지 수 계산, 주석 처리
     **/
//    @Override
//    public int defaultBookTotalPage(Pageable pageable) {
//        BookSearchCondition bookSearchCondition = setDefaultBookSearchCondition();
//        return bookRepository.bookTotalPage(bookSearchCondition, pageable);
//    }
    @Override
    public List<ResponseLectureList> searchTeacherLectures(MemberResponseDto member, String search, String keyword) {
        log.info("[ItemServiceImpl searchTeacherLectures] member : {}", member);

        LectureSearchCondition lectureSearchCondition = setTLectureSearchCondition(search, keyword);

        return itemRepository.tSearchByCondition(lectureSearchCondition, member);
    }

    @Override
    @Transactional
    public void deleteLecture(Long itemId, MemberResponseDto member) {
        log.info("[ItemServiceImpl deleteLecture] member : {}", member);

        if (member.getMemberId() == null || !member.getRole().equals("ROLE_TEACHER")) {
            throw new CustomException(ErrorCode.UNSUCCESSFUL_DELETE);
        }

        itemRepository.deleteById(itemId);
    }

    @Override
    @Transactional
    public void invalidLecture(Long itemId, MemberResponseDto member) {
        log.info("[ItemServiceImpl invalidLecture] member : {}", member);

        if (member.getMemberId() == null || !member.getRole().equals("ROLE_TEACHER")) {
            throw new CustomException(ErrorCode.UNSUCCESSFUL_MODIFY);
        }
        Lecture lecture = itemRepository.findIsDeleteById(itemId);
        boolean isDelete = !lecture.isDelete();
        lecture.setDelete(isDelete);
    }

    @Override
    @Transactional
    public void createNewLecture(RequestCreateLecture requestCreateLecture, MemberResponseDto member) {
        log.info("[ItemServiceImpl createNewLecture] RequestCreateLecture : {}", requestCreateLecture);

        if (member.getMemberId() == null || !member.getRole().equals("ROLE_TEACHER") || requestCreateLecture.getCategory() == null || requestCreateLecture.getRealVideoName() == null) {
            throw new CustomException(ErrorCode.UNSUCCESSFUL_INSERT);
        }

        requestCreateLecture.setSavedPath(getSavedPath());
        Lecture lecture = Lecture.createLecture(requestCreateLecture);
        Video video = Video.createNewVideo(requestCreateLecture, lecture);

        lectureRepository.save(lecture);
        videoRepository.save(video);
    }

    @Override
    @Transactional
    public void updateLecture(RequestCreateLecture requestCreateLecture, MemberResponseDto member) {
        log.info("[ItemServiceImpl updateLecture] RequestCreateLecture : {}", requestCreateLecture);

        if (member.getMemberId() == null || !member.getRole().equals("ROLE_TEACHER")) {
            throw new CustomException(ErrorCode.UNSUCCESSFUL_MODIFY);
        }
        requestCreateLecture.setSavedPath(getSavedPath());
        Lecture lecture = lectureRepository.findById(requestCreateLecture.getItemId()).orElseThrow();
        Video video = videoRepository.findByItemFetch(requestCreateLecture.getItemId());

        lecture.updateLecture(requestCreateLecture);
        video.updateVideo(requestCreateLecture);
    }

    @Override
    public void uploadImg(MultipartFile multipartFile) throws IOException {
        log.info("[ItemServiceImpl uploadImg] MultipartFile : {}", multipartFile);
        if (!multipartFile.isEmpty()) {
            Path filePath = Paths.get(savedImage, multipartFile.getOriginalFilename());
            log.info("[ItemServiceImpl uploadImg] filePath : {}", filePath);

            File file = new File(String.valueOf(filePath));
            multipartFile.transferTo(file);
            
//            try (OutputStream os = Files.newOutputStream(filePath)) {
//                os.write(multipartFile.getBytes());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
        }

    }

    @Override
    @Transactional
    public ResponseLectureDetail updateLikeCnt(Long itemId) {
        Lecture lecture = (Lecture) itemRepository.findById(itemId)
                .orElseThrow(IllegalArgumentException::new);
        lecture.setLikeCnt(lecture.getLikeCnt() + 1);

        ModelMapper mapper = new ModelMapper();
        return mapper.map(lecture, ResponseLectureDetail.class);
    }


    @Override
    public Page<ResponsePostList> searchPostKeyword(String search, String keyword, Long itemId, Pageable pageable) {
        PostSearchCondition postSearchCondition = setPostSearchCondition(search, keyword);
        Page<ResponsePostList> postLists = postRepository.searchByKeyword(postSearchCondition, itemId, pageable);
        log.info("LectureDetail: {}", postLists);
        return postLists;
    }

    private String getThumbnailFileName(Lecture lectureDetails) {
        return lectureDetails.getVideos()
                .stream()
                .findAny()
                .map(Video::getThumbnailFileName)
                .orElse(null);
    }

    private String getSavedPathInVideo(Lecture lectureDetails) {
        return lectureDetails.getVideos()
                .stream()
                .findAny()
                .map(Video::getSavedPath)
                .orElse(null);
    }

    private String getSubVideoName(Lecture lectureDetails) {
        return lectureDetails.getVideos()
                .stream()
                .findAny()
                .map(Video::getSubVideoName)
                .orElse(null);
    }

    private ResponseLectureDetail getLectureDetail(Lecture lectureDetails, String savedPath, String thumbnailFileName, String subVideoName) {
        return ResponseLectureDetail.builder()
                .id(lectureDetails.getId())
                .title(lectureDetails.getTitle())
                .content(lectureDetails.getContent())
                .isDelete(lectureDetails.isDelete())
                .price(lectureDetails.getPrice())
                .likeCnt(lectureDetails.getLikeCnt())
                .teacherName(lectureDetails.getTeacherName())
                .savedPath(savedPath)
                .thumbnailFileName(thumbnailFileName)
                .subVideoName(subVideoName)
                .build();
    }

    /**
     * ItemSearchCondition 세팅
     */
    private LectureSearchCondition setLectureSearchCondition(Category category, String search, String keyword) {
        LectureSearchCondition lectureSearchCondition = new LectureSearchCondition();
        /* category select 를 통한 category 설정 */
        lectureSearchCondition.setCategory(category);

        /* 검색어를 통한 title, content 설정 */
        if (search.equals("title")) {
            lectureSearchCondition.setTitle(keyword);
        } else if (search.equals("content")) {
            lectureSearchCondition.setContent(keyword);
        } else {
            lectureSearchCondition.setTitle(keyword);
            lectureSearchCondition.setContent(keyword);

        }

        return lectureSearchCondition;
    }

    /**
     * 강사용 ItemSearchCondition 세팅
     */
    private LectureSearchCondition setTLectureSearchCondition(String search, String keyword) {
        LectureSearchCondition lectureSearchCondition = new LectureSearchCondition();

        /* 검색어를 통한 title, content 설정 */
        if (search.equals("title")) {
            lectureSearchCondition.setTitle(keyword);
        } else if (search.equals("content")) {
            lectureSearchCondition.setContent(keyword);
        } else {
            lectureSearchCondition.setTitle(keyword);
            lectureSearchCondition.setContent(keyword);

        }

        return lectureSearchCondition;
    }


//    private LectureSearchCondition setDefaultLectureSearchCondition() {
//        LectureSearchCondition lectureSearchCondition = new LectureSearchCondition();
//        /* category select 를 통한 category 설정 */
//        String search = "";
//        String keyword = "";
//
//        lectureSearchCondition.setCategory(Category.valueOf("NONE"));
//
//        /* 검색어를 통한 title, content 설정 */
//        if (search.equals("title")) {
//            lectureSearchCondition.setTitle(keyword);
//        } else if (search.equals("content")) {
//            lectureSearchCondition.setContent(keyword);
//        } else {
//            lectureSearchCondition.setTitle(keyword);
//            lectureSearchCondition.setContent(keyword);
//
//        }
//
//        return lectureSearchCondition;
//    }

    /**
     * BookSearchCondition 세팅
     */
    private BookSearchCondition setBookSearchCondition(Category category, String search, String keyword) {
        BookSearchCondition bookSearchCondition = new BookSearchCondition();
        /* category select 를 통한 category 설정 */
        bookSearchCondition.setCategory(category);

        /* 검색어를 통한 title, content 설정 */
        if (search.equals("title")) {
            bookSearchCondition.setTitle(keyword);
        } else if (search.equals("author")) {
            bookSearchCondition.setAuthor(keyword);
        } else {
            bookSearchCondition.setTitle(keyword);
            bookSearchCondition.setAuthor(keyword);

        }

        return bookSearchCondition;
    }


    /**
     * 일반 컨트롤러용 BookSearchCondition 세팅 - Default 값 입력
     */
//    private BookSearchCondition setDefaultBookSearchCondition() {
//        BookSearchCondition bookSearchCondition = new BookSearchCondition();
//        /* category select 를 통한 category 설정 */
//        String search = "";
//        String keyword = "";
//
//        bookSearchCondition.setCategory(Category.valueOf("NONE"));
//
//        /* 검색어를 통한 title, content 설정 */
//        if (search.equals("title")) {
//            bookSearchCondition.setTitle(keyword);
//        } else if (search.equals("author")) {
//            bookSearchCondition.setAuthor(keyword);
//        } else {
//            bookSearchCondition.setTitle(keyword);
//            bookSearchCondition.setAuthor(keyword);
//
//        }
//
//        return bookSearchCondition;
//    }

    /**
     * PostSearchCondition 세팅
     **/
    public PostSearchCondition setPostSearchCondition(String search, String keyword) {
        PostSearchCondition postSearchCondition = new PostSearchCondition();

        /* 검색어를 통한 title, content 설정 */
        if (search.equals("title")) {
            postSearchCondition.setTitle(keyword);

        } else if (search.equals("content")) {
            postSearchCondition.setContent(keyword);

        }
        return postSearchCondition;
    }

    /**
     * video 저장 경로
     */
    private String getSavedPath() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String now = sdf.format(System.currentTimeMillis());
        return "https://coviscon-bucket.s3.ap-northeast-2.amazonaws.com/video/";
    }
}
