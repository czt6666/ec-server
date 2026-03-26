-- 给 village_homestay 增加展示顺序 display_no（越小越靠前）
ALTER TABLE village_homestay
    ADD COLUMN display_no INT NOT NULL DEFAULT 1 COMMENT '展示顺序（越小越靠前）';

ALTER TABLE village_homestay
    ADD INDEX idx_village_homestay_display_no (display_no);


-- 将全表 display_no 重排成 1..N（全局顺序），按 create_time 升序（create_time 相同再按 id 升序）
-- 说明：使用“先排序再用变量生成行号”的写法，避免 MySQL 对派生表排序与变量赋值的不确定性。
UPDATE village_homestay vh
    JOIN (
        SELECT
            id,
            (@rn := @rn + 1) AS new_display_no
        FROM (
                 SELECT
                     id,
                     COALESCE(create_time, '1970-01-01 00:00:00') AS ct
                 FROM village_homestay
                 ORDER BY ct ASC, id ASC
             ) s
                 CROSS JOIN (SELECT @rn := 0) vars
    ) t ON vh.id = t.id
SET vh.display_no = t.new_display_no;


