#!/bin/bash

pushd `dirname $0` > /dev/null
root=$(pwd -P)/..
popd > /dev/null

type firefox >/dev/null 2>&1 && firefox -v

artifact=selenium-server-standalone-2.53.1.jar
url="http://selenium-release.storage.googleapis.com/2.53/$artifact"

curl -o $root/$artifact -O $url

java -jar $root/$artifact &
jar_pid=$!

mvn clean install
mvn surefire:test

code=$?

kill -9 $jar_pid

rm -f $root/$artifact

exit $code
