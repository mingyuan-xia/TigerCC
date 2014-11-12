SableCC Node Line/Position Info Patch

F.R.C.


There is a problem I encountered when using SableCC in my Tiger Compiler project, which is an assignment.
The problem is that there is no access to line/position info from most node(named Node) of the Abstract Syntax Tree.
It's caused when eliminating Concrete Syntax Tree node(named Token) and transforming Token to Node.

I just patched the some source code to add this info property to every Node.


File list:

sablecc-3.2.zip       SableCC 3.2 stable release.

patch\*.*             The patch. You should use them to overwrite the files in
                          sablecc-3.2.zip\sablecc-3.2\src\org\sablecc\sablecc
                      to build.

sablecc.jar           Patched version of SableCC.

COPYING-LESSER        The LGPL license file.


Note that the SableCC project itself is licensed under the LGPL license.
So the patch also follow the LGPL license.
