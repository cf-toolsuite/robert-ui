#!/usr/bin/env bash

APP_NAME="robert-ui"
APP_VERSION="0.0.1-SNAPSHOT"

REFACTOR_SERVICE_HOST="robert-brash-pangolin-dr.apps.dhaka.cf-app.com"

COMMAND=$1

CONFIG_SERVICE_NAME="robert-config"

case $COMMAND in

setup)

    echo && printf "\e[37mℹ️  Creating user-provided service to hold connection details to an available robert instance ...\e[m\n" && echo

    cf create-user-provided-service $CONFIG_SERVICE_NAME -p "{\"REFACTOR_SERVICE_SCHEME\": \"https\", \"REFACTOR_SERVICE_HOST\": \"$REFACTOR_SERVICE_HOST\", \"REFACTOR_SERVICE_PORT\": \"443\" }"

    echo && printf "\e[37mℹ️  Deploying $APP_NAME application ...\e[m\n" && echo
    cf push $APP_NAME -k 1GB -m 1GB -p build/libs/$APP_NAME-$APP_VERSION.jar --no-start --random-route

    echo && printf "\e[37mℹ️  Binding services ...\e[m\n" && echo
    cf bind-service $APP_NAME $CONFIG_SERVICE_NAME

    echo && printf "\e[37mℹ️  Setting environment variables for use by $APP_NAME application ...\e[m\n" && echo
    cf set-env $APP_NAME JAVA_OPTS "-Djava.security.egd=file:///dev/urandom -XX:+UseG1GC -XX:+UseStringDeduplication"
    cf set-env $APP_NAME SPRING_PROFILES_ACTIVE "default,cloud"
    cf set-env $APP_NAME JBP_CONFIG_OPEN_JDK_JRE "{ jre: { version: 21.+ } }"
    cf set-env $APP_NAME JBP_CONFIG_SPRING_AUTO_RECONFIGURATION "{ enabled: false }"

    echo && printf "\e[37mℹ️  Starting $APP_NAME application ...\e[m\n" && echo
    cf start $APP_NAME

    ;;

teardown)
    cf unbind-service $APP_NAME $CONFIG_SERVICE_NAME

    cf delete-service $CONFIG_SERVICE_NAME -f

    cf delete $APP_NAME -f -r

    ;;

*)
    echo && printf "\e[31m⏹  Usage: setup/teardown \e[m\n" && echo
    ;;
esac
