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
	if_icmpge notLess_53
	iconst_1
	goto endLess_53
notLess_53:
	iconst_0
endLess_53:
	ifeq else_39
	iconst_0
	istore_3
	goto endIf_39
else_39:
	iconst_2
	iload_2
	if_icmpge notLess_54
	iconst_1
	goto endLess_54
notLess_54:
	iconst_0
endLess_54:
	ifeq else_40
	iconst_0
	istore_3
	goto endIf_40
else_40:
	iconst_0
	aload_1
	iload_2
	iaload
	if_icmpge notLess_55
	iconst_1
	goto endLess_55
notLess_55:
	iconst_0
endLess_55:
	ifeq else_41
	iconst_0
	istore_3
	goto endIf_41
else_41:
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
endIf_41:
endIf_40:
endIf_39:
	iload_3
	ireturn
.end method

.method public Move(II)Z
	.limit stack 10
	.limit locals 5
	iload_1
	iconst_0
	if_icmpge notLess_56
	iconst_1
	goto endLess_56
notLess_56:
	iconst_0
endLess_56:
	iconst_1
	ixor
	iconst_0
	iload_1
	if_icmpge notLess_57
	iconst_1
	goto endLess_57
notLess_57:
	iconst_0
endLess_57:
	iconst_1
	ixor
	iand
	ifeq else_42
	aload_0
	aload_0
	getfield TicTacToe/row0 [I
	iload_2
	invokevirtual TicTacToe/MoveRow([II)Z
	istore_3
	goto endIf_42
else_42:
	iload_1
	iconst_1
	if_icmpge notLess_58
	iconst_1
	goto endLess_58
notLess_58:
	iconst_0
endLess_58:
	iconst_1
	ixor
	iconst_1
	iload_1
	if_icmpge notLess_59
	iconst_1
	goto endLess_59
notLess_59:
	iconst_0
endLess_59:
	iconst_1
	ixor
	iand
	ifeq else_43
	aload_0
	aload_0
	getfield TicTacToe/row1 [I
	iload_2
	invokevirtual TicTacToe/MoveRow([II)Z
	istore_3
	goto endIf_43
else_43:
	iload_1
	iconst_2
	if_icmpge notLess_60
	iconst_1
	goto endLess_60
notLess_60:
	iconst_0
endLess_60:
	iconst_1
	ixor
	iconst_2
	iload_1
	if_icmpge notLess_61
	iconst_1
	goto endLess_61
notLess_61:
	iconst_0
endLess_61:
	iconst_1
	ixor
	iand
	ifeq else_44
	aload_0
	aload_0
	getfield TicTacToe/row2 [I
	iload_2
	invokevirtual TicTacToe/MoveRow([II)Z
	istore_3
	goto endIf_44
else_44:
	iconst_0
	istore_3
endIf_44:
endIf_43:
endIf_42:
	iload_3
	ireturn
.end method

.method public inbounds(II)Z
	.limit stack 4
	.limit locals 5
	iload_1
	iconst_0
	if_icmpge notLess_62
	iconst_1
	goto endLess_62
notLess_62:
	iconst_0
endLess_62:
	ifeq else_45
	iconst_0
	istore_3
	goto endIf_45
else_45:
	iload_2
	iconst_0
	if_icmpge notLess_63
	iconst_1
	goto endLess_63
notLess_63:
	iconst_0
endLess_63:
	ifeq else_46
	iconst_0
	istore_3
	goto endIf_46
else_46:
	iconst_2
	iload_1
	if_icmpge notLess_64
	iconst_1
	goto endLess_64
notLess_64:
	iconst_0
endLess_64:
	ifeq else_47
	iconst_0
	istore_3
	goto endIf_47
else_47:
	iconst_2
	iload_2
	if_icmpge notLess_65
	iconst_1
	goto endLess_65
notLess_65:
	iconst_0
endLess_65:
	ifeq else_48
	iconst_0
	istore_3
	goto endIf_48
else_48:
	iconst_1
	istore_3
endIf_48:
endIf_47:
endIf_46:
endIf_45:
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
	if_icmpge notLess_66
	iconst_1
	goto endLess_66
notLess_66:
	iconst_0
endLess_66:
	iand
	ifeq else_49
	aload_0
	getfield TicTacToe/row0 [I
	iconst_0
	iaload
	istore_2
	goto endIf_49
else_49:
	aload_0
	getfield TicTacToe/row1 [I
	invokestatic BoardBase/sameArray([I)Z
	iconst_0
	aload_0
	getfield TicTacToe/row1 [I
	iconst_0
	iaload
	if_icmpge notLess_67
	iconst_1
	goto endLess_67
notLess_67:
	iconst_0
endLess_67:
	iand
	ifeq else_50
	aload_0
	getfield TicTacToe/row1 [I
	iconst_0
	iaload
	istore_2
	goto endIf_50
else_50:
	aload_0
	getfield TicTacToe/row2 [I
	invokestatic BoardBase/sameArray([I)Z
	iconst_0
	aload_0
	getfield TicTacToe/row2 [I
	iconst_0
	iaload
	if_icmpge notLess_68
	iconst_1
	goto endLess_68
notLess_68:
	iconst_0
endLess_68:
	iand
	ifeq else_51
	aload_0
	getfield TicTacToe/row2 [I
	iconst_0
	iaload
	istore_2
	goto endIf_51
else_51:
	iconst_0
	istore_3
while_19:
	iload_2
	iconst_1
	if_icmpge notLess_69
	iconst_1
	goto endLess_69
notLess_69:
	iconst_0
endLess_69:
	iload_3
	iconst_3
	if_icmpge notLess_70
	iconst_1
	goto endLess_70
notLess_70:
	iconst_0
endLess_70:
	iand
	ifeq endWhile_19
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
	if_icmpge notLess_71
	iconst_1
	goto endLess_71
notLess_71:
	iconst_0
endLess_71:
	iand
	ifeq else_52
	aload_1
	iconst_0
	iaload
	istore_2
	goto endIf_52
else_52:
endIf_52:
	iload_3
	iconst_1
	iadd
	istore_3
	goto while_19
endWhile_19:
	iload_2
	iconst_1
	if_icmpge notLess_72
	iconst_1
	goto endLess_72
notLess_72:
	iconst_0
endLess_72:
	ifeq else_53
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
	if_icmpge notLess_73
	iconst_1
	goto endLess_73
notLess_73:
	iconst_0
endLess_73:
	iand
	ifeq else_54
	aload_1
	iconst_0
	iaload
	istore_2
	goto endIf_54
else_54:
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
	if_icmpge notLess_74
	iconst_1
	goto endLess_74
notLess_74:
	iconst_0
endLess_74:
	iand
	ifeq else_55
	aload_1
	iconst_0
	iaload
	istore_2
	goto endIf_55
else_55:
endIf_55:
endIf_54:
	goto endIf_53
else_53:
endIf_53:
endIf_51:
endIf_50:
endIf_49:
	iload_2
	iconst_1
	if_icmpge notLess_75
	iconst_1
	goto endLess_75
notLess_75:
	iconst_0
endLess_75:
	aload_0
	getfield TicTacToe/movesmade I
	bipush	9
	if_icmpge notLess_76
	iconst_1
	goto endLess_76
notLess_76:
	iconst_0
endLess_76:
	iconst_1
	ixor
	bipush	9
	aload_0
	getfield TicTacToe/movesmade I
	if_icmpge notLess_77
	iconst_1
	goto endLess_77
notLess_77:
	iconst_0
endLess_77:
	iconst_1
	ixor
	iand
	iand
	ifeq else_56
	iconst_0
	istore_2
	goto endIf_56
else_56:
endIf_56:
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
while_20:
	aload_1
	invokevirtual TicTacToe/winner()I
	iconst_0
	iconst_1
	isub
	if_icmpge notLess_78
	iconst_1
	goto endLess_78
notLess_78:
	iconst_0
endLess_78:
	iconst_1
	ixor
	iconst_0
	iconst_1
	isub
	aload_1
	invokevirtual TicTacToe/winner()I
	if_icmpge notLess_79
	iconst_1
	goto endLess_79
notLess_79:
	iconst_0
endLess_79:
	iconst_1
	ixor
	iand
	ifeq endWhile_20
	iconst_0
	istore_3
while_21:
	iload_3
	iconst_1
	ixor
	ifeq endWhile_21
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
	ifeq else_57
	invokestatic BoardBase/wrongMove()V
	goto endIf_57
else_57:
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
	ifeq else_58
	invokestatic BoardBase/placeTaken()V
	goto endIf_58
else_58:
	iconst_1
	istore_3
endIf_58:
endIf_57:
	goto while_21
endWhile_21:
	aload_1
	invokevirtual TicTacToe/changeturn()Z
	pop
	goto while_20
endWhile_20:
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


