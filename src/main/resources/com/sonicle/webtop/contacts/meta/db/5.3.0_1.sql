@DataSource[default@com.sonicle.webtop.contacts]

-- ----------------------------
-- Update structure for categories
-- ----------------------------

ALTER TABLE "contacts"."categories"
ADD COLUMN "remote_sync_frequency" int2,
ADD COLUMN "remote_sync_timestamp" timestamptz,
ADD COLUMN "remote_sync_tag" varchar(255);

-- ----------------------------
-- Indexes structure for table categories
-- ----------------------------
CREATE INDEX "categories_ak3" ON "contacts"."categories" ("provider", "remote_sync_frequency");
