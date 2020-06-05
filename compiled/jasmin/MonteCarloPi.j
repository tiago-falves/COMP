.class public MonteCarloPi
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public performSingleEstimate()Z
	.limit stack 4
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
	if_icmpge notLess_39
	iconst_1
	goto endLess_39
notLess_39:
	iconst_0
endLess_39:
	ifeq else_26
	iconst_1
	istore_3
	goto endIf_26
else_26:
	iconst_0
	istore_3
endIf_26:
	iload_3
	ireturn
.end method

.method public estimatePi100(I)I
	.limit stack 4
	.limit locals 6
	iconst_0
	istore_3
	iconst_0
	istore_2
while_10:
	iload_3
	iload_1
	if_icmpge notLess_40
	iconst_1
	goto endLess_40
notLess_40:
	iconst_0
endLess_40:
	ifeq endWhile_10
	aload_0
	invokevirtual MonteCarloPi/performSingleEstimate()Z
	ifeq else_27
	iload_2
	iconst_1
	iadd
	istore_2
	goto endIf_27
else_27:
endIf_27:
	iload_3
	iconst_1
	iadd
	istore_3
	goto while_10
endWhile_10:
	sipush	400
	iload_2
	imul
	iload_1
	idiv
	istore	4
	iload	4
	ireturn
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 4
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


