//==========================================================================
//
//  File:        Verifier.java
//  Location:    TigerCompiler <Java>
//  Description: Verifier responsible to check Naming, Typing and Variable Initializing
//  Version:     2008.12.26.
//  License:     BSD License
//
//  Copyright(C) F.R.C., All rights reserved.
//  Redistribution and use in source and binary forms, with or without
//  modification, are permitted provided that the following conditions are met:
//      * Redistributions of source code must retain the above copyright
//        notice, this list of conditions and the following disclaimer.
//      * Redistributions in binary form must reproduce the above copyright
//        notice, this list of conditions and the following disclaimer in the
//        documentation and/or other materials provided with the distribution.
//      * Neither the name of the the copyright holders nor the
//        names of its contributors may be used to endorse or promote products
//        derived from this software without specific prior written permission.
//  
//  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
//  EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
//  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
//  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
//  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
//  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
//  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
//  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
//  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//
//==========================================================================

package Semantics.verifier;

public class Verifier extends VerifierExpr {

    //Mainly 4 parts take place here.
    //0.      Map all declarations, TypeName->TypeIdentifier or Name->Identifier.
    //        Check TypeRedefination, Redefination.
    //1.      Map all identifier accesses, Node->Identifier.
    //        Check TypeMismatch, IdentifierNotFound.
    //2.      Map all expression, Node->TypeOfType or Node->Type.
    //        Check TypeNotFound, TypeMismatch, RecordIncomplete, ExpressionNotRecieved(Warning).
    //3.      Map all declarations, TypeIdentifier->TypeOfType or Identifier->Type.
    //        Check TypeBindMismatch, TypeBindVoid, TypeBindDirectRecursion, TypeBindEquivalenceRecursion and do variable type inferring, type alias elimilation, primitive type binding.
    //
    //Additionally parts are:
    //4.      Check VariableNotInitialized(Warning), by creating a Identifier->Initialized mapping when writing and checking it when reading.
    //5.      Check IllegalBreakStatement, by checking outer For or While level.
    //6.      Check IntegerLiteralOverflow.
    //
    //They take place in a non-deterministic order and are divided into 3 passes.
    //Pass 0 is used as we need to ensure type and method recursive references and cross references.
    //Pass 1,2 is used to complete type declaration and fill all type info for type-identifiers.
    //
    //Pass 0: 
    //        TypeDeclaration(In)
    //        ---- 0.TypeName->TypeIdentifier, 3.TypeIdentifier->TypeOfType
    //        MethodDeclaration(In)
    //        ---- 0.Name->Identifier
    //
    //Pass 1: 
    //        TypeDeclaration(In)
    //        ---- 3.TypeBindDirectRecursion, 3.TypeBindEquivalenceRecursion, 3.type alias elimilation
    //
    //Pass 2: 
    //        SingleTypeCombination(In), ArrayTypeCombination(Out), RecordTypeCombination(Out)
    //        ---- 2.Node->TypeOfType
    //        TypeDeclaration(Out)
    //        ---- 4.TypeIdentifier->TypeOfType, 3.TypeBindEquivalenceRecursion
    //
    //Pass 3:
    //        MethodDeclaration(Out)
    //        ---- 3.Identifier->Type
    //        ForExpr(In)
    //        ---- 0.Name->Identifier, 3.Identifier->Type
    //
    //Pass 4:
    //        VariableDeclaration(Out)
    //        ---- 1.Name->Identifier, 3.variable type inferring, 3.Identifier->Type
    //        MethodDeclaration(Out)
    //        ---- Check Method TypeMismatch
    //        SingleLvalue(In), MethodExpr(In)
    //        ---- 1.Node->Identifier
    //        SingleLvalue(Out), ListLvalue(Out), ArrayLvalue(Out), xxxExpr(Out)
    //        ---- 2.Node->Type
    //        WhileExpr(In, Out), ForExpr(In, Out), LetExpr(In, Out), BreakExpr(In)
    //        ---- 5.LoopLevelStack
    //        IntExpr(In)
    //        ---- 6.IntegerParseTest

    public Verifier() {
        super(5);
    }
}
