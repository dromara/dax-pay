package org.dromara.daxpay.payment.merchant.service.app;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.BizInfoException;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.iam.service.client.ClientCodeService;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import org.dromara.daxpay.payment.common.exception.ConfigErrorException;
import org.dromara.daxpay.payment.common.exception.ConfigNotEnableException;
import org.dromara.daxpay.payment.common.exception.ConfigNotExistException;
import org.dromara.daxpay.payment.common.exception.OperationFailException;
import org.dromara.daxpay.payment.merchant.convert.app.MchAppConvert;
import org.dromara.daxpay.payment.merchant.dao.app.MchAppManager;
import org.dromara.daxpay.payment.merchant.dao.config.ChannelConfigManager;
import org.dromara.daxpay.payment.merchant.dao.info.MerchantManager;
import org.dromara.daxpay.payment.merchant.entity.app.MchApp;
import org.dromara.daxpay.payment.merchant.entity.info.Merchant;
import org.dromara.daxpay.payment.merchant.enums.MerchantStatusEnum;
import org.dromara.daxpay.payment.merchant.local.MchContextLocal;
import org.dromara.daxpay.payment.merchant.param.app.MchAppParam;
import org.dromara.daxpay.payment.merchant.param.app.MchAppQuery;
import org.dromara.daxpay.payment.merchant.result.app.MchAppResult;
import org.dromara.daxpay.payment.pay.entity.config.ChannelConfig;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商户应用管理
 * @author xxm
 * @since 2024/8/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MchAppService {

    private final MerchantManager merchantManager;

    private final MchAppManager mchAppManager;

    private final ClientCodeService clientCodeService;

    private final ChannelConfigManager channelConfigManager;

    /**
     * 添加应用
     */
    public void add(MchAppParam param) {
        String mchNo;
        if (clientCodeService.getClientCode().equals(DaxPayCode.Client.MERCHANT)) {
            mchNo = MchContextLocal.getMchNo();
        } else {
            mchNo = param.getMchNo();
        }
        if (mchNo == null) {
            throw new BizInfoException("数据错误, 未发现商户号");
        }
        if (!merchantManager.existedByField(Merchant::getMchNo, mchNo)){
            throw new BizException("商户信息不存在");
        }
        param.setMchNo(mchNo);
        MchApp entity = MchAppConvert.CONVERT.toEntity(param);
        // 生成应用号
        entity.setAppId(this.generateAppId());
        mchAppManager.save(entity);
    }
    /**
     * 修改
     */
    public void update(MchAppParam param) {
        MchApp mchApp = mchAppManager.findById(param.getId()).orElseThrow(()->new ConfigNotExistException("商户应用未找到"));
        this.checkApp(mchApp);
        BeanUtil.copyProperties(param, mchApp, CopyOptions.create().ignoreNullValue());
        mchAppManager.updateById(mchApp);
    }

    /**
     * 设为默认
     */
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(Long id){
        // 先清除默认应用
        MchApp mchApp = mchAppManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("商户应用未找到"));
        mchAppManager.clearDefault(mchApp.getMchNo());
        // 如果已经是默认应用, 不需要处理
        if (mchApp.isDefaultApp()){
            return;
        }
        // 已经更新, 需要重新查询
        mchApp = mchAppManager.findById(id).orElseThrow(() -> new DataNotExistException("商户应用未找到"));
        mchApp.setDefaultApp(true);
        mchAppManager.updateById(mchApp);
    }

    /**
     * 清除默认配置
     */
    @Transactional(rollbackFor = Exception.class)
    public void clearDefault(Long id) {
        MchApp mchApp = mchAppManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("商户应用未找到"));
        mchApp.setDefaultApp(false);
        mchAppManager.updateById(mchApp);
    }

    /**
     * 分页
     */
    public PageResult<MchAppResult> page(PageParam pageParam, MchAppQuery query) {
        return MpUtil.toPageResult(mchAppManager.page(pageParam, query));
    }

    /**
     * 商户应用列表
     */
    public List<MchAppResult> list() {
        String mchNo = MchContextLocal.getMchNo();
        // 查询启用的应用
        return mchAppManager.findAllByMchNo(mchNo).stream()
                .map(MchApp::toResult)
                .collect(Collectors.toList());
    }

    /**
     * 根据应用AppId获取应用详情
     */
    public MchAppResult findByAppId(String appId) {
        MchApp mchApp = mchAppManager.findByAppId(appId)
                .orElseThrow(() -> new DataNotExistException("商户应用未找到"));
        this.checkApp(mchApp);
        return mchApp.toResult();
    }

    /**
     * 获取单条
     */
    public MchAppResult findById(Long id) {
        var mchApp = mchAppManager.findById(id).orElseThrow(()->new ConfigNotExistException("商户应用未找到"));
        this.checkApp(mchApp);
        return mchApp.toResult();
    }

    /**
     * 启用下拉列表, 需要应用和商户都是启用状态
     */
    public List<LabelValue> dropdownByEnable(String mchNo){
        if (clientCodeService.getClientCode().equals(DaxPayCode.Client.MERCHANT)){
            mchNo = MchContextLocal.getMchNo();
        }
        // 判断商户状态
        Merchant merchant = merchantManager.findByMchNo(mchNo)
                .orElseThrow(() -> new DataNotExistException("商户信息未找到"));
        if (!Objects.equals(merchant.getStatus(), MerchantStatusEnum.ENABLE.getCode())){
            throw new ConfigNotEnableException("商户未启用");
        }
        // 查询启用的应用
        return mchAppManager.findAllByMchNoAndEnable(mchNo).stream()
                .map(o->new LabelValue(o.getAppName(),o.getAppId()))
                .collect(Collectors.toList());
    }

    /**
     * 下拉列表, 不判断应用和商户的状态
     */
    public List<LabelValue> dropdown(String mchNo){
        if (clientCodeService.getClientCode().equals(DaxPayCode.Client.MERCHANT)){
            mchNo = MchContextLocal.getMchNo();
        }
        // 查询启用的应用
        return mchAppManager.findAllByMchNo(mchNo).stream()
                .map(o->new LabelValue(o.getAppName(),o.getAppId()))
                .collect(Collectors.toList());
    }

    /**
     * 删除
     */
    public void delete(Long id) {
        MchApp mchApp = mchAppManager.findById(id).orElseThrow(()->new ConfigNotExistException("商户应用未找到"));
        this.checkApp(mchApp);
        // 查看是否有配置的支付配置
        if (channelConfigManager.existedByField(ChannelConfig::getAppId, mchApp.getAppId())){
            throw new OperationFailException("该商户应用已有通道配置，不允许被删除");
        }
        mchAppManager.deleteById(id);
    }

    /**
     * 生成应用号
     */
    public String generateAppId() {
        String appId = "A"+ RandomUtil.randomNumbers(16);
        for (int i = 0; i < 10; i++){
            if (!mchAppManager.existsByAppId(appId)){
                return appId;
            }
            appId = "A"+ RandomUtil.randomNumbers(16);
        }
        throw new BizException("应用号生成失败");
    }

    /**
     * 如果和商户不匹配, 抛出错误
     */
    public void checkApp(MchApp mchApp){
        if (clientCodeService.getClientCode().equals(DaxPayCode.Client.MERCHANT)) {
            if (!mchApp.getMchNo().equals(MchContextLocal.getMchNo())) {
                throw new ConfigErrorException("商户和应用号不匹配");
            }
        }
    }
}
