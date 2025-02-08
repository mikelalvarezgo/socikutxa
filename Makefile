
SHELL := /bin/bash

EXECUTABLES=docker docker-compose sbt npm widdershins


ifeq (logs, $(firstword $(MAKECMDGOALS)))
   logargs := $(wordlist 2, $(words $(MAKECMDGOALS)), $(MAKECMDGOALS))
   $(eval $(logargs):;@true)
endif

##	build:			Build or rebuild hermes services
.PHONY : build
build:
	@docker-compose -f stack/services.yaml build

 ## 	up:			Build and UP de environment
.PHONY : up
up:
	@docker-compose -f stack/services.yaml up -d --build

## 	down:			Brings the environment down
 .PHONY : down
 down:
	@docker-compose -f stack/services.yaml down

## 	buildMongo:		Build mongo environment
 .PHONY : buildMongo
 buildMongo:
	sbt "project odds_checker; runMain com.mikelalvarezgo.socikutxa.odds_checker.infrastructure.mongo.MongoEnvironmentBuilder;"

## 	dropMongo:		Drop mongo environment
 .PHONY : buildMongo
 dropMongo:
	sbt "project odds_checker; runMain com.mikelalvarezgo.socikutxa.odds_checker.infrastructure.mongo.MongoEnvironmentDropper;"
