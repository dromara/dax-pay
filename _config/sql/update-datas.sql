-- 微信通道旧应用体系下线: 删除已废弃的直连/服务商应用管理菜单
-- 4040113=WechatMchAppManage(直连商户应用)、4040119=WechatIsvMchAppManage(ISV子商户应用)、40503=WechatIsvAppManage(ISV服务商应用)
-- 入口已由 WxAppHub(id=40106, menu.payment.wx.app)统一接管

DELETE FROM "public"."iam_perm_menu" WHERE "id" IN (4040113, 4040119, 40503);
