//package com.coviscon.memberservice.service.impl;
//
//import com.coviscon.memberservice.dto.MemberResponseDto;
//import com.coviscon.memberservice.dto.client.ResponseCartDetail;
//import com.coviscon.memberservice.dto.client.ResponseLectureDetail;
//import com.coviscon.memberservice.dto.client.ResponsePostDetail;
//import com.coviscon.memberservice.dto.querydsl.LectureSearchCondition;
//import com.coviscon.memberservice.dto.vo.MemberCart;
//import com.coviscon.memberservice.dto.vo.MemberQna;
//import com.coviscon.memberservice.dto.vo.TeacherLecture;
//import com.coviscon.memberservice.dto.vo.TeacherQna;
//import com.coviscon.memberservice.entity.member.Member;
//import com.coviscon.memberservice.entity.member.Role;
//import com.coviscon.memberservice.exception.CustomException;
//import com.coviscon.memberservice.exception.ErrorCode;
//import com.coviscon.memberservice.repository.MemberRepository;
//import com.coviscon.memberservice.service.MemberService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.modelmapper.ModelMapper;
//import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
//import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityNotFoundException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//@Transactional(readOnly = true)
//public class MemberServiceImpl implements MemberService {
//
//
//    private final MemberRepository memberRepository;
//    private final CircuitBreakerFactory circuitBreakerFactory;
//
//    /**
//     * member - 결제한 강의 list
//     * order-service
//     */
//    // member
//
//    /**
//     * member - 장바구니 조회
//     * item-service Feign Client
//     * @return MemberCart : member 정보 + cart 정보
//     */
//    @Override
//    public MemberCart searchCart(String email) {
//        log.info("[MemberServiceImpl searchCartByFeign] email : {}", email);
//
//        MemberResponseDto memberResponseDto = memberRepository.searchByEmail(email)
//                .orElseThrow(EntityNotFoundException::new);
//
//        // item feign client
//        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
//        List<ResponseCartDetail> cartDetails = circuitBreaker.run(
//                () -> itemServiceClient.searchMemberCart(memberResponseDto.getMemberId()),
//                throwable -> new ArrayList<>());
//
//        return new MemberCart(memberResponseDto, cartDetails);
//    }
//
//    /**
//     * student - qna list
//     * post-service Feign Client
//     * @return MemberQna : member 정보 + 작성한 qna list
//     */
//    @Override
//    public MemberQna searchAllMemberQna(String email) {
//        log.info("[MemberServiceImpl searchAllQnaByFeign] email : {}", email);
//
//        MemberResponseDto memberResponseDto = memberRepository.searchByEmail(email)
//                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
//
//        // post feign client
//        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
//        List<ResponsePostDetail> responsePostDetails = circuitBreaker.run(
//                () -> postServiceClient.searchMemberQna(memberResponseDto.getMemberId()),
//                throwable -> new ArrayList<>());
//
//        /**
//         * 작성한 qna 가 있으면 ? 없으면 ?
//         * 이거는 그냥 조회해도 괜찮을 것 같다
//         *
//         * DDD -> read / write 용 따로
//         * read 용 레플리카 가 많은편
//         */
//
//        return new MemberQna(memberResponseDto, responsePostDetails);
//    }
//    // teacher
//
//    /**
//     * teacher - 등록한 강의 list
//     * item-service Feign Client
//     * @return TeacherLecture : teacher 정보 + 작성한 강의 정보
//     */
//    @Override
//    public TeacherLecture searchTeacherLectures(String email, LectureSearchCondition condition) {
//        log.info("[MemberServiceImpl searchTeacherLectures] email : {}", email);
//
//        MemberResponseDto memberResponseDto = memberRepository.searchByEmail(email)
//                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
//
//        // item feign client
//        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
//        List<ResponseLectureDetail> lectures = circuitBreaker.run(
//                () -> itemServiceClient.searchTeacherLectures(memberResponseDto.getMemberId(), condition),
//                throwable -> new ArrayList<>());
//
//        return new TeacherLecture(memberResponseDto, lectures);
//    }
//
//    /**
//     * teacher - qna list
//     * teacher id -> item id -> qna id
//     * item-service -> qna-service
//     */
//    @Override
//    public TeacherQna searchAllTeacherQna(String email) {
//        log.info("[MemberServiceImpl searchAllTeacherQna] email : {}", email);
//
//        MemberResponseDto memberResponseDto = memberRepository.searchByEmail(email)
//                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
//
//        // post feign client
//        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
//        List<ResponsePostDetail> responsePostDetails = circuitBreaker.run(
//                () -> postServiceClient.searchTeacherQna(memberResponseDto.getMemberId()),
//                throwable -> new ArrayList<>());
//
//        return new TeacherQna(memberResponseDto, responsePostDetails);
//    }
//}
