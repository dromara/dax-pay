package org.dromara.daxpay.payment.device.service.qrcode;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.bootx.platform.common.mybatisplus.base.MpRealDelEntity;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.payment.common.entity.config.PlatformUrlConfig;
import org.dromara.daxpay.payment.common.exception.OperationFailException;
import org.dromara.daxpay.payment.common.service.config.PlatformConfigService;
import org.dromara.daxpay.payment.common.util.PayUtil;
import org.dromara.daxpay.payment.device.convert.qrcode.CashierCodeConvert;
import org.dromara.daxpay.payment.device.dao.qrcode.info.CashierCodeManager;
import org.dromara.daxpay.payment.device.dao.qrcode.template.CashierCodeTemplateManager;
import org.dromara.daxpay.payment.device.entity.qrcode.info.CashierCode;
import org.dromara.daxpay.payment.device.param.commom.AssignMerchantParam;
import org.dromara.daxpay.payment.device.param.qrcode.info.*;
import org.dromara.daxpay.payment.device.result.qrcode.info.CashierCodeExport;
import org.dromara.daxpay.payment.device.result.qrcode.info.CashierCodeResult;
import org.dromara.daxpay.payment.merchant.dao.app.MchAppManager;
import org.dromara.daxpay.payment.merchant.dao.info.MerchantManager;
import org.dromara.daxpay.payment.merchant.entity.info.Merchant;
import org.dromara.daxpay.payment.merchant.local.MchContextLocal;
import org.dromara.daxpay.payment.unipay.enums.CashierAmountTypeEnum;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.IntStream;

