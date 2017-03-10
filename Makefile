SERVER_ROOT = server/
SERVER_ADMIN_BUILD = server/src/main/webapp/admin
ADMIN_ROOT = admin/
ADMIN_BUILD = admin/build/web

$(ADMIN_BUILD):
	cd $(ADMIN_ROOT); pub get
	cd $(ADMIN_ROOT); pub build

$(ADMIN_BUILD)-always:
	cd $(ADMIN_ROOT); pub get
	cd $(ADMIN_ROOT); pub build
	cd $(ADMIN_ROOT)/build; rm `find . -name *ng_*.json`

admin-clean:
	rm -rf $(ADMIN_BUILD)

admin: $(ADMIN_BUILD)-always

admin-install: admin
	mkdir -p $(SERVER_ADMIN_BUILD)
	cp -r $(ADMIN_BUILD)/* $(SERVER_ADMIN_BUILD)

server-clean:
	rm -rf $(SERVER_ADMIN_BUILD)

appengine-prod: admin-install
	cd $(SERVER_ROOT); mvn appengine:update -Pprod

appengine-test: admin-install
	cd $(SERVER_ROOT); mvn appengine:update -Ptest

