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
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantUserManager;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.payment.merchant.entity.info.MerchantUser;
import cn.daxpay.open.platform.core.enums.merchant.MerchantStatusEnum;
import cn.daxpay.open.payment.merchant.param.info.MerchantInfoParam;
import cn.daxpay.open.payment.merchant.param.info.MerchantInfoQuery;
import cn.daxpay.open.payment.merchant.param.info.MerchantRegisterParam;
import cn.daxpay.open.payment.merchant.result.info.MerchantInfoResult;
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

    /// 删除
    public void delete(Long id) {
        // 商户: 商户不允许删除
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
    public List<LabelValue> dropdown() {
        List<MerchantInfo> merchants = merchantInfoManager.findAllByEnable();
        return merchants.stream()
                .map(m -> new LabelValue(m.getMchName(), m.getMchNo()))
                .collect(Collectors.toList());
    }

}
