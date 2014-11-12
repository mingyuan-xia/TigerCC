@echo ------------------------------
@echo Compiling SableCC files...
@echo ------------------------------
@call Compile.cmd
@echo ------------------------------
@cd src
@echo ------------------------------
@echo Compiling java files...
@echo ------------------------------
@javac -cp ..\lib\asm-all-3.1.jar -d ..\bin *.java Syntax\node\*.java Syntax\lexer\*.java Syntax\parser\*.java Syntax\analysis\*.java StringUtil\*.java Dumps\*.java Semantics\lexer\*.java Semantics\verifier\*.java Semantics\generator\*.java
@echo ------------------------------
@echo Build finished.
@echo ------------------------------
@cd..
@pause
