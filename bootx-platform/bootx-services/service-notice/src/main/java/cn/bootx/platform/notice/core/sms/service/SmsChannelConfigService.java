package cn.bootx.platform.notice.core.sms.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.function.CollectorsFunction;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.common.jackson.util.JacksonUtil;
import cn.bootx.platform.notice.core.sms.dao.SmsChannelConfigManager;
import cn.bootx.platform.notice.core.sms.entity.SmsChannelConfig;
import cn.bootx.platform.notice.dto.sms.SmsChannelConfigDto;
import cn.bootx.platform.notice.event.sms.SmsChannelAddEvent;
import cn.bootx.platform.notice.event.sms.SmsChannelUpdateEvent;
import cn.bootx.platform.notice.param.sms.SmsChannelConfigParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.cloopen.config.CloopenConfig;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.core.config.SupplierFactory;
import org.dromara.sms4j.ctyun.config.CtyunConfig;
import org.dromara.sms4j.emay.config.EmayConfig;
import org.dromara.sms4j.huawei.config.HuaweiConfig;
import org.dromara.sms4j.jdcloud.config.JdCloudConfig;
import org.dromara.sms4j.netease.config.NeteaseConfig;
import org.dromara.sms4j.provider.enumerate.SupplierType;
import org.dromara.sms4j.tencent.config.TencentConfig;
import org.dromara.sms4j.unisms.config.UniConfig;
import org.dromara.sms4j.yunpian.config.YunpianConfig;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsChannelConfigService {

    private final SmsChannelConfigManager configManager;

    private final ApplicationEventPublisher eventPublisher;

    /**
     * 添加
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(Map<String,Object> map){
        SmsChannelConfigParam param = BeanUtil.toBean(map, SmsChannelConfigParam.class);
        SmsChannelConfig channelConfig = SmsChannelConfig.init(param);
        // 编码不能重复
        if (configManager.existsByCode(param.getCode())) {
            throw new BizException("编码已存在");
        }
        String supplierConfig = getSupplierConfig(channelConfig, map);
        channelConfig.setConfig(supplierConfig);
        configManager.save(channelConfig);
        this.initChannelConfig(channelConfig);
        eventPublisher.publishEvent(new SmsChannelAddEvent(this,channelConfig.toDto()));
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(Map<String,Object> map){
        SmsChannelConfigParam param = BeanUtil.toBean(map, SmsChannelConfigParam.class);
        SmsChannelConfig channelConfig = configManager.findById(param.getId())
                .orElseThrow(DataNotExistException::new);
        // 编码不能重复
        if (configManager.existsByCode(param.getCode(), param.getId())) {
            throw new BizException("编码已存在");
        }
        BeanUtil.copyProperties(param, channelConfig, CopyOptions.create().ignoreNullValue());
        String supplierConfig = getSupplierConfig(channelConfig, map);
        channelConfig.setConfig(supplierConfig);
        configManager.updateById(channelConfig);
        this.initChannelConfig(channelConfig);
        eventPublisher.publishEvent(new SmsChannelUpdateEvent(this,channelConfig.toDto()));
    }


    /**
     * 查询全部配置
     */
    public List<SmsChannelConfigDto> findAll(){
        // 遍历数据库
        Map<String, SmsChannelConfig> map = configManager.findAll()
                .stream()
                .collect(Collectors.toMap(SmsChannelConfig::getCode, Function.identity(), CollectorsFunction::retainLatest));
        return Arrays.stream(SupplierType.values())
                .map(type-> Optional.ofNullable(map.get(type.name().toLowerCase()))
                        .orElse(new SmsChannelConfig()
                                .setCode(type.name().toLowerCase())
                                .setSortNo(0.0)
                                .setName(type.getName())))
                .sorted(Comparator.comparing(SmsChannelConfig::getSortNo))
                .map(SmsChannelConfig::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID查询
     */
    public SmsChannelConfigDto findById(Long id) {
        return configManager.findById(id).map(SmsChannelConfig::toDto)
                .orElseThrow(DataNotExistException::new);
    }

    /**
     * 根据code查询
     */
    public SmsChannelConfigDto findByCode(String code) {
        return configManager.findByCode(code).map(SmsChannelConfig::toDto)
                .orElseThrow(DataNotExistException::new);
    }

    /**
     * 启动成功后初始化所有的短信通道配置
     */
    @EventListener(WebServerInitializedEvent.class)
    public void initChannelConfig(){
        List<SmsChannelConfig> smsChannelConfigs = configManager.findAll();
        smsChannelConfigs.forEach(this::initChannelConfig);
    }

    /**
     * 启动成功后初始化所有的短信通道配置
     */
    public void initChannelConfig(SmsChannelConfig channelConfig){
        SupplierType supplierType = getSupplierType(channelConfig.getCode());
        String config = channelConfig.getConfig();
        if (StrUtil.isBlank(config)){
            return;
        }
        SupplierConfig bean;
        if (supplierType == SupplierType.ALIBABA) {
            bean = JacksonUtil.toBean(config, AlibabaConfig.class);
        } else if (supplierType == SupplierType.HUAWEI) {
            bean = JacksonUtil.toBean(config, HuaweiConfig.class);
        } else if (supplierType == SupplierType.UNI_SMS) {
            bean = JacksonUtil.toBean(config, UniConfig.class);
        } else if (supplierType == SupplierType.TENCENT) {
            bean = JacksonUtil.toBean(config, TencentConfig.class);
        } else if (supplierType == SupplierType.YUNPIAN) {
            bean = JacksonUtil.toBean(config, YunpianConfig.class);
        } else if (supplierType == SupplierType.JD_CLOUD) {
            bean = JacksonUtil.toBean(config, JdCloudConfig.class);
        } else if (supplierType == SupplierType.CLOOPEN) {
            bean = JacksonUtil.toBean(config, CloopenConfig.class);
        } else if (supplierType == SupplierType.EMAY) {
            bean = JacksonUtil.toBean(config, EmayConfig.class);
        } else if (supplierType == SupplierType.CTYUN) {
            bean = JacksonUtil.toBean(config, CtyunConfig.class);
        } else if (supplierType == SupplierType.NETEASE) {
            bean = JacksonUtil.toBean(config, NeteaseConfig.class);
        } else {
            throw new SmsBlendException("短信加载失败！请检查配置类型.");
        }
        SupplierFactory.setSupplierConfig(bean);
    }

    /**
     * 获取供应商配置的序列化
     */
    private String getSupplierConfig(SmsChannelConfig channelConfig, Map<String,Object> map){
        SupplierType supplierType = getSupplierType(channelConfig.getCode());
        if (CollUtil.isEmpty(map)){
            return null;
        }
        if (supplierType == SupplierType.ALIBABA) {
            val bean = BeanUtil.toBean(map, AlibabaConfig.class);
            bean.setAccessKeyId(channelConfig.getAccessKey());
            bean.setAccessKeySecret(channelConfig.getAccessSecret());
            return JacksonUtil.toJson(bean);
        } else if (supplierType == SupplierType.HUAWEI) {
            val bean = BeanUtil.toBean(map, HuaweiConfig.class);
            bean.setAppKey(channelConfig.getAccessKey());
            bean.setAppSecret(channelConfig.getAccessSecret());
            return JacksonUtil.toJson(bean);
        } else if (supplierType == SupplierType.UNI_SMS) {
            val bean = BeanUtil.toBean(map, UniConfig.class);
            bean.setAccessKeyId(channelConfig.getAccessKey());
            bean.setAccessKeySecret(channelConfig.getAccessSecret());
            return JacksonUtil.toJson(bean);
        } else if (supplierType == SupplierType.TENCENT) {
            val bean = BeanUtil.toBean(map, TencentConfig.class);
            bean.setAccessKeyId(channelConfig.getAccessKey());
            bean.setAccessKeySecret(channelConfig.getAccessSecret());
            return JacksonUtil.toJson(bean);
        } else if (supplierType == SupplierType.YUNPIAN) {
            val bean = BeanUtil.toBean(map, YunpianConfig.class);
            bean.setAccessKeyId(channelConfig.getAccessKey());
            bean.setAccessKeySecret(channelConfig.getAccessSecret());
            return JacksonUtil.toJson(bean);
        } else if (supplierType == SupplierType.JD_CLOUD) {
            val bean = BeanUtil.toBean(map, JdCloudConfig.class);
            bean.setAccessKeyId(channelConfig.getAccessKey());
            bean.setAccessKeySecret(channelConfig.getAccessSecret());
            return JacksonUtil.toJson(bean);
        } else if (supplierType == SupplierType.CLOOPEN) {
            val bean = BeanUtil.toBean(map, CloopenConfig.class);
            bean.setAccessKeyId(channelConfig.getAccessKey());
            bean.setAccessKeySecret(channelConfig.getAccessSecret());
            return JacksonUtil.toJson(bean);
        } else if (supplierType == SupplierType.EMAY) {
            val bean = BeanUtil.toBean(map, EmayConfig.class);
            bean.setAppId(channelConfig.getAccessKey());
            bean.setSecretKey(channelConfig.getAccessSecret());
            return JacksonUtil.toJson(bean);
        } else if (supplierType == SupplierType.CTYUN) {
            val bean = BeanUtil.toBean(map, CtyunConfig.class);
            bean.setAccessKeyId(channelConfig.getAccessKey());
            bean.setAccessKeySecret(channelConfig.getAccessSecret());
            return JacksonUtil.toJson(bean);
        } else if (supplierType == SupplierType.NETEASE) {
            val bean = BeanUtil.toBean(map, NeteaseConfig.class);
            bean.setAccessKeyId(channelConfig.getAccessKey());
            bean.setAccessKeySecret(channelConfig.getAccessSecret());
            return JacksonUtil.toJson(bean);
        } else {
            throw new SmsBlendException("短信加载失败！请检查配置类型.");
        }

    }

    /**
     * 获取 短信供应商 枚举
     */
    public SupplierType getSupplierType(String code){
        return Arrays.stream(SupplierType.values())
                .filter(supplierType -> supplierType.name().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new BizException("短信供应商未找到"));
    }

}
