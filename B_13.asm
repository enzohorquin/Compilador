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
@out1 db "y es igual a 9", 0
	@out2 db "y no es igual a 9", 0
	_fd@main@sumados dq ?
_x@main@suma dd ?
@aux6 dd ?
@aux5 dq ?
@aux4 dq ?
@aux3 dd ?
@aux2 dd ?
@aux1 dd ?
_x@main dd ?
_fc@main@sumados dq ?
_y@main dd ?
_1_1 dq 1.1
_fc@main@suma dd ?
_9 dd 9
_3_3 dq 3.3
_z@main dq ?
_5 dd 5
_4 dd 4
_1 dd 1
_fd@main@suma dd ?
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
suma@main:
MOV EAX, _5
MUL _1
JO errorMult
MOV @aux1, EAX
MOV EAX, _4
ADD EAX, @aux1
JO errorMult
MOV @aux2, EAX
MOV EAX, @aux2
MOV __ret1, EAX
ret
sumados@main:
FLD _1_1
FADD _3_3
FSTSW AX
SAHF
JO errorMult
FST @aux4
FLD  @aux4
FST __ret2
ret
start:
call suma@main
MOV EAX, __ret1
MOV @aux3, EAX
FINIT 
MOV EAX, @aux3
MOV _y@main, EAX
call sumados@main
FLD __ret2
FST @aux5
FINIT 
FLD @aux5
FST _z@main
MOV EAX, _y@main
CMP EAX, _9
JNE label1
invoke MessageBox, NULL, addr @out1, addr @out1, MB_OK
JMP label2
label1:
invoke MessageBox, NULL, addr @out2, addr @out2, MB_OK
label2:
invoke ExitProcess, 0
end start


