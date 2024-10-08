package org.dromara.daxpay.service.service.merchant;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.exception.ConfigNotExistException;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.service.convert.merchant.MchAppConvert;
import org.dromara.daxpay.service.dao.config.ChannelConfigManager;
import org.dromara.daxpay.service.dao.merchant.MchAppManager;
import org.dromara.daxpay.service.entity.config.ChannelConfig;
import org.dromara.daxpay.service.entity.merchant.MchApp;
import org.dromara.daxpay.service.enums.MchAppStautsEnum;
import org.dromara.daxpay.service.param.merchant.MchAppParam;
import org.dromara.daxpay.service.param.merchant.MchAppQuery;
import org.dromara.daxpay.service.result.merchant.MchAppResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商户应用服务类
 * @author xxm
 * @since 2024/5/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MchAppService {

    private final MchAppManager mchAppManager;

    private final ChannelConfigManager channelConfigManager;

    /**
     * 添加应用
     */
    public void add(MchAppParam param) {
        MchApp entity = MchAppConvert.CONVERT.toEntity(param);
        // 生成应用号
        entity.setAppId(this.generateAppId())
                .setStatus(MchAppStautsEnum.ENABLE.getCode());
        mchAppManager.save(entity);
    }
    /**
     * 修改
     */
    public void update(MchAppParam param) {
        MchApp mchApp = mchAppManager.findById(param.getId()).orElseThrow(ConfigNotExistException::new);
        BeanUtil.copyProperties(param, mchApp, CopyOptions.create().ignoreNullValue());
        mchAppManager.updateById(mchApp);
    }

    /**
     * 分页
     */
    public PageResult<MchAppResult> page(PageParam pageParam, MchAppQuery query) {
        return MpUtil.toPageResult(mchAppManager.page(pageParam, query));
    }

    /**
     * 获取单条
     */
    public MchAppResult findById(Long id) {
        return mchAppManager.findById(id).map(MchApp::toResult).orElseThrow(ConfigNotExistException::new);
    }

    /**
     * 下拉列表
     */
    public List<LabelValue> dropdown(){
        return mchAppManager.findAll().stream()
                .map(o->new LabelValue(o.getAppName(),o.getAppId()))
                .collect(Collectors.toList());
    }

    /**
     * 删除
     */
    public void delete(Long id) {
        MchApp mchApp = mchAppManager.findById(id)
                .orElseThrow(ConfigNotExistException::new);
        // 查看是否有配置的支付配置
        if (channelConfigManager.existedByField(ChannelConfig::getAppId, mchApp.getAppId())){
            throw new OperationFailException("该商户应用已绑定支付配置，请先删除支付配置");
        }

        mchAppManager.deleteById(id);
    }

    /**
     * 生成应用号
     */
    private String generateAppId() {
        String mchNo = RandomUtil.randomNumbers(16);
        for (int i = 0; i < 10; i++){
            if (!mchAppManager.existedByField(MchApp::getAppId, mchNo)){
                return "A"+mchNo;
            }
            mchNo = String.valueOf(System.currentTimeMillis());
        }
        throw new BizException("应用号生成失败");
    }
}
