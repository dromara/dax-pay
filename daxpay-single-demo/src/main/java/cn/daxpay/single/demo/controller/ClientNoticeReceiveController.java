package cn.daxpay.single.demo.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.daxpay.single.demo.configuration.DaxPayDemoProperties;
import cn.daxpay.single.sdk.model.notice.AllocNoticeModel;
import cn.daxpay.single.sdk.model.notice.PayNoticeModel;
import cn.daxpay.single.sdk.model.notice.RefundNoticeModel;
import cn.daxpay.single.sdk.util.PaySignUtil;
import cn.hutool.core.bean.BeanUtil;
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
    private final DaxPayDemoProperties daxPayDemoProperties;

    @Operation(summary = "支付消息(map接收)")
    @PostMapping("/pay")
    public String pay(@RequestBody Map<String,Object> map){
        log.info("接收到支付回调消息: {}",map);
        // 转换为对象
        PayNoticeModel bean = BeanUtil.toBean(map, PayNoticeModel.class);
        log.info("验签结果: {}", PaySignUtil.hmacSha256Sign(bean, daxPayDemoProperties.getSignSecret()));
        return "SUCCESS";
    }


    @Operation(summary = "支付消息(对象接收)")
    @PostMapping("/payObject")
    public String pay(@RequestBody PayNoticeModel model){
        log.info("接收到支付回调消息: {}",model);
        log.info("验签结果: {}", PaySignUtil.verifyHmacSha256Sign(model, daxPayDemoProperties.getSignSecret(),model.getSign()));
        return "SUCCESS";
    }

    @Operation(summary = "退款消息")
    @PostMapping("/refund")
    public String refund(@RequestBody Map<String,Object> map) {
        log.info("接收到退款回调消息: {}",map);
        // 转换为对象
        RefundNoticeModel model = BeanUtil.toBean(map, RefundNoticeModel.class);
        log.info("验签结果: {}", PaySignUtil.hmacSha256Sign(model, daxPayDemoProperties.getSignSecret()));
        return "SUCCESS";
    }

    @Operation(summary = "退款消息(对象)")
    @PostMapping("/refundObject")
    public String refund(@RequestBody RefundNoticeModel model) {
        log.info("接收到退款回调消息: {}",model);
        log.info("验签结果: {}", PaySignUtil.verifyHmacSha256Sign(model, daxPayDemoProperties.getSignSecret(),model.getSign()));
        return "SUCCESS";
    }

    @Operation(summary = "分账消息")
    @PostMapping("/allocation")
    public String allocation(@RequestBody Map<String,Object> map) {
        log.info("接收到退款分账消息: {}",map);
        // 转换为对象
        AllocNoticeModel model = BeanUtil.toBean(map, AllocNoticeModel.class);
        log.info("验签结果: {}", PaySignUtil.hmacSha256Sign(model, daxPayDemoProperties.getSignSecret()));
        return "SUCCESS";
    }

    @Operation(summary = "分账消息(对象)")
    @PostMapping("/allocationObject")
    public String allocation(@RequestBody AllocNoticeModel model) {
        log.info("接收到分账回调消息: {}",model);
        log.info("验签结果: {}", PaySignUtil.verifyHmacSha256Sign(model, daxPayDemoProperties.getSignSecret(),model.getSign()));
        return "SUCCESS";
    }

}
