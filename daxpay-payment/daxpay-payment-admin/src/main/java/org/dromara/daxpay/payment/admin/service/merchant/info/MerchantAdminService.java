package org.dromara.daxpay.payment.admin.service.merchant.info;

import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.exception.BizException;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.exception.ValidationFailedException;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.code.CommonCode;
import org.dromara.daxpay.platform.core.rest.dto.LabelValue;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.iam.dao.role.RoleManager;
import org.dromara.daxpay.platform.iam.entity.role.Role;
import org.dromara.daxpay.platform.iam.entity.user.UserInfo;
import org.dromara.daxpay.platform.iam.param.user.UserInfoParam;
import org.dromara.daxpay.platform.iam.service.client.ClientCodeService;
import org.dromara.daxpay.platform.iam.service.upms.UserRoleService;
import org.dromara.daxpay.platform.iam.service.user.UserAdminService;
import org.dromara.daxpay.payment.common.context.PaymentContext;

import org.dromara.daxpay.platform.core.exception.config.ConfigNotExistException;
import org.dromara.daxpay.platform.core.exception.operation.OperationFailException;
import org.dromara.daxpay.payment.merchant.convert.info.MerchantInfoConvert;
import org.dromara.daxpay.payment.merchant.dao.info.MerchantInfoManager;
import org.dromara.daxpay.payment.merchant.dao.info.MerchantUserManager;
import org.dromara.daxpay.payment.merchant.entity.info.MerchantInfo;
import org.dromara.daxpay.payment.merchant.entity.info.MerchantUser;
import org.dromara.daxpay.platform.core.enums.merchant.MerchantStatusEnum;
import org.dromara.daxpay.payment.merchant.param.info.MerchantInfoParam;
import org.dromara.daxpay.payment.merchant.param.info.MerchantInfoQuery;
import org.dromara.daxpay.payment.merchant.param.info.MerchantRegisterParam;
import org.dromara.daxpay.payment.merchant.result.info.MerchantInfoResult;
import org.dromara.daxpay.platform.common.translate.service.TransService;
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
import org.dromara.daxpay.platform.core.code.CommonCode;

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
    private final PaymentContext apiContext;
    private final TransService transService;

    /// 添加商户
    @Transactional(rollbackFor = Exception.class)
    public void add(MerchantRegisterParam param) {
        var merchant = MerchantInfoConvert.CONVERT.toEntity(param);
        merchant.setMchNo(this.getMchNo());


        merchant.setStatus(MerchantStatusEnum.ENABLE.getCode());
        // 创建商户管理员
        this.createMerchantAdmin(param,  merchant);
        merchantInfoManager.save(merchant);
    }

    /// 创建商户管理员
    public void createMerchantAdmin(MerchantRegisterParam param, MerchantInfo merchant) {
        // 校验服务商下商户登录账号是否已存在
        if (this.existsAccountByIsvNo(param.getAccount(), merchant.getIsvNo())) {
            throw new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.accountExistsInIsv");
        }
        // 校验服务商下商户手机号是否已存在
        if (this.existsPhoneByIsvNo(param.getPhone(), merchant.getIsvNo())) {
            throw new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.phoneUsedInIsv");
        }
        // 创建用户（跳过通用重复校验，因为已做服务商维度校验）
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
        // 查询商户管理员角色
        // 商户管理员角色不存在
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

    /// 删除
    public void delete(Long id) {
        // 商户不允许删除
        throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.mchNotAllowDelete");
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
        // 商户号生成失败
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
    public List<LabelValue> dropdown() {
        List<MerchantInfo> merchants = merchantInfoManager.findAllByEnable();
        return merchants.stream()
                .map(m -> new LabelValue(m.getMchName(), m.getMchNo()))
                .collect(Collectors.toList());
    }

    /// 校验服务商下商户登录账号是否已存在
    public boolean existsAccountByIsvNo(String account, String isvNo) {
        MPJLambdaWrapper<MerchantUser> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(MerchantUser.class)
                .innerJoin(UserInfo.class, UserInfo::getId, MerchantUser::getUserId)
                .innerJoin(MerchantInfo.class, MerchantInfo::getMchNo, MerchantUser::getMchNo)
                .eq(UserInfo::getAccount, account)
                .eq(UserInfo::getClientCode, ClientEnum.MERCHANT.getCode())
                .eq(MerchantInfo::getIsvNo, isvNo)
                .last("LIMIT 1");
        return merchantUserManager.selectJoinOne(MerchantUser.class, wrapper) != null;
    }

    /// 校验服务商下商户手机号是否已存在
    public boolean existsPhoneByIsvNo(String phone, String isvNo) {
        if (StrUtil.isBlank(phone)) {
            return false;
        }
        MPJLambdaWrapper<MerchantUser> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(MerchantUser.class)
                .innerJoin(UserInfo.class, UserInfo::getId, MerchantUser::getUserId)
                .innerJoin(MerchantInfo.class, MerchantInfo::getMchNo, MerchantUser::getMchNo)
                .eq(UserInfo::getPhone, phone)
                .eq(UserInfo::getClientCode, ClientEnum.MERCHANT.getCode())
                .eq(MerchantInfo::getIsvNo, isvNo)
                .last("LIMIT 1");
        return merchantUserManager.selectJoinOne(MerchantUser.class, wrapper) != null;
    }

    /// 校验服务商下商户邮箱是否已存在
    public boolean existsEmailByIsvNo(String email, String isvNo) {
        if (StrUtil.isBlank(email)) {
            return false;
        }
        MPJLambdaWrapper<MerchantUser> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(MerchantUser.class)
                .innerJoin(UserInfo.class, UserInfo::getId, MerchantUser::getUserId)
                .innerJoin(MerchantInfo.class, MerchantInfo::getMchNo, MerchantUser::getMchNo)
                .eq(UserInfo::getEmail, email)
                .eq(UserInfo::getClientCode, ClientEnum.MERCHANT.getCode())
                .eq(MerchantInfo::getIsvNo, isvNo)
                .last("LIMIT 1");
        return merchantUserManager.selectJoinOne(MerchantUser.class, wrapper) != null;
    }
}
