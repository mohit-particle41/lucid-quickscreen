#!/bin/zsh
echo "################ @Siddharth Trying to have a POST build STEP ################"
APKFILE2=`find . -name *.apk | head -1`
APPPATH=$APPCENTER_OUTPUT_DIRECTORY/*.apk
APPDIRECTORY=$APPCENTER_OUTPUT_DIRECTORY
cd ..
cd lucid-hearing-automation-tests
mvn -DskipTests -P prepare-for-upload package
ls -l
echo "apk file path is"
echo "$APPPATH"
appcenter test run appium --app "HearingLabs/LucidQuickscreen" --devices "HearingLabs/sample-test" --app-path $APPPATH --test-series "launch-tests" --locale "en_US" --build-dir target/upload
