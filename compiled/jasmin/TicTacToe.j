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
	.limit stack 4
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
	getfield TicTacToe/pieces [I
	iconst_0
	iconst_1
	iastore
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
	.limit stack 1
	.limit locals 1
	aload_0
	getfield TicTacToe/row0 [I
	areturn
.end method

.method public getRow1()[I
	.limit stack 1
	.limit locals 1
	aload_0
	getfield TicTacToe/row1 [I
	areturn
.end method

.method public getRow2()[I
	.limit stack 1
	.limit locals 1
	aload_0
	getfield TicTacToe/row2 [I
	areturn
.end method

.method public MoveRow([II)Z
	.limit stack 6
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
	.limit stack 10
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
	.limit stack 4
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
	.limit stack 3
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
	.limit stack 2
	.limit locals 1
	aload_0
	getfield TicTacToe/whoseturn I
	iconst_1
	iadd
	ireturn
.end method

.method public winner()I
	.limit stack 14
	.limit locals 5
	iconst_0
	iconst_1
	isub
	istore_2
	iconst_3
	newarray int
	astore_1
	aload_0
	getfield TicTacToe/row0 [I
	invokestatic BoardBase/sameArray([I)Z
	iconst_0
	aload_0
	getfield TicTacToe/row0 [I
	iconst_0
	iaload
	if_icmpge notLess_13
	iconst_1
	goto endLess_13
notLess_13:
	iconst_0
endLess_13:
	iand
	ifeq else_10
	aload_0
	getfield TicTacToe/row0 [I
	iconst_0
	iaload
	istore_2
	goto endIf_10
else_10:
	aload_0
	getfield TicTacToe/row1 [I
	invokestatic BoardBase/sameArray([I)Z
	iconst_0
	aload_0
	getfield TicTacToe/row1 [I
	iconst_0
	iaload
	if_icmpge notLess_14
	iconst_1
	goto endLess_14
notLess_14:
	iconst_0
endLess_14:
	iand
	ifeq else_11
	aload_0
	getfield TicTacToe/row1 [I
	iconst_0
	iaload
	istore_2
	goto endIf_11
else_11:
	aload_0
	getfield TicTacToe/row2 [I
	invokestatic BoardBase/sameArray([I)Z
	iconst_0
	aload_0
	getfield TicTacToe/row2 [I
	iconst_0
	iaload
	if_icmpge notLess_15
	iconst_1
	goto endLess_15
notLess_15:
	iconst_0
endLess_15:
	iand
	ifeq else_12
	aload_0
	getfield TicTacToe/row2 [I
	iconst_0
	iaload
	istore_2
	goto endIf_12
else_12:
	iconst_0
	istore_3
while_0:
	iload_2
	iconst_1
	if_icmpge notLess_16
	iconst_1
	goto endLess_16
notLess_16:
	iconst_0
endLess_16:
	iload_3
	iconst_3
	if_icmpge notLess_17
	iconst_1
	goto endLess_17
notLess_17:
	iconst_0
endLess_17:
	iand
	ifeq endWhile_0
	aload_1
	iconst_0
	aload_0
	getfield TicTacToe/row0 [I
	iload_3
	iaload
	iastore
	aload_1
	iconst_1
	aload_0
	getfield TicTacToe/row1 [I
	iload_3
	iaload
	iastore
	aload_1
	iconst_2
	aload_0
	getfield TicTacToe/row2 [I
	iload_3
	iaload
	iastore
	aload_1
	invokestatic BoardBase/sameArray([I)Z
	iconst_0
	aload_1
	iconst_0
	iaload
	if_icmpge notLess_18
	iconst_1
	goto endLess_18
notLess_18:
	iconst_0
endLess_18:
	iand
	ifeq else_13
	aload_1
	iconst_0
	iaload
	istore_2
	goto endIf_13
else_13:
endIf_13:
	iload_3
	iconst_1
	iadd
	istore_3
	goto while_0
endWhile_0:
	iload_2
	iconst_1
	if_icmpge notLess_19
	iconst_1
	goto endLess_19
notLess_19:
	iconst_0
endLess_19:
	ifeq else_14
	aload_1
	iconst_0
	aload_0
	getfield TicTacToe/row0 [I
	iconst_0
	iaload
	iastore
	aload_1
	iconst_1
	aload_0
	getfield TicTacToe/row1 [I
	iconst_1
	iaload
	iastore
	aload_1
	iconst_2
	aload_0
	getfield TicTacToe/row2 [I
	iconst_2
	iaload
	iastore
	aload_1
	invokestatic BoardBase/sameArray([I)Z
	iconst_0
	aload_1
	iconst_0
	iaload
	if_icmpge notLess_20
	iconst_1
	goto endLess_20
notLess_20:
	iconst_0
endLess_20:
	iand
	ifeq else_15
	aload_1
	iconst_0
	iaload
	istore_2
	goto endIf_15
else_15:
	aload_1
	iconst_0
	aload_0
	getfield TicTacToe/row0 [I
	iconst_2
	iaload
	iastore
	aload_1
	iconst_1
	aload_0
	getfield TicTacToe/row1 [I
	iconst_1
	iaload
	iastore
	aload_1
	iconst_2
	aload_0
	getfield TicTacToe/row2 [I
	iconst_0
	iaload
	iastore
	aload_1
	invokestatic BoardBase/sameArray([I)Z
	iconst_0
	aload_1
	iconst_0
	iaload
	if_icmpge notLess_21
	iconst_1
	goto endLess_21
notLess_21:
	iconst_0
endLess_21:
	iand
	ifeq else_16
	aload_1
	iconst_0
	iaload
	istore_2
	goto endIf_16
else_16:
endIf_16:
endIf_15:
	goto endIf_14
else_14:
endIf_14:
endIf_12:
endIf_11:
endIf_10:
	iload_2
	iconst_1
	if_icmpge notLess_22
	iconst_1
	goto endLess_22
notLess_22:
	iconst_0
endLess_22:
	aload_0
	getfield TicTacToe/movesmade I
	bipush	9
	if_icmpge notLess_23
	iconst_1
	goto endLess_23
notLess_23:
	iconst_0
endLess_23:
	iconst_1
	ixor
	bipush	9
	aload_0
	getfield TicTacToe/movesmade I
	if_icmpge notLess_24
	iconst_1
	goto endLess_24
notLess_24:
	iconst_0
endLess_24:
	iconst_1
	ixor
	iand
	iand
	ifeq else_17
	iconst_0
	istore_2
	goto endIf_17
else_17:
endIf_17:
	iload_2
	ireturn
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 12
	.limit locals 7
	new TicTacToe
	dup
	invokespecial TicTacToe/<init>()V
	astore_1
	aload_1
	invokevirtual TicTacToe/init()Z
	pop
while_1:
	aload_1
	invokevirtual TicTacToe/winner()I
	iconst_0
	iconst_1
	isub
	if_icmpge notLess_25
	iconst_1
	goto endLess_25
notLess_25:
	iconst_0
endLess_25:
	iconst_1
	ixor
	iconst_0
	iconst_1
	isub
	aload_1
	invokevirtual TicTacToe/winner()I
	if_icmpge notLess_26
	iconst_1
	goto endLess_26
notLess_26:
	iconst_0
endLess_26:
	iconst_1
	ixor
	iand
	ifeq endWhile_1
	iconst_0
	istore_3
while_2:
	iload_3
	iconst_1
	ixor
	ifeq endWhile_2
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
	iload	5
	invokestatic BoardBase/playerTurn(I)[I
	astore	4
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
	ifeq else_18
	invokestatic BoardBase/wrongMove()V
	goto endIf_18
else_18:
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
	ifeq else_19
	invokestatic BoardBase/placeTaken()V
	goto endIf_19
else_19:
	iconst_1
	istore_3
endIf_19:
endIf_18:
	goto while_2
endWhile_2:
	aload_1
	invokevirtual TicTacToe/changeturn()Z
	pop
	goto while_1
endWhile_1:
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


