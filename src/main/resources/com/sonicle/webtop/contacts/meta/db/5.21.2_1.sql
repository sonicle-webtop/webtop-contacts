@DataSource[default@com.sonicle.webtop.contacts]

-- ----------------------------
-- Update contacts table
-- ----------------------------
ALTER TABLE "contacts"."contacts" ADD COLUMN "e_invoicing_code" varchar(255);
