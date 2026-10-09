package cn.daxpay.open.payment.merchant.service.wxverify;

import cn.daxpay.open.payment.merchant.convert.wxverify.WxDomainVerifyConvert;
import cn.daxpay.open.payment.merchant.dao.wxverify.WxDomainVerifyManager;
import cn.daxpay.open.payment.merchant.entity.wxverify.WxDomainVerify;
import cn.daxpay.open.payment.merchant.param.wxverify.WxDomainVerifyParam;
import cn.daxpay.open.payment.merchant.param.wxverify.WxDomainVerifyQuery;
import cn.daxpay.open.payment.merchant.param.wxverify.WxDomainVerifyUploadParam;
import cn.daxpay.open.payment.merchant.result.wxverify.WxDomainVerifyResult;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.common.translate.service.TransService;
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
import java.util.Objects;

/// # 域名校验文件管理
///
/// 层级语义：
/// - 平台级（运营端支付配置上传）：平台公众号/小程序域名验证 + 平台收银台等小程序的
///   「扫普通链接二维码」规则校验文件，允许 MP_verify_*.txt 与随机名 .txt/.html；
/// - 商户级（商户端自助 / 运营代传 / App 端代传）：商户自己公众号/小程序的域名验证（微信授权回调），
///   仅允许 MP_verify_*.txt。
/// 上传方式：前端读取文件内容后以 JSON 提交（fileName + fileContent），不走 multipart。
@Slf4j
@Service
@RequiredArgsConstructor
public class WxDomainVerifyService {

    /// 微信命名的域名校验文件规则：MP_verify_ + 字母数字 + .txt（验证码取随机段）
    private static final Pattern WX_FILE_NAME_PATTERN = Pattern.compile("^MP_verify_([a-zA-Z0-9]+)\\.txt$");

    /// 通用校验文件名规则：字母/数字/下划线 + .txt/.html（仅平台级允许）
    /// 微信「扫普通链接二维码」随机名 .txt 与支付宝「关联普通二维码」32位hex .html，验证码取文件名主体
    private static final Pattern GENERIC_FILE_NAME_PATTERN = Pattern.compile("^([a-zA-Z0-9_]{1,60})\\.(txt|html)$");

    /// 文件内容最大长度（微信验证文件通常为几十字节的随机字符串）
    private static final int MAX_CONTENT_LENGTH = 200;

    /// 平台级验证文件的商户号占位值
    private static final String PLATFORM_MCH_NO = "0";

    private final WxDomainVerifyManager wxDomainVerifyManager;
    private final TransService transService;

    /// 商户级上传单个验证文件（商户端自助 / 运营代传 / App 端代传共用，仅允许微信 MP_verify_*.txt）
    @Transactional(rollbackFor = Exception.class)
    public WxDomainVerifyResult upload(WxDomainVerifyUploadParam param, String mchNo) {
        this.assertMchNo(mchNo);
        WxDomainVerify entity = this.build(param, false);
        entity.setMchNo(mchNo);
        entity.setPlatform(false);
        this.checkDuplicate(entity.getVerifyCode());
        wxDomainVerifyManager.save(entity);
        return entity.toResult();
    }

    /// 平台级上传单个验证文件（允许微信 MP_verify 与小程序二维码随机名/支付宝 html 等通用形态）
    @Transactional(rollbackFor = Exception.class)
    public WxDomainVerifyResult uploadPlatform(WxDomainVerifyUploadParam param) {
        WxDomainVerify entity = this.build(param, true);
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
                // 域名校验文件不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.wxVerifyNotFound"));
        WxDomainVerifyConvert.CONVERT.copy(param, entity);
        wxDomainVerifyManager.updateById(entity);
    }

    /// 分页
    public PageResult<WxDomainVerifyResult> page(PageParam pageParam, WxDomainVerifyQuery query) {
        PageResult<WxDomainVerifyResult> pageResult = MpUtil.toPageResult(wxDomainVerifyManager.page(pageParam, query));
        // 翻译商户名称(mchNo -> mchName; 平台级无 mchNo 则跳过)
        transService.translate(pageResult);
        return pageResult;
    }

    /// 详情
    public WxDomainVerifyResult findById(Long id) {
        WxDomainVerify entity = wxDomainVerifyManager.findById(id)
                // 域名校验文件不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.wxVerifyNotFound"));
        WxDomainVerifyResult result = entity.toResult();
        // 翻译商户名称
        transService.translate(result);
        return result;
    }

    /// 删除
    public void delete(Long id) {
        WxDomainVerify entity = wxDomainVerifyManager.findById(id)
                // 域名校验文件不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.wxVerifyNotFound"));
        wxDomainVerifyManager.deleteById(id);
    }

    /// 根据完整文件名获取文件内容（供网关响应使用，忽略租户）
    public Optional<String> findContentByFileName(String fileName) {
        return wxDomainVerifyManager.findByFileNameNotTenant(fileName)
                .map(WxDomainVerify::getFileContent);
    }

    /// 校验参数并构建实体（不设置 mchNo/platform，由调用方补充；platform 决定允许的文件名形态）
    private WxDomainVerify build(WxDomainVerifyUploadParam param, boolean platform) {
        String fileName = param.getFileName();
        if (Objects.isNull(fileName) || fileName.isBlank()) {
            // 域名校验文件: 文件名为空
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.wxVerifyFileNameEmpty");
        }
        // 微信命名的域名校验文件取随机段为验证码; 平台级其余形态(微信扫普通链接随机名/支付宝关联普通二维码)取文件名主体
        Matcher wxMatcher = WX_FILE_NAME_PATTERN.matcher(fileName);
        String verifyCode;
        if (wxMatcher.matches()) {
            verifyCode = wxMatcher.group(1);
        } else if (platform) {
            Matcher genericMatcher = GENERIC_FILE_NAME_PATTERN.matcher(fileName);
            if (!genericMatcher.matches()) {
                // 域名校验文件: 文件名格式不正确
                throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.wxVerifyFileNameInvalid");
            }
            verifyCode = genericMatcher.group(1);
        } else {
            // 域名校验文件: 商户级仅支持微信下载的 MP_verify_*.txt
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.wxVerifyMchOnlyMp");
        }
        String content = Objects.isNull(param.getFileContent()) ? "" : param.getFileContent().trim();
        if (content.isEmpty()) {
            // 域名校验文件: 文件内容为空
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.wxVerifyFileContentEmpty");
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            // 域名校验文件: 文件内容过长
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
            // 域名校验文件: 验证码已存在
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.wxVerifyCodeDuplicate");
        }
    }

    /// 校验商户号非空（运营代商户上传时必须指定）
    private void assertMchNo(String mchNo) {
        if (Objects.isNull(mchNo) || mchNo.isBlank()) {
            // 商户: 数据错误，未发现商户号
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.dataErrorNoMchNo");
        }
    }

}
