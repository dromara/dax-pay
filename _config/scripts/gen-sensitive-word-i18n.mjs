/**
 * 写入敏感词相关 i18n（六语）
 */
import fs from 'node:fs'
import path from 'node:path'

const root = path.resolve(
  'daxpay-platform/daxpay-platform-common/common-i18n/src/main/resources/i18n',
)
const locales = ['zh-CN', 'en-US', 'zh-TW', 'zh-HK', 'ja-JP', 'ko-KR']

const enums = {
  sensitive_word_status: {
    'zh-CN': { enable: '启用', disable: '禁用' },
    'en-US': { enable: 'Enabled', disable: 'Disabled' },
    'zh-TW': { enable: '啟用', disable: '停用' },
    'zh-HK': { enable: '啟用', disable: '停用' },
    'ja-JP': { enable: '有効', disable: '無効' },
    'ko-KR': { enable: '사용', disable: '중지' },
  },
  sensitive_word_category: {
    'zh-CN': { politic: '政治', porn: '色情', violence: '暴力', ad: '广告', custom: '自定义' },
    'en-US': { politic: 'Political', porn: 'Pornography', violence: 'Violence', ad: 'Ads', custom: 'Custom' },
    'zh-TW': { politic: '政治', porn: '色情', violence: '暴力', ad: '廣告', custom: '自訂' },
    'zh-HK': { politic: '政治', porn: '色情', violence: '暴力', ad: '廣告', custom: '自訂' },
    'ja-JP': { politic: '政治', porn: 'ポルノ', violence: '暴力', ad: '広告', custom: 'カスタム' },
    'ko-KR': { politic: '정치', porn: '음란', violence: '폭력', ad: '광고', custom: '사용자정의' },
  },
  sensitive_word_match_mode: {
    'zh-CN': { contains: '包含', exact: '精确' },
    'en-US': { contains: 'Contains', exact: 'Exact' },
    'zh-TW': { contains: '包含', exact: '精確' },
    'zh-HK': { contains: '包含', exact: '精確' },
    'ja-JP': { contains: '含む', exact: '完全一致' },
    'ko-KR': { contains: '포함', exact: '정확' },
  },
  sensitive_word_scene: {
    'zh-CN': {
      pay_title: '支付标题', pay_description: '支付描述', goods_name: '商品名称',
      goods_description: '商品描述', mch_name: '商户名称', app_name: '应用名称',
      store_name: '门店名称', user_name: '用户名称', qr_name: '码牌名称',
      notice: '公告', protocol: '协议', manual_check: '试检', general: '通用',
    },
    'en-US': {
      pay_title: 'Pay title', pay_description: 'Pay description', goods_name: 'Goods name',
      goods_description: 'Goods description', mch_name: 'Merchant name', app_name: 'App name',
      store_name: 'Store name', user_name: 'User name', qr_name: 'QR name',
      notice: 'Notice', protocol: 'Protocol', manual_check: 'Manual check', general: 'General',
    },
    'zh-TW': {
      pay_title: '支付標題', pay_description: '支付描述', goods_name: '商品名稱',
      goods_description: '商品描述', mch_name: '商戶名稱', app_name: '應用名稱',
      store_name: '門店名稱', user_name: '用戶名稱', qr_name: '碼牌名稱',
      notice: '公告', protocol: '協議', manual_check: '試檢', general: '通用',
    },
    'zh-HK': {
      pay_title: '支付標題', pay_description: '支付描述', goods_name: '商品名稱',
      goods_description: '商品描述', mch_name: '商戶名稱', app_name: '應用名稱',
      store_name: '門店名稱', user_name: '用戶名稱', qr_name: '碼牌名稱',
      notice: '公告', protocol: '協議', manual_check: '試檢', general: '通用',
    },
    'ja-JP': {
      pay_title: '支払タイトル', pay_description: '支払説明', goods_name: '商品名',
      goods_description: '商品説明', mch_name: '加盟店名', app_name: 'アプリ名',
      store_name: '店舗名', user_name: 'ユーザー名', qr_name: 'QR名',
      notice: 'お知らせ', protocol: '規約', manual_check: 'テスト', general: '一般',
    },
    'ko-KR': {
      pay_title: '결제 제목', pay_description: '결제 설명', goods_name: '상품명',
      goods_description: '상품 설명', mch_name: '가맹점명', app_name: '앱 이름',
      store_name: '매장명', user_name: '사용자명', qr_name: 'QR 이름',
      notice: '공지', protocol: '약관', manual_check: '검사', general: '일반',
    },
  },
  sensitive_word_source: {
    'zh-CN': { admin: '运营端', merchant: '商户端', unipay: '开放支付', app_admin: '管理端小程序', unknown: '未知' },
    'en-US': { admin: 'Admin', merchant: 'Merchant', unipay: 'Open API', app_admin: 'Admin App', unknown: 'Unknown' },
    'zh-TW': { admin: '運營端', merchant: '商戶端', unipay: '開放支付', app_admin: '管理端小程式', unknown: '未知' },
    'zh-HK': { admin: '運營端', merchant: '商戶端', unipay: '開放支付', app_admin: '管理端小程式', unknown: '未知' },
    'ja-JP': { admin: '管理画面', merchant: '加盟店', unipay: 'Open API', app_admin: '管理アプリ', unknown: '不明' },
    'ko-KR': { admin: '운영단', merchant: '가맹점', unipay: 'Open API', app_admin: '관리 앱', unknown: '알 수 없음' },
  },
}

