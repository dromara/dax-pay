package cn.daxpay.multi.admin.service.merchant;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.daxpay.multi.core.exception.ConfigNotExistException;
import cn.daxpay.multi.core.exception.OperationFailException;
import cn.daxpay.multi.service.convert.merchant.MchAppConvert;
import cn.daxpay.multi.service.dao.config.ChannelConfigManager;
import cn.daxpay.multi.service.dao.merchant.MchAppManager;
import cn.daxpay.multi.service.dao.merchant.MerchantManager;
import cn.daxpay.multi.service.entity.config.ChannelConfig;
import cn.daxpay.multi.service.entity.merchant.MchApp;
import cn.daxpay.multi.service.entity.merchant.Merchant;
import cn.daxpay.multi.service.enums.MchAppStautsEnum;
import cn.daxpay.multi.service.param.merchant.MchAppParam;
import cn.daxpay.multi.service.param.merchant.MchAppQuery;
import cn.daxpay.multi.service.result.merchant.MchAppResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final MerchantManager merchantManager;

    private final MchAppManager mchAppManager;

    private final ChannelConfigManager channelConfigManager;

    /**
     * 添加应用
     */
    public void add(MchAppParam param) {
        // 校验商户是否存在
        if (!merchantManager.existedByField(Merchant::getMchNo, param.getMchNo())) {
            throw new ValidationFailedException("该商户号不存在");
        }
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
    public List<LabelValue> dropdown(String mchNo){
        return mchAppManager.findAllByField(MchApp::getMchNo,mchNo).stream()
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
