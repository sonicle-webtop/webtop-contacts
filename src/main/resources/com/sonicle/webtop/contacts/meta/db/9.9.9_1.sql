@DataSource[default@com.sonicle.webtop.contacts]

-- ----------------------------
-- New table: contacts_tags
-- ----------------------------
CREATE TABLE "contacts"."contacts_tags" (
"contact_id" int4 NOT NULL,
"tag_id" varchar(22) NOT NULL
)
WITH (OIDS=FALSE)

;

CREATE INDEX "contacts_tags_ak1" ON "contacts"."contacts_tags" USING btree ("tag_id");
ALTER TABLE "contacts"."contacts_tags" ADD PRIMARY KEY ("contact_id", "tag_id");
ALTER TABLE "contacts"."contacts_tags" ADD FOREIGN KEY ("tag_id") REFERENCES "core"."tags" ("tag_id") ON DELETE CASCADE ON UPDATE CASCADE;
