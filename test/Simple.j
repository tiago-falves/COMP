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
	aload	5
	iconst_1
	iconst_1
	invokevirtual Simple/add(II)I
	iconst_4
	iadd
	istore_2
	aload	5
	iconst_1
	iconst_1
	invokevirtual Simple/add(II)I
	newarray int
	astore	4
	iload_2
	invokestatic ioa/println(I)V
	iconst_4
	iconst_3
	imul
	iconst_2
	iadd
	istore	4
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


