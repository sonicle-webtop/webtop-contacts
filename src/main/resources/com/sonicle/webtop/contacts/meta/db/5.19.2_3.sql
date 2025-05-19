@DataSource[default@com.sonicle.webtop.contacts]

-- ----------------------------
-- Add triggers to keep history tables clean
-- ----------------------------
DROP TRIGGER IF EXISTS "history_categories_trg_prune" ON "contacts"."history_categories";
CREATE TRIGGER "history_categories_trg_prune" AFTER INSERT ON "contacts"."history_categories"
FOR EACH ROW
EXECUTE PROCEDURE "contacts"."prune_categories_history_on"();

DROP TRIGGER IF EXISTS "history_contacts_trg_prune" ON "contacts"."history_contacts";
CREATE TRIGGER "history_contacts_trg_prune" AFTER INSERT ON "contacts"."history_contacts"
FOR EACH ROW
EXECUTE PROCEDURE "contacts"."prune_contacts_history_on"();