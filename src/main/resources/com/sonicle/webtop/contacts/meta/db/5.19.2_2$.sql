@DataSource[default@com.sonicle.webtop.contacts]

CREATE OR REPLACE FUNCTION "contacts"."prune_contacts_history_on"()
  RETURNS "pg_catalog"."trigger" AS $BODY$BEGIN
	IF TG_OP = 'INSERT' THEN
		DELETE FROM "contacts"."history_contacts" WHERE "contact_id" = NEW."contact_id" AND "change_timestamp" < NEW."change_timestamp" AND "change_type" = NEW."change_type" AND "category_id" = NEW."category_id";
		RETURN NEW;
	END IF;
END$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100