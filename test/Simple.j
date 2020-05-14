.class public Simple
.super java/lang/Object
.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 99
	.limit locals 99
	iconst_3
	istore	4
	iconst_1
	ifeq else_0
	iconst_4
	istore	4
	iconst_3
	istore	4
	iconst_1
	ifeq else_1
	iconst_5
	istore	4
	new Simple
	dup
	invokespecial Simple/<init>()V
	astore_3
	aload_3
	iload	4
	iload	4
	invokevirtual Simple/add(II)I
	istore	4
	iconst_1
	ifeq else_2
	iconst_3
	istore	4
	goto endIf_2
else_2:
	iconst_4
	istore	4
endIf_2:
	goto endIf_1
else_1:
	iconst_2
	istore	4
endIf_1:
	goto endIf_0
else_0:
	bipush	6
	istore	4
	new Simple
	dup
	invokespecial Simple/<init>()V
	astore_3
endIf_0:
	iconst_3
	istore	4
	return
.end method

.method public add(II)I
	.limit stack 99
	.limit locals 99
	iload_1
	iload_2
	iadd
	ireturn
.end method


