
ab -c 1 -n 1 -v 5 http://localhost:$port/
pause
for c in 1 10 100;do
	ab -c $c -n 10000 http://localhost:$port/
done
