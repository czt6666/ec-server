
ALTER TABLE village_homestay 
ADD COLUMN latitude DOUBLE COMMENT '纬度',
ADD COLUMN longitude DOUBLE COMMENT '经度',
ADD COLUMN qualification_images TEXT COMMENT '资质凭证图片',
ADD COLUMN link_address VARCHAR(500) COMMENT '链接地址';

ALTER TABLE village_homestay DROP COLUMN price;
