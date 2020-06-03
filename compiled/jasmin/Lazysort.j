.class public Lazysort
.super Quicksort

.method public <init>()V
	aload_0
	invokenonvirtual Quicksort/<init>()V
	return
.end method



.method public static main([Ljava/lang/String;)V
	.limit stack 99
	.limit locals 6
	bipush	10
	newarray int
	astore_1
	iconst_0
	istore_2
while_0:
	iload_2
	aload_1
	arraylength
	if_icmpge notLess_0
	iconst_1
	goto endLess_0
notLess_0:
	iconst_0
endLess_0:
	ifeq endWhile_0
	aload_1
	iload_2
	aload_1
	arraylength
	iload_2
	isub
	iastore
	iload_2
	iconst_1
	iadd
	istore_2
	goto while_0
endWhile_0:
	new Lazysort
	dup
	invokespecial Lazysort/<init>()V
	astore	4
	return
.end method


