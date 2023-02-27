package com.auto.track.plugin

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter

class DefaultMethodVisitor extends AdviceAdapter{

    /**
     * Creates a new {@link AdviceAdapter}.
     *
     * @param api
     *            the ASM API version implemented by this visitor. Must be one
     *            of {@link Opcodes#ASM4}, {@link Opcodes#ASM5} or {@link Opcodes#ASM6}.
     * @param mv
     *            the method visitor to which this adapter delegates calls.
     * @param access
     *            the method's access flags (see {@link Opcodes}).
     * @param name
     *            the method's name.
     * @param desc
     *            the method's descriptor (see {@link Type Type}).
     */
    protected DefaultMethodVisitor(int api, MethodVisitor mv, int access, String name, String desc) {
        super(api, mv, access, name, desc)
    }
}