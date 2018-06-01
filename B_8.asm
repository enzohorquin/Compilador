.386 
.model flat, stdcall 
option casemap :none  
include \masm32\include\windows.inc 
include \masm32\include\kernel32.inc 
include \masm32\include\masm32.inc  
includelib \masm32\lib\kernel32.lib 
includelib \masm32\lib\masm32.lib
include \masm32\include\user32.inc 
includelib \masm32\lib\user32.lib 

.data
@out1 db "paso decl", 0
	@out2 db "paso div entera", 0
	@out3 db "paso decl 2", 0
	@out4 db "paso div double", 0
	@out5 db "entro case 1 bloque then", 0
	@out6 db "entro case 2 bloque then", 0
	@out7 db "entro case 3 bloque then", 0
	@out8 db "entro case 4 bloque then", 0
	@out9 db "entro case 1 bloque else", 0
	@out10 db "entro case 2 bloque else", 0
	@out11 db "entro case 3 bloque else", 0
	@out12 db "entro case 4 bloque else", 0
	@out13 db "entro case 1,1 bloque then", 0
	@out14 db "entro case 2,1 bloque then", 0
	@out15 db "entro case 3,1 bloque then", 0
	@out16 db "entro case 4,1 bloque then", 0
	@out17 db "entro case 1,1 bloque else", 0
	@out18 db "entro case 2,1 bloque else", 0
	@out19 db "entro case 3,1 bloque else", 0
	@out20 db "entro case 4,1 bloque else", 0
	_y@main dq ?
@aux4 dq ?
@aux3 dd ?
@aux2 dq ?
@aux1 dd ?
_8 dd 8
_4 dd 4
_4_1 dq 4.1
_3 dd 3
_2 dd 2
_1 dd 1
_x@main dd ?
_3_1 dq 3.1
_2_2 dq 2.2
_2_1 dq 2.1
_2_0 dq 2.0
_1_1 dq 1.1
__ret1 dd ?
__ret2 dq ?
@aux_Flags dw ?
errorMsgMult db "Error: Overflow.", 0
errorMsgResta db "Error: La resta dio como resultado un numero negativo.", 0
errorMsgDivCero db "Error: La division por cero no esta definida.", 0

.code
errorMult:
invoke MessageBox, NULL, addr errorMsgMult, addr errorMsgMult, MB_OK
invoke ExitProcess, 1
errorDivCero:
invoke MessageBox, NULL, addr errorMsgDivCero, addr errorMsgDivCero, MB_OK
invoke ExitProcess, 1
errorResta:
invoke MessageBox, NULL, addr errorMsgResta, addr errorMsgResta, MB_OK
invoke ExitProcess, 1
start:
invoke MessageBox, NULL, addr @out1, addr @out1, MB_OK
CMP _2, 0
JE errorDivCero
MOV EAX, _8
DIV _2
MOV @aux1, EAX
MOV EAX, @aux1
MOV _x@main, EAX
invoke MessageBox, NULL, addr @out2, addr @out2, MB_OK
invoke MessageBox, NULL, addr @out3, addr @out3, MB_OK
FLD _2_0
FSUB _2_0
FCOMP _2_0
FSTSW AX
SAHF
JE errorDivCero
FLD _2_2
FDIV _2_0
FST @aux2
FLD @aux2
FST _y@main
invoke MessageBox, NULL, addr @out4, addr @out4, MB_OK
MOV EAX, _x@main
CMP EAX, _2
JNE label1
MOV EAX, _x@main
CMP EAX, _1
JNE label2
invoke MessageBox, NULL, addr @out5, addr @out5, MB_OK
label2:
MOV EAX, _x@main
CMP EAX, _2
JNE label3
invoke MessageBox, NULL, addr @out6, addr @out6, MB_OK
label3:
MOV EAX, _x@main
CMP EAX, _3
JNE label4
invoke MessageBox, NULL, addr @out7, addr @out7, MB_OK
label4:
MOV EAX, _x@main
CMP EAX, _4
JNE label5
invoke MessageBox, NULL, addr @out8, addr @out8, MB_OK
label5:
JMP label6
label1:
MOV EAX, _x@main
CMP EAX, _1
JNE label7
invoke MessageBox, NULL, addr @out9, addr @out9, MB_OK
label7:
MOV EAX, _x@main
CMP EAX, _2
JNE label8
invoke MessageBox, NULL, addr @out10, addr @out10, MB_OK
label8:
MOV EAX, _x@main
CMP EAX, _3
JNE label9
invoke MessageBox, NULL, addr @out11, addr @out11, MB_OK
label9:
MOV EAX, _x@main
CMP EAX, _4
JNE label10
invoke MessageBox, NULL, addr @out12, addr @out12, MB_OK
label10:
label6:
FLD _y@main
FCOMP _1_1
FSTSW AX
SAHF
JNE label11
FLD _y@main
FCOMP _1_1
FSTSW AX
SAHF
JNE label12
invoke MessageBox, NULL, addr @out13, addr @out13, MB_OK
label12:
FLD _y@main
FCOMP _2_1
FSTSW AX
SAHF
JNE label13
invoke MessageBox, NULL, addr @out14, addr @out14, MB_OK
label13:
FLD _y@main
FCOMP _3_1
FSTSW AX
SAHF
JNE label14
invoke MessageBox, NULL, addr @out15, addr @out15, MB_OK
label14:
FLD _y@main
FCOMP _4_1
FSTSW AX
SAHF
JNE label15
invoke MessageBox, NULL, addr @out16, addr @out16, MB_OK
label15:
JMP label16
label11:
FLD _y@main
FCOMP _1_1
FSTSW AX
SAHF
JNE label17
invoke MessageBox, NULL, addr @out17, addr @out17, MB_OK
label17:
FLD _y@main
FCOMP _2_1
FSTSW AX
SAHF
JNE label18
invoke MessageBox, NULL, addr @out18, addr @out18, MB_OK
label18:
FLD _y@main
FCOMP _3_1
FSTSW AX
SAHF
JNE label19
invoke MessageBox, NULL, addr @out19, addr @out19, MB_OK
label19:
FLD _y@main
FCOMP _4_1
FSTSW AX
SAHF
JNE label20
invoke MessageBox, NULL, addr @out20, addr @out20, MB_OK
label20:
label16:
invoke ExitProcess, 0
end start


