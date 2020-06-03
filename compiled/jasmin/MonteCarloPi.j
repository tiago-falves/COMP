.class public MonteCarloPi
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public performSingleEstimate()Z
	.limit stack 99
	.limit locals 6
	iconst_0
	bipush	100
	isub
	bipush	100
	invokestatic MathUtils/random(II)I
	istore_1
	iconst_0
	bipush	100
	isub
	bipush	100
	invokestatic MathUtils/random(II)I
	istore_2
	iload_1
	iload_1
	imul
	iload_2
	iload_2
	imul
	iadd
	bipush	100
	idiv
	istore	4
	iload	4
	bipush	100
	if_icmpge notLess_0
	iconst_1
	goto endLess_0
notLess_0:
	iconst_0
endLess_0:
	ifeq else_0
	iconst_1
	istore_3
	goto endIf_0
else_0:
	iconst_0
	istore_3
endIf_0:
	iload_3
	ireturn
.end method

.method public estimatePi100(I)I
	.limit stack 99
	.limit locals 6
	iconst_0
	istore_3
	iconst_0
	istore_2
while_0:
	iload_3
	iload_1
	if_icmpge notLess_1
	iconst_1
	goto endLess_1
notLess_1:
	iconst_0
endLess_1:
	ifeq endWhile_0
	aload_0
	invokevirtual MonteCarloPi/performSingleEstimate()Z
	ifeq else_1
	iload_2
	iconst_1
	iadd
	istore_2
	goto endIf_1
else_1:
endIf_1:
	iload_3
	iconst_1
	iadd
	istore_3
	goto while_0
endWhile_0:
	iload_2
	iload_1
	idiv
	sipush	400
	imul
	istore	4
	iload	4
	ireturn
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 99
	.limit locals 3
	invokestatic ioPlus/requestNumber()I
	istore_2
	new MonteCarloPi
	dup
	invokespecial MonteCarloPi/<init>()V
	iload_2
	invokevirtual MonteCarloPi/estimatePi100(I)I
	istore_1
	iload_1
	invokestatic ioPlus/printResult(I)V
	return
.end method


