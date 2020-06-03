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
	if_icmpge notLess_31
	iconst_1
	goto endLess_31
notLess_31:
	iconst_0
endLess_31:
	ifeq else_31
	iload_1
	iconst_1
	isub
	istore_3
	goto endIf_31
else_31:
	iload_2
	iconst_1
	isub
	istore_3
endIf_31:
while_10:
	iconst_0
	iconst_1
	isub
	iload_3
	if_icmpge notLess_32
	iconst_1
	goto endLess_32
notLess_32:
	iconst_0
endLess_32:
	ifeq endWhile_10
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
	goto while_10
endWhile_10:
	iconst_0
	istore_3
while_11:
	iload_3
	aload	4
	arraylength
	if_icmpge notLess_33
	iconst_1
	goto endLess_33
notLess_33:
	iconst_0
endLess_33:
	ifeq endWhile_11
	aload	4
	iload_3
	iaload
	invokestatic io/println(I)V
	iload_3
	iconst_1
	iadd
	istore_3
	goto while_11
endWhile_11:
	return
.end method


