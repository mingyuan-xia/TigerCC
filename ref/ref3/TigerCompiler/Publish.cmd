@echo off
@PATH %ProgramFiles%\WinRar;%PATH%

@call Clear.cmd
@cd ..
@del TigerCompiler.rar
@rar a -av- -m5 -md4096 -tsm -tsc -s -k -t TigerCompiler.rar TigerCompiler
@copy TigerCompiler.rar Versions\
