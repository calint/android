#!/bin/sh
# restart server before tests
#
# curl -s -b i=101201-033325.124-163a736b3 http://localhost:8082/test/t014

host=localhost:8082
sessionid=aaaa-110307-064915.110-cb48e04f
cookie="i=$sessionid"
content="file;806f1263aa27837d294207679ff946bc;35325;-;2011-03-08--06:10:00.000;2011-03-08--06:10:00.000;logo.jpg"

echo t000: file transfer from cache &&
curl -s http://$host/qa/t000.html > file &&
curl -s http://$host/qa/t000.html > file &&
diff -q file t000.html &&
rm file &&

echo t001: file transfer bigger  &&
curl -s http://$host/qa/t001.txt > file &&
diff -q file t001.txt &&
rm file &&

echo t002: file transfer byterange from 1  &&
curl -s -r 1- http://$host/qa/t001.txt > file &&
diff -q file t002.cmp &&
rm file &&

echo t010: page empty &&
curl -s http://$host/qa/t010 > file &&
diff -q file t010.cmp &&
rm file &&

echo t013: page large &&
curl -s http://$host/qa/t013 > file &&
diff -q file t013.cmp &&
rm file &&

echo t014: page input utf8 &&
curl -s -b $cookie http://$host/qa/t014?rst > file &&
curl -s -b $cookie http://$host/qa/t014 > file &&
diff -q file t014.cmp &&
rm file &&

echo t015: page post utf8 &&
curl -s -b $cookie --header "Content-Type: text/plain; charset=utf-8" --data-binary @t015.dat http://$host/qa/t014 > file &&
diff -q ../u/$sessionid/test.txt t015.cmp &&
rm -f ../u/$sessionid/test.txt &&

echo t016: page post ut8 read &&
curl -s -b $cookie http://$host/qa/t014 > file &&
diff -q file t016.cmp &&
rm file &&

echo t020: upload file init &&
curl -s -b $cookie http://$host/upload?$content > file &&
diff -q file t020.cmp &&
rm file &&

echo t021: upload file &&
curl -s -b $cookie --header "Content-Type: $content" --data-binary @logo.jpg http://$host/upload &&
echo t022: upload file diff&&
diff -q ../u/$sessionid/upload/logo.jpg logo.jpg &&
bck=`pwd`&&
fl=$bck/file1&&
cd ../u/$sessionid/upload&&
ls -l logo.jpg>$fl&&
rm logo.jpg&&
cd $bck&&
ls -l logo.jpg>file2&&
diff -q file1 file2&&
rm file1 file2&&

rm -rf ../u/$sessionid &&
echo done

