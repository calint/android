BIN=xii
g++ -o $BIN *.cc -lX11 -lXft -I/usr/include/freetype2/ -Wfatal-errors -Wall -Wextra&&
ls -l $BIN
