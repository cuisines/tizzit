# mysql
alter table `edition` add column `WORK_SERVER_EDITION_ID` INTEGER NULL;
alter table `edition` add column `DEPLOY_STATUS` LONGBLOB NULL;
alter table `edition` add column `START_ACTION_TIMESTAMP` BIGINT(20) NULL;
alter table `edition` add column `END_ACTION_TIMESTAMP` BIGINT(20) NULL;
alter table `site` add column `UPDATE_SITE_INDEX` SMALLINT NULL;
alter table `site` add column `EXTERNAL_SITE_SEARCH` SMALLINT NULL;
alter table `edition` add column `DEPLOY_TYPE` INTEGER NULL;
# oracle
alter table edition add WORK_SERVER_EDITION_ID NUMBER(10) NULL;
alter table edition add DEPLOY_TYPE NUMBER(10) NULL;
alter table edition add DEPLOY_STATUS BLOB NULL;
alter table edition add START_ACTION_TIMESTAMP NUMBER(20) NULL;
alter table edition add END_ACTION_TIMESTAMP NUMBER(20) NULL;
alter table site add UPDATE_SITE_INDEX NUMBER(5) NULL;
alter table site add EXTERNAL_SITE_SEARCH NUMBER(5) NULL;
# sapdb
alter table edition add WORK_SERVER_EDITION_ID INTEGER NULL;
alter table edition add DEPLOY_TYPE INTEGER NULL;
alter table edition add DEPLOY_STATUS LONG BYTE NULL;
alter table edition add START_ACTION_TIMESTAMP DECIMAL(20) NULL;
alter table edition add END_ACTION_TIMESTAMP DECIMAL(20) NULL;
alter table site add UPDATE_SITE_INDEX SMALLINT NULL;
alter table site add EXTERNAL_SITE_SEARCH SMALLINT NULL;
# mssql
alter table edition add WORK_SERVER_EDITION_ID INTEGER NULL;
alter table edition add DEPLOY_TYPE INTEGER NULL;
alter table edition add DEPLOY_STATUS IMAGE NULL;
alter table edition add START_ACTION_TIMESTAMP BIGINT NULL;
alter table edition add END_ACTION_TIMESTAMP BIGINT NULL;
alter table site add UPDATE_SITE_INDEX SMALLINT NULL;
alter table site add EXTERNAL_SITE_SEARCH SMALLINT NULL;
