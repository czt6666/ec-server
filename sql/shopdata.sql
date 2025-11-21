-- 建议先清空表
TRUNCATE TABLE shop;

INSERT INTO shop (
    shop_name,
    shop_abbreviation,
    product_type,
    business_status,
    village,
    shop_intro,
    shop_avatar,
    shop_address,
    qualification_files,
    user_id,
    display_no,
    create_time,
    update_time
) VALUES
      ('稻香米铺', 'dxmp', '粮油副食', 1, '稻香村', '主营本地新鲜大米、小米等粮油副食。', '/uploads/shop/avatar_dxmp.jpg', '稻香村 88 号', '["/uploads/license/dxmp_business.jpg"]', NULL, 1, NOW(), NOW()),
      ('青山果园', 'qsgy', '鲜果直供', 1, '青山村', '自营果园采摘，提供有机苹果、梨等水果。', '/uploads/shop/avatar_qsgy.jpg', '青山村 西头 果园区', '["/uploads/license/qsgy_business.jpg","/uploads/license/qsgy_food.jpg"]', NULL, 2, NOW(), NOW()),
      ('渔家鲜味', 'yjxw', '水产海鲜', 0, '金沙村', '主打当日捕捞河鲜，支持线上预约配送。', '/uploads/shop/avatar_yjxw.jpg', '金沙村 渔民码头 6 号', '["/uploads/license/yjxw_business.jpg"]', NULL, 3, NOW(), NOW()),
      ('乡味豆坊', 'xwdf', '豆制品', 1, '稻香村', '祖传手工豆腐坊，提供豆腐、豆干等产品。', '/uploads/shop/avatar_xwdf.jpg', '稻香村 中街 15 号', '["/uploads/license/xwdf_business.jpg","/uploads/license/xwdf_food.jpg"]', NULL, 4, NOW(), NOW()),
      ('花田药草铺', 'htycp', '中草药材', 1, '花田村', '销售本地种植的中草药材与养生茶。', '/uploads/shop/avatar_htycp.jpg', '花田村 老街 3 号', '["/uploads/license/htycp_business.jpg"]', NULL, 5, NOW(), NOW());
