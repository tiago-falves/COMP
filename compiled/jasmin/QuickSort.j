.class public QuickSort
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 4
	.limit locals 5
	bipush	10
	newarray int
	astore_1
	iconst_0
	istore_2
while_25:
	iload_2
	aload_1
	arraylength
	if_icmpge notLess_85
	iconst_1
	goto endLess_85
notLess_85:
	iconst_0
endLess_85:
	ifeq endWhile_25
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
	goto while_25
endWhile_25:
	new QuickSort
	dup
	invokespecial QuickSort/<init>()V
	astore_3
	aload_3
	aload_1
	invokevirtual QuickSort/quicksort([I)Z
	pop
	aload_3
	aload_1
	invokevirtual QuickSort/printL([I)Z
	pop
	return
.end method

.method public printL([I)Z
	.limit stack 4
	.limit locals 4
	iconst_0
	istore_2
while_26:
	iload_2
	aload_1
	arraylength
	if_icmpge notLess_86
	iconst_1
	goto endLess_86
notLess_86:
	iconst_0
endLess_86:
	ifeq endWhile_26
	aload_1
	iload_2
	iaload
	invokestatic io/println(I)V
	iload_2
	iconst_1
	iadd
	istore_2
	goto while_26
endWhile_26:
	iconst_1
	ireturn
.end method

.method public quicksort([I)Z
	.limit stack 5
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
	.limit stack 5
	.limit locals 6
	iload_2
	iload_3
	if_icmpge notLess_87
	iconst_1
	goto endLess_87
notLess_87:
	iconst_0
endLess_87:
	ifeq else_61
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
	goto endIf_61
else_61:
endIf_61:
	iconst_1
	ireturn
.end method

.method public partition([III)I
	.limit stack 6
	.limit locals 9
	aload_1
	iload_3
	iaload
	istore	4
	iload_2
	istore	5
	iload_2
	istore	6
while_27:
	iload	6
	iload_3
	if_icmpge notLess_88
	iconst_1
	goto endLess_88
notLess_88:
	iconst_0
endLess_88:
	ifeq endWhile_27
	aload_1
	iload	6
	iaload
	iload	4
	if_icmpge notLess_89
	iconst_1
	goto endLess_89
notLess_89:
	iconst_0
endLess_89:
	ifeq else_62
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
	goto endIf_62
else_62:
endIf_62:
	iload	6
	iconst_1
	iadd
	istore	6
	goto while_27
endWhile_27:
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


