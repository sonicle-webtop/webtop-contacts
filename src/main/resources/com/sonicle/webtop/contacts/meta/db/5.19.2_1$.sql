@DataSource[default@com.sonicle.webtop.contacts]

CREATE OR REPLACE FUNCTION "contacts"."prune_categories_history_on"()
  RETURNS "pg_catalog"."trigger" AS $BODY$BEGIN
	IF TG_OP = 'INSERT' THEN
		DELETE FROM "contacts"."history_categories" WHERE "category_id" = NEW."category_id" AND "change_timestamp" < NEW."change_timestamp" AND "change_type" = NEW."change_type";
		RETURN NEW;
	END IF;
END$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100