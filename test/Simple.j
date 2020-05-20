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
	iconst_1
	ifeq else_0
	iconst_5
	iconst_4
	iconst_3
	imul
	imul
	newarray int
	astore	4
	goto endIf_0
else_0:
	iconst_5
	iconst_4
	iconst_3
	imul
	imul
	newarray int
	astore	4
endIf_0:
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


