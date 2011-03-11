file=ab-test.log
host=localhost
port=8088
nreq=10000
echo>$file
ab -c1    -n1 -v5 http://$host:$port/>>$file
for c in 1 10 100 1000 10000;do 
	echo "clients: $c    requests: $nreq" 
	ab -c$c -n$nreq http://$host:$port/>>$file
done
