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
@out1 db "entro case 1,1", 0
	@out2 db "entro case 1", 0
	@out3 db "case 2 case 2", 0
	@out4 db "entro case 2", 0
	@out5 db "hollaaa", 0
	@out6 db "incoorrecto", 0
	@out7 db "coorrecto", 0
	@out8 db "mal", 0
	@out9 db "mal 2", 0
	@out10 db "mal 3", 0
	@out11 db "correcto de los correctos", 0
	@out12 db "entro case 2,1", 0
	@out13 db "entro case 1", 0
	@out14 db "entro case 2", 0
	_y@main dq ?
@aux5 dd ?
@aux4 dd ?
@aux3 dd ?
@aux2 dd ?
@aux1 dq ?
_b@main dd ?
_3 dd 3
_2 dd 2
_1 dd 1
_0 dd 0
_x@main dd ?
_z@main dq ?
_a@main dd ?
_c@main dd ?
_2_2 dq 2.2
_2_1 dq 2.1
_2_0 dq 2.0
_1_1 dq 1.1
_0_2 dq 0.2
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
MOV EAX, _2
MOV _x@main, EAX
FLD _1_1
FST _y@main
FLD _2_2
FST _z@main
FLD _y@main
FCOMP _1_1
FSTSW AX
SAHF
JNE label1
invoke MessageBox, NULL, addr @out1, addr @out1, MB_OK
MOV EAX, _x@main
CMP EAX, _1
JNE label2
invoke MessageBox, NULL, addr @out2, addr @out2, MB_OK
label2:
MOV EAX, _x@main
CMP EAX, _2
JNE label3
invoke MessageBox, NULL, addr @out3, addr @out3, MB_OK
FLD _2_0
FST _y@main
FLD _y@main
FCOMP _1_1
FSTSW AX
SAHF
JNE label4
invoke MessageBox, NULL, addr @out4, addr @out4, MB_OK
JMP label5
label4:
invoke MessageBox, NULL, addr @out5, addr @out5, MB_OK
FLD _z@main
FCOMP _0_2
FSTSW AX
SAHF
JNE label6
invoke MessageBox, NULL, addr @out6, addr @out6, MB_OK
label6:
FLD _z@main
FCOMP _2_2
FSTSW AX
SAHF
JNE label7
invoke MessageBox, NULL, addr @out7, addr @out7, MB_OK
MOV EAX, _1
MOV _a@main, EAX
MOV EAX, _2
MOV _b@main, EAX
MOV EAX, _3
MOV _c@main, EAX
MOV EAX, _a@main
CMP EAX, _0
JNE label8
invoke MessageBox, NULL, addr @out8, addr @out8, MB_OK
JMP label9
label8:
MOV EAX, _b@main
CMP EAX, _1
JNE label10
invoke MessageBox, NULL, addr @out9, addr @out9, MB_OK
JMP label11
label10:
MOV EAX, _c@main
CMP EAX, _2
JNE label12
invoke MessageBox, NULL, addr @out10, addr @out10, MB_OK
JMP label13
label12:
MOV EAX, _c@main
CMP EAX, _3
JNE label14
invoke MessageBox, NULL, addr @out11, addr @out11, MB_OK
label14:
label13:
label11:
label9:
label7:
label5:
label3:
label1:
FLD _y@main
FCOMP _2_1
FSTSW AX
SAHF
JNE label15
invoke MessageBox, NULL, addr @out12, addr @out12, MB_OK
MOV EAX, _x@main
CMP EAX, _1
JNE label16
invoke MessageBox, NULL, addr @out13, addr @out13, MB_OK
label16:
MOV EAX, _x@main
CMP EAX, _2
JNE label17
invoke MessageBox, NULL, addr @out14, addr @out14, MB_OK
label17:
label15:
invoke ExitProcess, 0
end start


