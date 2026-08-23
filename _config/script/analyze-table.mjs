#!/usr/bin/env node
/**
 * table.sql 结构规范分析脚本（适配 Navicat 导出格式）
 *
 * 检查维度（对照 AGENTS.md 数据库 SQL 规则）：
 *   1. timestamp without time zone 违规（项目规定统一 timestamptz）
 *   2. 缺表注释 / 缺列注释 / 缺索引注释（COMMENT ON 覆盖差集）
 *   3. 索引注释规则：纯主键约束豁免，UNIQUE 约束与业务索引必须有注释
 *   4. 非中文注释（表/字段注释要求中文说明）
 *   5. 时间精度不统一（timestamptz 有无精度声明混用）
 *
 * 格式假设（Navicat "Premium Data Transfer" 导出）：
 *   CREATE TABLE "public"."t" ( ... ) 换行 ;
 *   CREATE [UNIQUE] INDEX "name" ON ... USING btree ( 可能跨行 );
 *   ALTER TABLE ... ADD CONSTRAINT "name" PRIMARY KEY/UNIQUE (...);
 *   COMMENT ON TABLE|COLUMN|INDEX "public"."x"[."y"] IS '...';
 *
 * 用法：node analyze-table.mjs <table.sql>
 */

import { createReadStream } from 'node:fs'
import { createInterface } from 'node:readline'
import { argv, exit } from 'node:process'

const inputPath = argv[2]
if (!inputPath) {
  console.error('用法：node analyze-table.mjs <table.sql>')
  exit(1)
}

// ---------- 解析结果容器 ----------
const tables = new Map()        // 表名 -> 列数组 [{ name, type }]
const tableComments = new Map() // 表名 -> 注释文本
const columnComments = new Map()// "表.列" -> 注释文本
const indexComments = new Map() // 索引名 -> 注释文本
const indexDefs = new Map()     // 索引/约束名 -> { kind: 'index'|'unique'|'pkey', table, cols }
const sequences = new Set()     // CREATE SEQUENCE 名

// ---------- 状态机解析 ----------
const lines = await new Promise((resolve) => {
  const arr = []
  const rl = createInterface({ input: createReadStream(inputPath, 'utf8'), crlfDelay: Infinity })
  rl.on('line', (l) => arr.push(l))
  rl.on('close', () => resolve(arr))
})

let i = 0
while (i < lines.length) {
  const line = lines[i]

  // CREATE TABLE 块（到 ")\s*;" 或 ")" 单独成行为止）
  let m = line.match(/^CREATE TABLE "public"\."(\w+)" \($/)
  if (m) {
    const table = m[1]
    const cols = []
    i++
    while (i < lines.length && !/^\)/.test(lines[i])) {
      const cm = lines[i].match(/^  "(\w+)" (\S+(?: \w+)?(?:\(\d+\))?).*/)
      if (cm) cols.push({ name: cm[1], type: cm[2].trim() })
      i++
    }
    tables.set(table, cols)
    i++
    continue
  }

  // CREATE [UNIQUE] INDEX（索引名在首行，语句可能跨行）
  m = line.match(/^CREATE (UNIQUE )?INDEX "(\w+)" ON "public"\."(\w+)"/)
  if (m) {
    indexDefs.set(m[2], { kind: m[1] ? 'unique' : 'index', table: m[3] })
    while (i < lines.length && !/;\s*$/.test(lines[i])) i++
    i++
    continue
  }

  // ALTER TABLE ADD CONSTRAINT（单行）
  m = line.match(/^ALTER TABLE "public"\."(\w+)" ADD CONSTRAINT "(\w+)" (PRIMARY KEY|UNIQUE)\b/)
  if (m) {
    indexDefs.set(m[2], { kind: m[3] === 'PRIMARY KEY' ? 'pkey' : 'unique', table: m[1] })
    i++
    continue
  }

  // CREATE SEQUENCE
  m = line.match(/^CREATE SEQUENCE "public"\."(\w+)"/)
  if (m) sequences.add(m[1])

  // COMMENT ON 三类
  m = line.match(/^COMMENT ON TABLE "public"\."(\w+)" IS '(.*)';/)
  if (m) { tableComments.set(m[1], m[2]); i++; continue }
  m = line.match(/^COMMENT ON COLUMN "public"\."(\w+)"\."(\w+)" IS '(.*)';/)
  if (m) { columnComments.set(`${m[1]}.${m[2]}`, m[3]); i++; continue }
  m = line.match(/^COMMENT ON INDEX "public"\."(\w+)" IS '(.*)';/)
  if (m) { indexComments.set(m[1], m[2]); i++; continue }

  i++
}

