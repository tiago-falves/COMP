.class public WhileAndIF
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 4
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
	if_icmpge notLess_80
	iconst_1
	goto endLess_80
notLess_80:
	iconst_0
endLess_80:
	ifeq else_59
	iload_1
	iconst_1
	isub
	istore_3
	goto endIf_59
else_59:
	iload_2
	iconst_1
	isub
	istore_3
endIf_59:
while_22:
	iconst_0
	iconst_1
	isub
	iload_3
	if_icmpge notLess_81
	iconst_1
	goto endLess_81
notLess_81:
	iconst_0
endLess_81:
	ifeq endWhile_22
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
	goto while_22
endWhile_22:
	iconst_0
	istore_3
while_23:
	iload_3
	aload	4
	arraylength
	if_icmpge notLess_82
	iconst_1
	goto endLess_82
notLess_82:
	iconst_0
endLess_82:
	ifeq endWhile_23
	aload	4
	iload_3
	iaload
	invokestatic io/println(I)V
	iload_3
	iconst_1
	iadd
	istore_3
	goto while_23
endWhile_23:
	return
.end method


