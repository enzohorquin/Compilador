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
@out1 db "entro case 1", 0
	@out2 db "entro case 2", 0
	@out3 db "entro case 3", 0
	@out4 db "entro case 4", 0
	@out5 db "entro case 1,1", 0
	@out6 db "entro case 2,1", 0
	@out7 db "entro case 3,1", 0
	@out8 db "entro case 4,1", 0
	_2_1 dq 2.1
_4_1 dq 4.1
_x@main dd ?
_y@main dq ?
_1_1 dq 1.1
_3_1 dq 3.1
_4 dd 4
_3 dd 3
_2 dd 2
_1 dd 1
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
MOV EAX, _1
MOV _x@main, EAX
FLD _1_1
FST _y@main
MOV EAX, _x@main
CMP EAX, _1
JNE label1
invoke MessageBox, NULL, addr @out1, addr @out1, MB_OK
label1:
MOV EAX, _x@main
CMP EAX, _2
JNE label2
invoke MessageBox, NULL, addr @out2, addr @out2, MB_OK
label2:
MOV EAX, _x@main
CMP EAX, _3
JNE label3
invoke MessageBox, NULL, addr @out3, addr @out3, MB_OK
label3:
MOV EAX, _x@main
CMP EAX, _4
JNE label4
invoke MessageBox, NULL, addr @out4, addr @out4, MB_OK
label4:
FLD _y@main
FCOMP _1_1
FSTSW AX
SAHF
JNE label5
invoke MessageBox, NULL, addr @out5, addr @out5, MB_OK
label5:
FLD _y@main
FCOMP _2_1
FSTSW AX
SAHF
JNE label6
invoke MessageBox, NULL, addr @out6, addr @out6, MB_OK
label6:
FLD _y@main
FCOMP _3_1
FSTSW AX
SAHF
JNE label7
invoke MessageBox, NULL, addr @out7, addr @out7, MB_OK
label7:
FLD _y@main
FCOMP _4_1
FSTSW AX
SAHF
JNE label8
invoke MessageBox, NULL, addr @out8, addr @out8, MB_OK
label8:
invoke ExitProcess, 0
end start


