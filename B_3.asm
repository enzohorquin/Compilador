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
@out1 db "si, 2 es mayor que 1", 0
	@aux1 dd ?
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
MOV EAX, _2
CMP EAX, _1
JLE label1
invoke MessageBox, NULL, addr @out1, addr @out1, MB_OK
label1:
invoke ExitProcess, 0
end start


