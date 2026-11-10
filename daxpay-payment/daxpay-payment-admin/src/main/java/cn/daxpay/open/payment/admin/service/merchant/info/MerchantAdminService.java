package cn.daxpay.open.payment.admin.service.merchant.info;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.core.enums.role.RoleCodeEnum;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.ValidationFailedException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.iam.dao.role.RoleManager;
import cn.daxpay.open.platform.iam.entity.role.Role;
import cn.daxpay.open.platform.iam.param.user.UserInfoParam;
import cn.daxpay.open.platform.iam.result.user.UserPasswordResult;
import cn.daxpay.open.platform.iam.service.client.ClientCodeService;
import cn.daxpay.open.platform.iam.service.upms.UserRoleService;
import cn.daxpay.open.platform.iam.service.user.UserAdminService;

import cn.daxpay.open.platform.core.exception.config.ConfigNotExistException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.payment.merchant.convert.info.MerchantInfoConvert;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.dao.config.MerchantCredentialManager;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayCashierItemManager;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayPayClientEnvManager;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayPayConfigManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantUserManager;
import cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager;
import cn.daxpay.open.payment.merchant.dao.wxverify.WxDomainVerifyManager;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.merchant.entity.config.MerchantCredential;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayCashierItem;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayPayClientEnv;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayPayConfig;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.payment.merchant.entity.info.MerchantUser;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.payment.merchant.entity.wxverify.WxDomainVerify;
import cn.daxpay.open.platform.core.enums.merchant.MerchantStatusEnum;
import cn.daxpay.open.payment.merchant.param.info.MerchantInfoParam;
import cn.daxpay.open.payment.merchant.param.info.MerchantInfoQuery;
import cn.daxpay.open.payment.merchant.param.info.MerchantRegisterParam;
import cn.daxpay.open.payment.merchant.result.info.MerchantInfoResult;
import cn.daxpay.open.payment.merchant.service.appinfo.MchAppInfoService;
import cn.daxpay.open.payment.merchant.service.store.MchStoreInfoService;
import cn.daxpay.open.payment.merchant.service.user.MerchantUserAdminService;
import cn.daxpay.open.payment.route.dao.strategy.PayRouteStrategyManager;
import cn.daxpay.open.payment.route.entity.strategy.PayRouteStrategy;
import cn.daxpay.open.payment.strategy.device.MerchantDeleteDeviceHook;
import cn.daxpay.open.payment.device.qrcode.dao.DeviceQrCodeManager;
import cn.daxpay.open.payment.device.qrcode.entity.DeviceQrCode;
import cn.daxpay.open.payment.device.terminal.dao.TerminalDeviceManager;
import cn.daxpay.open.payment.device.terminal.entity.DeviceTerminal;
import cn.daxpay.open.payment.trade.notice.dao.MchNoticeTaskManager;
import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.dao.RefundOrderManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.capability.file.service.PlatformFileService;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import cn.daxpay.open.platform.core.code.CommonCode;

