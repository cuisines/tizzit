# mysql
alter table `edition` add column `WORK_SERVER_EDITION_ID` INTEGER NULL;
alter table `edition` add column `DEPLOY_STATUS` LONGBLOB NULL;
alter table `edition` add column `START_ACTION_TIMESTAMP` BIGINT(20) NULL;
alter table `edition` add column `END_ACTION_TIMESTAMP` BIGINT(20) NULL;
alter table `site` add column `UPDATE_SITE_INDEX` SMALLINT NULL;
alter table `site` add column `EXTERNAL_SITE_SEARCH` SMALLINT NULL;
alter table `edition` add column `DEPLOY_TYPE` INTEGER NULL;
ALTER TABLE `picture` ADD COLUMN `title` VARCHAR(255) NULL;
ALTER TABLE `picture` ADD COLUMN `VIEW_COMPONENT_ID_FK` INTEGER NULL;
ALTER TABLE document ADD COLUMN `VIEW_COMPONENT_ID_FK` INTEGER NULL;
alter table `site` add column `PREVIEW_URL_LIVE_SERVER` VARCHAR(255) NULL;
alter table `site` add column `PREVIEW_URL_WORK_SERVER` VARCHAR(255) NULL;
alter table `site` drop COLUMN `PREVIEW_URL` ;
drop table EDITIONSLICE;
CREATE TABLE access_roles2view_components (
	access_role2_view_component_id INTEGER NOT NULL,
	login_page_id_fk INTEGER NOT NULL,
	access_roles_id_fk VARCHAR(255) NOT NULL, 
	view_components_id_fk INTEGER NOT NULL,
	PRIMARY KEY (access_role2_view_component_id)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 ;

CREATE TABLE ACCESSROLE (
	role_id VARCHAR(255) NOT NULL, 
	PRIMARY KEY (role_id)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 ;
alter table viewcomponent add column ACCESSROLE2VC_ID_FK integer null;

# oracle
alter table edition add WORK_SERVER_EDITION_ID NUMBER(10) NULL;
alter table edition add DEPLOY_TYPE NUMBER(10) NULL;
alter table edition add DEPLOY_STATUS BLOB NULL;
alter table edition add START_ACTION_TIMESTAMP NUMBER(20) NULL;
alter table edition add END_ACTION_TIMESTAMP NUMBER(20) NULL;
alter table site add UPDATE_SITE_INDEX NUMBER(5) NULL;
alter table site add EXTERNAL_SITE_SEARCH NUMBER(5) NULL;
ALTER TABLE picture ADD title VARCHAR(255) NULL;
ALTER TABLE picture ADD VIEW_COMPONENT_ID_FK NUMBER(10) NULL;
ALTER TABLE document ADD VIEW_COMPONENT_ID_FK NUMBER(10) NULL;
ALTER TABLE site add PREVIEW_URL_LIVE_SERVER VARCHAR(255) null;
ALTER TABLE site add PREVIEW_URL_WORK_SERVER VARCHAR(255) null;
alter table site drop COLUMN PREVIEW_URL ;
drop table EDITIONSLICE;
CREATE TABLE "ACCESSROLE" (
	"ROLE_ID" VARCHAR2(255) NOT NULL,
	PRIMARY KEY (role_id));
CREATE TABLE access_roles2view_components (
	access_roles_id_fk VARCHAR(255) NOT NULL, 
	view_components_id_fk INTEGER NOT NULL,
	PRIMARY KEY (access_roles_id_fk, view_components_id_fk)
);
alter table viewcomponent add ACCESSROLE2VC_ID_FK NUMBER(10) null;
	
# sapdb
alter table edition add WORK_SERVER_EDITION_ID INTEGER NULL;
alter table edition add DEPLOY_TYPE INTEGER NULL;
alter table edition add DEPLOY_STATUS LONG BYTE NULL;
alter table edition add START_ACTION_TIMESTAMP DECIMAL(20) NULL;
alter table edition add END_ACTION_TIMESTAMP DECIMAL(20) NULL;
alter table site add UPDATE_SITE_INDEX SMALLINT NULL;
alter table site add EXTERNAL_SITE_SEARCH SMALLINT NULL;
ALTER TABLE picture ADD (title VARCHAR(255) NULL);
ALTER TABLE picture ADD (VIEW_COMPONENT_ID_FK INTEGER(10) NULL);
ALTER TABLE document ADD (VIEW_COMPONENT_ID_FK INTEGER(10) NULL);
ALTER TABLE site add (PREVIEW_URL_LIVE_SERVER VARCHAR(255) NULL);
ALTER TABLE site add (PREVIEW_URL_WORK_SERVER VARCHAR(255) NULL);
alter table site drop COLUMN PREVIEW_URL ;
drop table EDITIONSLICE;
CREATE TABLE ACCESSROLE (
	"ROLE_ID" VARCHAR(255) NOT NULL,
	PRIMARY KEY (role_id))
//

alter table viewcomponent add ACCESSROLE2VC_ID_FK integer null;
# mssql
alter table edition add WORK_SERVER_EDITION_ID INTEGER NULL;
alter table edition add DEPLOY_TYPE INTEGER NULL;
alter table edition add DEPLOY_STATUS IMAGE NULL;
alter table edition add START_ACTION_TIMESTAMP BIGINT NULL;
alter table edition add END_ACTION_TIMESTAMP BIGINT NULL;
alter table site add UPDATE_SITE_INDEX SMALLINT NULL;
alter table site add EXTERNAL_SITE_SEARCH SMALLINT NULL;
ALTER TABLE picture ADD title VARCHAR(255) NULL;
ALTER TABLE picture ADD VIEW_COMPONENT_ID_FK INTEGER NULL;
ALTER TABLE document ADD VIEW_COMPONENT_ID_FK INTEGER NULL;
ALTER TABLE site add PREVIEW_URL_LIVE_SERVER VARCHAR(255) null;
ALTER TABLE site add PREVIEW_URL_WORK_SERVER VARCHAR(255) null;
alter table site drop COLUMN PREVIEW_URL ;
drop table EDITIONSLICE;
CREATE TABLE ACCESSROLE (
	role_id VARCHAR(255) NOT NULL, 
	PRIMARY KEY CLUSTERED(role_id)
);
alter table viewcomponent add ACCESSROLE2VC_ID_FK integer null;
ALTER TABLE document ADD password VARCHAR(45) NULL;
alter table unit add COLOUR VARCHAR2(255 BYTE);
ALTER TABLE DOCUMENT
ADD (
	LABEL VARCHAR2(255 BYTE), 
	DESCRIPTION VARCHAR2(255 BYTE), 
	SEARCHABLE NUMBER(5,0) DEFAULT '1'
);