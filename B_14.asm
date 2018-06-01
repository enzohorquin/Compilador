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
@out1 db "correcto perro", 0
	@out2 db "hola", 0
	@out3 db "no tendria q entrar", 0
	_x@main@suma dq ?
_13_0 dq 13.0
_2_0 dq 2.0
@aux4 dq ?
_4_0 dq 4.0
@aux3 dq ?
@aux2 dq ?
@aux1 dq ?
_x@main dq ?
_y@main dq ?
_1_0 dq 1.0
_fc@main@suma dq ?
_3_0 dq 3.0
_fd@main@suma dq ?
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
FLD _3_0
FST _x@main@suma
FLD _4_0
FMUL _x@main@suma
FSTSW AX
SAHF
JO errorMult
FST @aux1
FLD _1_0
FADD @aux1
FSTSW AX
SAHF
JO errorMult
FST @aux2
FLD  @aux2
FST __ret2
ret
start:
FLD _2_0
FST _x@main
call suma@main
FLD __ret2
FST @aux3
FINIT 
FLD @aux3
FST _y@main
FLD _y@main
FCOMP _13_0
FSTSW AX
SAHF
JNE label1
invoke MessageBox, NULL, addr @out1, addr @out1, MB_OK
invoke MessageBox, NULL, addr @out2, addr @out2, MB_OK
JMP label2
label1:
invoke MessageBox, NULL, addr @out3, addr @out3, MB_OK
label2:
invoke ExitProcess, 0
end start


