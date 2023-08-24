STRESS_TEST_PATH := /tmp/rinha-de-backend-2023-q3
GATLING_PATH := /tmp/gatling

infra-local-up:
	@docker-compose -f docker-compose-local.yml up --remove-orphans

infra-local-destroy:
	@docker-compose -f docker-compose-local.yml down
	@docker volume rm rinha-app_postgresql

deploy:
	@docker-compose -f docker-compose.yml up --remove-orphans

undeploy:
	@docker-compose -f docker-compose.yml down
	@docker volume rm rinha-app_postgresql

.PHONY: build
build:
	@{\
        ./gradlew clean build;\
        cp build/libs/rinha-app-0.0.1-SNAPSHOT.jar ./docker/application.jar;\
        cd ./docker;\
        docker build --no-cache -t weibemoura/rinha-app:latest .;\
    }

download-stress-test:
	@{\
	    rm -rf /tmp/rinha*;\
	    git clone --single-branch --quiet https://github.com/zanfranceschi/rinha-de-backend-2023-q3 $(STRESS_TEST_PATH);\
	}

download-gatling:
	@{\
	    cd /tmp;\
	    rm -rf /tmp/gatling*;\
	    wget --no-verbose https://repo1.maven.org/maven2/io/gatling/highcharts/gatling-charts-highcharts-bundle/3.9.5/gatling-charts-highcharts-bundle-3.9.5-bundle.zip;\
	    unzip gatling-charts-highcharts-bundle-3.9.5-bundle.zip;\
	    mkdir -p $(GATLING_PATH);\
	    cp -R gatling-charts-highcharts-bundle-3.9.5/* $(GATLING_PATH);\
	}

run-stress-test: download-stress-test download-gatling
	@sh $(GATLING_PATH)/bin/gatling.sh -rm local \
	    -s RinhaBackendSimulation \
	    -rd "DESCRICAO" \
	    -rf $(STRESS_TEST_PATH)/stress-test/user-files/results \
	    -sf $(STRESS_TEST_PATH)/stress-test/user-files/simulations \
	    -rsf $(STRESS_TEST_PATH)/stress-test/user-files/resources