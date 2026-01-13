@DataSource[default@com.sonicle.webtop.contacts]

-- ----------------------------
-- Update contacts table
-- ----------------------------
ALTER TABLE "contacts"."contacts" ADD COLUMN "tax_code" varchar(255), ADD COLUMN "vat_number" varchar(255);