// ---------- 检查 1: timestamp without time zone ----------
const noTzCols = []
for (const [t, cols] of tables) {
  for (const c of cols) {
    if (/^timestamp/i.test(c.type) && !/^timestamptz/i.test(c.type)) {
      noTzCols.push(`${t}.${c.name}  ${c.type}`)
    }
  }
}

// ---------- 检查 2/3: 注释覆盖差集 ----------
const missingTableComment = [...tables.keys()].filter((t) => !tableComments.has(t))
const missingColumnComments = []
for (const [t, cols] of tables) {
  for (const c of cols) {
    if (!columnComments.has(`${t}.${c.name}`)) missingColumnComments.push(`${t}.${c.name}`)
  }
}
// 索引注释: 纯主键豁免（AGENTS: 纯外键/主键无需单独注释）
const needCommentIndexes = [...indexDefs].filter(([, d]) => d.kind !== 'pkey').map(([n]) => n)
const missingIndexComments = needCommentIndexes.filter((n) => !indexComments.has(n))

// ---------- 检查 4: 非中文注释 ----------
const hasChinese = (s) => /[\u4e00-\u9fff]/.test(s)
const nonChineseComments = []
for (const [t, c] of tableComments) if (!hasChinese(c)) nonChineseComments.push(`表 ${t}: "${c}"`)
for (const [tc, c] of columnComments) if (!hasChinese(c)) nonChineseComments.push(`列 ${tc}: "${c}"`)
for (const [n, c] of indexComments) if (!hasChinese(c)) nonChineseComments.push(`索引 ${n}: "${c}"`)

// ---------- 检查 5: timestamptz 精度声明统一性 ----------
const tzPrecision = new Map()
for (const [, cols] of tables) {
  for (const c of cols) {
    if (/^timestamptz/i.test(c.type)) tzPrecision.set(c.type, (tzPrecision.get(c.type) ?? 0) + 1)
  }
}

// ---------- 报告 ----------
const out = []
out.push('================ table.sql 规范分析报告 ================')
out.push(`表: ${tables.size}  列: ${[...tables.values()].reduce((a, c) => a + c.length, 0)}  索引/约束: ${indexDefs.size}  序列: ${sequences.size}`)
out.push(`注释: 表 ${tableComments.size}  列 ${columnComments.size}  索引 ${indexComments.size}`)
out.push('')

out.push(`[1] timestamp without time zone 违规: ${noTzCols.length} 列`)
noTzCols.forEach((c) => out.push(`    ${c}`))
out.push('')

out.push(`[2] 缺表注释: ${missingTableComment.length} 张`)
missingTableComment.forEach((t) => out.push(`    ${t}`))
out.push('')

out.push(`[3] 缺列注释: ${missingColumnComments.length} 列`)
missingColumnComments.forEach((c) => out.push(`    ${c}`))
out.push('')

out.push(`[4] 缺索引注释(纯主键已豁免): ${missingIndexComments.length} 个`)
missingIndexComments.forEach((n) => {
  const d = indexDefs.get(n)
  out.push(`    ${n}  [${d.kind}] on ${d.table}`)
})
out.push('')

out.push(`[5] 非中文注释: ${nonChineseComments.length} 条`)
nonChineseComments.slice(0, 60).forEach((c) => out.push(`    ${c}`))
if (nonChineseComments.length > 60) out.push(`    ... 其余 ${nonChineseComments.length - 60} 条略`)
out.push('')

out.push('[6] timestamptz 精度声明分布(写法统一性):')
for (const [t, n] of [...tzPrecision].sort((a, b) => b[1] - a[1])) out.push(`    ${t}: ${n} 列`)
out.push('======================== 结束 ========================')
console.log(out.join('\n'))
