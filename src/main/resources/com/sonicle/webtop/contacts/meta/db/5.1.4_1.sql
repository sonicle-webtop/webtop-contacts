@DataSource[default@com.sonicle.webtop.contacts]

-- ----------------------------
-- Update structure for contacts
-- ----------------------------
ALTER TABLE "contacts"."contacts"
ALTER COLUMN "searchfield" TYPE varchar(1024),
ALTER COLUMN "firstname" TYPE varchar(255),
ALTER COLUMN "lastname" TYPE varchar(255);
