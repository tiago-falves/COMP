.class public Life
.super java/lang/Object
.field UNDERPOP_LIM I
.field OVERPOP_LIM I
.field REPRODUCE_NUM I
.field LOOPS_PER_MS I
.field xMax I
.field yMax I
.field _field [I

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 2
	.limit locals 3
	new Life
	dup
	invokespecial Life/<init>()V
	astore_1
	aload_1
	invokevirtual Life/init()Z
	pop
while_11:
	iconst_1
	ifeq endWhile_11
	aload_1
	invokevirtual Life/printField()Z
	pop
	aload_1
	invokevirtual Life/update()Z
	pop
	invokestatic io/read()I
	istore_2
	goto while_11
endWhile_11:
	return
.end method

.method public init()Z
	.limit stack 4
	.limit locals 3
	iconst_1
	newarray int
	astore_1
	aload_0
	iconst_2
	putfield Life/UNDERPOP_LIM I
	aload_0
	iconst_3
	putfield Life/OVERPOP_LIM I
	aload_0
	iconst_3
	putfield Life/REPRODUCE_NUM I
	aload_0
	ldc	225000
	putfield Life/LOOPS_PER_MS I
	aload_0
	aload_0
	aload_1
	invokevirtual Life/field([I)[I
	putfield Life/_field [I
	aload_1
	iconst_0
	iaload
	istore_2
	aload_0
	iload_2
	iconst_1
	isub
	putfield Life/xMax I
	aload_0
	aload_0
	getfield Life/_field [I
	arraylength
	iload_2
	idiv
	iconst_1
	isub
	putfield Life/yMax I
	iconst_1
	ireturn
.end method

.method public field([I)[I
	.limit stack 3
	.limit locals 3
	bipush	100
	newarray int
	astore_2
	aload_1
	iconst_0
	bipush	10
	iastore
	aload_2
	iconst_0
	iconst_0
	iastore
	aload_2
	iconst_1
	iconst_0
	iastore
	aload_2
	iconst_2
	iconst_1
	iastore
	aload_2
	iconst_3
	iconst_0
	iastore
	aload_2
	iconst_4
	iconst_0
	iastore
	aload_2
	iconst_5
	iconst_0
	iastore
	aload_2
	bipush	6
	iconst_0
	iastore
	aload_2
	bipush	7
	iconst_0
	iastore
	aload_2
	bipush	8
	iconst_0
	iastore
	aload_2
	bipush	9
	iconst_0
	iastore
	aload_2
	bipush	10
	iconst_1
	iastore
	aload_2
	bipush	11
	iconst_0
	iastore
	aload_2
	bipush	12
	iconst_1
	iastore
	aload_2
	bipush	13
	iconst_0
	iastore
	aload_2
	bipush	14
	iconst_0
	iastore
	aload_2
	bipush	15
	iconst_0
	iastore
	aload_2
	bipush	16
	iconst_0
	iastore
	aload_2
	bipush	17
	iconst_0
	iastore
	aload_2
	bipush	18
	iconst_0
	iastore
	aload_2
	bipush	19
	iconst_0
	iastore
	aload_2
	bipush	20
	iconst_0
	iastore
	aload_2
	bipush	21
	iconst_1
	iastore
	aload_2
	bipush	22
	iconst_1
	iastore
	aload_2
	bipush	23
	iconst_0
	iastore
	aload_2
	bipush	24
	iconst_0
	iastore
	aload_2
	bipush	25
	iconst_0
	iastore
	aload_2
	bipush	26
	iconst_0
	iastore
	aload_2
	bipush	27
	iconst_0
	iastore
	aload_2
	bipush	28
	iconst_0
	iastore
	aload_2
	bipush	29
	iconst_0
	iastore
	aload_2
	bipush	30
	iconst_0
	iastore
	aload_2
	bipush	31
	iconst_0
	iastore
	aload_2
	bipush	32
	iconst_0
	iastore
	aload_2
	bipush	33
	iconst_0
	iastore
	aload_2
	bipush	34
	iconst_0
	iastore
	aload_2
	bipush	35
	iconst_0
	iastore
	aload_2
	bipush	36
	iconst_0
	iastore
	aload_2
	bipush	37
	iconst_0
	iastore
	aload_2
	bipush	38
	iconst_0
	iastore
	aload_2
	bipush	39
	iconst_0
	iastore
	aload_2
	bipush	40
	iconst_0
	iastore
	aload_2
	bipush	41
	iconst_0
	iastore
	aload_2
	bipush	42
	iconst_0
	iastore
	aload_2
	bipush	43
	iconst_0
	iastore
	aload_2
	bipush	44
	iconst_0
	iastore
	aload_2
	bipush	45
	iconst_0
	iastore
	aload_2
	bipush	46
	iconst_0
	iastore
	aload_2
	bipush	47
	iconst_0
	iastore
	aload_2
	bipush	48
	iconst_0
	iastore
	aload_2
	bipush	49
	iconst_0
	iastore
	aload_2
	bipush	50
	iconst_0
	iastore
	aload_2
	bipush	51
	iconst_0
	iastore
	aload_2
	bipush	52
	iconst_0
	iastore
	aload_2
	bipush	53
	iconst_0
	iastore
	aload_2
	bipush	54
	iconst_0
	iastore
	aload_2
	bipush	55
	iconst_0
	iastore
	aload_2
	bipush	56
	iconst_0
	iastore
	aload_2
	bipush	57
	iconst_0
	iastore
	aload_2
	bipush	58
	iconst_0
	iastore
	aload_2
	bipush	59
	iconst_0
	iastore
	aload_2
	bipush	60
	iconst_0
	iastore
	aload_2
	bipush	61
	iconst_0
	iastore
	aload_2
	bipush	62
	iconst_0
	iastore
	aload_2
	bipush	63
	iconst_0
	iastore
	aload_2
	bipush	64
	iconst_0
	iastore
	aload_2
	bipush	65
	iconst_0
	iastore
	aload_2
	bipush	66
	iconst_0
	iastore
	aload_2
	bipush	67
	iconst_0
	iastore
	aload_2
	bipush	68
	iconst_0
	iastore
	aload_2
	bipush	69
	iconst_0
	iastore
	aload_2
	bipush	70
	iconst_0
	iastore
	aload_2
	bipush	71
	iconst_0
	iastore
	aload_2
	bipush	72
	iconst_0
	iastore
	aload_2
	bipush	73
	iconst_0
	iastore
	aload_2
	bipush	74
	iconst_0
	iastore
	aload_2
	bipush	75
	iconst_0
	iastore
	aload_2
	bipush	76
	iconst_0
	iastore
	aload_2
	bipush	77
	iconst_0
	iastore
	aload_2
	bipush	78
	iconst_0
	iastore
	aload_2
	bipush	79
	iconst_0
	iastore
	aload_2
	bipush	80
	iconst_0
	iastore
	aload_2
	bipush	81
	iconst_0
	iastore
	aload_2
	bipush	82
	iconst_0
	iastore
	aload_2
	bipush	83
	iconst_0
	iastore
	aload_2
	bipush	84
	iconst_0
	iastore
	aload_2
	bipush	85
	iconst_0
	iastore
	aload_2
	bipush	86
	iconst_0
	iastore
	aload_2
	bipush	87
	iconst_0
	iastore
	aload_2
	bipush	88
	iconst_0
	iastore
	aload_2
	bipush	89
	iconst_0
	iastore
	aload_2
	bipush	90
	iconst_0
	iastore
	aload_2
	bipush	91
	iconst_0
	iastore
	aload_2
	bipush	92
	iconst_0
	iastore
	aload_2
	bipush	93
	iconst_0
	iastore
	aload_2
	bipush	94
	iconst_0
	iastore
	aload_2
	bipush	95
	iconst_0
	iastore
	aload_2
	bipush	96
	iconst_0
	iastore
	aload_2
	bipush	97
	iconst_0
	iastore
	aload_2
	bipush	98
	iconst_0
	iastore
	aload_2
	bipush	99
	iconst_0
	iastore
	aload_2
	areturn
.end method

.method public update()Z
	.limit stack 5
	.limit locals 6
	aload_0
	getfield Life/_field [I
	arraylength
	newarray int
	astore	5
	iconst_0
	istore_1
while_12:
	iload_1
	aload_0
	getfield Life/_field [I
	arraylength
	if_icmpge notLess_41
	iconst_1
	goto endLess_41
notLess_41:
	iconst_0
endLess_41:
	ifeq endWhile_12
	aload_0
	getfield Life/_field [I
	iload_1
	iaload
	istore_2
	aload_0
	iload_1
	invokevirtual Life/getLiveNeighborN(I)I
	istore_3
	iload_2
	iconst_1
	if_icmpge notLess_42
	iconst_1
	goto endLess_42
notLess_42:
	iconst_0
endLess_42:
	iconst_1
	ixor
	ifeq else_28
	aload_0
	iload_3
	aload_0
	getfield Life/UNDERPOP_LIM I
	invokevirtual Life/ge(II)Z
	aload_0
	iload_3
	aload_0
	getfield Life/OVERPOP_LIM I
	invokevirtual Life/le(II)Z
	iand
	istore	4
	iload	4
	iconst_1
	ixor
	ifeq else_29
	aload	5
	iload_1
	iconst_0
	iastore
	goto endIf_29
else_29:
	aload	5
	iload_1
	aload_0
	getfield Life/_field [I
	iload_1
	iaload
	iastore
endIf_29:
	goto endIf_28
else_28:
	aload_0
	iload_3
	aload_0
	getfield Life/REPRODUCE_NUM I
	invokevirtual Life/eq(II)Z
	ifeq else_30
	aload	5
	iload_1
	iconst_1
	iastore
	goto endIf_30
else_30:
	aload	5
	iload_1
	aload_0
	getfield Life/_field [I
	iload_1
	iaload
	iastore
endIf_30:
endIf_28:
	iload_1
	iconst_1
	iadd
	istore_1
	goto while_12
endWhile_12:
	aload_0
	aload	5
	putfield Life/_field [I
	iconst_1
	ireturn
.end method

.method public printField()Z
	.limit stack 4
	.limit locals 3
	iconst_0
	istore_1
	iconst_0
	istore_2
while_13:
	iload_1
	aload_0
	getfield Life/_field [I
	arraylength
	if_icmpge notLess_43
	iconst_1
	goto endLess_43
notLess_43:
	iconst_0
endLess_43:
	ifeq endWhile_13
	aload_0
	iload_2
	aload_0
	getfield Life/xMax I
	invokevirtual Life/gt(II)Z
	ifeq else_31
	invokestatic io/println()V
	iconst_0
	istore_2
	goto endIf_31
else_31:
endIf_31:
	aload_0
	getfield Life/_field [I
	iload_1
	iaload
	invokestatic io/print(I)V
	iload_1
	iconst_1
	iadd
	istore_1
	iload_2
	iconst_1
	iadd
	istore_2
	goto while_13
endWhile_13:
	invokestatic io/println()V
	invokestatic io/println()V
	iconst_1
	ireturn
.end method

.method public trIdx(II)I
	.limit stack 2
	.limit locals 3
	aload_0
	getfield Life/xMax I
	iconst_1
	iadd
	iload_2
	imul
	iload_1
	iadd
	ireturn
.end method

.method public cartIdx(I)[I
	.limit stack 3
	.limit locals 6
	aload_0
	getfield Life/xMax I
	iconst_1
	iadd
	istore	4
	iload_1
	iload	4
	idiv
	istore_3
	iconst_0
	iload_3
	iload	4
	imul
	isub
	iload_1
	iadd
	istore_2
	iconst_2
	newarray int
	astore	5
	aload	5
	iconst_0
	iload_2
	iastore
	aload	5
	iconst_1
	iload_3
	iastore
	aload	5
	areturn
.end method

.method public getNeighborCoords(I)[I
	.limit stack 5
	.limit locals 10
	aload_0
	iload_1
	invokevirtual Life/cartIdx(I)[I
	astore	8
	aload	8
	iconst_0
	iaload
	istore_2
	aload	8
	iconst_1
	iaload
	istore_3
	iload_2
	aload_0
	getfield Life/xMax I
	if_icmpge notLess_44
	iconst_1
	goto endLess_44
notLess_44:
	iconst_0
endLess_44:
	ifeq else_32
	iload_2
	iconst_1
	iadd
	istore	6
	aload_0
	iload_2
	iconst_0
	invokevirtual Life/gt(II)Z
	ifeq else_33
	iload_2
	iconst_1
	isub
	istore	4
	goto endIf_33
else_33:
	aload_0
	getfield Life/xMax I
	istore	4
endIf_33:
	goto endIf_32
else_32:
	iconst_0
	istore	6
	iload_2
	iconst_1
	isub
	istore	4
endIf_32:
	iload_3
	aload_0
	getfield Life/yMax I
	if_icmpge notLess_45
	iconst_1
	goto endLess_45
notLess_45:
	iconst_0
endLess_45:
	ifeq else_34
	iload_3
	iconst_1
	iadd
	istore	7
	aload_0
	iload_3
	iconst_0
	invokevirtual Life/gt(II)Z
	ifeq else_35
	iload_3
	iconst_1
	isub
	istore	5
	goto endIf_35
else_35:
	aload_0
	getfield Life/yMax I
	istore	5
endIf_35:
	goto endIf_34
else_34:
	iconst_0
	istore	7
	iload_3
	iconst_1
	isub
	istore	5
endIf_34:
	bipush	8
	newarray int
	astore	9
	aload	9
	iconst_0
	aload_0
	iload_2
	iload	5
	invokevirtual Life/trIdx(II)I
	iastore
	aload	9
	iconst_1
	aload_0
	iload	4
	iload	5
	invokevirtual Life/trIdx(II)I
	iastore
	aload	9
	iconst_2
	aload_0
	iload	4
	iload_3
	invokevirtual Life/trIdx(II)I
	iastore
	aload	9
	iconst_3
	aload_0
	iload	4
	iload	7
	invokevirtual Life/trIdx(II)I
	iastore
	aload	9
	iconst_4
	aload_0
	iload_2
	iload	7
	invokevirtual Life/trIdx(II)I
	iastore
	aload	9
	iconst_5
	aload_0
	iload	6
	iload	7
	invokevirtual Life/trIdx(II)I
	iastore
	aload	9
	bipush	6
	aload_0
	iload	6
	iload_3
	invokevirtual Life/trIdx(II)I
	iastore
	aload	9
	bipush	7
	aload_0
	iload	6
	iload	5
	invokevirtual Life/trIdx(II)I
	iastore
	aload	9
	areturn
.end method

.method public getLiveNeighborN(I)I
	.limit stack 7
	.limit locals 5
	iconst_0
	istore	4
	aload_0
	iload_1
	invokevirtual Life/getNeighborCoords(I)[I
	astore_2
	iconst_0
	istore_3
while_14:
	iload_3
	aload_2
	arraylength
	if_icmpge notLess_46
	iconst_1
	goto endLess_46
notLess_46:
	iconst_0
endLess_46:
	ifeq endWhile_14
	aload_0
	aload_0
	getfield Life/_field [I
	aload_2
	iload_3
	iaload
	iaload
	iconst_0
	invokevirtual Life/ne(II)Z
	ifeq else_36
	iload	4
	iconst_1
	iadd
	istore	4
	goto endIf_36
else_36:
endIf_36:
	iload_3
	iconst_1
	iadd
	istore_3
	goto while_14
endWhile_14:
	iload	4
	ireturn
.end method

.method public busyWait(I)Z
	.limit stack 4
	.limit locals 4
	iload_1
	aload_0
	getfield Life/LOOPS_PER_MS I
	imul
	istore_3
	iconst_0
	istore_2
while_15:
	iload_2
	iload_3
	if_icmpge notLess_47
	iconst_1
	goto endLess_47
notLess_47:
	iconst_0
endLess_47:
	ifeq endWhile_15
	iload_2
	iconst_1
	iadd
	istore_2
	goto while_15
endWhile_15:
	iconst_1
	ireturn
.end method

.method public eq(II)Z
	.limit stack 6
	.limit locals 3
	aload_0
	iload_1
	iload_2
	invokevirtual Life/lt(II)Z
	iconst_1
	ixor
	aload_0
	iload_2
	iload_1
	invokevirtual Life/lt(II)Z
	iconst_1
	ixor
	iand
	ireturn
.end method

.method public ne(II)Z
	.limit stack 3
	.limit locals 3
	aload_0
	iload_1
	iload_2
	invokevirtual Life/eq(II)Z
	iconst_1
	ixor
	ireturn
.end method

.method public lt(II)Z
	.limit stack 4
	.limit locals 3
	iload_1
	iload_2
	if_icmpge notLess_48
	iconst_1
	goto endLess_48
notLess_48:
	iconst_0
endLess_48:
	ireturn
.end method

.method public le(II)Z
	.limit stack 7
	.limit locals 3
	aload_0
	iload_1
	iload_2
	invokevirtual Life/lt(II)Z
	iconst_1
	ixor
	aload_0
	iload_1
	iload_2
	invokevirtual Life/eq(II)Z
	iconst_1
	ixor
	iand
	iconst_1
	ixor
	ireturn
.end method

.method public gt(II)Z
	.limit stack 3
	.limit locals 3
	aload_0
	iload_1
	iload_2
	invokevirtual Life/le(II)Z
	iconst_1
	ixor
	ireturn
.end method

.method public ge(II)Z
	.limit stack 7
	.limit locals 3
	aload_0
	iload_1
	iload_2
	invokevirtual Life/gt(II)Z
	iconst_1
	ixor
	aload_0
	iload_1
	iload_2
	invokevirtual Life/eq(II)Z
	iconst_1
	ixor
	iand
	iconst_1
	ixor
	ireturn
.end method


