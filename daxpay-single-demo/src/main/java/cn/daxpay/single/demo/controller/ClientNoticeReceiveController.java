package cn.daxpay.single.demo.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.daxpay.single.sdk.model.notice.PayNoticeModel;
import cn.daxpay.single.sdk.model.notice.RefundNoticeModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 *
 * @author xxm
 * @since 2024/2/24
 */
@Slf4j
@IgnoreAuth
@Tag(name = "回调测试")
@RestController
@RequestMapping("/demo/callback")
@RequiredArgsConstructor
public class ClientNoticeReceiveController {

    @Operation(summary = "支付消息(map接收)")
    @PostMapping("/pay")
    public String pay(@RequestBody Map<String,Object> map){
        log.info("接收到支付回调消息: {}",map);
        return "SUCCESS";
    }


    @Operation(summary = "支付消息(对象接收)")
    @PostMapping("/payObject")
    public String pay(@RequestBody PayNoticeModel model){
        log.info("接收到支付回调消息: {}",model);
        return "SUCCESS";
    }

    @Operation(summary = "退款消息")
    @PostMapping("/refund")
    public String refund(@RequestBody Map<String,Object> map) {
        log.info("接收到退款回调消息: {}",map);
        return "SUCCESS";
    }

    @Operation(summary = "退款消息(对象)")
    @PostMapping("/refundObject")
    public String refund(@RequestBody RefundNoticeModel map) {
        log.info("接收到退款回调消息: {}",map);
        return "SUCCESS";
    }

}
