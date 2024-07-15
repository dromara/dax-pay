package cn.daxpay.multi.service.service.constant;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.daxpay.multi.service.dao.constant.MethodConstManager;
import cn.daxpay.multi.service.param.constant.MethodConstQuery;
import cn.daxpay.multi.service.result.constant.MethodConstResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
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
    public PageResult<MethodConstResult> page(PageParam pageParam, MethodConstQuery query) {
        return MpUtil.toPageResult(methodConstManager.page(pageParam, query));
    }
}
