pwd&&
cp -vp alias ~/.alias&&
cp -vp bashrc ~/.bashrc&&
cp -vp xinitrc ~/.xinitrc&&
cp -vp gtkrc-2.0 ~/.gtkrc-2.0&&
cd ~&&
echo -n link&&
ln -vfs .xinitrc .xsession&&
pwd&&
echo "done"
