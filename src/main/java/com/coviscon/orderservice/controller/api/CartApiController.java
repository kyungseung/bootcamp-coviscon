package com.coviscon.orderservice.controller.api;

import com.coviscon.orderservice.dto.MemberResponseDto;
import com.coviscon.orderservice.dto.ResponseCartDetail;
import com.coviscon.orderservice.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CartApiController {

    private final CartService cartService;
    private final Environment env;
    private final HttpSession session;

    @CrossOrigin
    /** 장바구니에 상품 추가하기 **/
    @PostMapping("/add/cart")
    public ResponseEntity<ResponseCartDetail> addCart(@RequestParam Long itemId) {
        log.info("[CartApiController addCart] itemId : {}", itemId);

        MemberResponseDto member = setMemberResponseDto();

        ResponseCartDetail cartDetail = cartService.addCart(member, itemId);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartDetail);
    }

    /**
     * 장바구니의 항목을 cartId를 기준으로 삭제하기
     **/
    @DeleteMapping("/{cartId}/delete")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long cartId) {
        log.info("[CartApiController deleteCartItem] cartId : {}", cartId);
        cartService.deleteCartItem(cartId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * session -> set MemberResponseDto
     */
    private MemberResponseDto setMemberResponseDto() {
        String memberId = (String) session.getAttribute("memberId");
        String email = (String) session.getAttribute("email");
        String nickName = (String) session.getAttribute("nickName");
        String role = (String) session.getAttribute("role");

        if (memberId == null)
            return null;

        return MemberResponseDto.builder()
                .memberId(Long.valueOf(memberId))
                .email(email)
                .nickName(nickName)
                .role(role)
                .build();
    }
}
