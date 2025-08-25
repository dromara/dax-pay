package org.dromara.daxpay.service.merchant.service.info;

import cn.bootx.platform.core.exception.BizInfoException;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.iam.service.client.ClientCodeService;
import org.dromara.daxpay.service.merchant.dao.info.MerchantManager;
import org.dromara.daxpay.service.merchant.entity.info.Merchant;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.merchant.local.MchContextLocal;
import org.dromara.daxpay.service.merchant.param.info.MerchantParam;
import org.dromara.daxpay.service.merchant.result.info.MerchantResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商户信息管理
 * @author xxm
 * @since 2024/8/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantInfoService {
    private final MerchantManager merchantManager;
    private final ClientCodeService clientCodeService;

    /**
     * 获取商户信息
     */
    public MerchantResult getMerchant() {
        String mchNo = MchContextLocal.getMchNo();
        if (mchNo == null){
            throw new BizInfoException("数据错误, 未发现商户号");
        }
        return merchantManager.findByMchNo(mchNo).map(Merchant::toResult).orElseThrow(() -> new DataNotExistException("商户信息不存在"));
    }

    /**
     * 修改
     */
    public void update(MerchantParam param) {
        String mchNo = MchContextLocal.getMchNo();
        if (mchNo == null){
            throw new BizInfoException("数据错误, 未发现商户号");
        }
        Merchant merchant = merchantManager.findByMchNo(mchNo)
                .orElseThrow(() -> new DataNotExistException("商户信息不存在"));
        BeanUtil.copyProperties(param, merchant, CopyOptions.create().ignoreNullValue());
        merchantManager.updateById(merchant);
    }

    /**
     * 商户下拉框
     */
    public List<LabelValue> dropdown() {
        List<Merchant> list;
        if (clientCodeService.getClientCode().equals(DaxPayCode.Client.ADMIN)){
            list = merchantManager.findAll();
        } else {
            list = merchantManager.findAllByField(Merchant::getMchNo, MchContextLocal.getMchNo());
        }
        return list.stream()
                .map(mch -> new LabelValue(mch.getMchName(),mch.getMchNo()))
                .collect(Collectors.toList());
    }

    /**
     * 商户下拉框
     */
    public List<LabelValue> dropdownByEnable() {
        List<Merchant> list;
        if (clientCodeService.getClientCode().equals(DaxPayCode.Client.ADMIN)){
            list = merchantManager.findAllByEnable();
        } else {
            list = merchantManager.findAllByField(Merchant::getMchNo, MchContextLocal.getMchNo());
        }
        return list.stream()
                .map(mch -> new LabelValue(mch.getMchName(),mch.getMchNo()))
                .collect(Collectors.toList());
    }

}
