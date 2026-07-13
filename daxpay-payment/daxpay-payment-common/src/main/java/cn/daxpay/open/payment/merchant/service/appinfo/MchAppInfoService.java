package cn.daxpay.open.payment.merchant.service.appinfo;

import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.iam.service.client.ClientCodeService;
import cn.daxpay.open.platform.core.exception.config.ConfigErrorException;
import cn.daxpay.open.platform.core.exception.config.ConfigNotEnableException;
import cn.daxpay.open.platform.core.exception.config.ConfigNotExistException;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.convert.appinfo.MchAppInfoConvert;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.platform.core.enums.merchant.MerchantStatusEnum;
import cn.daxpay.open.payment.merchant.param.appinfo.MchAppInfoParam;
import cn.daxpay.open.payment.merchant.param.appinfo.MchAppInfoQuery;
import cn.daxpay.open.payment.merchant.result.appinfo.MchAppInfoResult;
import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/// # 商户应用信息管理
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MchAppInfoService {

    private final MerchantInfoManager merchantInfoManager;

    private final MchAppInfoManager mchAppInfoManager;

    private final ClientCodeService clientCodeService;

    private final PaymentContext paymentContext;

    /// 添加应用
    public void add(MchAppInfoParam param) {
        String mchNo;
        if (clientCodeService.getClientCode().equals(ClientEnum.MERCHANT.getCode())) {
            mchNo = paymentContext.getMchNo();
        } else {
            mchNo = param.getMchNo();
        }
        if (mchNo == null) {
            // 商户: 数据错误，未发现商户号
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.dataErrorNoMchNo");
        }
        MerchantInfo merchant = merchantInfoManager.findByMchNo(mchNo)
                // 商户: 商户不存在
                .orElseThrow(() -> new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.mchNotExist"));
        param.setMchNo(mchNo);
        // 新建应用默认为非默认，需通过 setDefault 或编辑时手动指定
        param.setDefaultApp(false);
        MchAppInfo entity = MchAppInfoConvert.CONVERT.toEntity(param);
        // 生成应用号
        entity.setAppId(this.generateAppId());
        entity.setMchNo(mchNo);
        mchAppInfoManager.save(entity);
    }

    /// 修改
    @Transactional(rollbackFor = Exception.class)
    public void update(MchAppInfoParam param) {
        var mchApp = mchAppInfoManager.findById(param.getId())
                // 商户: 商户应用不存在
                .orElseThrow(() -> new ConfigNotExistException("error.payment.merchant.mchAppNotFound"));
        boolean defaultApp = mchApp.isDefaultApp();
        this.checkApp(mchApp);
        MchAppInfoConvert.CONVERT.copy(param, mchApp);
        mchAppInfoManager.updateById(mchApp);
        if (!Objects.equals(defaultApp, param.isDefaultApp())) {
            if (param.isDefaultApp()) {
                this.setDefault(mchApp.getId());
            } else {
                this.clearDefault(mchApp.getId());
            }
        }
    }

    /// 设为默认
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(Long id) {
        // 先清除默认应用
        MchAppInfo mchApp = mchAppInfoManager.findById(id)
                // 商户: 商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.mchAppNotFound"));
        mchAppInfoManager.clearDefault(mchApp.getMchNo());
        // 已经更新, 需要重新查询
        mchApp = mchAppInfoManager.findById(id)
                // 商户: 商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.mchAppNotFound"));
        mchApp.setDefaultApp(true);
        mchAppInfoManager.updateById(mchApp);
    }

    /// 清除默认配置
    @Transactional(rollbackFor = Exception.class)
    public void clearDefault(Long id) {
        MchAppInfo mchApp = mchAppInfoManager.findById(id)
                // 商户: 商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.mchAppNotFound"));
        mchApp.setDefaultApp(false);
        mchAppInfoManager.updateById(mchApp);
    }

    /// 分页
    public PageResult<MchAppInfoResult> page(PageParam pageParam, MchAppInfoQuery query) {
        return MpUtil.toPageResult(mchAppInfoManager.page(pageParam, query));
    }

    /// 商户应用列表
    public List<MchAppInfoResult> list() {
        String mchNo = paymentContext.getMchNo();
        return mchAppInfoManager.findAllByMchNo(mchNo).stream()
                .map(MchAppInfo::toResult)
                .collect(Collectors.toList());
    }

    /// 根据应用AppId获取应用详情
    public MchAppInfoResult findByAppId(String appId) {
        MchAppInfo mchApp = mchAppInfoManager.findByAppId(appId)
                // 商户: 商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.mchAppNotFound"));
        this.checkApp(mchApp);
        return mchApp.toResult();
    }

    /// 获取单条
    public MchAppInfoResult findById(Long id) {
        var mchApp = mchAppInfoManager.findById(id)
                // 商户: 商户应用不存在
                .orElseThrow(() -> new ConfigNotExistException("error.payment.merchant.mchAppNotFound"));
        this.checkApp(mchApp);
        return mchApp.toResult();
    }

    /// 启用下拉列表, 需要应用和商户都是启用状态
    public List<LabelValue> dropdownByEnable(String mchNo) {
        if (clientCodeService.getClientCode().equals(ClientEnum.MERCHANT.getCode())) {
            mchNo = paymentContext.getMchNo();
        }
        // 判断商户状态
        MerchantInfo merchantInfo = merchantInfoManager.findByMchNo(mchNo)
                // 商户: 商户不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.mchNotExist"));
        if (!Objects.equals(merchantInfo.getStatus(), MerchantStatusEnum.ENABLE.getCode())) {
            throw new ConfigNotEnableException(CommonCode.FAIL_CODE, "pay.error.assist.mchNotEnabled");
        }
        // 查询启用的应用
        return mchAppInfoManager.findAllByMchNoAndEnable(mchNo).stream()
                .map(o -> new LabelValue(o.getAppName(), o.getAppId()))
                .collect(Collectors.toList());
    }

    /// 查询商户默认应用号
    public String findDefaultAppId(String mchNo) {
        return mchAppInfoManager.findDefaultByMchNo(mchNo)
                .map(MchAppInfo::getAppId)
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.defaultAppNotConfigured"));
    }

    /// 下拉列表, 不判断应用和商户的状态
    public List<LabelValue> dropdown(String mchNo) {
        if (clientCodeService.getClientCode().equals(ClientEnum.MERCHANT.getCode())) {
            mchNo = paymentContext.getMchNo();
        }
        return mchAppInfoManager.findAllByMchNo(mchNo).stream()
                .map(o -> new LabelValue(o.getAppName(), o.getAppId()))
                .collect(Collectors.toList());
    }

    /// 删除
    public void delete(Long id) {
        MchAppInfo mchApp = mchAppInfoManager.findById(id)
                // 商户: 商户应用不存在
                .orElseThrow(() -> new ConfigNotExistException("error.payment.merchant.mchAppNotFound"));
        this.checkApp(mchApp);
        mchAppInfoManager.deleteById(id);
    }

    /// 生成应用号
    public String generateAppId() {
        String appId = "A" + RandomUtil.randomNumbers(16);
        for (int i = 0; i < 10; i++) {
            if (!mchAppInfoManager.existsByAppId(appId)) {
                return appId;
            }
            appId = "A" + RandomUtil.randomNumbers(16);
        }
        // 商户: 应用号生成失败
        throw new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.appNoGenFailed");
    }

    /// 如果和商户不匹配, 抛出错误
    public void checkApp(MchAppInfo mchApp) {
        if (clientCodeService.getClientCode().equals(ClientEnum.MERCHANT.getCode())) {
            if (!mchApp.getMchNo().equals(paymentContext.getMchNo())) {
                // 商户: 商户应用不匹配
                throw new ConfigErrorException("error.payment.merchant.mchAppNoMatch");
            }
        }
    }
}