const platformConfigTypeExtra = {
  'zh-CN': '敏感词策略配置',
  'en-US': 'Sensitive word policy',
  'zh-TW': '敏感詞策略配置',
  'zh-HK': '敏感詞策略配置',
  'ja-JP': 'センシティブワード設定',
  'ko-KR': '민감어 정책 설정',
}

const errors = {
  'zh-CN': {
    sensitiveWord: '内容包含敏感词，请修改后重试',
    sensitiveWordHit: '内容包含敏感词「{0}」',
    sensitiveWordDuplicate: '该敏感词已存在',
    sensitiveWordNotFound: '敏感词不存在',
    sensitiveWordHitNotFound: '命中记录不存在',
    sensitiveWordStatusInvalid: '敏感词状态无效',
    sensitiveWordMatchModeInvalid: '匹配模式无效',
    sensitiveWordCategoryInvalid: '分类无效',
  },
  'en-US': {
    sensitiveWord: 'Content contains sensitive words. Please modify and try again.',
    sensitiveWordHit: 'Content contains sensitive word "{0}"',
    sensitiveWordDuplicate: 'This sensitive word already exists',
    sensitiveWordNotFound: 'Sensitive word not found',
    sensitiveWordHitNotFound: 'Hit record not found',
    sensitiveWordStatusInvalid: 'Invalid sensitive word status',
    sensitiveWordMatchModeInvalid: 'Invalid match mode',
    sensitiveWordCategoryInvalid: 'Invalid category',
  },
  'zh-TW': {
    sensitiveWord: '內容包含敏感詞，請修改後重試',
    sensitiveWordHit: '內容包含敏感詞「{0}」',
    sensitiveWordDuplicate: '該敏感詞已存在',
    sensitiveWordNotFound: '敏感詞不存在',
    sensitiveWordHitNotFound: '命中記錄不存在',
    sensitiveWordStatusInvalid: '敏感詞狀態無效',
    sensitiveWordMatchModeInvalid: '匹配模式無效',
    sensitiveWordCategoryInvalid: '分類無效',
  },
  'zh-HK': {
    sensitiveWord: '內容包含敏感詞，請修改後重試',
    sensitiveWordHit: '內容包含敏感詞「{0}」',
    sensitiveWordDuplicate: '該敏感詞已存在',
    sensitiveWordNotFound: '敏感詞不存在',
    sensitiveWordHitNotFound: '命中記錄不存在',
    sensitiveWordStatusInvalid: '敏感詞狀態無效',
    sensitiveWordMatchModeInvalid: '匹配模式無效',
    sensitiveWordCategoryInvalid: '分類無效',
  },
  'ja-JP': {
    sensitiveWord: '不適切な語句が含まれています。修正してください。',
    sensitiveWordHit: '不適切な語句「{0}」が含まれています',
    sensitiveWordDuplicate: 'この語句は既に存在します',
    sensitiveWordNotFound: '語句が見つかりません',
    sensitiveWordHitNotFound: 'ヒット記録が見つかりません',
    sensitiveWordStatusInvalid: '状態が無効です',
    sensitiveWordMatchModeInvalid: '一致モードが無効です',
    sensitiveWordCategoryInvalid: '分類が無効です',
  },
  'ko-KR': {
    sensitiveWord: '민감한 단어가 포함되어 있습니다. 수정 후 다시 시도하세요.',
    sensitiveWordHit: '민감한 단어 "{0}"이(가) 포함되어 있습니다',
    sensitiveWordDuplicate: '이미 존재하는 민감어입니다',
    sensitiveWordNotFound: '민감어를 찾을 수 없습니다',
    sensitiveWordHitNotFound: '적중 기록을 찾을 수 없습니다',
    sensitiveWordStatusInvalid: '상태가 올바르지 않습니다',
    sensitiveWordMatchModeInvalid: '일치 모드가 올바르지 않습니다',
    sensitiveWordCategoryInvalid: '분류가 올바르지 않습니다',
  },
}

