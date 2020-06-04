.class public LazySort
.super QuickSort

.method public <init>()V
	aload_0
	invokenonvirtual QuickSort/<init>()V
	return
.end method


.method public quicksort([I)Z
	.limit stack 99
	.limit locals 4
	iconst_0
	iconst_5
	invokestatic MathUtils/random(II)I
	ifeq else_0
	aload_0
	aload_1
	invokevirtual LazySort/beLazy([I)Z
	pop
	iconst_1
	istore_2
	goto endIf_0
else_0:
	iconst_0
	istore_2
endIf_0:
	iload_2
	ifeq else_1
	iload_2
	iconst_1
	ixor
	istore_2
	goto endIf_1
else_1:
	aload_0
	aload_1
	iconst_0
	aload_1
	arraylength
	iconst_1
	isub
	invokevirtual LazySort/quicksort([III)Z
	istore_2
endIf_1:
	iload_2
	ireturn
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
	.limit stack 99
	.limit locals 5
	aload_1
	arraylength
	istore_2
	iconst_0
	istore_3
	iconst_0
	bipush	10
	invokestatic MathUtils/random(II)I
while_1:
	iload_3
	iload_2
	iconst_2
	idiv
	if_icmpge notLess_1
	iconst_1
	goto endLess_1
notLess_1:
	iconst_0
endLess_1:
	ifeq endWhile_1
	aload_1
	iload_3
	iastore
	iload_3
	iconst_1
	iadd
	istore_3
	goto while_1
endWhile_1:
	iconst_0
	bipush	10
	invokestatic MathUtils/random(II)I
while_2:
	iload_3
	iload_2
	if_icmpge notLess_2
	iconst_1
	goto endLess_2
notLess_2:
	iconst_0
endLess_2:
	ifeq endWhile_2
	aload_1
	iload_3
	iconst_1
	iastore
	iload_3
	iconst_1
	iadd
	istore_3
	goto while_2
endWhile_2:
	iconst_1
	ireturn
.end method


