package cn.daxpay.multi.merchant.service.merchant;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.daxpay.multi.core.exception.ConfigNotExistException;
import cn.daxpay.multi.core.exception.OperationFailException;
import cn.daxpay.multi.service.common.local.MchContextLocal;
import cn.daxpay.multi.service.convert.merchant.MchAppConvert;
import cn.daxpay.multi.service.dao.config.ChannelConfigManager;
import cn.daxpay.multi.service.dao.merchant.MchAppManager;
import cn.daxpay.multi.service.entity.config.ChannelConfig;
import cn.daxpay.multi.service.entity.merchant.MchApp;
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
 *
 * @author xxm
 * @since 2024/8/28
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
        // 覆盖
        param.setMchNo(MchContextLocal.getMchNo());
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
     * 分页
     */
    public PageResult<MchAppResult> page(PageParam pageParam, MchAppQuery query) {
        query.setMchNo(MchContextLocal.getMchNo());
        return MpUtil.toPageResult(mchAppManager.page(pageParam, query));
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
     * 下拉列表
     */
    public List<LabelValue> dropdown(){
        return mchAppManager.findAllByField(MchApp::getMchNo, MchContextLocal.getMchNo()).stream()
                .map(o->new LabelValue(o.getAppName(),o.getAppId()))
                .collect(Collectors.toList());
    }

    /**
     * 删除
     */
    public void delete(Long id) {
        MchApp mchApp = mchAppManager.findById(id)
                .orElseThrow(()->new ConfigNotExistException("商户应用未找到"));
        this.checkApp(mchApp);
        // 查看是否有配置的支付配置
        if (channelConfigManager.existedByField(ChannelConfig::getAppId, mchApp.getAppId())){
            throw new OperationFailException("该商户应用已绑定支付配置，请先删除支付配置");
        }
        if (!mchApp.getMchNo().equals(MchContextLocal.getMchNo())){
            throw new ConfigNotExistException();
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
                return "M"+mchNo;
            }
            mchNo = String.valueOf(System.currentTimeMillis());
        }
        throw new BizException("应用号生成失败");
    }

    /**
     *
     */
    public void checkApp(String appId){
        mchAppManager.findByAppId(appId).orElseThrow(()->new ConfigNotExistException("商户应用未找到"));
    }

    /**
     * 如果和商户不匹配, 抛出错误
     */
    public void checkApp(MchApp mchApp){
        if (!mchApp.getMchNo().equals(MchContextLocal.getMchNo())){
            throw new ConfigNotExistException("商户应用未找到");
        }
    }
}
