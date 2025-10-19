-- 乡村 1（id=1）—— 桃花村，3 家民宿
INSERT INTO village_homestay
(village_id, homestay_name, address, `status`, star_level, room_count, bed_count, max_capacity, contact_name, contact_phone, description, price, cover_image)
VALUES
    (1, '桃花里·隐所', '桃花村桃花坞8号', 1, 5, 12, 18, 24, '李青青', '13800001001', '临溪而建，可见漫山桃花', 6800.00, 'https://demo.com/img/tao1.jpg'),
    (1, '花间小筑', '桃花村花海路28号', 1, 4, 8, 12, 16, '王芳', '13800001002', '百年老宅改造，带院子的农家乐', 4500.00, 'https://demo.com/img/tao2.jpg'),
    (1, '桃然山居', '桃花村后山腰', 2, 3, 6, 9, 12, '陈大伟', '13800001003', '山顶观景房，冬季暂停营业', 3800.00, 'https://demo.com/img/tao3.jpg');

-- 乡村 2（id=2）—— 稻香村，3 家民宿
INSERT INTO village_homestay
(village_id, homestay_name, address, `status`, star_level, room_count, bed_count, max_capacity, contact_name, contact_phone, description, price, cover_image)
VALUES
    (2, '稻香慢舍', '稻香村稻花路66号', 1, 4, 10, 15, 20, '赵亮', '13800002001', '稻田环绕，可体验收割', 5200.00, 'https://demo.com/img/dao1.jpg'),
    (2, '田畔小居', '稻香村桥头坝', 1, 3, 5, 8, 10, '孙娟', '13800002002', '亲子主题房，配儿童滑梯', 3600.00, 'https://demo.com/img/dao2.jpg'),
    (2, '稻舍·青旅', '稻香村文化广场东侧', 3, 2, 4, 14, 14, '周凯', '13800002003', '背包客青旅，已整体下架改造', 2100.00, 'https://demo.com/img/dao3.jpg');

-- 乡村 3（id=3）—— 竹林村，2 家民宿
INSERT INTO village_homestay
(village_id, homestay_name, address, `status`, star_level, room_count, bed_count, max_capacity, contact_name, contact_phone, description, price, cover_image)
VALUES
    (3, '竹里馆', '竹林村竹径通幽7号', 1, 5, 15, 22, 30, '胡雪', '13800003001', '全竹结构，自带书吧与茶室', 7800.00, 'https://demo.com/img/zhu1.jpg'),
    (3, '半山竹隐', '竹林村半山坡', 1, 4, 7, 10, 14, '高峰', '13800003002', '无边泳池俯瞰竹海', 6200.00, 'https://demo.com/img/zhu2.jpg');