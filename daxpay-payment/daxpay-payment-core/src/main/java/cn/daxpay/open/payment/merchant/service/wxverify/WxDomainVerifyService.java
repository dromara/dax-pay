package cn.daxpay.open.payment.merchant.service.wxverify;

import cn.daxpay.open.payment.merchant.convert.wxverify.WxDomainVerifyConvert;
import cn.daxpay.open.payment.merchant.dao.wxverify.WxDomainVerifyManager;
import cn.daxpay.open.payment.merchant.entity.wxverify.WxDomainVerify;
import cn.daxpay.open.payment.merchant.param.wxverify.WxDomainVerifyParam;
import cn.daxpay.open.payment.merchant.param.wxverify.WxDomainVerifyQuery;
import cn.daxpay.open.payment.merchant.param.wxverify.WxDomainVerifyUploadParam;
import cn.daxpay.open.payment.merchant.result.wxverify.WxDomainVerifyResult;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/// # 微信域名验证文件管理
///
/// 当前系统仅有 admin 端，商户级验证文件由运营在商户工作台代为管理（通过 mchNo 指定商户），
/// 平台级验证文件由运营在支付配置下管理。
/// 上传方式：前端读取 .txt 文件内容后以 JSON 提交（fileName + fileContent），不走 multipart。
@Slf4j
@Service
@RequiredArgsConstructor
public class WxDomainVerifyService {

    /// 微信域名校验文件名规则：MP_verify_ + 字母数字 + .txt
    private static final Pattern FILE_NAME_PATTERN = Pattern.compile("^MP_verify_([a-zA-Z0-9]+)\\.txt$");

    /// 文件内容最大长度（微信验证文件通常为几十字节的随机字符串）
    private static final int MAX_CONTENT_LENGTH = 200;

    /// 平台级验证文件的商户号占位值
    private static final String PLATFORM_MCH_NO = "0";

    private final WxDomainVerifyManager wxDomainVerifyManager;

    /// 商户级上传单个验证文件（运营代指定商户上传）
    @Transactional(rollbackFor = Exception.class)
    public WxDomainVerifyResult upload(WxDomainVerifyUploadParam param, String mchNo) {
        this.assertMchNo(mchNo);
        WxDomainVerify entity = this.build(param);
        entity.setMchNo(mchNo);
        entity.setPlatform(false);
        this.checkDuplicate(entity.getVerifyCode());
        wxDomainVerifyManager.save(entity);
        return entity.toResult();
    }

    /// 平台级上传单个验证文件（平台自己的公众号/小程序验证文件）
    @Transactional(rollbackFor = Exception.class)
    public WxDomainVerifyResult uploadPlatform(WxDomainVerifyUploadParam param) {
        WxDomainVerify entity = this.build(param);
        entity.setMchNo(PLATFORM_MCH_NO);
        entity.setPlatform(true);
        this.checkDuplicate(entity.getVerifyCode());
        wxDomainVerifyManager.save(entity);
        return entity.toResult();
    }

    /// 修改备注等元数据
    @Transactional(rollbackFor = Exception.class)
    public void update(WxDomainVerifyParam param) {
        WxDomainVerify entity = wxDomainVerifyManager.findById(param.getId())
                // 微信域名验证文件不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.wxVerifyNotFound"));
        WxDomainVerifyConvert.CONVERT.copy(param, entity);
        wxDomainVerifyManager.updateById(entity);
    }

    /// 分页
    public PageResult<WxDomainVerifyResult> page(PageParam pageParam, WxDomainVerifyQuery query) {
        return MpUtil.toPageResult(wxDomainVerifyManager.page(pageParam, query));
    }

    /// 详情
    public WxDomainVerifyResult findById(Long id) {
        WxDomainVerify entity = wxDomainVerifyManager.findById(id)
                // 微信域名验证文件不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.wxVerifyNotFound"));
        return entity.toResult();
    }

    /// 删除
    public void delete(Long id) {
        WxDomainVerify entity = wxDomainVerifyManager.findById(id)
                // 微信域名验证文件不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.wxVerifyNotFound"));
        wxDomainVerifyManager.deleteById(id);
    }

    /// 根据验证码获取文件内容（供网关响应使用，忽略租户）
    public Optional<String> findContentByVerifyCode(String verifyCode) {
        return wxDomainVerifyManager.findByVerifyCodeNotTenant(verifyCode)
                .map(WxDomainVerify::getFileContent);
    }

    /// 校验参数并构建实体（不设置 mchNo/platform，由调用方补充）
    private WxDomainVerify build(WxDomainVerifyUploadParam param) {
        String fileName = param.getFileName();
        if (fileName == null || fileName.isBlank()) {
            // 微信域名验证文件: 文件名为空
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.wxVerifyFileNameEmpty");
        }
        Matcher matcher = FILE_NAME_PATTERN.matcher(fileName);
        if (!matcher.matches()) {
            // 微信域名验证文件: 文件名格式不正确
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.wxVerifyFileNameInvalid");
        }
        String verifyCode = matcher.group(1);
        String content = param.getFileContent() == null ? "" : param.getFileContent().trim();
        if (content.isEmpty()) {
            // 微信域名验证文件: 文件内容为空
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.wxVerifyFileContentEmpty");
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            // 微信域名验证文件: 文件内容过长
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.wxVerifyFileContentTooLong");
        }
        return new WxDomainVerify()
                .setFileName(fileName)
                .setVerifyCode(verifyCode)
                .setFileContent(content)
                .setRemark(param.getRemark());
    }

    /// 查重，存在则抛异常
    private void checkDuplicate(String verifyCode) {
        if (wxDomainVerifyManager.existsByVerifyCode(verifyCode)) {
            // 微信域名验证文件: 验证码已存在
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.wxVerifyCodeDuplicate");
        }
    }

    /// 校验商户号非空（运营代商户上传时必须指定）
    private void assertMchNo(String mchNo) {
        if (mchNo == null || mchNo.isBlank()) {
            // 商户: 数据错误，未发现商户号
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.dataErrorNoMchNo");
        }
    }

}
