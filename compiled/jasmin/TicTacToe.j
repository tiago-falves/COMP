.class public TicTacToe
.super java/lang/Object
.field row0 [I
.field row1 [I
.field row2 [I
.field whoseturn I
.field movesmade I
.field pieces [I

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public init()Z
	.limit stack 99
	.limit locals 1
	aload_0
	iconst_3
	newarray int
	putfield TicTacToe/row0 [I
	aload_0
	iconst_3
	newarray int
	putfield TicTacToe/row1 [I
	aload_0
	iconst_3
	newarray int
	putfield TicTacToe/row2 [I
	aload_0
	iconst_2
	newarray int
	putfield TicTacToe/pieces [I
	aload_0
	aload_0
	getfield TicTacToe/pieces [I
	iconst_0
	iconst_1
	iastore
	aload_0
	aload_0
	getfield TicTacToe/pieces [I
	iconst_1
	iconst_2
	iastore
	aload_0
	iconst_0
	putfield TicTacToe/whoseturn I
	aload_0
	iconst_0
	putfield TicTacToe/movesmade I
	iconst_1
	ireturn
.end method

.method public getRow0()[I
	.limit stack 99
	.limit locals 1
	aload_0
	getfield TicTacToe/row0 [I
	areturn
.end method

.method public getRow1()[I
	.limit stack 99
	.limit locals 1
	aload_0
	getfield TicTacToe/row1 [I
	areturn
.end method

.method public getRow2()[I
	.limit stack 99
	.limit locals 1
	aload_0
	getfield TicTacToe/row2 [I
	areturn
.end method

.method public MoveRow([II)Z
	.limit stack 99
	.limit locals 5
	iload_2
	iconst_0
	if_icmpge notLess_0
	iconst_1
	goto endLess_0
notLess_0:
	iconst_0
endLess_0:
	ifeq else_0
	iconst_0
	istore_3
	goto endIf_0
else_0:
	iconst_2
	iload_2
	if_icmpge notLess_1
	iconst_1
	goto endLess_1
notLess_1:
	iconst_0
endLess_1:
	ifeq else_1
	iconst_0
	istore_3
	goto endIf_1
else_1:
	iconst_0
	aload_1
	iload_2
	iaload
	if_icmpge notLess_2
	iconst_1
	goto endLess_2
notLess_2:
	iconst_0
endLess_2:
	ifeq else_2
	iconst_0
	istore_3
	goto endIf_2
else_2:
	aload_1
	iload_2
	aload_0
	getfield TicTacToe/pieces [I
	aload_0
	getfield TicTacToe/whoseturn I
	iaload
	iastore
	aload_0
	aload_0
	getfield TicTacToe/movesmade I
	iconst_1
	iadd
	putfield TicTacToe/movesmade I
	iconst_1
	istore_3
endIf_2:
endIf_1:
endIf_0:
	iload_3
	ireturn
.end method

.method public Move(II)Z
	.limit stack 99
	.limit locals 5
	iload_1
	iconst_0
	if_icmpge notLess_3
	iconst_1
	goto endLess_3
notLess_3:
	iconst_0
endLess_3:
	iconst_1
	ixor
	iconst_0
	iload_1
	if_icmpge notLess_4
	iconst_1
	goto endLess_4
notLess_4:
	iconst_0
endLess_4:
	iconst_1
	ixor
	iand
	ifeq else_3
	aload_0
	aload_0
	getfield TicTacToe/row0 [I
	iload_2
	invokevirtual TicTacToe/MoveRow([II)Z
	istore_3
	goto endIf_3
else_3:
	iload_1
	iconst_1
	if_icmpge notLess_5
	iconst_1
	goto endLess_5
notLess_5:
	iconst_0
endLess_5:
	iconst_1
	ixor
	iconst_1
	iload_1
	if_icmpge notLess_6
	iconst_1
	goto endLess_6
notLess_6:
	iconst_0
endLess_6:
	iconst_1
	ixor
	iand
	ifeq else_4
	aload_0
	aload_0
	getfield TicTacToe/row1 [I
	iload_2
	invokevirtual TicTacToe/MoveRow([II)Z
	istore_3
	goto endIf_4
else_4:
	iload_1
	iconst_2
	if_icmpge notLess_7
	iconst_1
	goto endLess_7
notLess_7:
	iconst_0
endLess_7:
	iconst_1
	ixor
	iconst_2
	iload_1
	if_icmpge notLess_8
	iconst_1
	goto endLess_8
notLess_8:
	iconst_0
endLess_8:
	iconst_1
	ixor
	iand
	ifeq else_5
	aload_0
	aload_0
	getfield TicTacToe/row2 [I
	iload_2
	invokevirtual TicTacToe/MoveRow([II)Z
	istore_3
	goto endIf_5
else_5:
	iconst_0
	istore_3
endIf_5:
endIf_4:
endIf_3:
	iload_3
	ireturn
.end method

.method public inbounds(II)Z
	.limit stack 99
	.limit locals 5
	iload_1
	iconst_0
	if_icmpge notLess_9
	iconst_1
	goto endLess_9
notLess_9:
	iconst_0
endLess_9:
	ifeq else_6
	iconst_0
	istore_3
	goto endIf_6
else_6:
	iload_2
	iconst_0
	if_icmpge notLess_10
	iconst_1
	goto endLess_10
notLess_10:
	iconst_0
endLess_10:
	ifeq else_7
	iconst_0
	istore_3
	goto endIf_7
else_7:
	iconst_2
	iload_1
	if_icmpge notLess_11
	iconst_1
	goto endLess_11
notLess_11:
	iconst_0
endLess_11:
	ifeq else_8
	iconst_0
	istore_3
	goto endIf_8
else_8:
	iconst_2
	iload_2
	if_icmpge notLess_12
	iconst_1
	goto endLess_12
notLess_12:
	iconst_0
endLess_12:
	ifeq else_9
	iconst_0
	istore_3
	goto endIf_9
else_9:
	iconst_1
	istore_3
endIf_9:
endIf_8:
endIf_7:
endIf_6:
	iload_3
	ireturn
.end method

.method public changeturn()Z
	.limit stack 99
	.limit locals 1
	aload_0
	iconst_1
	aload_0
	getfield TicTacToe/whoseturn I
	isub
	putfield TicTacToe/whoseturn I
	iconst_1
	ireturn
.end method

.method public getCurrentPlayer()I
	.limit stack 99
	.limit locals 1
	aload_0
	getfield TicTacToe/whoseturn I
	iconst_1
	iadd
	ireturn
.end method

.method public winner()I
	.limit stack 99
	.limit locals 1
	iconst_1
	ireturn
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 99
	.limit locals 7
	new TicTacToe
	dup
	invokespecial TicTacToe/<init>()V
	astore_1
	aload_1
	invokevirtual TicTacToe/init()Z
	pop
while_0:
	aload_1
	invokevirtual TicTacToe/winner()I
	iconst_0
	iconst_1
	isub
	if_icmpge notLess_13
	iconst_1
	goto endLess_13
notLess_13:
	iconst_0
endLess_13:
	iconst_1
	ixor
	iconst_0
	isub
	iconst_1
	ixor
	iand
	ifeq endWhile_0
	iconst_0
	istore_3
while_1:
	iload_3
	iconst_1
	ixor
	ifeq endWhile_1
	aload_1
	invokevirtual TicTacToe/getRow0()[I
	aload_1
	invokevirtual TicTacToe/getRow1()[I
	aload_1
	invokevirtual TicTacToe/getRow2()[I
	invokestatic BoardBase/printBoard([I[I[I)V
	aload_1
	invokevirtual TicTacToe/getCurrentPlayer()I
	istore	5
	iconst_2
	newarray int
	astore	4
	aload	4
	iconst_0
	iconst_0
	iastore
	aload	4
	iconst_1
	iconst_1
	iastore
	aload_1
	aload	4
	iconst_0
	iaload
	aload	4
	iconst_1
	iaload
	invokevirtual TicTacToe/inbounds(II)Z
	iconst_1
	ixor
	ifeq else_10
	invokestatic BoardBase/wrongMove()V
	goto endIf_10
else_10:
	aload_1
	aload	4
	iconst_0
	iaload
	aload	4
	iconst_1
	iaload
	invokevirtual TicTacToe/Move(II)Z
	iconst_1
	ixor
	ifeq else_11
	invokestatic BoardBase/placeTaken()V
	goto endIf_11
else_11:
	iconst_1
	istore_3
endIf_11:
endIf_10:
	goto while_1
endWhile_1:
	goto while_0
endWhile_0:
	aload_1
	invokevirtual TicTacToe/getRow0()[I
	aload_1
	invokevirtual TicTacToe/getRow1()[I
	aload_1
	invokevirtual TicTacToe/getRow2()[I
	invokestatic BoardBase/printBoard([I[I[I)V
	aload_1
	invokevirtual TicTacToe/winner()I
	istore_2
	iload_2
	invokestatic BoardBase/printWinner(I)V
	return
.end method


