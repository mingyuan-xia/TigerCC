del *.txt
for %%i in (*.tig) do java -classpath ../bin Parser.Main %%i >> %%i.txt 2>&1 