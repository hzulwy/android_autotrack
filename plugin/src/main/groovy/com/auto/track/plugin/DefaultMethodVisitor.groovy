package com.auto.track.plugin

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter

class DefaultMethodVisitor extends AdviceAdapter{

    DefaultMethodVisitor(MethodVisitor mv, int access, String name, String desc) {
        super(Opcodes.ASM6, mv, access, name, desc)
    }

    @Override
    void visitEnd() {
        super.visitEnd()
    }
}