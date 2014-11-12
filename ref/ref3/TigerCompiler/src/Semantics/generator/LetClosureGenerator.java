//==========================================================================
//
//  File:        LetClosureGenerator.java
//  Location:    TigerCompiler <Java>
//  Description: Generate Closure for Let block
//  Version:     2008.12.28.
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

package Semantics.generator;

import java.util.*;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

import Syntax.node.*;
import Syntax.analysis.*;
import Semantics.generator.Generator.*;
import Semantics.verifier.VerifierBase.*;

public class LetClosureGenerator {
    public final String Name;
    protected final Generator cg;
    protected ClassWriter cw;

    protected final int THIS = 0;

    public LetClosureGenerator(String name, final Generator cg, final BlockNamespace c, ALetExpr cNode) {
        this.Name = name;
        this.cg = cg;

        cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cw.visit(V1_1, ACC_PUBLIC, name, null, "java/lang/Object", null);

        if (c.Parent != cg.vf.GlobalNamespace) {
            FieldVisitor fv = cw.visitField(ACC_PUBLIC, "_Closure", "L" + cg.GetClosureName(c.Parent) + ";", null, null);
            fv.visitEnd();
        }

        cNode.apply((new DepthFirstAdapter() {
            @Override
            public void caseAVariableDeclaration(AVariableDeclaration node) {
                Identifier id = c.GetIdentifier(node.getId().getText());

                FieldVisitor fv = cw.visitField(ACC_PUBLIC, id.SimpleName, cg.GetTypeJvmDescriptor(id.Type), null, null);
                fv.visitEnd();
            }

            @Override
            public void caseAMethodDeclaration(AMethodDeclaration node) {
                Identifier methodId = c.GetIdentifier(node.getId().getText());
                MethodType mt = (MethodType) (methodId.Type);

                Type t = mt.ReturnType;
                MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, methodId.SimpleName, cg.GetTypeJvmDescriptor(methodId.Type), null, null);

                ClosureSignature ca = cg.ClosureAccesses.get(node);
                mv.visitTypeInsn(NEW, ca.Name);
                mv.visitInsn(DUP);

                if (ca.ParentClosureName.equals("")) {
                    mv.visitMethodInsn(INVOKESPECIAL, ca.Name, "<init>", "()V");
                }
                else {
                    mv.visitVarInsn(ALOAD, THIS);
                    mv.visitMethodInsn(INVOKESPECIAL, ca.Name, "<init>", "(" + "L" + ca.ParentClosureName + ";" + ")V");
                }

                for (int i = 0; i < mt.Parameters.length; i += 1) {
                    Identifier id = mt.Parameters[i];

                    mv.visitInsn(DUP);

                    if (id.Type instanceof IntType) {
                        mv.visitVarInsn(ILOAD, i + 1);
                    }
                    else {
                        mv.visitVarInsn(ALOAD, i + 1);
                    }

                    mv.visitFieldInsn(PUTFIELD, ca.Name, id.SimpleName, cg.GetTypeJvmDescriptor(id.Type));
                }

                mv.visitMethodInsn(INVOKEVIRTUAL, ca.Name, "Eval", "()" + cg.GetTypeJvmDescriptor(t));

                if (t instanceof IntType) {
                    mv.visitInsn(IRETURN);
                }
                else if (t instanceof UnitType) {
                    mv.visitInsn(RETURN);
                }
                else {
                    mv.visitInsn(ARETURN);
                }
                mv.visitMaxs(0, 0); //maximum stack and local
                mv.visitEnd();
            }

            @Override
            public void caseALetExpr(ALetExpr node) {
                List<PDeclaration> copy = new ArrayList<PDeclaration>(node.getDeclaration());
                for (PDeclaration e : copy) {
                    e.apply(this);
                }
            }
        }));

        //constructor
        final MethodVisitor cmv;
        if (c.Parent == cg.vf.GlobalNamespace) {
            cmv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            //invokes super.<init>
            cmv.visitVarInsn(ALOAD, THIS);
            cmv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        }
        else {
            cmv = cw.visitMethod(ACC_PUBLIC, "<init>", "(" + "L" + cg.GetClosureName(c.Parent) + ";" + ")V", null, null);
            //invokes super.<init>
            cmv.visitVarInsn(ALOAD, THIS);
            cmv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");

            final int CLOSURE = 1;
            cmv.visitVarInsn(ALOAD, THIS);
            cmv.visitVarInsn(ALOAD, CLOSURE);
            cmv.visitFieldInsn(PUTFIELD, Name, "_Closure", "L" + cg.GetClosureName(c.Parent) + ";");
        }

        cNode.apply((new DepthFirstAdapter() {
            @Override
            public void caseAVariableDeclaration(AVariableDeclaration node) {
                Identifier id = c.GetIdentifier(node.getId().getText());

                cmv.visitVarInsn(ALOAD, THIS);
                node.getExpr().apply(new ExprAssembler(cg, cmv));
                cmv.visitFieldInsn(PUTFIELD, Name, id.SimpleName, cg.GetTypeJvmDescriptor(id.Type));
            }

            @Override
            public void caseAMethodDeclaration(AMethodDeclaration node) {
            }

            @Override
            public void caseALetExpr(ALetExpr node) {
                List<PDeclaration> copy = new ArrayList<PDeclaration>(node.getDeclaration());
                for (PDeclaration e : copy) {
                    e.apply(this);
                }
            }
        }));

        cmv.visitInsn(RETURN);
        cmv.visitMaxs(0, 0); //maximum stack and local
        cmv.visitEnd();


        //Evaluate method
        Type t = cg.vf.NodeTypeMapping.get(cNode.getExpr());
        final MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "Eval", "()" + cg.GetTypeJvmDescriptor(t), null, null);

        cNode.getExpr().apply(new ExprAssembler(cg, mv));

        if (t instanceof IntType) {
            mv.visitInsn(IRETURN);
        }
        else if (t instanceof UnitType) {
            mv.visitInsn(RETURN);
        }
        else {
            mv.visitInsn(ARETURN);
        }
        mv.visitMaxs(0, 0); //maximum stack and local
        mv.visitEnd();


        cw.visitEnd();
    }

    public byte[] Generate() {
        return cw.toByteArray();
    }
}
