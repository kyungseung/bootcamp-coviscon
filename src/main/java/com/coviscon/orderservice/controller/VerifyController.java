package com.coviscon.orderservice.controller;


import com.coviscon.orderservice.service.OrderService;
import com.coviscon.orderservice.service.RefundService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.BillingCustomerData;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.BillingCustomer;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;

@Slf4j
@RestController
public class VerifyController {

    private final Environment env;

    /** Iamport 결제 검증 컨트롤러 **/
    private final IamportClient iamportClient;

    private final RefundService refundService;
    private final OrderService orderService;
    /**
     * 포트원에서 제공하는 REST API 와 REST API secret 를
     * Application.yml 을 통해 가져오기
     */
    public VerifyController(Environment env, OrderService orderService, RefundService refundService) {
        this.env = env;
        this.iamportClient = new IamportClient(
                env.getProperty("iamport.apiKey"),
                env.getProperty("iamport.secretKey"));
        this.orderService = orderService;
        this.refundService = refundService;
    }

//    @PostMapping("/save/order")
//    public ResponseEntity<RequestPaymentDto> savedOrder(@RequestBody RequestPaymentDto paymentDto) throws Exception {
//        log.info("[VerifyController savedOrder] PaymentDto : {}", paymentDto);
////        orderService.savedOrder(paymentDto);
//
//        return ResponseEntity.status(HttpStatus.OK).body(paymentDto);
//    }

    /**
     * 결제 진행
     * imp_uid : 포트원 결제 번호
     */
    @PostMapping(value = "/payments/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(
            @PathVariable("imp_uid") String imp_uid
    ) throws Exception{

        log.info("[VerifyController paymentByImpUid] imp_uid : {}", imp_uid);

        return iamportClient.paymentByImpUid(imp_uid);
    }

    /**
     *  결제 취소
     */
    @PostMapping("/payments/cancel")
    public  IamportResponse<Payment> cancelPaymentByImpUid(CancelData cancelData, String merchantUid, String reason) throws IamportResponseException, IOException {

        refundService.refundRequest(refundService.getToken(env.getProperty("iamport.apiKey"), env.getProperty("iamport.secretKey")), merchantUid, reason);

        return iamportClient.cancelPaymentByImpUid(cancelData);
    }

    /**
     *  결제 내역 조회
     */
    @PostMapping("/subscribe/member/{customerUid}")
    public IamportResponse<BillingCustomer> postBillingCustomer (@PathVariable("customerUid") String merchantUid,
                                                                 BillingCustomerData billingData) throws IamportResponseException, IOException {

        log.info("VerifyController.merchantUid : {}", merchantUid);
        log.info("iamportClient.postBillingCustomer(customerUid, billingData) : {}",
                iamportClient.postBillingCustomer(merchantUid, billingData));


        return iamportClient.postBillingCustomer(merchantUid, billingData);
    }
}


