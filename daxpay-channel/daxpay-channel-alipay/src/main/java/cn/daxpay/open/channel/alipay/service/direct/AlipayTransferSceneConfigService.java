package cn.daxpay.open.channel.alipay.service.direct;

import cn.daxpay.open.channel.alipay.dao.direct.AlipayTransferSceneConfigManager;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayTransferSceneConfig;
import cn.daxpay.open.channel.alipay.enums.AlipayTransferSceneEnum;
import cn.daxpay.open.channel.alipay.result.direct.AlipayTransferSceneConfigResult;
import cn.daxpay.open.channel.alipay.result.direct.AlipayTransferSceneOptionResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/// # 支付宝转账场景配置
///
/// 管理转账场景配置的启用/默认状态, 采用主数据枚举驱动模式:
/// - 场景元数据(8个场景 + 报备字段)由 [AlipayTransferSceneEnum] 投影, 不查库、不预置, 见 [AlipayTransferSceneOptionResult]
/// - 配置表仅存"被操作过的"场景行(启用/默认状态), 启用或设默认时按需创建, 无行即为未启用
/// - 最多启用3个, 默认1个(必须启用), 发起转账时通过 [findEffective] 解析生效配置
///
/// 2026 年起支付宝对新接入商户强制要求 `transfer_scene_name` 与 `transfer_scene_report_infos`,
/// 未配置场景的商户发起转账将被支付宝拒单。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayTransferSceneConfigService {

    /// 最大启用场景数
    private static final int MAX_ENABLED = 3;

    private final AlipayTransferSceneConfigManager alipayTransferSceneConfigManager;

    /// 查询支付宝转账场景选项列表(主数据枚举投影, 不查库, 供前端卡片渲染与报备字段动态展示)
    public List<AlipayTransferSceneOptionResult> findSceneOptions() {
        return Arrays.stream(AlipayTransferSceneEnum.values())
                .map(scene -> new AlipayTransferSceneOptionResult()
                        .setSceneName(scene.getSceneName())
                        .setReportInfoTypes(scene.getReportInfoTypes())
                        .setReportInfoDescriptions(scene.getReportInfoDescriptions()))
                .toList();
    }

    /// 查询通道商户下的场景配置行(仅已操作过的行, 按枚举固定顺序排序, 填充报备元数据)
    ///
    /// 未操作过的场景无行, 前端以场景选项为基准渲染, 本方法返回的状态映射到对应卡片。
    public List<AlipayTransferSceneConfigResult> list(String mchNo, String channelMchNo) {
        List<AlipayTransferSceneConfig> entities = alipayTransferSceneConfigManager.listByChannelMchNo(channelMchNo);
        // 按枚举固定顺序排序
        Map<String, Integer> orderMap = new HashMap<>();
        AlipayTransferSceneEnum[] enums = AlipayTransferSceneEnum.values();
        for (int i = 0; i < enums.length; i++) {
            orderMap.put(enums[i].getSceneName(), i);
        }
        return entities.stream()
                .filter(e -> mchNo == null || mchNo.equals(e.getMchNo()))
                .sorted(Comparator.comparingInt(e -> orderMap.getOrDefault(e.getSceneName(), Integer.MAX_VALUE)))
                .map(this::toResultWithMeta)
                .toList();
    }

    /// 将实体转为Result并填充报备字段元数据(由枚举推导)
    private AlipayTransferSceneConfigResult toResultWithMeta(AlipayTransferSceneConfig entity) {
        AlipayTransferSceneConfigResult result = entity.toResult();
        AlipayTransferSceneEnum scene = AlipayTransferSceneEnum.findBySceneName(entity.getSceneName());
        if (scene != null) {
            result.setReportInfoTypes(scene.getReportInfoTypes());
            result.setReportInfoDescriptions(scene.getReportInfoDescriptions());
        }
        return result;
    }

    /// 设置场景启用状态(按场景名称操作, 场景行不存在时自动创建)
    ///
    /// @param mchNo       商户号(归属校验, 新行写入用)
    /// @param channelMchNo 通道商户号
    /// @param sceneName   场景名称(支付宝协议固定中文取值)
    /// @param enabled     是否启用; 启用时校验上限(最多3个), 禁用时校验非默认
    @Transactional(rollbackFor = Exception.class)
    public void setEnabled(String mchNo, String channelMchNo, String sceneName, boolean enabled) {
        // 校验场景名称合法性
        AlipayTransferSceneEnum scene = AlipayTransferSceneEnum.findBySceneName(sceneName);
        if (scene == null) {
            // 支付宝: 不支持的转账场景: {0}
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.alipay.transferSceneNameInvalid", sceneName);
        }
        AlipayTransferSceneConfig entity = alipayTransferSceneConfigManager
                .findByChannelMchNoAndSceneName(channelMchNo, sceneName)
                // 主数据模式下无行即为未操作过, 按需创建(启用/设默认才落行)
                .orElseGet(() -> {
                    var newEntity = new AlipayTransferSceneConfig();
                    // 运营端写 MchBaseEntity 必须显式 setMchNo(父类 setter 返回类型不匹配, 单独赋值)
                    newEntity.setMchNo(mchNo);
                    newEntity.setChannelMchNo(channelMchNo);
                    newEntity.setSceneName(sceneName);
                    newEntity.setEnabled(false);
                    newEntity.setIsDefault(false);
                    return newEntity;
                });
        if (!mchNo.equals(entity.getMchNo())) {
            // 支付宝: 转账场景配置不属于当前商户
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.alipay.transferSceneNotBelong");
        }
        if (enabled) {
            // 已启用的幂等返回, 未启用的校验上限
            if (!Boolean.TRUE.equals(entity.getEnabled())) {
                long count = alipayTransferSceneConfigManager.countEnabled(channelMchNo);
                if (count >= MAX_ENABLED) {
                    // 支付宝: 启用场景不能超过{0}个
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "error.channel.alipay.transferSceneEnabledLimit", MAX_ENABLED);
                }
            }
            entity.setEnabled(true);
        } else {
            // 默认场景不允许禁用
            if (Boolean.TRUE.equals(entity.getIsDefault())) {
                // 支付宝: 默认场景不能禁用, 请先取消默认或切换默认到其他场景
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.alipay.transferSceneCannotDisableDefault");
            }
            entity.setEnabled(false);
        }
        if (entity.getId() == null) {
            // 新行先落库(自动填充主键)
            alipayTransferSceneConfigManager.save(entity);
        } else {
            alipayTransferSceneConfigManager.updateById(entity);
        }
    }

    /// 设为默认场景(按场景名称操作, 场景行不存在时自动创建; 自动启用, 含上限校验, 事务内清旧默认)
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(String mchNo, String channelMchNo, String sceneName) {
        // 校验场景名称合法性
        AlipayTransferSceneEnum scene = AlipayTransferSceneEnum.findBySceneName(sceneName);
        if (scene == null) {
            // 支付宝: 不支持的转账场景: {0}
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.alipay.transferSceneNameInvalid", sceneName);
        }
        AlipayTransferSceneConfig entity = alipayTransferSceneConfigManager
                .findByChannelMchNoAndSceneName(channelMchNo, sceneName)
                .orElseGet(() -> {
                    var newEntity = new AlipayTransferSceneConfig();
                    // 运营端写 MchBaseEntity 必须显式 setMchNo(父类 setter 返回类型不匹配, 单独赋值)
                    newEntity.setMchNo(mchNo);
                    newEntity.setChannelMchNo(channelMchNo);
                    newEntity.setSceneName(sceneName);
                    newEntity.setEnabled(false);
                    newEntity.setIsDefault(false);
                    return newEntity;
                });
        if (!mchNo.equals(entity.getMchNo())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.alipay.transferSceneNotBelong");
        }
        // 设默认时自动启用(含上限校验)
        if (!Boolean.TRUE.equals(entity.getEnabled())) {
            long count = alipayTransferSceneConfigManager.countEnabled(channelMchNo);
            if (count >= MAX_ENABLED) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.alipay.transferSceneEnabledLimit", MAX_ENABLED);
            }
            entity.setEnabled(true);
        }
        // 清旧默认
        alipayTransferSceneConfigManager.clearDefault(channelMchNo);
        entity.setIsDefault(true);
        if (entity.getId() == null) {
            // 新行先落库(自动填充主键)
            alipayTransferSceneConfigManager.save(entity);
        } else {
            alipayTransferSceneConfigManager.updateById(entity);
        }
    }

    /// 解析发起转账时生效的场景配置
    ///
    /// @param channelMchNo 通道商户号
    /// @param configIdStr  订单指定的配置 id(字符串,可空); 非空则优先用指定,空则用默认
    /// @return 生效的场景配置, 无可用配置抛 transferSceneNotConfigured
    public AlipayTransferSceneConfig findEffective(String channelMchNo, String configIdStr) {
        if (StrUtil.isNotBlank(configIdStr)) {
            Long configId;
            try {
                configId = Long.parseLong(configIdStr);
            } catch (NumberFormatException e) {
                // 支付宝: 转账场景配置ID格式错误
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.alipay.transferSceneIdInvalid", configIdStr);
            }
            var config = alipayTransferSceneConfigManager.findById(configId)
                    .orElseThrow(() -> new DataNotExistException("error.channel.alipay.transferSceneNotFound"));
            if (!channelMchNo.equals(config.getChannelMchNo())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.alipay.transferSceneNotBelong");
            }
            // 禁用的场景不可用
            if (!Boolean.TRUE.equals(config.getEnabled())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.alipay.transferSceneNotConfigured");
            }
            return config;
        }
        return alipayTransferSceneConfigManager.findDefault(channelMchNo)
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.alipay.transferSceneNotConfigured"));
    }
}
