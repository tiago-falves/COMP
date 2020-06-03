.class public QuickSort
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 99
	.limit locals 5
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
	new QuickSort
	dup
	invokespecial QuickSort/<init>()V
	astore_3
	aload_3
	aload_1
	invokevirtual QuickSort/quicksort([I)Z
	aload_3
	aload_1
	invokevirtual QuickSort/printL([I)Z
	return
.end method

.method public printL([I)Z
	.limit stack 99
	.limit locals 4
	iconst_0
	istore_2
while_1:
	iload_2
	aload_1
	arraylength
	if_icmpge notLess_1
	iconst_1
	goto endLess_1
notLess_1:
	iconst_0
endLess_1:
	ifeq endWhile_1
	aload_1
	iload_2
	iaload
	invokestatic io/println(I)V
	iload_2
	iconst_1
	iadd
	istore_2
	goto while_1
endWhile_1:
	iconst_1
	ireturn
.end method

.method public quicksort([I)Z
	.limit stack 99
	.limit locals 2
	aload_0
	aload_1
	iconst_0
	aload_1
	arraylength
	iconst_1
	isub
	invokevirtual QuickSort/quicksort([III)Z
	ireturn
.end method

.method public quicksort([III)Z
	.limit stack 99
	.limit locals 6
	iload_2
	iload_3
	if_icmpge notLess_2
	iconst_1
	goto endLess_2
notLess_2:
	iconst_0
endLess_2:
	ifeq else_0
	aload_0
	aload_1
	iload_2
	iload_3
	invokevirtual QuickSort/partition([III)I
	istore	4
	aload_0
	aload_1
	iload_2
	iload	4
	iconst_1
	isub
	invokevirtual QuickSort/quicksort([III)Z
	pop
	aload_0
	aload_1
	iload	4
	iconst_1
	iadd
	iload_3
	invokevirtual QuickSort/quicksort([III)Z
	pop
	goto endIf_0
else_0:
endIf_0:
	iconst_1
	ireturn
.end method

.method public partition([III)I
	.limit stack 99
	.limit locals 9
	aload_1
	iload_3
	iaload
	istore	4
	iload_2
	istore	5
	iload_2
	istore	6
while_2:
	iload	6
	iload_3
	if_icmpge notLess_3
	iconst_1
	goto endLess_3
notLess_3:
	iconst_0
endLess_3:
	ifeq endWhile_2
	aload_1
	iload	6
	iaload
	iload	4
	if_icmpge notLess_4
	iconst_1
	goto endLess_4
notLess_4:
	iconst_0
endLess_4:
	ifeq else_1
	aload_1
	iload	5
	iaload
	istore	7
	aload_1
	iload	5
	aload_1
	iload	6
	iaload
	iastore
	aload_1
	iload	6
	iload	7
	iastore
	iload	5
	iconst_1
	iadd
	istore	5
	goto endIf_1
else_1:
endIf_1:
	iload	6
	iconst_1
	iadd
	istore	6
	goto while_2
endWhile_2:
	aload_1
	iload	5
	iaload
	istore	7
	aload_1
	iload	5
	aload_1
	iload_3
	iaload
	iastore
	aload_1
	iload_3
	iload	7
	iastore
	iload	5
	ireturn
.end method


