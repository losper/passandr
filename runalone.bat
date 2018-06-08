cd app/build/outputs/apk/debug
adb push ./app-debug.apk /data/local/tmp/org.passoa.poseidonandr
adb shell pm install -t -r "/data/local/tmp/org.passoa.poseidonandr"
adb shell am startservice -n org.passoa.poseidonandr/.AutoService
cd ../../../../../