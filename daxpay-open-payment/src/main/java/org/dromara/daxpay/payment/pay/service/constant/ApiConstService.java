package org.dromara.daxpay.payment.pay.service.constant;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.payment.pay.dao.constant.ApiConstManager;
import org.dromara.daxpay.payment.pay.result.constant.ApiConstResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 支付接口信息
 * @author xxm
 * @since 2024/7/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiConstService {
    private final ApiConstManager apiConstManager;

    /**
     * 下拉列表
     */
    public List<ApiConstResult> list() {
        return MpUtil.toListResult(apiConstManager.findAllByEnable());
    }
}
