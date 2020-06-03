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
while_13:
	iload_2
	aload_1
	arraylength
	if_icmpge notLess_36
	iconst_1
	goto endLess_36
notLess_36:
	iconst_0
endLess_36:
	ifeq endWhile_13
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
	goto while_13
endWhile_13:
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
while_14:
	iload_2
	aload_1
	arraylength
	if_icmpge notLess_37
	iconst_1
	goto endLess_37
notLess_37:
	iconst_0
endLess_37:
	ifeq endWhile_14
	aload_1
	iload_2
	iaload
	invokestatic io/println(I)V
	iload_2
	iconst_1
	iadd
	istore_2
	goto while_14
endWhile_14:
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
	if_icmpge notLess_38
	iconst_1
	goto endLess_38
notLess_38:
	iconst_0
endLess_38:
	ifeq else_33
	aload_0
	aload_1
	iload_2
	iload_3
	invokevirtual QuickSort/partition([III)I
	istore	4
	goto endIf_33
else_33:
endIf_33:
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
while_15:
	iload	6
	iload_3
	if_icmpge notLess_39
	iconst_1
	goto endLess_39
notLess_39:
	iconst_0
endLess_39:
	ifeq endWhile_15
	aload_1
	iload	6
	iaload
	iload	4
	if_icmpge notLess_40
	iconst_1
	goto endLess_40
notLess_40:
	iconst_0
endLess_40:
	ifeq else_34
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
	goto endIf_34
else_34:
endIf_34:
	iload	6
	iconst_1
	iadd
	istore	6
	goto while_15
endWhile_15:
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