/// # 商户服务类
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantAdminService {
    private final MerchantInfoManager merchantInfoManager;
    private final UserAdminService userAdminService;
    private final RoleManager roleManager;
    private final UserRoleService userRoleService;
    private final MerchantUserManager merchantUserManager;
    private final TransService transService;
    private final MchAppInfoService mchAppInfoService;
    private final MchStoreInfoService mchStoreInfoService;

    // === 删除商户所需的级联 Manager（按 mchNo 关联的子表）===

    // 应用 / 门店 / 通道商户主表 / 凭证
    private final MchAppInfoManager mchAppInfoManager;
    private final MchStoreInfoManager mchStoreInfoManager;
    private final ChannelMerchantManager channelMerchantManager;
    private final MerchantCredentialManager merchantCredentialManager;
    // 网关与路由配置
    private final GatewayPayConfigManager gatewayPayConfigManager;
    private final GatewayCashierItemManager gatewayCashierItemManager;
    private final PayRouteStrategyManager payRouteStrategyManager;
    // 杂项
    private final WxDomainVerifyManager wxDomainVerifyManager;
    // 交易/订单/退款（删除前置校验：硬阻塞）
    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager normalPayOrderManager;
    private final RefundOrderManager refundOrderManager;
    // 网关支付订单(删除前置校验: 硬阻塞; 易支付单在插件模块不阻塞, 见 delete 注释)
    private final GatewayPayOrderManager gatewayPayOrderManager;
    // 设备(删除前置校验: 硬阻塞; 码牌/终端 core 直查, 虚拟设备经设备模块 SPI)
    private final DeviceQrCodeManager deviceQrCodeManager;
    private final TerminalDeviceManager terminalDeviceManager;
    private final ObjectProvider<MerchantDeleteDeviceHook> merchantDeleteDeviceHookProvider;
    // 公共配置类级联扩充(待发通知任务/网关支付环境)
    private final MchNoticeTaskManager mchNoticeTaskManager;
    private final GatewayPayClientEnvManager gatewayPayClientEnvManager;
    // 商户用户级联注销(删除商户时名下用户逐个注销)
    private final MerchantUserAdminService merchantUserAdminService;
    // 门店图片附件本体删除(注销后 best-effort)
    private final PlatformFileService platformFileService;

    /// 添加商户
    /// @return 商户管理员的初始密码结果(未传密码时由系统生成随机密码)
    @Transactional(rollbackFor = Exception.class)
    public UserPasswordResult add(MerchantRegisterParam param) {
        var merchant = MerchantInfoConvert.CONVERT.toEntity(param);
        merchant.setMchNo(this.getMchNo());

        merchant.setStatus(MerchantStatusEnum.ENABLE.getCode());
        // 创建商户管理员
        UserPasswordResult adminResult = this.createMerchantAdmin(param,  merchant);
        merchantInfoManager.save(merchant);
        // 创建默认应用（名称按请求语言）
        mchAppInfoService.createDefaultApp(merchant.getMchNo(), merchant.getMchShortName());
        // 创建默认门店（名称按请求语言）
        mchStoreInfoService.createDefaultStore(merchant.getMchNo(), merchant.getMchShortName(), param.getPhone());
        return adminResult;
    }

    /// 创建商户管理员
    public UserPasswordResult createMerchantAdmin(MerchantRegisterParam param, MerchantInfo merchant) {
        // 创建用户
        var userInfoParam = new UserInfoParam();
        MerchantInfoConvert.CONVERT.copy(param, userInfoParam);
        // 用户名称(管理员名称, 必填)
        userInfoParam.setName(param.getAdminName().trim());
        // 设置终端归属为商户端
        userInfoParam.setClientCode(ClientEnum.MERCHANT.getCode());
        // 商户管理员账号必须按商户终端校验唯一性, 不可跳过:
        // 跳过会产生重复登录账号, 登录按 client_code+account 查单条会抛多结果异常, 导致新旧账号一并不可用
        UserPasswordResult adminResult = userAdminService.add(userInfoParam);
        Role role;
        // 商户: 商户管理员角色不存在
        role = roleManager.findByCode(RoleCodeEnum.MERCHANT_ADMIN.getCode())
                .orElseThrow(ConfigNotExistException::new);
        // 分配角色
        userRoleService.saveAssign(adminResult.getUserId(), role.getId(), true);
        // 创建商户绑定关系
        merchantUserManager.save(new MerchantUser(adminResult.getUserId(), merchant.getMchNo(), true));
        // 商户信息更新
        merchant.setAdminUserId(adminResult.getUserId());
        merchantInfoManager.updateById(merchant);
        return adminResult;
    }

    /// 修改
    public void update(MerchantInfoParam param) {
        MerchantInfo merchant = merchantInfoManager.findById(param.getId())
                .orElseThrow(DataNotExistException::new);
        MerchantInfoConvert.CONVERT.copy(param, merchant);
        merchantInfoManager.updateById(merchant);
    }

    /// 分页
    public PageResult<MerchantInfoResult> page(PageParam pageParam, MerchantInfoQuery query) {
        Page<MerchantInfoResult> mpPage = MpUtil.getMpPage(pageParam);
        MPJLambdaWrapper<MerchantInfo> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(MerchantInfo.class)
                .like(StrUtil.isNotBlank(query.getMchName()),MerchantInfo::getMchName, query.getMchName())
                .eq(StrUtil.isNotBlank(query.getSubjectType()),MerchantInfo::getSubjectType, query.getSubjectType())
                .eq(StrUtil.isNotBlank(query.getStatus()),MerchantInfo::getStatus, query.getStatus());
        var page = merchantInfoManager.selectJoinListPage(mpPage, MerchantInfoResult.class, wrapper);
        PageResult<MerchantInfoResult> pageResult = MpUtil.toPageResult(page);
        transService.translate(pageResult);
        return pageResult;
    }

    /// 获取单条
    public MerchantInfoResult findById(Long id) {
        return merchantInfoManager.findById(id)
                .map(MerchantInfo::toResult)
                .orElseThrow(DataNotExistException::new);
    }

    /// 根据商户号查询
    public MerchantInfoResult findByMchNo(String mchNo) {
        return merchantInfoManager.findByMchNo(mchNo)
                .map(MerchantInfo::toResult)
                .orElseThrow(DataNotExistException::new);
    }

    /// 删除商户(注销: 阻塞校验 + 级联脱敏 + 级联逻辑删 + 用户级联注销)
    ///
    /// 硬阻塞: 交易/订单/退款任一存在即拒删(合规留存); 网关支付订单存在即拒删
    /// (资金痕迹不容"有单无商"; 易支付单在 plugin-easypay 插件模块, 端模块不得依赖插件,
    /// 维持孤儿不阻塞——mchNo 系统生成不复用, 无重建碰撞风险);
    /// 名下绑定设备任一存在即拒删(虚拟设备经设备模块 SPI, 码牌/终端 core 直查,
    /// 先解绑或处置设备后再删, 防止无主设备);
    /// 关联用户不再阻塞, 随主体自动级联注销, 确认弹窗须明示连带删除用户账号。
    /// 级联: 应用/门店/凭证先脱敏改写(名称占位、PII/密钥置空)再逻辑删;
    /// 待发通知任务/网关支付环境直接逻辑删; 设备域商户私有配置(虚拟设备绑定/应用联动配置)经 SPI 级联
    /// (无设备域 Bean 时优雅跳过); 名下用户逐个注销(见 [MerchantUserAdminService#deleteByMchNo],
    /// 含 iam 侧级联清理/脱敏/逻辑删/踢线)。
    /// 主表骨架保留(mchNo/时间戳不动), 名称脱敏为占位符后逻辑删;
    /// 门店图片引用置空前收集, 事务提交后 best-effort 删除对象存储本体。
    /// 注意：
    /// - 通道扩展表（各通道 `XxxChannelMerchant` + `XxxKeyConfig`）**不在本次级联范围**，
    ///   避免事务过深与跨模块循环依赖；主表 `mch_channel_merchant` 删除后路由不再命中，
    ///   扩展表孤儿数据对业务无影响。
    /// - 通道商户主表删除仅清主表，不触发 [cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy]。
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        MerchantInfo merchant = merchantInfoManager.findById(id)
                .orElseThrow(DataNotExistException::new);
        String mchNo = merchant.getMchNo();

        // 1a. 硬阻塞校验：交易/订单/退款存在则拒删
        if (payTradeManager.existedByField(PayTrade::getMchNo, mchNo)) {
            // 商户: 商户存在交易记录，不允许删除
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.hasTrade");
        }
        if (normalPayOrderManager.existedByField(NormalPayOrder::getMchNo, mchNo)) {
            // 商户: 商户存在订单记录，不允许删除
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.hasOrder");
        }
        if (refundOrderManager.existedByField(RefundOrder::getMchNo, mchNo)) {
            // 商户: 商户存在退款记录，不允许删除
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.hasRefund");
        }

        // 1b. 硬阻塞校验：网关支付订单(资金痕迹不容"有单无商"; 开源版无预授权/支付分域)
        if (gatewayPayOrderManager.existedByField(GatewayPayOrder::getMchNo, mchNo)) {
            // 商户: 商户存在网关支付订单，不允许删除
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.hasGatewayOrder");
        }

        // 1c. 硬阻塞校验：名下绑定设备(虚拟设备经设备模块 SPI 幂等空判, 码牌/终端 core 直查)
        MerchantDeleteDeviceHook merchantDeleteDeviceHook = merchantDeleteDeviceHookProvider.getIfAvailable();
        boolean virtualDeviceBound = merchantDeleteDeviceHook != null
                && merchantDeleteDeviceHook.countBoundByMchNo(mchNo) > 0;
        if (virtualDeviceBound
                || deviceQrCodeManager.existedByField(DeviceQrCode::getMchNo, mchNo)
                || terminalDeviceManager.existedByField(DeviceTerminal::getMchNo, mchNo)) {
            // 商户: 商户名下存在绑定设备，请先解绑或处置设备后删除
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.hasBoundDevice");
        }

        // 2. 级联脱敏前收集门店图片引用(置空前收集, 提交后删对象存储本体; 多门店全量)
        List<String> attachmentFilenames = new ArrayList<>();
        for (MchStoreInfo store : mchStoreInfoManager.findAllByMchNo(mchNo)) {
            this.collectAttachment(attachmentFilenames, store.getLogoUrl());
            this.collectAttachment(attachmentFilenames, store.getFacadeUrl());
            this.collectAttachment(attachmentFilenames, store.getInteriorUrl());
        }

        // 2a. 级联脱敏 + 逻辑删（应用/门店/凭证; 名称占位、PII/密钥置空后再删）
        mchAppInfoManager.sanitizeByMchNo(mchNo);
        mchAppInfoManager.deleteByField(MchAppInfo::getMchNo, mchNo);
        mchStoreInfoManager.sanitizeByMchNo(mchNo);
        mchStoreInfoManager.deleteByField(MchStoreInfo::getMchNo, mchNo);
        merchantCredentialManager.sanitizeByMchNo(mchNo);
        merchantCredentialManager.deleteByField(MerchantCredential::getMchNo, mchNo);
        // 配置类子表（强关联，业务上属于商户私有数据）
        channelMerchantManager.deleteByField(ChannelMerchant::getMchNo, mchNo);
        // 网关与路由配置（按 mchNo 隔离的主表，其子表留孤儿不影响业务）
        gatewayPayConfigManager.deleteByField(GatewayPayConfig::getMchNo, mchNo);
        gatewayCashierItemManager.deleteByField(GatewayCashierItem::getMchNo, mchNo);
        payRouteStrategyManager.deleteByField(PayRouteStrategy::getMchNo, mchNo);
        // 杂项
        wxDomainVerifyManager.deleteByField(WxDomainVerify::getMchNo, mchNo);
        // 公共配置类级联扩充: 待发通知任务/网关支付环境
        mchNoticeTaskManager.deleteByField(MchNoticeTask::getMchNo, mchNo);
        gatewayPayClientEnvManager.deleteByField(GatewayPayClientEnv::getMchNo, mchNo);
        // 设备域商户私有配置(虚拟设备绑定/应用设备联动配置)经 SPI 级联(无设备域 Bean 时优雅跳过)
        if (merchantDeleteDeviceHook != null) {
            merchantDeleteDeviceHook.deleteMerchantScopedByMchNo(mchNo);
        }

        // 3. 名下用户逐个注销(删挂靠行 + iam 注销 + 踢线, 无最后管理员护栏——主体随之删除)
        merchantUserAdminService.deleteByMchNo(mchNo);

        // 4. 主表：置禁用落库后脱敏再逻辑删
        // 注意：MchBaseEntity#setMchNo 链式返回父类型，单独赋值；此处仅更新状态不涉及 mchNo
        // updateById 会回写内存旧值, 脱敏必须放在其后覆盖
        merchant.setStatus(MerchantStatusEnum.DISABLED.getCode());
        merchantInfoManager.updateById(merchant);
        merchantInfoManager.sanitizeByMchNo(mchNo);
        merchantInfoManager.deleteById(id);
        // 门店图片本体删除(事务提交后 best-effort, 失败仅告警不重试, 不影响注销结果)
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                for (String filename : attachmentFilenames) {
                    try {
                        platformFileService.deleteByFilename(filename);
                    }
                    catch (Exception e) {
                        log.warn("门店图片本体删除失败(不重试): filename={}", filename, e);
                    }
                }
            }
        });
        log.info("商户注销: mchNo={}", mchNo);
    }

    /// 收集非空附件文件名(空值跳过; 脱敏置空前调用, 值即业务表图片字段存的文件名标识)
    private void collectAttachment(List<String> filenames, String filename) {
        if (StrUtil.isNotBlank(filename)) {
            filenames.add(filename);
        }
    }

    /// 生成商户号
    public String getMchNo(){
        String mchNo = "M" + System.currentTimeMillis();
        for (int i = 0; i < 10; i++){
            if (!merchantInfoManager.existedByField(MerchantInfo::getMchNo, mchNo)){
                return mchNo;
            }
            mchNo = "M" + System.currentTimeMillis();
        }
        // 商户: 商户号生成失败
        throw new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.mchNoGenFailed");
    }

    /// 启用商户
    public void enable(Long id) {
        MerchantInfo merchant = merchantInfoManager.findById(id)
                .orElseThrow(DataNotExistException::new);
        merchant.setStatus(MerchantStatusEnum.ENABLE.getCode());
        merchantInfoManager.updateById(merchant);
    }

    /// 禁用商户
    public void disable(Long id) {
        MerchantInfo merchant = merchantInfoManager.findById(id)
                .orElseThrow(DataNotExistException::new);
        merchant.setStatus(MerchantStatusEnum.DISABLED.getCode());
        merchantInfoManager.updateById(merchant);
    }

    /// 商户下拉列表
    ///
    /// 显示商户简称(mchShortName), 简称在建表时已强制必填。
    /// includeDisabled=true 时返回全量商户(管理/查询场景), 停用商户简称带「（已停用）」后缀;
    /// 默认仅启用(调试发起交易等业务动作场景的防线)。
    public List<LabelValue> dropdown(boolean includeDisabled) {
        List<MerchantInfo> merchants = includeDisabled
                ? merchantInfoManager.findAll()
                : merchantInfoManager.findAllByEnable();
        return merchants.stream()
                .map(m -> new LabelValue(
                        MerchantStatusEnum.DISABLED.getCode().equals(m.getStatus())
                                ? m.getMchShortName() + "（已停用）"
                                : m.getMchShortName(),
                        m.getMchNo()))
                .collect(Collectors.toList());
    }

}
