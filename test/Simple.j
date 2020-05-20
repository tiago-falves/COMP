.class public Simple
.super java/lang/Object
.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 99
	.limit locals 99
	new Simple
	dup
	invokespecial Simple/<init>()V
	astore	5
	iconst_1
	iconst_0
	iand
	istore_3
	aload	5
	iconst_1
	iconst_1
	invokevirtual Simple/add(II)I
	iconst_4
	iconst_2
	isub
	imul
	iconst_3
	imul
	istore_2
	iload_3
	ifeq else_0
	iload_2
	invokestatic ioa/println(I)V
	goto endIf_0
else_0:
endIf_0:
	iload_2
	iconst_1
	iadd
	istore_2
	iload_2
	invokestatic ioa/println(I)V
	return
.end method

.method public add(II)I
	.limit stack 99
	.limit locals 99
	iload_1
	iload_2
	iadd
	ireturn
.end method


