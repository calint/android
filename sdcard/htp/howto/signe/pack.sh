jar="upload.jar"
wd=`pwd`
cd ../../bin&&
jar cvf $wd/$jar applet/upload*.class&&
jarsigner $wd/$jar applet.upload&&
ls -l $wd/$jar
echo done

