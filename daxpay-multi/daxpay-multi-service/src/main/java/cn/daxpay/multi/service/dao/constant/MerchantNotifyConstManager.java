package cn.daxpay.multi.service.dao.constant;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.multi.service.entity.constant.MerchantNotifyConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 商户通知类型
 * @author xxm
 * @since 2024/7/30
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MerchantNotifyConstManager extends BaseManager<MerchantNotifyConstMapper,MerchantNotifyConst> {
}
