.class public Simple
.super java/lang/Object
.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 99
	.limit locals 3
	iconst_5
	newarray int
	astore_3
	aload_3
	iconst_1
	iconst_5
	iastore
	aload_3
	iconst_1
	iaload
	istore_2
	iload_2
	invokestatic ioa/println(I)V
	return
.end method

.method public add(II)I
	.limit stack 99
	.limit locals 3
	iload_1
	iload_2
	iadd
	ireturn
.end method


