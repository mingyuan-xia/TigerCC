Tiger to JVM ByteCode Compiler
Copyright(C) F.R.C., All rights reserved.

This is an exercise project finished in Shanghai Jiaotong University.



Language Specification Modification:

1.
Binary operators "&" and "|" now have the same precedence, and both are left associated. But they can not associate each other.
The original specification says that "&" is more precedential than "|".

2.
All type-identifiers in the declaration list of the let block are now in the same namespace.
So do all the variables and methods.
This forbids cases in "test47.tig" and "test48.tig", which are quite confusing.



Deployment Issues:

1.
You need a Java Runtime Environment SE to get the compiler and your compiled code to run.
I develop it with Java Development Kit 6 Update 10 (jdk-6u10-windows-i586-p.exe).

2.
Compiled code will not run if the TigerStandardLibrary.class is not placed with it. You can copy it from "bin\".
The Tiger Standard Library contains the standard functions specified in the "Tiger Language Reference Manual"("doc\tiger.pdf").



Copyright Issues:

1.
The compiler itself is licensed under the BSD license, see "doc\License.txt" for detail.

2.
The compiler use a patched SableCC 3.2 to build its lexer and parser.
SableCC and the patch is licensed under the LGPL license, see "sablecc\" and "sablecc\COPYING-LESSER" for detail.

3.
The compiler use the ASM 3.1 to access Java Virtual Machine (JVM) Byte-Code and generate class file.
ASM is licensed under the BSD license, see "asm\" for detail.
