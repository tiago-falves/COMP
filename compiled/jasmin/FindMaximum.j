.class public FindMaximum
.super java/lang/Object
.field test_arr [I

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public find_maximum([I)I
	.limit stack 99
	.limit locals 6
	iconst_1
	istore_2
	aload_1
	iconst_0
	iaload
	istore_3
while_12:
	iload_2
	aload_1
	arraylength
	if_icmpge notLess_34
	iconst_1
	goto endLess_34
notLess_34:
	iconst_0
endLess_34:
	ifeq endWhile_12
	aload_1
	iload_2
	iaload
	istore	4
	iload_3
	iload	4
	if_icmpge notLess_35
	iconst_1
	goto endLess_35
notLess_35:
	iconst_0
endLess_35:
	ifeq else_32
	iload	4
	istore_3
	goto endIf_32
else_32:
endIf_32:
	iload_2
	iconst_1
	iadd
	istore_2
	goto while_12
endWhile_12:
	iload_3
	ireturn
.end method

.method public build_test_arr()I
	.limit stack 99
	.limit locals 1
	aload_0
	iconst_5
	newarray int
	putfield FindMaximum/test_arr [I
	aload_0
	aload_0
	getfield FindMaximum/test_arr [I
	iconst_0
	bipush	14
	iastore
	aload_0
	aload_0
	getfield FindMaximum/test_arr [I
	iconst_1
	bipush	28
	iastore
	aload_0
	aload_0
	getfield FindMaximum/test_arr [I
	iconst_2
	iconst_0
	iastore
	aload_0
	aload_0
	getfield FindMaximum/test_arr [I
	iconst_3
	iconst_0
	iconst_5
	isub
	iastore
	aload_0
	aload_0
	getfield FindMaximum/test_arr [I
	iconst_4
	bipush	12
	iastore
	iconst_0
	ireturn
.end method

.method public get_array()[I
	.limit stack 99
	.limit locals 1
	aload_0
	getfield FindMaximum/test_arr [I
	areturn
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 99
	.limit locals 3
	new FindMaximum
	dup
	invokespecial FindMaximum/<init>()V
	astore_1
	aload_1
	invokevirtual FindMaximum/build_test_arr()I
	aload_1
	aload_1
	invokevirtual FindMaximum/get_array()[I
	invokevirtual FindMaximum/find_maximum([I)I
	istore_2
	iload_2
	invokestatic ioPlus/printResult(I)V
	return
.end method

