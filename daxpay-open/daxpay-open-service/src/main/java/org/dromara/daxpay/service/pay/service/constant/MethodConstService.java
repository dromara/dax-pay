package org.dromara.daxpay.service.pay.service.constant;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.service.pay.dao.constant.MethodConstManager;
import org.dromara.daxpay.service.pay.param.constant.MethodConstQuery;
import org.dromara.daxpay.service.pay.result.constant.PayMethodConstResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付方式常量
 * @author xxm
 * @since 2024/7/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MethodConstService {
    private final MethodConstManager methodConstManager;

    /**
     * 分页
     */
    public PageResult<PayMethodConstResult> page(PageParam pageParam, MethodConstQuery query) {
        return MpUtil.toPageResult(methodConstManager.page(pageParam, query));
    }
}
