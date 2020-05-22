.class public Simple
.super java/lang/Object
.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 11
	.limit locals 6
	new Simple
	dup
	invokespecial Simple/<init>()V
	astore	4
	iconst_5
	newarray int
	astore_3
	aload_3
	iconst_1
	iconst_5
	iastore
	aload	4
	iconst_1
	iconst_1
	invokevirtual Simple/add(II)I
	aload_3
	iconst_1
	iaload
	imul
	iconst_2
	aload_3
	iconst_1
	iaload
	imul
	iadd
	istore_2
	iconst_1
	ifeq else_0
	aload_3
	iconst_1
	iaload
	istore_2
	goto endIf_0
else_0:
	iconst_1
	istore_2
endIf_0:
	iload_2
	invokestatic ioa/println(I)V
	return
.end method

.method public add(II)I
	.limit stack 2
	.limit locals 3
	iload_1
	iload_2
	iadd
	ireturn
.end method


