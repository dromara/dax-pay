package cn.bootx.platform.daxpay.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.core.channel.voucher.service.VoucherQueryService;
import cn.bootx.platform.daxpay.core.channel.voucher.service.VoucherService;
import cn.bootx.platform.daxpay.dto.channel.voucher.VoucherDto;
import cn.bootx.platform.daxpay.param.channel.voucher.VoucherGenerationParam;
import cn.bootx.platform.daxpay.param.channel.voucher.VoucherImportParam;
import cn.bootx.platform.daxpay.param.channel.voucher.VoucherParam;
import cn.hutool.core.io.IoUtil;
import com.alibaba.excel.EasyExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * @author xxm
 * @since 2022/3/14
 */
@Tag(name = "储值卡")
@RestController
@RequestMapping("/voucher")
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;

    private final VoucherQueryService voucherQueryService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<VoucherDto>> page(PageParam pageParam, VoucherParam param) {
        return Res.ok(voucherQueryService.page(pageParam, param));
    }

    @Operation(summary = "单条查询")
    @GetMapping("/findById")
    public ResResult<VoucherDto> findById(Long id) {
        return Res.ok(voucherQueryService.findById(id));
    }

    @Operation(summary = "根据卡号查询")
    @GetMapping("/findByCardNo")
    public ResResult<VoucherDto> findByCardNo(String cardNo) {
        return Res.ok(voucherQueryService.findByCardNo(cardNo));
    }

    @Operation(summary = "获取并判断卡状态")
    @GetMapping("/getAndJudgeVoucher")
    public ResResult<VoucherDto> getAndJudgeVoucher(String cardNo) {
        return Res.ok(voucherQueryService.getAndJudgeVoucher(cardNo));
    }

    @Operation(summary = "批量生成储值卡")
    @PostMapping("/generationBatch")
    public ResResult<Void> generationBatch(@RequestBody VoucherGenerationParam param) {
        voucherService.generationBatch(param);
        return Res.ok();
    }

    @Operation(summary = "冻结")
    @PostMapping("/lock")
    public ResResult<Void> lock(Long id) {
        voucherService.lock(id);
        return Res.ok();
    }

    @Operation(summary = "启用")
    @PostMapping("/unlock")
    public ResResult<Void> unlock(Long id) {
        voucherService.unlock(id);
        return Res.ok();
    }

    @Operation(summary = "批量冻结")
    @PostMapping("/lockBatch")
    public ResResult<Void> lockBatch(@RequestBody List<Long> ids) {
        voucherService.lockBatch(ids);
        return Res.ok();
    }

    @Operation(summary = "批量启用")
    @PostMapping("/unlockBatch")
    public ResResult<Void> unlockBatch(@RequestBody List<Long> ids) {
        voucherService.unlockBatch(ids);
        return Res.ok();
    }

    @SneakyThrows
    @Operation(summary = "导入已有的储值卡")
    @PostMapping("/importBatch")
    public ResResult<Void> importBatch(Boolean skip, String mchCode, String mchAppCode, MultipartFile file){
        List<VoucherImportParam> voucherImportParams = EasyExcel.read(file.getInputStream())
                // 设置与Excel表映射的类
                .head(VoucherImportParam.class)
                // 设置sheet,默认读取第一个
                .sheet()
                // 设置标题所在行数
                .headRowNumber(1)
                // 异步读取
                .doReadSync();
        voucherService.importBatch(skip,mchCode,mchAppCode,voucherImportParams);
        return Res.ok();
    }

    @SneakyThrows
    @Operation(summary = "下载导入模板")
    @GetMapping("/excelTemplate")
    public ResponseEntity<byte[]> excelTemplate(){
        //设置header信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment",
                "ImportVoucher.xlsx");

        ResourceLoader resourceLoader = new DefaultResourceLoader();
        InputStream inputStream = resourceLoader.getResource("classpath:templates//ImportVoucher.xlsx")
                .getInputStream();

        return new ResponseEntity<>(IoUtil.readBytes(inputStream),headers, HttpStatus.OK);
    }
}
