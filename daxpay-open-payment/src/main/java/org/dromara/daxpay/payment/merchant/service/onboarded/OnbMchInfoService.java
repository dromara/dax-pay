package org.dromara.daxpay.payment.merchant.service.onboarded;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.payment.merchant.convert.onboarded.OnbMchInfoConvert;
import org.dromara.daxpay.payment.merchant.dao.info.MerchantManager;
import org.dromara.daxpay.payment.merchant.dao.onboarded.OnbMchInfoManager;
import org.dromara.daxpay.payment.merchant.entity.onboarded.OnbMchInfo;
import org.dromara.daxpay.payment.merchant.param.onboarded.OnbMchInfoParam;
import org.dromara.daxpay.payment.merchant.param.onboarded.OnbMchInfoQuery;
import org.dromara.daxpay.payment.merchant.result.onboarded.OnbMchInfoResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 进件商户信息管理
 *
 * @author xxm
 * @since 2025/11/11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OnbMchInfoService {
    private final MerchantManager merchantManager;
    private final OnbMchInfoManager onbMchInfoManager;

    /**
     * 添加进件商户信息
     */
    public void add(OnbMchInfoParam param) {
        if (!merchantManager.existedByMchNo(param.getMchNo())) {
            throw new BizException("商户信息不存在");
        }
        // 判断在商户+通道中是否已存在
        if (onbMchInfoManager.exists(param.getOnbMchNo(), param.getMchNo(), param.getOnbChannel())) {
            throw new BizException("该进件商户在该商户名下已存在");
        }
        // 判断进件商户号是否已存在
        if (onbMchInfoManager.existsByOnbMchNo(param.getOnbMchNo())) {
            throw new BizException("进件商户号已存在");
        }
        OnbMchInfo entity = OnbMchInfoConvert.CONVERT.toEntity(param);
        onbMchInfoManager.save(entity);
    }

    /**
     * 修改进件商户信息
     */
    public void update(OnbMchInfoParam param) {
        var onbMchInfo = onbMchInfoManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("进件商户信息未找到"));
        // 检查进件商户号是否被修改
        if (!onbMchInfo.getOnbMchNo().equals(param.getOnbMchNo())) {
            throw new BizException("进件商户号不允许修改");
        }

        OnbMchInfoConvert.CONVERT.copy(param, onbMchInfo);
        onbMchInfoManager.updateById(onbMchInfo);
    }

    /**
     * 根据进件商户号、商户号和所属通道判断是否存在
     */
    public boolean existsByOnbMchNo(String onbMchNo, String mchNo, String onbChannel) {
        return onbMchInfoManager.exists(onbMchNo, mchNo, onbChannel);
    }

    /**
     * 根据进件商户号查询
     */
    public OnbMchInfoResult findByOnbMchNo(String onbMchNo) {
        var onbMchInfo = onbMchInfoManager.findByOnbMchNo(onbMchNo);
        if (onbMchInfo == null) {
            return null;
        }
        return OnbMchInfoConvert.CONVERT.toResult(onbMchInfo);
    }

    /**
     * 分页
     */
    public PageResult<OnbMchInfoResult> page(PageParam pageParam, OnbMchInfoQuery query) {
        return MpUtil.toPageResult(onbMchInfoManager.page(pageParam, query));
    }

    /**
     * 获取单条
     */
    public OnbMchInfoResult findById(Long id) {
        var onbMchInfo = onbMchInfoManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("进件商户信息未找到"));
        return onbMchInfo.toResult();
    }


    /**
     * 删除
     */
    public void delete(Long id) {
        onbMchInfoManager.deleteById(id);
    }

    /**
     * 根据所属通道查询所有进件商户列表
     */
    public List<LabelValue> findByChannel(String mchNo, String channel) {
        return onbMchInfoManager.findAllByChannel(mchNo, channel)
                .stream()
                .map(onbMchInfo -> new LabelValue(onbMchInfo.getOnbMchName(), onbMchInfo.getOnbMchNo()))
                .toList();
    }
}
