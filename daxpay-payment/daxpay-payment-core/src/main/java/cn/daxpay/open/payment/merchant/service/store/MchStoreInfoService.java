package cn.daxpay.open.payment.merchant.service.store;

import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.device.qrcode.dao.DeviceQrCodeManager;
import cn.daxpay.open.payment.device.terminal.dao.TerminalDeviceManager;
import cn.daxpay.open.payment.device.qrcode.entity.DeviceQrCode;
import cn.daxpay.open.payment.merchant.convert.store.MchStoreInfoConvert;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.payment.merchant.param.store.MchStoreInfoParam;
import cn.daxpay.open.payment.merchant.param.store.MchStoreInfoQuery;
import cn.daxpay.open.payment.merchant.result.store.MchStoreInfoResult;
import cn.daxpay.open.payment.merchant.result.store.MchStoreOverviewResult;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.core.enums.merchant.StoreStatusEnum;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.config.ConfigErrorException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.iam.service.client.ClientCodeService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/// # 门店信息管理
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MchStoreInfoService {

    private final MchStoreInfoManager mchStoreInfoManager;

    private final MchAppInfoManager mchAppInfoManager;

    private final MerchantInfoManager merchantInfoManager;

    private final DeviceQrCodeManager deviceQrCodeManager;

    private final TerminalDeviceManager terminalDeviceManager;

    private final ClientCodeService clientCodeService;

    private final PaymentContext paymentContext;

    private final MerchantContextLoader merchantContextLoader;

    /// 新增门店
    @Transactional(rollbackFor = Exception.class)
    public void add(MchStoreInfoParam param) {
        String mchNo = this.resolveMchNo(param.getMchNo());
        MerchantInfo merchant = merchantInfoManager.findByMchNo(mchNo)
                // 商户: 商户不存在
                .orElseThrow(() -> new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.mchNotExist"));
        param.setMchNo(mchNo);
        // 门店默认应用: 非空须为本商户存在且启用的应用
        this.validateAppId(mchNo, param.getAppId());
        MchStoreInfo entity = MchStoreInfoConvert.CONVERT.toEntity(param);
        // 应用号空串归一为 null(语义: 跟随商户默认应用)
        entity.setAppId(StrUtil.blankToDefault(entity.getAppId(), null));
        // 生成门店号
        entity.setStoreNo(this.generateStoreNo());
        entity.setMchNo(mchNo);
        // 首店或显式 defaultStore: 设为默认(同商户仅一个)
        boolean firstStore = !mchStoreInfoManager.existsByMchNo(mchNo);
        boolean wantDefault = firstStore || param.isDefaultStore();
        entity.setDefaultStore(false);
        mchStoreInfoManager.save(entity);
        if (wantDefault) {
            this.setDefault(entity.getId());
        }
    }

    /// 为商户创建默认门店（启用 + defaultStore=true；名称按当前请求语言生成）
    ///
    /// @param mchNo         商户号
    /// @param mchShortName  商户简称（用于生成默认门店名）
    /// @param contactPhone  联系电话（可空）
    public void createDefaultStore(String mchNo, String mchShortName, String contactPhone) {
        MchStoreInfo store = new MchStoreInfo();
        // 默认门店名称（payment.merchant.defaultStoreName，{0}=商户简称）
        store.setStoreName(I18nUtil.get("payment.merchant.defaultStoreName", mchShortName));
        store.setContactPhone(contactPhone);
        store.setStatus(StoreStatusEnum.ENABLE.getCode());
        store.setDefaultStore(true);
        store.setStoreNo(this.generateStoreNo());
        // 运营端创建无 PaymentContext，必须显式写 mchNo
        store.setMchNo(mchNo);
        mchStoreInfoManager.save(store);
    }

    /// 修改门店
    @Transactional(rollbackFor = Exception.class)
    public void update(MchStoreInfoParam param) {
        MchStoreInfo mchStore = mchStoreInfoManager.findById(param.getId())
                // 商户: 门店不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        this.checkStore(mchStore);
        // 门店默认应用: 非空须为本商户存在且启用的应用
        this.validateAppId(mchStore.getMchNo(), param.getAppId());
        boolean wasDefault = mchStore.isDefaultStore();
        MchStoreInfoConvert.CONVERT.copy(param, mchStore);
        // 应用号: null 表示未携带该字段(门店编辑), 保持不变;
        // 空串表示清空(跟随商户默认应用), 非空表示指定应用
        if (param.getAppId() != null) {
            mchStore.setAppId(StrUtil.blankToDefault(param.getAppId(), null));
        }
        // 默认标记不直接 copy 覆盖, 走 set/clear 保证同商户唯一
        mchStore.setDefaultStore(wasDefault);
        mchStoreInfoManager.updateById(mchStore);
        // updateById 忽略 null 字段, 显式清空需单独置空落库
        if (param.getAppId() != null && StrUtil.isBlank(param.getAppId())) {
            mchStoreInfoManager.lambdaUpdate()
                    .set(MchStoreInfo::getAppId, null)
                    .eq(MchStoreInfo::getId, mchStore.getId())
                    .update();
        }
        if (!Objects.equals(wasDefault, param.isDefaultStore())) {
            if (param.isDefaultStore()) {
                this.setDefault(mchStore.getId());
            } else {
                this.clearDefault(mchStore.getId());
            }
        }
    }

    /// 分页
    public PageResult<MchStoreInfoResult> page(PageParam pageParam, MchStoreInfoQuery query) {
        PageResult<MchStoreInfoResult> result = MpUtil.toPageResult(mchStoreInfoManager.page(pageParam, query));
        this.fillAppName(result.getRecords());
        return result;
    }

    /// 根据id查询
    public MchStoreInfoResult findById(Long id) {
        MchStoreInfo mchStore = mchStoreInfoManager.findById(id)
                // 商户: 门店不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        this.checkStore(mchStore);
        MchStoreInfoResult result = mchStore.toResult();
        this.fillAppName(List.of(result));
        return result;
    }

    /// 门店列表(商户端按当前商户过滤)
    public List<MchStoreInfoResult> list() {
        String mchNo = this.resolveMchNo(null);
        return this.listAllByMchNo(mchNo);
    }

    /// 门店列表(运营端按商户号全量查询, 门店管理卡片墙用)
    ///
    /// 过滤逻辑删除, 不分页; 排序由实体继承的 `@OrderBy`(主键) 注解统一注入, 与分页接口保持一致。
    ///
    /// @param mchNo 商户号
    /// @return 该商户全部门店
    public List<MchStoreInfoResult> listAllByMchNo(String mchNo) {
        List<MchStoreInfoResult> resultList = mchStoreInfoManager.findAllByMchNo(mchNo).stream()
                .map(MchStoreInfo::toResult)
                .collect(Collectors.toList());
        this.fillAppName(resultList);
        return resultList;
    }

    /// 门店概况聚合(门店工作台)
    ///
    /// 按门店主键聚合门店默认应用/商户默认应用/本店默认终端与绑定计数(码牌/系统终端);
    /// 应用与终端解析直接查台账展示事实(不做存在性/启用校验抛错, 配置失效时字段留空由前端呈现)。
    /// 开源版裁剪: 收款设备(虚拟设备)域模块独立不在此聚合, 设备计数由前端经分页接口 total 取数。
    public MchStoreOverviewResult overview(Long id) {
        MchStoreInfo mchStore = mchStoreInfoManager.findById(id)
                // 商户: 门店不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        this.checkStore(mchStore);
        MchStoreOverviewResult result = new MchStoreOverviewResult();
        // 门店默认应用(未配置留空, 前端展示「跟随商户默认」)
        if (StrUtil.isNotBlank(mchStore.getAppId())) {
            mchAppInfoManager.findByAppId(mchStore.getAppId()).ifPresent(app -> {
                result.setStoreAppId(app.getAppId());
                result.setStoreAppName(app.getAppName());
            });
        }
        // 商户默认应用
        mchAppInfoManager.findDefaultByMchNo(mchStore.getMchNo()).ifPresent(app -> {
            result.setMchDefaultAppId(app.getAppId());
            result.setMchDefaultAppName(app.getAppName());
        });
        // 本店默认终端(不限启用态, 展示台账事实; 停用的默认终端支付不回落, 前端据此标注)
        terminalDeviceManager.findDefaultByStoreNoAnyStatus(mchStore.getStoreNo()).ifPresent(terminal -> {
            result.setDefaultTerminalNo(terminal.getTerminalNo());
            result.setDefaultTerminalName(terminal.getName());
            result.setDefaultTerminalEnable(terminal.getEnable());
        });
        // 码牌绑定计数(强转 int 前置: 计数为非负值且门店维度量级远小于 int 上限)
        Long qrCodeCount = deviceQrCodeManager.countByField(DeviceQrCode::getStoreNo, mchStore.getStoreNo());
        result.setQrCodeCount(qrCodeCount == null ? 0 : qrCodeCount.intValue());
        // 本店系统终端计数(不限启用态)
        result.setTerminalCount((int) terminalDeviceManager.countByStoreNo(mchStore.getStoreNo()));
        return result;
    }

    /// 设置/清空门店默认应用(门店工作台一级操作, 不走整表单 update)
    ///
    /// 非空应用号须为本商户存在且启用的应用([#validateAppId]); 空白表示清空, 回落跟随商户默认应用。
    ///
    /// @param id    门店主键
    /// @param appId 应用号(空白 = 跟随商户默认应用)
    @Transactional(rollbackFor = Exception.class)
    public void setApp(Long id, String appId) {
        MchStoreInfo mchStore = mchStoreInfoManager.findById(id)
                // 商户: 门店不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        this.checkStore(mchStore);
        this.validateAppId(mchStore.getMchNo(), appId);
        // 显式 set 落库: 清空场景需写入 null, updateById 会忽略 null 字段
        mchStoreInfoManager.lambdaUpdate()
                .set(MchStoreInfo::getAppId, StrUtil.blankToDefault(appId, null))
                .eq(MchStoreInfo::getId, mchStore.getId())
                .update();
    }

    /// 删除
    ///
    /// 默认门店禁止删除(与 [MchAppInfoService#delete] 对齐), 需先调用 [setDefault] 转交默认标记。
    public void delete(Long id) {
        MchStoreInfo mchStore = mchStoreInfoManager.findById(id)
                // 商户: 门店不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        this.checkStore(mchStore);
        if (mchStore.isDefaultStore()) {
            // 商户: 默认门店不可删除, 请先转交默认标记
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.defaultStoreCannotDelete");
        }
        mchStoreInfoManager.deleteById(id);
    }

    /// 生成门店号
    public String generateStoreNo() {
        String storeNo = "S" + RandomUtil.randomNumbers(16);
        for (int i = 0; i < 10; i++) {
            if (!mchStoreInfoManager.existsByStoreNo(storeNo)) {
                return storeNo;
            }
            storeNo = "S" + RandomUtil.randomNumbers(16);
        }
        // 商户: 门店号生成失败
        throw new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.storeNoGenFailed");
    }

    /// 解析商户号, 商户端从上下文获取, 管理端从参数获取
    private String resolveMchNo(String paramMchNo) {
        if (clientCodeService.getClientCode().equals(ClientEnum.MERCHANT.getCode())) {
            return paymentContext.getMchNo();
        }
        if (Objects.isNull(paramMchNo)) {
            // 商户: 数据错误，未发现商户号
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.dataErrorNoMchNo");
        }
        return paramMchNo;
    }

    /// 如果和当前商户不匹配, 抛出错误(商户端校验)
    public void checkStore(MchStoreInfo mchStore) {
        if (clientCodeService.getClientCode().equals(ClientEnum.MERCHANT.getCode())) {
            if (!mchStore.getMchNo().equals(paymentContext.getMchNo())) {
                // 商户: 门店不属于当前商户
                throw new ConfigErrorException("error.payment.merchant.storeNoMatch");
            }
        }
    }

    /// 校验门店默认应用: 非空时须为本商户存在且启用的应用
    ///
    /// 复用 [MerchantContextLoader#resolveApp] 的应用解析(含存在性/启用/归属校验, 异常文案走 i18n),
    /// 空值表示跟随商户默认应用, 不做校验。
    private void validateAppId(String mchNo, String appId) {
        if (StrUtil.isBlank(appId)) {
            return;
        }
        merchantContextLoader.resolveApp(mchNo, appId);
    }

    /// 批量回填门店默认应用名称(一次查询 + Map 映射, 避免 N+1; 应用不可见时留空)
    private void fillAppName(List<MchStoreInfoResult> records) {
        if (CollUtil.isEmpty(records)) {
            return;
        }
        Set<String> appIds = records.stream()
                .map(MchStoreInfoResult::getAppId)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toSet());
        if (CollUtil.isEmpty(appIds)) {
            return;
        }
        Map<String, String> appNameMap = mchAppInfoManager.findAllByFields(MchAppInfo::getAppId, appIds).stream()
                .collect(Collectors.toMap(MchAppInfo::getAppId, MchAppInfo::getAppName, (a, b) -> a));
        records.forEach(record -> record.setAppName(appNameMap.get(record.getAppId())));
    }

    /// 设为默认门店(同商户先清后设)
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(Long id) {
        MchStoreInfo mchStore = mchStoreInfoManager.findById(id)
                // 商户: 门店不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        this.checkStore(mchStore);
        mchStoreInfoManager.clearDefault(mchStore.getMchNo());
        // clearDefault 后重新加载, 避免 version 冲突
        mchStore = mchStoreInfoManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        mchStore.setDefaultStore(true);
        mchStoreInfoManager.updateById(mchStore);
    }

    /// 取消默认门店
    @Transactional(rollbackFor = Exception.class)
    public void clearDefault(Long id) {
        MchStoreInfo mchStore = mchStoreInfoManager.findById(id)
                // 商户: 门店不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        this.checkStore(mchStore);
        mchStore.setDefaultStore(false);
        mchStoreInfoManager.updateById(mchStore);
    }

    /// 下单校验门店: 空则跳过; 非空须存在、归属商户、启用
    ///
    /// @param storeNo 门店号(可空)
    /// @param mchNo   当前下单商户号
    public void validateStoreForPay(String storeNo, String mchNo) {
        if (StrUtil.isBlank(storeNo)) {
            return;
        }
        MchStoreInfo store = mchStoreInfoManager.findByStoreNo(storeNo)
                // 商户: 门店不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        if (StrUtil.isNotBlank(mchNo) && !Objects.equals(store.getMchNo(), mchNo)) {
            // 商户: 门店不属于当前商户
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.payment.merchant.storeNoMatch");
        }
        if (!Objects.equals(StoreStatusEnum.ENABLE.getCode(), store.getStatus())) {
            // 商户: 门店已停用
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.payment.merchant.storeDisabled");
        }
    }

    /// 解析门店号: 非空原样返回; 空则取商户默认门店。
    ///
    /// 与 [cn.daxpay.open.payment.common.context.MerchantContextLoader#resolveApp] 对齐,
    /// 默认门店不存在或已停用时**抛异常阻断下单**(替代早期返回 null 的宽松策略),
    /// 保证订单 storeNo 维度完整。
    ///
    /// 异常分类(便于排查):
    /// - 未传 storeNo 且无默认门店 → `error.payment.merchant.defaultStoreNotConfigured`
    /// - 未传 storeNo 且默认门店已停用 → `error.payment.merchant.defaultStoreDisabled`
    ///
    /// @param mchNo   商户号
    /// @param storeNo 显式门店号(可空)
    /// @return 解析后的门店号
    public String resolveStoreNo(String mchNo, String storeNo) {
        if (StrUtil.isNotBlank(storeNo)) {
            return storeNo;
        }
        if (StrUtil.isBlank(mchNo)) {
            // 商户: 商户未配置默认门店
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.defaultStoreNotConfigured");
        }
        MchStoreInfo defaultStore = mchStoreInfoManager.findDefaultByMchNo(mchNo)
                // 商户: 商户未配置默认门店
                .orElseThrow(() -> new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.defaultStoreNotConfigured"));
        if (!Objects.equals(StoreStatusEnum.ENABLE.getCode(), defaultStore.getStatus())) {
            // 商户: 默认门店已停用, 请先启用或转交默认标记
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.defaultStoreDisabled");
        }
        return defaultStore.getStoreNo();
    }
}
