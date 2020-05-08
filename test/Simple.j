.class public HelloWorld
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
	iconst_0
	istore_3
	iconst_0
	istore_2
	iload_2
	iconst_2
	iadd
	istore_3
	invokestatic ioPlus/printHelloWorld()V
	return
.end method


