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
import cn.daxpay.open.platform.iam.entity.user.UserInfo;
import cn.daxpay.open.platform.iam.param.user.UserInfoParam;
import cn.daxpay.open.platform.iam.service.client.ClientCodeService;
import cn.daxpay.open.platform.iam.service.upms.UserRoleService;
import cn.daxpay.open.platform.iam.service.user.UserAdminService;

import cn.daxpay.open.platform.core.exception.config.ConfigNotExistException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.payment.merchant.convert.info.MerchantInfoConvert;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.dao.config.MchAppNotifyConfigManager;
import cn.daxpay.open.payment.merchant.dao.config.MerchantCredentialManager;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayAggregateConfigManager;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayCashierItemManager;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayCodeConfigManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantUserManager;
import cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager;
import cn.daxpay.open.payment.merchant.dao.wxverify.WxDomainVerifyManager;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.merchant.entity.config.MchAppNotifyConfig;
import cn.daxpay.open.payment.merchant.entity.config.MerchantCredential;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayAggregateConfig;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayCashierItem;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayCodeConfig;
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
import cn.daxpay.open.payment.route.dao.strategy.PayRouteStrategyManager;
import cn.daxpay.open.payment.route.entity.strategy.PayRouteStrategy;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.dao.RefundOrderManager;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ClientCodeService clientCodeService;
    private final TransService transService;
    private final MchAppInfoService mchAppInfoService;
    private final MchStoreInfoService mchStoreInfoService;

    // === 删除商户所需的级联 Manager（按 mchNo 关联的子表）===

    // 应用 / 门店 / 通道商户主表 / 凭证 / 通知配置
    private final MchAppInfoManager mchAppInfoManager;
    private final MchStoreInfoManager mchStoreInfoManager;
    private final ChannelMerchantManager channelMerchantManager;
    private final MerchantCredentialManager merchantCredentialManager;
    private final MchAppNotifyConfigManager mchAppNotifyConfigManager;
    // 网关与路由配置
    private final GatewayCodeConfigManager gatewayCodeConfigManager;
    private final GatewayAggregateConfigManager gatewayAggregateConfigManager;
    private final GatewayCashierItemManager gatewayCashierItemManager;
    private final PayRouteStrategyManager payRouteStrategyManager;
    // 杂项
    private final WxDomainVerifyManager wxDomainVerifyManager;
    // 交易/订单/退款（删除前置校验：硬阻塞）
    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager normalPayOrderManager;
    private final RefundOrderManager refundOrderManager;

    /// 添加商户
    @Transactional(rollbackFor = Exception.class)
    public void add(MerchantRegisterParam param) {
        var merchant = MerchantInfoConvert.CONVERT.toEntity(param);
        merchant.setMchNo(this.getMchNo());

        merchant.setStatus(MerchantStatusEnum.ENABLE.getCode());
        // 创建商户管理员
        this.createMerchantAdmin(param,  merchant);
        merchantInfoManager.save(merchant);
        // 创建默认应用（名称按请求语言）
        mchAppInfoService.createDefaultApp(merchant.getMchNo(), merchant.getMchName());
        // 创建默认门店（名称按请求语言）
        mchStoreInfoService.createDefaultStore(merchant.getMchNo(), merchant.getMchName(), param.getPhone());
    }

    /// 创建商户管理员
    public void createMerchantAdmin(MerchantRegisterParam param, MerchantInfo merchant) {
        // 创建用户
        var userInfoParam = new UserInfoParam();
        MerchantInfoConvert.CONVERT.copy(param, userInfoParam);
        // 用户名称
        userInfoParam.setName(merchant.getMchName()+"管理员");
        // 设置手机号
        userInfoParam.setPhone(param.getPhone());
        // 设置终端归属为商户端
        userInfoParam.setClientCode(ClientEnum.MERCHANT.getCode());
        UserInfo userInfo = userAdminService.add(userInfoParam, true);
        Role role;
        // 商户: 商户管理员角色不存在
        role = roleManager.findByCode(RoleCodeEnum.MERCHANT_ADMIN.getCode())
                .orElseThrow(ConfigNotExistException::new);
        // 分配角色
        userRoleService.saveAssign(userInfo.getId(), role.getId(), true);
        // 创建商户绑定关系
        merchantUserManager.save(new MerchantUser(userInfo.getId(), merchant.getMchNo(), true));
        // 商户信息更新
        merchant.setAdminUserId(userInfo.getId());
        merchantInfoManager.updateById(merchant);
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

    /// 删除商户（逻辑删除 + 禁用）
    ///
    /// 策略：
    /// - **交易类数据存在则拒删**（合规/对账要求，历史数据必须可追溯）
    /// - **启用态关联用户存在则拒删**（请先在商户用户管理中解绑或禁用）
    /// - 通过校验后，按 `mchNo` 级联逻辑删除所有配置类子表，主表置禁用 + 逻辑删
    ///
    /// 注意：
    /// - 通道扩展表（各通道 `XxxChannelMerchant` + `XxxKeyConfig`）**不在本次级联范围**，
    ///   避免事务过深与跨模块循环依赖；主表 `mch_channel_merchant` 删除后路由不再命中，
    ///   扩展表孤儿数据对业务无影响。
    /// - 通道商户主表删除仅清主表，不触发 [cn.daxpay.open.payment.merchant.service.channel.ChannelMerchantCleanupSupport]。
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        MerchantInfo merchant = merchantInfoManager.findById(id)
                .orElseThrow(DataNotExistException::new);
        String mchNo = merchant.getMchNo();

        // 1. 硬阻塞校验：交易/订单/退款存在则拒删
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

        // 2. 硬阻塞校验：关联用户存在则拒删（提示先到商户用户管理中解绑或删除）
        // 关联用户表非空即阻塞，避免删除后用户登录查不到商户报错；启用/禁用用户均要求先解绑
        if (merchantUserManager.existedByField(MerchantUser::getMchNo, mchNo)) {
            // 商户: 商户存在关联用户，请先在商户用户管理中解绑
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.hasUser");
        }

        // 3. 级联逻辑删除（按 mchNo 批量更新 deleted=true）
        // 配置类子表（强关联，业务上属于商户私有数据）
        mchAppInfoManager.deleteByField(MchAppInfo::getMchNo, mchNo);
        mchStoreInfoManager.deleteByField(MchStoreInfo::getMchNo, mchNo);
        channelMerchantManager.deleteByField(ChannelMerchant::getMchNo, mchNo);
        merchantCredentialManager.deleteByField(MerchantCredential::getMchNo, mchNo);
        mchAppNotifyConfigManager.deleteByField(MchAppNotifyConfig::getMchNo, mchNo);
        // 网关与路由配置（按 mchNo 隔离的主表，其子表如 GatewayCodeClientEnv 留孤儿不影响业务）
        gatewayCodeConfigManager.deleteByField(GatewayCodeConfig::getMchNo, mchNo);
        gatewayAggregateConfigManager.deleteByField(GatewayAggregateConfig::getMchNo, mchNo);
        gatewayCashierItemManager.deleteByField(GatewayCashierItem::getMchNo, mchNo);
        payRouteStrategyManager.deleteByField(PayRouteStrategy::getMchNo, mchNo);
        // 杂项
        wxDomainVerifyManager.deleteByField(WxDomainVerify::getMchNo, mchNo);

        // 4. 主表：置禁用 + 逻辑删
        // 注意：MchBaseEntity#setMchNo 链式返回父类型，单独赋值；此处仅更新状态不涉及 mchNo
        merchant.setStatus(MerchantStatusEnum.DISABLED.getCode());
        merchantInfoManager.updateById(merchant);
        merchantInfoManager.deleteById(id);
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
    public List<LabelValue> dropdown() {
        List<MerchantInfo> merchants = merchantInfoManager.findAllByEnable();
        return merchants.stream()
                .map(m -> new LabelValue(m.getMchShortName(), m.getMchNo()))
                .collect(Collectors.toList());
    }

}
