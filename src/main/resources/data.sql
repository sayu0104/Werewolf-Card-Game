-- 挿入する 無視して 〜の中へ rolesに （この3つの列に）             値は↓
INSERT IGNORE INTO roles (name, faction, description) VALUES
    ('村人', '村人陣営', '専用カードを持つ'),
    ('人狼', '人狼陣営', '夜に襲撃を行う。複数人いる場合はお互いを認識する'),
    ('占い師', '村人陣営', '対象を占い、結果は朝に判明する。占い師自身が死亡しても結果は判明する'),
    ('狩人', '村人陣営', '対象を護衛する。護衛は人狼の襲撃より優先処理される'),
    ('狂人', '人狼陣営', '「偽占い」カードを持ち、場をかき乱す。人狼の正体は知らず、人狼陣営の勝利が目的');

-- 挿入する 無視して 〜の中へ usersに （この5つの列に）                                       値は↓
INSERT IGNORE INTO users (username, password_hash, email, is_admin, created_at) VALUES
    ('admin', '$2a$10$2kBLUxo.iAFiwpEGMjmnC.LbnFCPiqMhTtX5mlWnCvR.Py9GaoYVy', 'admin@example.com', true, NOW());
-- is_admin     = true    ← これがtrueだから、ログインできる
-- created_at   = NOW()   ← NOW()＝「今の日時」を入れる関数
    
-- cards の各列に、下で用意したカードの値を入れる
INSERT INTO cards (name, effect_type, timing, cost, value, required_role_id, is_mandatory, is_rare, is_usage_public)
SELECT v.name, v.effect_type, v.timing, v.cost, v.value, v.required_role_id, v.is_mandatory, v.is_rare, v.is_usage_public
FROM (
-- 1行＝カード1枚の詳細（名前・効果・タイミング・コスト…） ここで列名と並び順を決める
    SELECT '占い' AS name, '情報取得' AS effect_type, '夜' AS timing, 1 AS cost, NULL AS value, (SELECT id FROM roles WHERE name = '占い師') AS required_role_id, false AS is_mandatory, false AS is_rare, true AS is_usage_public
    UNION ALL SELECT '護衛', '防御', '夜', 1, NULL, (SELECT id FROM roles WHERE name = '狩人'), false, false, true
    UNION ALL SELECT '襲撃', '除去', '夜', 3, NULL, (SELECT id FROM roles WHERE name = '人狼'), false, false, true
    UNION ALL SELECT '投票観察', '情報取得', '夜', 1, NULL, NULL, false, false, true
    UNION ALL SELECT '名乗り', '宣言', '昼', 0, NULL, NULL, false, false, true
    UNION ALL SELECT '結果報告', '報告', '昼', 0, NULL, NULL, true, false, true
    UNION ALL SELECT '騙り', '宣言', '昼', 0, NULL, NULL, false, false, true
    UNION ALL SELECT '偽報告', '報告', '昼', 0, NULL, NULL, true, false, true
    UNION ALL SELECT '疑う（弱）', '被疑心操作', '昼', 1, 3, NULL, false, false, true
    UNION ALL SELECT '疑う（中）', '被疑心操作', '昼', 2, 5, NULL, false, false, true
    UNION ALL SELECT '疑う（強）', '被疑心操作', '昼', 3, 7, NULL, false, false, true
    UNION ALL SELECT '擁護（弱）', '被疑心操作', '昼', 1, 3, NULL, false, false, true
    UNION ALL SELECT '擁護（中）', '被疑心操作', '昼', 2, 5, NULL, false, false, true
    UNION ALL SELECT '擁護（強）', '被疑心操作', '昼', 3, 7, NULL, false, false, true
    UNION ALL SELECT '扇動', '被疑心操作', '昼', 1, 3, (SELECT id FROM roles WHERE name = '狂人'), false, false, true
    UNION ALL SELECT 'カウンター', '被疑心操作', '昼', 3, 5, (SELECT id FROM roles WHERE name = '人狼'), false, true, true
) v
-- 同じ名前のカードが既にあれば入れない（二重挿入防止）
WHERE NOT EXISTS (SELECT 1 FROM cards c WHERE c.name = v.name);
