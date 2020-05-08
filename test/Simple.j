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
	iconst_2
	istore_2
	iconst_2
	istore_2
	iconst_4
	istore_2
	iload_2
	iconst_2
	iadd
	istore_3
	invokestatic ioPlus/printHelloWorld()V
	return
.end method


