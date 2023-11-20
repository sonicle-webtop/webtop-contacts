@DataSource[default@com.sonicle.webtop.contacts]

-- ----------------------------
-- Update structure for contacts
-- ----------------------------
ALTER TABLE "contacts"."contacts" ADD COLUMN "creation_timestamp" timestamptz;

CREATE INDEX "contacts_ak6" ON "contacts"."contacts" USING btree ("category_id", "is_list", "revision_status", "href");

-- ----------------------------
-- Fix data for contacts
-- ----------------------------
UPDATE "contacts"."contacts" SET "creation_timestamp" = "revision_timestamp";

UPDATE contacts.contacts AS ccnts
SET
public_uid = md5(ccnts.public_uid || '.' || ccnts.contact_id) || '@' || cdoms.internet_name
FROM contacts.categories AS ccats, core.domains AS cdoms
WHERE (ccnts.category_id = ccats.category_id)
AND (ccats.domain_id = cdoms.domain_id)
AND (ccnts.href IS NULL);

UPDATE contacts.contacts AS ccnts
SET
href = ccnts.public_uid || '.vcf'
FROM contacts.categories AS ccats
WHERE (ccnts.category_id = ccats.category_id)
AND (ccnts.href IS NULL);

-- ----------------------------
-- Update structure for contacts
-- ----------------------------
ALTER TABLE "contacts"."contacts"
ALTER COLUMN "creation_timestamp" SET DEFAULT now(), 
ALTER COLUMN "creation_timestamp" SET NOT NULL;

-- ----------------------------
-- Table structure for contacts_vcards
-- ----------------------------
CREATE TABLE "contacts"."contacts_vcards" (
"contact_id" int4 NOT NULL,
"raw_data" text
);

-- ----------------------------
-- Primary Key structure for table contacts_vcards
-- ----------------------------
ALTER TABLE "contacts"."contacts_vcards" ADD PRIMARY KEY ("contact_id");
