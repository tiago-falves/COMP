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
	if_icmpge notLess_13
	iconst_1
	goto endLess_13
notLess_13:
	iconst_0
endLess_13:
	ifeq else_13
	iconst_0
	istore_3
	goto endIf_13
else_13:
	iconst_2
	iload_2
	if_icmpge notLess_14
	iconst_1
	goto endLess_14
notLess_14:
	iconst_0
endLess_14:
	ifeq else_14
	iconst_0
	istore_3
	goto endIf_14
else_14:
	iconst_0
	aload_1
	iload_2
	iaload
	if_icmpge notLess_15
	iconst_1
	goto endLess_15
notLess_15:
	iconst_0
endLess_15:
	ifeq else_15
	iconst_0
	istore_3
	goto endIf_15
else_15:
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
endIf_15:
endIf_14:
endIf_13:
	iload_3
	ireturn
.end method

.method public Move(II)Z
	.limit stack 99
	.limit locals 5
	iload_1
	iconst_0
	if_icmpge notLess_16
	iconst_1
	goto endLess_16
notLess_16:
	iconst_0
endLess_16:
	iconst_1
	ixor
	iconst_0
	iload_1
	if_icmpge notLess_17
	iconst_1
	goto endLess_17
notLess_17:
	iconst_0
endLess_17:
	iconst_1
	ixor
	iand
	ifeq else_16
	aload_0
	aload_0
	getfield TicTacToe/row0 [I
	iload_2
	invokevirtual TicTacToe/MoveRow([II)Z
	istore_3
	goto endIf_16
else_16:
	iload_1
	iconst_1
	if_icmpge notLess_18
	iconst_1
	goto endLess_18
notLess_18:
	iconst_0
endLess_18:
	iconst_1
	ixor
	iconst_1
	iload_1
	if_icmpge notLess_19
	iconst_1
	goto endLess_19
notLess_19:
	iconst_0
endLess_19:
	iconst_1
	ixor
	iand
	ifeq else_17
	aload_0
	aload_0
	getfield TicTacToe/row1 [I
	iload_2
	invokevirtual TicTacToe/MoveRow([II)Z
	istore_3
	goto endIf_17
else_17:
	iload_1
	iconst_2
	if_icmpge notLess_20
	iconst_1
	goto endLess_20
notLess_20:
	iconst_0
endLess_20:
	iconst_1
	ixor
	iconst_2
	iload_1
	if_icmpge notLess_21
	iconst_1
	goto endLess_21
notLess_21:
	iconst_0
endLess_21:
	iconst_1
	ixor
	iand
	ifeq else_18
	aload_0
	aload_0
	getfield TicTacToe/row2 [I
	iload_2
	invokevirtual TicTacToe/MoveRow([II)Z
	istore_3
	goto endIf_18
else_18:
	iconst_0
	istore_3
endIf_18:
endIf_17:
endIf_16:
	iload_3
	ireturn
.end method

.method public inbounds(II)Z
	.limit stack 99
	.limit locals 5
	iload_1
	iconst_0
	if_icmpge notLess_22
	iconst_1
	goto endLess_22
notLess_22:
	iconst_0
endLess_22:
	ifeq else_19
	iconst_0
	istore_3
	goto endIf_19
else_19:
	iload_2
	iconst_0
	if_icmpge notLess_23
	iconst_1
	goto endLess_23
notLess_23:
	iconst_0
endLess_23:
	ifeq else_20
	iconst_0
	istore_3
	goto endIf_20
else_20:
	iconst_2
	iload_1
	if_icmpge notLess_24
	iconst_1
	goto endLess_24
notLess_24:
	iconst_0
endLess_24:
	ifeq else_21
	iconst_0
	istore_3
	goto endIf_21
else_21:
	iconst_2
	iload_2
	if_icmpge notLess_25
	iconst_1
	goto endLess_25
notLess_25:
	iconst_0
endLess_25:
	ifeq else_22
	iconst_0
	istore_3
	goto endIf_22
else_22:
	iconst_1
	istore_3
endIf_22:
endIf_21:
endIf_20:
endIf_19:
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
	.limit locals 5
	iconst_0
	iconst_1
	isub
	istore_2
	iconst_3
	newarray int
	astore_1
	ifeq else_23
	aload_0
	getfield TicTacToe/row0 [I
	invokestatic BoardBase/sameArray([I)Z
	aload_0
	getfield TicTacToe/row0 [I
	iconst_0
	iaload
	istore_2
	goto endIf_23
else_23:
	ifeq else_24
	aload_0
	getfield TicTacToe/row1 [I
	invokestatic BoardBase/sameArray([I)Z
	aload_0
	getfield TicTacToe/row1 [I
	iconst_0
	iaload
	istore_2
	goto endIf_24
else_24:
	ifeq else_25
	aload_0
	getfield TicTacToe/row2 [I
	invokestatic BoardBase/sameArray([I)Z
	aload_0
	getfield TicTacToe/row2 [I
	iconst_0
	iaload
	istore_2
	goto endIf_25
else_25:
	iconst_0
	istore_3
while_9:
	iload_2
	iconst_1
	if_icmpge notLess_26
	iconst_1
	goto endLess_26
notLess_26:
	iconst_0
endLess_26:
	iload_3
	iconst_3
	if_icmpge notLess_27
	iconst_1
	goto endLess_27
notLess_27:
	iconst_0
endLess_27:
	iand
	ifeq endWhile_9
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
	ifeq else_26
	aload_1
	invokestatic BoardBase/sameArray([I)Z
	aload_1
	iconst_0
	iaload
	istore_2
	goto endIf_26
else_26:
endIf_26:
	iload_3
	iconst_1
	iadd
	istore_3
	goto while_9
endWhile_9:
	iload_2
	iconst_1
	if_icmpge notLess_28
	iconst_1
	goto endLess_28
notLess_28:
	iconst_0
endLess_28:
	ifeq else_27
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
	ifeq else_28
	aload_1
	invokestatic BoardBase/sameArray([I)Z
	aload_1
	iconst_0
	iaload
	istore_2
	goto endIf_28
else_28:
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
	ifeq else_29
	aload_1
	invokestatic BoardBase/sameArray([I)Z
	aload_1
	iconst_0
	iaload
	istore_2
	goto endIf_29
else_29:
endIf_29:
endIf_28:
	goto endIf_27
else_27:
endIf_27:
endIf_25:
endIf_24:
endIf_23:
	iload_2
	iconst_1
	if_icmpge notLess_29
	iconst_1
	goto endLess_29
notLess_29:
	iconst_0
endLess_29:
	aload_0
	getfield TicTacToe/movesmade I
	bipush	9
	if_icmpge notLess_30
	iconst_1
	goto endLess_30
notLess_30:
	iconst_0
endLess_30:
	iconst_1
	ixor
	bipush	9
	aload_0
	getfield TicTacToe/movesmade I
	if_icmpge notLess_31
	iconst_1
	goto endLess_31
notLess_31:
	iconst_0
endLess_31:
	iconst_1
	ixor
	iand
	iand
	ifeq else_30
	iconst_0
	istore_2
	goto endIf_30
else_30:
endIf_30:
	iload_2
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
	iload	5
	invokestatic BoardBase/playerTurn(I)[I
while_10:
	aload_1
	invokevirtual TicTacToe/winner()I
	iconst_0
	iconst_1
	isub
	if_icmpge notLess_32
	iconst_1
	goto endLess_32
notLess_32:
	iconst_0
endLess_32:
	iconst_1
	ixor
	iconst_0
	isub
	iconst_1
	ixor
	iand
	ifeq endWhile_10
	iconst_0
	istore_3
while_11:
	iload_3
	iconst_1
	ixor
	ifeq endWhile_11
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
	ifeq else_31
	invokestatic BoardBase/wrongMove()V
	goto endIf_31
else_31:
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
	ifeq else_32
	invokestatic BoardBase/placeTaken()V
	goto endIf_32
else_32:
	iconst_1
	istore_3
endIf_32:
endIf_31:
	goto while_11
endWhile_11:
	goto while_10
endWhile_10:
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


