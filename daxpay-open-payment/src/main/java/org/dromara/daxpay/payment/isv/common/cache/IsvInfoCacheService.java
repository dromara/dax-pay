package org.dromara.daxpay.payment.isv.common.cache;

import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.payment.isv.dao.isv.IsvInfoManager;
import org.dromara.daxpay.payment.isv.entity.info.IsvInfo;
import org.dromara.daxpay.payment.isv.result.info.IsvInfoResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 服务商缓存
 * @author xxm
 * @since 2025/4/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IsvInfoCacheService {

    private final IsvInfoManager isvInfoManager;

    public IsvInfoResult get(String isvNo) {
        return isvInfoManager.findByIsvNo(isvNo)
                .map(IsvInfo::toResult)
                .orElseThrow(() -> new DataNotExistException("服务商信息不存在"));
    }
}
