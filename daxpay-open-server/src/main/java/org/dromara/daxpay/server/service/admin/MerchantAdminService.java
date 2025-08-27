package org.dromara.daxpay.server.service.admin;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.iam.dao.role.RoleManager;
import cn.bootx.platform.iam.entity.role.Role;
import cn.bootx.platform.iam.entity.user.UserInfo;
import cn.bootx.platform.iam.param.user.UserInfoParam;
import cn.bootx.platform.iam.service.client.ClientCodeService;
import cn.bootx.platform.iam.service.upms.UserRoleService;
import cn.bootx.platform.iam.service.user.UserAdminService;
import org.dromara.daxpay.core.enums.MerchantNotifyTypeEnum;
import org.dromara.daxpay.core.enums.MerchantStatusEnum;
import org.dromara.daxpay.core.enums.SignTypeEnum;
import org.dromara.daxpay.core.exception.ConfigNotExistException;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.server.enums.UserRoleEnum;
import org.dromara.daxpay.service.merchant.convert.info.MerchantConvert;
import org.dromara.daxpay.service.merchant.dao.app.MchAppManager;
import org.dromara.daxpay.service.merchant.dao.info.MerchantManager;
import org.dromara.daxpay.service.merchant.dao.info.MerchantUserManager;
import org.dromara.daxpay.service.merchant.entity.app.MchApp;
import org.dromara.daxpay.service.merchant.entity.info.Merchant;
import org.dromara.daxpay.service.merchant.entity.info.MerchantUser;
import org.dromara.daxpay.service.merchant.param.info.MerchantCreateParam;
import org.dromara.daxpay.service.merchant.param.info.MerchantParam;
import org.dromara.daxpay.service.merchant.param.info.MerchantQuery;
import org.dromara.daxpay.service.merchant.result.info.MerchantResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;

/**
 * 商户服务类
 * @author xxm
 * @since 2024/5/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantAdminService {
    private final MerchantManager merchantManager;
    private final MchAppManager mchAppManager;
    private final UserAdminService userAdminService;
    private final RoleManager roleManager;
    private final UserRoleService userRoleService;
    private final MerchantUserManager merchantUserManager;
    private final ClientCodeService clientCodeService;

    /**
     * 添加商户
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(MerchantCreateParam param) {
        var merchant = MerchantConvert.CONVERT.toEntity(param);
        merchant.setMchNo(this.getMchNo());
        merchant.setStatus(MerchantStatusEnum.ENABLE.getCode());
        // 创建商户管理员
        this.createMerchantAdmin(param,  merchant);
        merchantManager.save(merchant);
        // 是否创建创建默认应用
        if (param.getCreateDefaultApp()){
            MchApp mchApp = new MchApp()
                    .setAppName("默认应用")
                    .setLimitAmount(BigDecimal.valueOf(2000))
                    .setReqTimeout(false)
                    .setReqTimeoutSecond(30)
                    .setOrderTimeout(30)
                    .setSignType(SignTypeEnum.HMAC_SHA256.getCode())
                    .setSignSecret(RandomUtil.randomString(32))
                    .setReqSign(true)
                    .setNotifyType(MerchantNotifyTypeEnum.NONE.getCode())
                    .setStatus(MerchantStatusEnum.ENABLE.getCode());
            mchApp.setAppId(this.generateAppId())
                    .setMchNo(merchant.getMchNo());
            mchAppManager.save(mchApp);
        }
    }

    /**
     * 修改
     */
    public void update(MerchantParam param) {
        Merchant merchant = merchantManager.findById(param.getId()).orElseThrow(DataNotExistException::new);
        BeanUtil.copyProperties(param, merchant, CopyOptions.create().ignoreNullValue());
        merchantManager.updateById(merchant);
    }

    /**
     * 分页
     */
    public PageResult<MerchantResult> page(PageParam pageParam, MerchantQuery query) {
        return MpUtil.toPageResult(merchantManager.page(pageParam,query));
    }

    /**
     * 获取单条
     */
    public MerchantResult findById(Long id) {
        return merchantManager.findById(id).map(Merchant::toResult).orElseThrow(DataNotExistException::new);
    }

    /**
     * 删除
     */
    public void delete(Long id) {
        if (true){
            throw new OperationFailException("商户不允许删除");
        }

        Merchant merchant = merchantManager.findById(id).orElseThrow(DataNotExistException::new);
        // 创建管理员后不可以被删除
        if (merchant.isAdministrator()){
            throw new ValidationFailedException("已存在关联商户管理员用户, 不允许删除");
        }
        // 判断是否存在应用APP, 存在不允许删除
        if (mchAppManager.existByMchNo(merchant.getMchNo())){
            throw new ValidationFailedException("商户下存在应用, 不允许删除");
        }
        merchantManager.deleteById(id);
    }

    /**
     * 创建商户管理员
     */
    private void createMerchantAdmin(MerchantCreateParam param, Merchant merchant){
        // 创建用户
        UserInfoParam userInfoParam = new UserInfoParam();
        BeanUtil.copyProperties(param, userInfoParam);
        UserInfo userInfo = userAdminService.add(userInfoParam);
        // 查询普通商户管理员角色
        Role role = roleManager.findByCode(UserRoleEnum.MERCHANT_ROLE.getCode())
                .orElseThrow(() -> new ConfigNotExistException("商户管理员角色不存在, 请检查"));
        // 分配角色
        userRoleService.saveAssign(userInfo.getId(), Collections.singletonList(role.getId()),true);
        // 创建商户绑定关系
        merchantUserManager.save(new MerchantUser(userInfo.getId(), merchant.getMchNo(),true));
        // 商户信息更新
        merchant.setAdministrator(true)
                .setAdminUserId(userInfo.getId());
        merchantManager.updateById(merchant);
    }

    /**
     * 生成商户号
     */
    private String getMchNo(){
        String mchNo = "M" + System.currentTimeMillis();
        for (int i = 0; i < 10; i++){
            if (!merchantManager.existedByField(Merchant::getMchNo, mchNo)){
                return mchNo;
            }
            mchNo = "M" + System.currentTimeMillis();
        }
        throw new BizException("商户号生成失败");
    }

    /**
     * 生成应用号
     */
    private String generateAppId() {
        String appId = "A"+RandomUtil.randomNumbers(16);
        for (int i = 0; i < 10; i++){
            if (!mchAppManager.existsByAppId(appId)){
                return appId;
            }
            appId = "A"+ RandomUtil.randomNumbers(16);
        }
        throw new BizException("应用号生成失败");
    }
}
