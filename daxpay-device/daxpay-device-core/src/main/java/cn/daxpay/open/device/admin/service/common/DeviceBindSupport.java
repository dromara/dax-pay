package cn.daxpay.open.device.admin.service.common;

import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/// # 虚拟设备绑定校验支撑
///
/// 集中设备绑定所需的商户、应用、门店存在性与归属校验
/// (开源版无代理商域, plus 的 agent 校验不移植)。
@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceBindSupport {

    private final MerchantInfoManager merchantInfoManager;
    private final MerchantContextLoader merchantContextLoader;
    private final MchStoreInfoManager mchStoreInfoManager;

    /// 校验商户存在, 返回商户实体(运营端无商户上下文, 走忽略租户口径)
    public MerchantInfo validateMerchant(String mchNo) {
        return merchantInfoManager.findByMchNoNotTenant(mchNo)
                // 商户: 商户不存在
                .orElseThrow(() -> new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.merchantNotExist"));
    }

    /// 绑商户可选应用: 空写 null(全部应用); 非空校验归属与启用后返回规范化 appId
    public String resolveOptionalAppId(String mchNo, String appId) {
        if (StrUtil.isBlank(appId)) {
            return null;
        }
        return merchantContextLoader.resolveApp(mchNo, appId).getAppId();
    }

    /// 绑商户可选门店: 空写 null; 非空校验门店存在且归属该商户
    public String resolveOptionalStoreNo(String mchNo, String storeNo) {
        if (StrUtil.isBlank(storeNo)) {
            return null;
        }
        MchStoreInfo store = mchStoreInfoManager.findByStoreNo(storeNo)
                // 商户: 门店不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        if (!Objects.equals(store.getMchNo(), mchNo)) {
            // 商户: 门店不属于当前商户
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.payment.merchant.storeNoMatch");
        }
        return storeNo;
    }
}
