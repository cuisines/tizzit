-- drop additionale column from Edtion table
ALTER TABLE edition DROP COLUMN update_site_index;
ALTER TABLE edition DROP COLUMN external_site_search;
-- drop addtionale table
DROP TABLE JMS_MESSAGES;
DROP TABLE JMS_ROLES;
DROP TABLE JMS_SUBSCRIPTIONS;
DROP TABLE JMS_TRANSACTIONS;
DROP TABLE JMS_USERS;
DROP TABLE TIMERS;
DROP TABLE XML_SEARCH_DB;
