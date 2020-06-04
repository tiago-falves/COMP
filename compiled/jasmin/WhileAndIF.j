.class public WhileAndIF
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 99
	.limit locals 7
	bipush	20
	istore_1
	bipush	10
	istore_2
	bipush	10
	newarray int
	astore	4
	iload_1
	iload_2
	if_icmpge notLess_0
	iconst_1
	goto endLess_0
notLess_0:
	iconst_0
endLess_0:
	ifeq else_0
	iload_1
	iconst_1
	isub
	istore_3
	goto endIf_0
else_0:
	iload_2
	iconst_1
	isub
	istore_3
endIf_0:
while_0:
	iconst_0
	iconst_1
	isub
	iload_3
	if_icmpge notLess_1
	iconst_1
	goto endLess_1
notLess_1:
	iconst_0
endLess_1:
	ifeq endWhile_0
	aload	4
	iload_3
	iload_1
	iload_2
	isub
	iastore
	iload_3
	iconst_1
	isub
	istore_3
	iload_1
	iconst_1
	isub
	istore_1
	iload_2
	iconst_1
	isub
	istore_2
	goto while_0
endWhile_0:
	iconst_0
	istore_3
while_1:
	iload_3
	aload	4
	arraylength
	if_icmpge notLess_2
	iconst_1
	goto endLess_2
notLess_2:
	iconst_0
endLess_2:
	ifeq endWhile_1
	aload	4
	iload_3
	iaload
	invokestatic io/println(I)V
	iload_3
	iconst_1
	iadd
	istore_3
	goto while_1
endWhile_1:
	return
.end method


