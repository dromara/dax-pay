package cn.bootx.daxpay.controller;

import cn.bootx.common.core.annotation.IgnoreAuth;
import cn.bootx.common.core.rest.PageResult;
import cn.bootx.common.core.rest.Res;
import cn.bootx.common.core.rest.ResResult;
import cn.bootx.common.core.rest.param.OrderParam;
import cn.bootx.common.core.rest.param.PageParam;
import cn.bootx.common.query.entity.QueryParams;
import cn.bootx.daxpay.core.payment.service.PaymentQueryService;
import cn.bootx.daxpay.dto.payment.PayChannelInfo;
import cn.bootx.daxpay.dto.payment.PaymentDto;
import cn.bootx.daxpay.param.payment.PaymentQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xxm
 * @date 2021/6/28
 */
@Tag(name = "支付记录")
@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentQueryService paymentQueryService;

    @Operation(summary = "根据id获取")
    @GetMapping("/findById")
    public ResResult<PaymentDto> findById(Long id) {
        return Res.ok(paymentQueryService.findById(id));
    }

    @Operation(summary = "根据userId获取列表")
    @GetMapping("/findByUser")
    public ResResult<List<PaymentDto>> findByUser(Long userid) {
        return Res.ok(paymentQueryService.findByUser(userid));
    }

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public ResResult<PageResult<PaymentDto>> page(PageParam pageParam, PaymentQuery param, OrderParam orderParam) {
        return Res.ok(paymentQueryService.page(pageParam, param, orderParam));
    }

    @Operation(summary = "分页查询(超级查询)")
    @PostMapping("/superPage")
    public ResResult<PageResult<PaymentDto>> superPage(PageParam pageParam, @RequestBody QueryParams queryParams) {
        return Res.ok(paymentQueryService.superPage(pageParam, queryParams));
    }

    @IgnoreAuth
    @Operation(summary = "根据业务ID获取支付状态`")
    @GetMapping("/findStatusByBusinessId")
    public ResResult<Integer> findStatusByBusinessId(String businessId) {
        return Res.ok(paymentQueryService.findStatusByBusinessId(businessId));
    }

    @IgnoreAuth
    @Operation(summary = "根据businessId获取订单支付方式")
    @GetMapping("/findPayTypeInfoByBusinessId")
    public ResResult<List<PayChannelInfo>> findPayTypeInfoByBusinessId(String businessId) {
        return Res.ok(paymentQueryService.findPayTypeInfoByBusinessId(businessId));
    }

    @Operation(summary = "根据id获取订单支付方式")
    @GetMapping("/findPayTypeInfoById")
    public ResResult<List<PayChannelInfo>> findPayTypeInfoById(Long id) {
        return Res.ok(paymentQueryService.findPayTypeInfoById(id));
    }

}
