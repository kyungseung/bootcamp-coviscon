package com.coviscon.orderservice.controller;

import com.coviscon.orderservice.dto.MemberResponseDto;
import com.coviscon.orderservice.dto.ResponseCartDetail;
import com.coviscon.orderservice.entity.OrderStatus;
import com.coviscon.orderservice.exception.CustomException;
import com.coviscon.orderservice.exception.ErrorCode;
import com.coviscon.orderservice.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CartController {

    private final CartService cartService;
    private final HttpSession session;

    /** 장바구니에 상품 추가하기 **/

    /** 해당 memberId 를 기준으로 장바구니 목록 가져오기 **/
    @GetMapping("/cart/list")
    public String cartList(Model model){
        MemberResponseDto member = setMemberResponseDto();
        log.info("[CartController cartList] member : {}", member);

        if (member == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        List<ResponseCartDetail> cartList = cartService.cartList(member.getMemberId());

        int totalPrice = 0;
        for (ResponseCartDetail cart : cartList) {
            totalPrice += cart.getPrice();
        }

        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cart", cartList);
        return "order/cart";
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