const validationField = {
  'zh-CN': {
    sensitiveWord: { rejected: '内容包含敏感词' },
    word: { notBlank: '敏感词不可为空', size: '敏感词不可超过64位' },
    category: { size: '分类不可超过32位' },
    matchMode: { size: '匹配模式不可超过16位' },
    status: { size: '状态不可超过16位' },
  },
  'en-US': {
    sensitiveWord: { rejected: 'Content contains sensitive words' },
    word: { notBlank: 'Word is required', size: 'Word must be at most 64 characters' },
    category: { size: 'Category must be at most 32 characters' },
    matchMode: { size: 'Match mode must be at most 16 characters' },
    status: { size: 'Status must be at most 16 characters' },
  },
  'zh-TW': {
    sensitiveWord: { rejected: '內容包含敏感詞' },
    word: { notBlank: '敏感詞不可為空', size: '敏感詞不可超過64位' },
    category: { size: '分類不可超過32位' },
    matchMode: { size: '匹配模式不可超過16位' },
    status: { size: '狀態不可超過16位' },
  },
  'zh-HK': {
    sensitiveWord: { rejected: '內容包含敏感詞' },
    word: { notBlank: '敏感詞不可為空', size: '敏感詞不可超過64位' },
    category: { size: '分類不可超過32位' },
    matchMode: { size: '匹配模式不可超過16位' },
    status: { size: '狀態不可超過16位' },
  },
  'ja-JP': {
    sensitiveWord: { rejected: '不適切な語句が含まれています' },
    word: { notBlank: '語句は必須です', size: '語句は64文字以内です' },
    category: { size: '分類は32文字以内です' },
    matchMode: { size: '一致モードは16文字以内です' },
    status: { size: '状態は16文字以内です' },
  },
  'ko-KR': {
    sensitiveWord: { rejected: '민감한 단어가 포함되어 있습니다' },
    word: { notBlank: '민감어는 필수입니다', size: '민감어는 64자 이내여야 합니다' },
    category: { size: '분류는 32자 이내여야 합니다' },
    matchMode: { size: '일치 모드는 16자 이내여야 합니다' },
    status: { size: '상태는 16자 이내여야 합니다' },
  },
}

function deepMergeField(locale, extra) {
  const p = path.join(root, locale, 'validation.json')
  const json = JSON.parse(fs.readFileSync(p, 'utf8'))
  json.field = { ...json.field, ...extra }
  fs.writeFileSync(p, JSON.stringify(json, null, 2) + '\n', 'utf8')
  console.log('validation', locale)
}

function mergeError(locale, extra) {
  const p = path.join(root, locale, 'error', 'common.json')
  const json = JSON.parse(fs.readFileSync(p, 'utf8'))
  Object.assign(json, extra)
  fs.writeFileSync(p, JSON.stringify(json, null, 2) + '\n', 'utf8')
  console.log('error', locale)
}

function writeEnum(name, locale, data) {
  const p = path.join(root, locale, 'enum', `${name}.json`)
  fs.writeFileSync(p, JSON.stringify(data, null, 2) + '\n', 'utf8')
  console.log('enum', name, locale)
}

function mergePlatformConfigType(locale) {
  const p = path.join(root, locale, 'enum', 'platform_config_type.json')
  const json = JSON.parse(fs.readFileSync(p, 'utf8'))
  json.sensitive_word = platformConfigTypeExtra[locale]
  fs.writeFileSync(p, JSON.stringify(json, null, 2) + '\n', 'utf8')
  console.log('platform_config_type', locale)
}

for (const locale of locales) {
  deepMergeField(locale, validationField[locale])
  mergeError(locale, errors[locale])
  mergePlatformConfigType(locale)
  for (const [name, byLocale] of Object.entries(enums)) {
    writeEnum(name, locale, byLocale[locale])
  }
}

console.log('i18n done')