/**
 * 收款码牌服务
 * @author xxm
 * @since 2025/7/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CashierCodeService {
    private final CashierCodeManager cashierCodeManager;
    private final CashierCodeTemplateManager cashierCodeTemplateManager;
    private final MerchantManager merchantManager;
    private final MchAppManager mchAppManager;
    private final PlatformConfigService platformConfigService;

    /**
     * 分页
     */
    public PageResult<CashierCodeResult> page(PageParam pageParam, CashierCodeQuery query){
        return MpUtil.toPageResult(cashierCodeManager.page(pageParam,query));
    }

    /**
     * 查询详情
     */
    public CashierCodeResult findById(Long id){
        return cashierCodeManager.findById(id).map(CashierCode::toResult)
                .orElseThrow(() -> new DataNotExistException("收银码牌不存在"));
    }

    /**
     * 批量创建空白码牌
     */
    @Transactional(rollbackFor = Exception.class)
    public void createBatch(CashierCodeBatchParam param){
        // 判断批次号是否已存在
        if (cashierCodeManager.existedByBatchNo(param.getBatchNo())){
            throw new ValidationFailedException("批次号已存在");
        }
        if (Objects.nonNull(param.getTemplateId())){
            if (!cashierCodeTemplateManager.existedById(param.getTemplateId())){
                throw new ValidationFailedException("码牌模板不存在");
            }
        }
        // 生成未绑定码牌
        var cashierCodes = IntStream.range(0, param.getCount())
                .mapToObj(i -> {
                    var entity = CashierCodeConvert.CONVERT.toEntity(param);
                    entity.setCode(param.getBatchNo() + String.format("%03d", i+1));
                    return entity;
                })
                .toList();
        cashierCodeManager.saveAll(cashierCodes);
    }

    /**
     * 更新码牌信息
     */
    public void update(CashierCodeUpdateParam param){
        var cashierCode = cashierCodeManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("收款码牌不存在"));
        if (Objects.nonNull(param.getTemplateId())){
            if (!cashierCodeTemplateManager.existedById(param.getTemplateId())){
                throw new ValidationFailedException("码牌模板不存在");
            }
        }
        // 如果是任意金额类型, 去除金额值
        if (Objects.equals(CashierAmountTypeEnum.RANDOM.getCode(), param.getAmountType())){
            param.setAmount(null);
        }
        CashierCodeConvert.CONVERT.copy(param, cashierCode);
        cashierCodeManager.updateById(cashierCode);
    }

    /**
     * 绑定商户和应用
     */
    @Transactional(rollbackFor = Exception.class)
    public void bindMerchant(AssignMerchantParam param){
        String isvNo = null;
        if (StrUtil.isNotBlank(param.getMchNo())){
            var merchant = merchantManager.findByMchNo(param.getMchNo()).orElseThrow(() -> new ValidationFailedException("商户不存在"));
            isvNo = merchant.getIsvNo();
            if (StrUtil.isNotBlank(param.getAppId())){
                var app = mchAppManager.findByAppId(param.getAppId()).orElseThrow(() -> new ValidationFailedException("应用不存在"));
                if (!Objects.equals(merchant.getMchNo(), app.getMchNo())){
                    throw new ValidationFailedException("商户和应用不匹配");
                }
            }
        } else {
            param.setAppId(null);
        }
        cashierCodeManager.lambdaUpdate()
                .set(CashierCode::getMchNo, param.getMchNo())
                .set(CashierCode::getAppId, param.getAppId())
                .set(CashierCode::getIsvNo, isvNo)
                .in(CashierCode::getId, param.getIds())
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
    }


    /**
     * 商户解绑
     */
    @Transactional(rollbackFor = Exception.class)
    public void unbindMerchant(AssignMerchantParam param){
        cashierCodeManager.lambdaUpdate()
                .set(CashierCode::getMchNo, null)
                .set(CashierCode::getAppId, null)
                .in(CashierCode::getId, param.getIds())
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();

    }

    /**
     * 绑定应用
     */
    @Transactional(rollbackFor = Exception.class)
    public void bindApp(AssignMerchantParam param){
        if (StrUtil.isNotBlank(param.getAppId())){
            var app = mchAppManager.findByAppId(param.getAppId()).orElseThrow(() -> new ValidationFailedException("应用不存在"));
            if (!Objects.equals(MchContextLocal.getMchNo(), app.getMchNo())){
                throw new ValidationFailedException("商户和应用不匹配");
            }
        }
        cashierCodeManager.lambdaUpdate()
                .set(CashierCode::getAppId, param.getAppId())
                .in(CashierCode::getId, param.getIds())
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
    }

    /**
     * 应用解绑
     */
    @Transactional(rollbackFor = Exception.class)
    public void unbindApp(AssignMerchantParam param){
        cashierCodeManager.lambdaUpdate()
                .set(CashierCode::getAppId, null)
                .in(CashierCode::getId, param.getIds())
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
    }

    /**
     * 空白码牌绑定, 只有商户端可用
     */
    public void bindBlankCode(CashierCodeBindBlankParam param){
        String mchNo = MchContextLocal.getMchNo();
        if (StrUtil.isBlank(mchNo)){
            throw new DataNotExistException("商户信息不存在!");
        }
        Merchant merchant = merchantManager.findByMchNo(mchNo)
                .orElseThrow(() -> new DataNotExistException("商户信息不存在!"));
        // AppId不为空判断是否为商户下的应用
        if (StrUtil.isNotBlank(param.getAppId())&&!mchAppManager.existsByAppId(param.getAppId())){
           throw new DataNotExistException("商户应用不存在");
        }

        // 判断码牌是否 存在/被绑定/不属于商户的代理
        var cashierCode = cashierCodeManager.findByCodeNotTenant(param.getCode())
                .orElseThrow(() -> new DataNotExistException("收款码牌不存在"));
        if (cashierCode.getMchNo() != null){
            throw new OperationFailException("码牌已绑定其他商户!");
        }
        // 绑定码牌
        cashierCode.setMchNo(mchNo)
                .setAppId(param.getAppId());
        cashierCodeManager.updateByIdNotTenant(cashierCode);
    }


    /**
     * 获取码牌
     */
    public CashierCode getAndCheckCode(String code) {
        var cashierCode = cashierCodeManager.findByCodeNotTenant(code)
                .orElseThrow(() -> new DataNotExistException("收款码牌不存在"));
        if (!cashierCode.isEnable()) {
            throw new ValidationFailedException("码牌已禁用");
        }
        //判断是否分配了商户号和应用号
        if (cashierCode.getMchNo() == null || cashierCode.getAppId() == null){
            throw new ValidationFailedException("码牌未分配商户与应用");
        }
        return cashierCode;
    }

    /**
     * 删除码牌
     */
    public void delete(Long id) {
        CashierCode cashierCode = cashierCodeManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("收款码牌不存在"));
        // 判断是否已经绑定了商户和应用
        if (cashierCode.getMchNo() != null || cashierCode.getAppId() != null){
//            throw new ValidationFailedException("码牌已绑定商户和应用，请先解绑");
        }
        cashierCodeManager.deleteById(id);
    }


    /**
     * 获取码牌链接
     */
    public String getCodeLink(String code) {
        PlatformUrlConfig urlConfig = platformConfigService.getUrlConfig();
        return StrUtil.format("{}/cashier/code/{}", urlConfig.getGatewayH5Url(),code);
    }

    /**
     * 判断批次号是否存在
     */
    public boolean existsByBatchNo(String batchNo){
        return cashierCodeManager.existedByBatchNo(batchNo);
    }

    /**
     * 批量导出码牌
     */
    @SneakyThrows
    public byte[] exportBatch(List<Long> ids) {
        List<CashierCode> list = cashierCodeManager.findAllByIds(ids);
        if (CollUtil.isEmpty(list)){
            throw new DataNotExistException("选中的码牌不可为空");
        }
        PlatformUrlConfig urlConfig = platformConfigService.getUrlConfig();
        // 转换为码牌导出对象
        var exports = list.stream()
                .map(cashierCode -> new CashierCodeExport()
                        .setAmountType(Objects.equals(cashierCode.getAmountType(),"random")?"任意金额" : "固定金额")
                        .setAmount(Optional.ofNullable(cashierCode.getAmount()).map(PayUtil::toDecimal).map(BigDecimal::toPlainString).orElse("无"))
                        .setName(Optional.ofNullable(cashierCode.getName()).orElse("未命名"))
                        .setCode(cashierCode.getCode())
                        .setEnable(cashierCode.isEnable() ? "是" : "否")
                        .setBatchNo(cashierCode.getBatchNo())
                        .setCodeUrl(StrUtil.format("{}/cashier/code/{}", urlConfig.getGatewayH5Url(),cashierCode.getCode()))
                ).toList();
        // 生成导出文件
        var params = new TemplateExportParams(ResourceUtil.getStream("template/码牌导出模板.xlsx"));
        params.setScanAllsheet(true);
        Map<String, Object> map = new HashMap<>();
        map.put("list", exports);
        // 生成码牌导出文件
        Workbook workbook = ExcelExportUtil.exportExcel(params,map);
        if (Objects.isNull(workbook)) {
            throw new OperationFailException("码牌批量导出异常, 模板文件可能为空! ");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        return baos.toByteArray();
    }

}
