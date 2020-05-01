.class public Simple
.super java/lang/Object
.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 99
	.limit locals 99
	bipush	30
	istore_2
	iconst_0
	bipush	10
	isub
	istore_3
	iload_2
	iload_3
	iadd
	istore_2
	new Simple
	dup
	invokespecial Simple/<init>()V
	astore	4
.end method

.method public add(II)I
	.limit stack 99
	.limit locals 99
	iload_1
	iload_2
	iadd
	ireturn
.end method


