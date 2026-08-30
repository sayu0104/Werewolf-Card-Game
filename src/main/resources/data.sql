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
    