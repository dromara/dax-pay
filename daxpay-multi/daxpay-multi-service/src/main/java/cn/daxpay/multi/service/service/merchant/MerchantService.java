package cn.daxpay.multi.service.service.merchant;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.daxpay.multi.service.convert.merchant.MerchantConvert;
import cn.daxpay.multi.service.dao.merchant.MchAppManager;
import cn.daxpay.multi.service.dao.merchant.MerchantManager;
import cn.daxpay.multi.service.entity.merchant.Merchant;
import cn.daxpay.multi.service.enums.MerchantStatusEnum;
import cn.daxpay.multi.service.param.merchant.MerchantParam;
import cn.daxpay.multi.service.param.merchant.MerchantQuery;
import cn.daxpay.multi.service.result.merchant.MerchantResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商户服务类
 * @author xxm
 * @since 2024/5/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantService {
    private final MerchantManager merchantManager;

    private final MchAppManager mchAppManager;

    /**
     * 添加商户
     */
    public void add(MerchantParam param) {
        String mchNo = this.getMchNo();
        Merchant entity = MerchantConvert.CONVERT.toEntity(param);
        entity.setMchNo(mchNo)
                .setStatus(MerchantStatusEnum.ENABLE.getCode());
        merchantManager.save(entity);
    }

    /**
     * 修改
     */
    public void update(MerchantParam param) {
        Merchant Merchant = merchantManager.findById(param.getId()).orElseThrow(DataNotExistException::new);
        BeanUtil.copyProperties(param, Merchant, CopyOptions.create().ignoreNullValue());
        merchantManager.updateById(Merchant);
    }

    /**
     * 分页
     */
    public PageResult<MerchantResult> page(PageParam pageParam, MerchantQuery query) {
        return MpUtil.toPageResult(merchantManager.page(pageParam,query));
    }

    /**
     * 获取单条
     */
    public MerchantResult findById(Long id) {
        return merchantManager.findById(id).map(Merchant::toResult).orElseThrow(DataNotExistException::new);
    }

    /**
     * 下拉框
     */
    public List<LabelValue> dropdown() {
        return merchantManager.findAll().stream()
                .map(mch -> new LabelValue(mch.getMchName(),mch.getMchNo()))
                .collect(Collectors.toList());
    }

    /**
     * 删除
     */

    public void delete(Long id) {
        Merchant merchant = merchantManager.findById(id)
                .orElseThrow(DataNotExistException::new);
        // 判断是否存在应用APP, 存在不允许删除
        if (mchAppManager.existByMchNo(merchant.getMchNo())){
            throw new BizException("商户下存在应用, 不允许删除");
        }
        merchantManager.deleteById(id);
    }


    /**
     * 生成商户号
     */
    private String getMchNo(){
        String mchNo = "M" + System.currentTimeMillis();
        for (int i = 0; i < 10; i++){
            if (!merchantManager.existedByField(Merchant::getMchNo, mchNo)){
                return mchNo;
            }
            mchNo = "M" + System.currentTimeMillis();
        }
        throw new BizException("商户号生成失败");
    }
}
