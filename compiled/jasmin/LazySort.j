.class public LazySort
.super QuickSort

.method public <init>()V
	aload_0
	invokenonvirtual QuickSort/<init>()V
	return
.end method


.method public quicksort([I)Z
	.limit stack 5
	.limit locals 3
	iconst_0
	iconst_5
	invokestatic MathUtils/random(II)I
	iconst_4
	if_icmpge notLess_49
	iconst_1
	goto endLess_49
notLess_49:
	iconst_0
endLess_49:
	ifeq else_37
	aload_0
	aload_1
	invokevirtual LazySort/beLazy([I)Z
	pop
	iconst_1
	istore_2
	goto endIf_37
else_37:
	iconst_0
	istore_2
endIf_37:
	iload_2
	ifeq else_38
	iload_2
	iconst_1
	ixor
	istore_2
	goto endIf_38
else_38:
	aload_0
	aload_1
	iconst_0
	aload_1
	arraylength
	iconst_1
	isub
	invokevirtual LazySort/quicksort([III)Z
	istore_2
endIf_38:
	iload_2
	ireturn
.end method


.method public static main([Ljava/lang/String;)V
	.limit stack 4
	.limit locals 5
	bipush	10
	newarray int
	astore_1
	iconst_0
	istore_2
while_16:
	iload_2
	aload_1
	arraylength
	if_icmpge notLess_50
	iconst_1
	goto endLess_50
notLess_50:
	iconst_0
endLess_50:
	ifeq endWhile_16
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
	goto while_16
endWhile_16:
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
	.limit locals 4
	aload_1
	arraylength
	istore_2
	iconst_0
	istore_3
while_17:
	iload_3
	iload_2
	iconst_2
	idiv
	if_icmpge notLess_51
	iconst_1
	goto endLess_51
notLess_51:
	iconst_0
endLess_51:
	ifeq endWhile_17
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
	goto while_17
endWhile_17:
while_18:
	iload_3
	iload_2
	if_icmpge notLess_52
	iconst_1
	goto endLess_52
notLess_52:
	iconst_0
endLess_52:
	ifeq endWhile_18
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
	goto while_18
endWhile_18:
	iconst_1
	ireturn
.end method


