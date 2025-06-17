#!/bin/bash

export AAPT2=
export JAR=
export D8=
export ZIPALIGN=
export APKSIGNER=
export MIN_SDK=
export TARGET_SDK=
export ADB=

$AAPT2 compile --dir res -o compiled_res/res.zip

$AAPT2 link -o base.apk -I $JAR --manifest AndroidManifest.xml --min-sdk-version $MIN_SDK  --target-sdk-version $TARGET_SDK --java gen compiled_res/res.zip

javac --release 21 -cp $JAR:gen -d classes src/com/azirariza/jflow/*.java
$D8 --lib $JAR --min-api $MIN_SDK --output . classes/com/azirariza/jflow/*.class

mkdir apk 
unzip base.apk -d apk 
cp classes.dex apk/

cd apk && zip -0 -X -r  ../app-unsigned.apk .

cd ..

if [ ! -f debug.keystore ]; then 
    keytool -genkeypair -v -keystore debug.keystore -storepass android -alias androiddebugkey -keypass android -dname "CN=Android Debug,O=Android,C=US" -keyalg RSA -keysize 2048 -validity 10000
fi 

$ZIPALIGN -f -p 4 app-unsigned.apk app-aligned.apk

$APKSIGNER sign --min-sdk-version $MIN_SDK --ks debug.keystore --ks-pass pass:android  --out app-release.apk app-aligned.apk

$ADB install ./app-release.apk
