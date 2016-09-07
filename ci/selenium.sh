#!/bin/bash

pushd `dirname $0` > /dev/null
root=$(pwd -P)/..
popd > /dev/null

artifact=selenium-server-standalone-3.0.0-beta3.jar
url="http://selenium-release.storage.googleapis.com/3.0-beta3/$artifact"

curl -o $root/$artifact -O $url

java -jar $root/$artifact &
jar_pid=$!

mvn clean install
mvn surefire:test

kill -9 $jar_pid

rm -f $root/$artifact
