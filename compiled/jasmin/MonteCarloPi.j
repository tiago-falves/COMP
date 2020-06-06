.class public MonteCarloPi
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public performSingleEstimate()Z
	.limit stack 4
	.limit locals 5
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
	if_icmpge notLess_51
	iconst_1
	goto endLess_51
notLess_51:
	iconst_0
endLess_51:
	ifeq else_37
	iconst_1
	istore_3
	goto endIf_37
else_37:
	iconst_0
	istore_3
endIf_37:
	iload_3
	ireturn
.end method

.method public estimatePi100(I)I
	.limit stack 4
	.limit locals 5
	iconst_0
	istore_3
	iconst_0
	istore_2
while_18:
	iload_3
	iload_1
	if_icmpge notLess_52
	iconst_1
	goto endLess_52
notLess_52:
	iconst_0
endLess_52:
	ifeq endWhile_18
	aload_0
	invokevirtual MonteCarloPi/performSingleEstimate()Z
	ifeq else_38
	iload_2
	iconst_1
	iadd
	istore_2
	goto endIf_38
else_38:
endIf_38:
	iload_3
	iconst_1
	iadd
	istore_3
	goto while_18
endWhile_18:
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


