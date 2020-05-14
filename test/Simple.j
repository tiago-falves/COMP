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
	iconst_0
	istore	4
while_0:
	iload	4
	iconst_1
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


