package cn.daxpay.open.payment.merchant.service.info;

import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.common.service.MerchantAssistQueryService;
import cn.daxpay.open.payment.merchant.convert.info.MerchantInfoConvert;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.payment.merchant.param.info.MerchantInfoParam;
import cn.daxpay.open.payment.merchant.result.info.MerchantInfoResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

/// # 商户信息管理
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantInfoService implements MerchantAssistQueryService {

    private final MerchantInfoManager merchantInfoManager;
    private final PaymentContext apiContext;

    /// 获取商户信息
    public MerchantInfoResult getMerchant() {
        String mchNo = apiContext.getTradeInfo().getMchNo();
        if (mchNo == null){
            // 商户: 数据错误, 未发现商户号
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.dataErrorNoMchNo");
        }
        // 商户: 商户不存在
        return merchantInfoManager.findByMchNo(mchNo).map(MerchantInfo::toResult).orElseThrow(() -> new DataNotExistException("error.payment.merchant.mchNotExist"));
    }

    /// 修改
    public void update(MerchantInfoParam param) {
        String mchNo = apiContext.getTradeInfo().getMchNo();
        if (mchNo == null){
            // 商户: 数据错误, 未发现商户号
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.dataErrorNoMchNo");
        }
        MerchantInfo merchant = merchantInfoManager.findByMchNo(mchNo)
                // 商户: 商户不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.mchNotExist"));
        MerchantInfoConvert.CONVERT.copy(param, merchant);
        merchantInfoManager.updateById(merchant);
    }

    /// 商户下拉框
    public List<LabelValue> dropdown() {
        List<MerchantInfo> list = merchantInfoManager.findAll();
        return list.stream()
                .map(mch -> new LabelValue(mch.getMchName(),mch.getMchNo()))
                .collect(Collectors.toList());
    }

    /// 商户下拉框
    public List<LabelValue> dropdownByEnable() {
        List<MerchantInfo> list = merchantInfoManager.findAllByEnable();
        return list.stream()
                .map(mch -> new LabelValue(mch.getMchName(),mch.getMchNo()))
                .collect(Collectors.toList());
    }

}
