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

-- ----------------------------
-- New table: contacts_custom_values
-- ----------------------------
CREATE TABLE "contacts"."contacts_custom_values" (
"contact_id" int4 NOT NULL,
"custom_field_id" varchar(22) NOT NULL,
"string_value" varchar(255),
"number_value" float8,
"boolean_value" bool,
"date_value" timestamptz(6),
"text_value" text
)
WITH (OIDS=FALSE)

;

ALTER TABLE "contacts"."contacts_custom_values" ADD PRIMARY KEY ("contact_id", "custom_field_id");
ALTER TABLE "contacts"."contacts_custom_values" ADD FOREIGN KEY ("contact_id") REFERENCES "contacts"."contacts" ("contact_id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "contacts"."contacts_custom_values" ADD FOREIGN KEY ("custom_field_id") REFERENCES "core"."custom_fields" ("custom_field_id") ON DELETE CASCADE ON UPDATE CASCADE;
