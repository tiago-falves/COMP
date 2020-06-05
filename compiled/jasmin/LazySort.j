.class public LazySort
.super QuickSort

.method public <init>()V
	aload_0
	invokenonvirtual QuickSort/<init>()V
	return
.end method


.method public quicksort([I)Z
	.limit stack 5
	.limit locals 4
	iconst_0
	iconst_5
	invokestatic MathUtils/random(II)I
	iconst_4
	if_icmpge notLess_10
	iconst_1
	goto endLess_10
notLess_10:
	iconst_0
endLess_10:
	ifeq else_11
	aload_0
	aload_1
	invokevirtual LazySort/beLazy([I)Z
	pop
	iconst_1
	istore_2
	goto endIf_11
else_11:
	iconst_0
	istore_2
endIf_11:
	iload_2
	ifeq else_12
	iload_2
	iconst_1
	ixor
	istore_2
	goto endIf_12
else_12:
	aload_0
	aload_1
	iconst_0
	aload_1
	arraylength
	iconst_1
	isub
	invokevirtual LazySort/quicksort([III)Z
	istore_2
endIf_12:
	iload_2
	ireturn
.end method


.method public static main([Ljava/lang/String;)V
	.limit stack 4
	.limit locals 6
	bipush	10
	newarray int
	astore_1
	iconst_0
	istore_2
while_6:
	iload_2
	aload_1
	arraylength
	if_icmpge notLess_11
	iconst_1
	goto endLess_11
notLess_11:
	iconst_0
endLess_11:
	ifeq endWhile_6
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
	goto while_6
endWhile_6:
	new LazySort
	dup
	invokespecial LazySort/<init>()V
	astore	4
	aload	4
	aload_1
	invokevirtual LazySort/quicksort([I)Z
	pop
	aload	4
	aload_1
	invokevirtual LazySort/printL([I)Z
	istore_3
	return
.end method

.method public beLazy([I)Z
	.limit stack 4
	.limit locals 5
	aload_1
	arraylength
	istore_2
	iconst_0
	istore_3
while_7:
	iload_3
	iload_2
	iconst_2
	idiv
	if_icmpge notLess_12
	iconst_1
	goto endLess_12
notLess_12:
	iconst_0
endLess_12:
	ifeq endWhile_7
	aload_1
	iload_3
	iconst_0
	bipush	10
	invokestatic MathUtils/random(II)I
	iastore
	iload_3
	iconst_1
	iadd
	istore_3
	goto while_7
endWhile_7:
while_8:
	iload_3
	iload_2
	if_icmpge notLess_13
	iconst_1
	goto endLess_13
notLess_13:
	iconst_0
endLess_13:
	ifeq endWhile_8
	aload_1
	iload_3
	iconst_0
	bipush	10
	invokestatic MathUtils/random(II)I
	iconst_1
	iadd
	iastore
	iload_3
	iconst_1
	iadd
	istore_3
	goto while_8
endWhile_8:
	iconst_1
	ireturn
.end method


