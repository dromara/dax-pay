package cn.bootx.platform.starter.wecom.core.robot.executor;

import cn.bootx.platform.starter.wecom.code.WeComCode;
import cn.bootx.platform.starter.wecom.core.robot.domin.UploadMedia;
import cn.hutool.http.HttpUtil;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.enums.WxType;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.fs.FileUtils;
import me.chanjar.weixin.common.util.http.RequestExecutor;
import me.chanjar.weixin.common.util.http.ResponseHandler;

import java.io.File;
import java.io.IOException;

/**
 * 机器人文件素材上传
 *
 * @author xxm
 * @since 2022/7/23
 */
public class RobotMediaFileUploadRequestExecutor implements RequestExecutor<WxMediaUploadResult, UploadMedia> {

    @Override
    public WxMediaUploadResult execute(String uri, UploadMedia uploadMedia, WxType wxType)
            throws WxErrorException, IOException {
        File tmpFile = FileUtils.createTmpFile(uploadMedia.getInputStream(), uploadMedia.getFilename(),
                uploadMedia.getFileType());
        String filename = uploadMedia.getFilename() + "." + uploadMedia.getFileType();
        String response;
        response = HttpUtil.createPost(uri).form(WeComCode.MEDIA, tmpFile, filename).execute().body();

        WxError result = WxError.fromJson(response);
        if (result.getErrorCode() != 0) {
            throw new WxErrorException(result);
        }
        return WxMediaUploadResult.fromJson(response);
    }

    @Override
    public void execute(String uri, UploadMedia data, ResponseHandler<WxMediaUploadResult> handler, WxType wxType)
            throws WxErrorException, IOException {
        handler.handle(this.execute(uri, data, wxType));
    }

}
