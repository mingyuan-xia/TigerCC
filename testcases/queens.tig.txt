.686
.model flat, stdcall
option casemap :none   ; case sensitive
include windows.inc
include user32.inc
include kernel32.inc
include masm32.inc
.data
stringConst0 db 0ah,0
stringConst1 db 0ah,0
stringConst2 db 020h,04fh,0
stringConst3 db 020h,02eh,0
__printboard_fp dd ?
__try_fp dd ?
__scope6_fp dd ?
__scope0_fp dd ?
.code
extrn tg_print :near
extrn tg_newrecord :near
extrn tg_getchar :near
extrn tg_newarray :near
extrn tg_finalize :near
extrn tg_ord :near
extrn tg_chr :near
extrn tg_printi :near
extrn tg_streq :near
extrn tg_strneq :near
main PROC C
	call scope0
	call tg_finalize
	call ExitProcess
	ret 0
main endp
scope0:
label0:
;frame_enter

push ebp
mov ebp,esp
push __scope0_fp
sub esp,00h
mov __scope0_fp,ebp
;call scope6of #0 args

call scope6
;frame_leave

mov eax,eax
add esp,00h
pop __scope0_fp
pop ebp
ret

scope6:
label1:
;frame_enter

push ebp
mov ebp,esp
push __scope6_fp
sub esp,038h
mov __scope6_fp,ebp
;N=#8

mov eax,08h
;$0=newarray[N]of#0

mov [ebp-04h],eax
mov ebx,[ebp-04h]
mov ecx,00h
push ecx
push ebx
call tg_newarray
add esp,08h
;row=$0

mov [ebp-018h],eax
;$1=newarray[N]of#0

mov [ebp-08h],eax
mov ebx,[ebp-04h]
mov ecx,00h
push ecx
push ebx
call tg_newarray
add esp,08h
;col=$1

mov [ebp-01ch],eax
;$3=N+N

mov ebx,[ebp-04h]
mov ecx,[ebp-04h]
add ebx,ecx
;$4=$3-#1

mov [ebp-020h],ebx
mov edx,01h
sub ebx,edx
;$2=newarray[$4]of#0

mov [ebp-0ch],eax
mov [ebp-024h],ebx
mov ebx,[ebp-024h]
mov ecx,00h
push ecx
push ebx
call tg_newarray
add esp,08h
;diag1=$2

mov [ebp-028h],eax
;$6=N+N

mov ebx,[ebp-04h]
mov ecx,[ebp-04h]
add ebx,ecx
;$7=$6-#1

mov [ebp-02ch],ebx
mov edx,01h
sub ebx,edx
;$5=newarray[$7]of#0

mov [ebp-010h],eax
mov [ebp-030h],ebx
mov ebx,[ebp-030h]
mov ecx,00h
push ecx
push ebx
call tg_newarray
add esp,08h
;diag2=$5

mov [ebp-034h],eax
;push #0

mov ebx,00h
push ebx
;$41=call tryof #1 args

mov [ebp-014h],eax
call try
add esp,04h
;frame_leave$41

mov [ebp-038h],eax
mov eax,eax
add esp,038h
pop __scope6_fp
pop ebp
ret

printboard:
label2:
;frame_enter

push ebp
mov ebp,esp
push __printboard_fp
sub esp,030h
mov __printboard_fp,ebp
;__loop_i=#0

mov eax,00h
;jump label3

mov [ebp-04h],eax
jmp label3

label3:
;__loop_j=#0

mov eax,00h
;jump label5

mov [ebp-08h],eax
jmp label5

label5:
;$8=col[__loop_i]

mov eax,[ebp-010h]
push ebp
mov ebp,__scope6_fp
mov ecx,[ebp-0ch]
pop ebp
mov edx,[ebp-04h]
mov ebx,edx
shl ebx,1
shl ebx,1
add ebx,ecx
mov eax,[ebx]
;$9=($8==__loop_j)

mov ebx,[ebp-014h]
mov esi,[ebp-08h]
mov ebx,00h
mov edi,01h
cmp eax,esi
cmove ebx,edi
;if $9!=0 then label7 else label8

mov [ebp-010h],eax
mov [ebp-014h],ebx
cmp ebx,00h
jne label7
jmp label8

label6:
;push #

lea eax,stringConst1
push eax
;$14=call printof #1 args

call tg_print
add esp,04h
;__loop_i=__loop_i+#1

mov ebx,[ebp-04h]
mov ecx,01h
add ebx,ecx
;$15=N-#1

push ebp
mov ebp,__scope6_fp
mov edx,[ebp-04h]
pop ebp
mov esi,01h
sub edx,esi
;$16=(__loop_i>$15)

mov edi,[ebp-018h]
mov edi,00h
mov ecx,01h
cmp ebx,edx
cmovg edi,ecx
;if $16==0 then label3 else label4

mov [ebp-01ch],eax
mov [ebp-04h],ebx
mov [ebp-020h],edx
mov [ebp-018h],edi
cmp edi,00h
je label3
jmp label4

label7:
;$10=#

lea eax,stringConst2
;jump label9

mov [ebp-024h],eax
jmp label9

label8:
;$10=#

lea eax,stringConst3
;jump label9

mov [ebp-024h],eax
jmp label9

label9:
;push $10

mov eax,[ebp-024h]
push eax
;$11=call printof #1 args

call tg_print
add esp,04h
;__loop_j=__loop_j+#1

mov ebx,[ebp-08h]
mov ecx,01h
add ebx,ecx
;$12=N-#1

push ebp
mov ebp,__scope6_fp
mov edx,[ebp-04h]
pop ebp
mov esi,01h
sub edx,esi
;$13=(__loop_j>$12)

mov edi,[ebp-028h]
mov edi,00h
mov esi,01h
cmp ebx,edx
cmovg edi,esi
;if $13==0 then label5 else label6

mov [ebp-02ch],eax
mov [ebp-08h],ebx
mov [ebp-030h],edx
mov [ebp-028h],edi
cmp edi,00h
je label5
jmp label6

label4:
;push #

lea eax,stringConst0
push eax
;$17=call printof #1 args

call tg_print
add esp,04h
;frame_leave$17

mov [ebp-0ch],eax
mov eax,eax
add esp,030h
pop __printboard_fp
pop ebp
ret


try:
label10:
;frame_enter

push ebp
mov ebp,esp
push __try_fp
sub esp,060h
mov __try_fp,ebp
;$18=(c==N)

mov eax,[ebp-08h]
mov ebx,[ebp+08h]
push ebp
mov ebp,__scope6_fp
mov ecx,[ebp-04h]
pop ebp
mov eax,00h
mov edx,01h
cmp ebx,ecx
cmove eax,edx
;if $18!=0 then label11 else label12

mov [ebp-08h],eax
cmp eax,00h
jne label11
jmp label12

label11:
;$19=call printboardof #0 args

call printboard
;jump label13

mov [ebp-0ch],eax
jmp label13

label12:
;__loop_r=#0

mov eax,00h
;jump label14

mov [ebp-04h],eax
jmp label14

label14:
;$20=row[__loop_r]

mov eax,[ebp-010h]
push ebp
mov ebp,__scope6_fp
mov ecx,[ebp-08h]
pop ebp
mov edx,[ebp-04h]
mov ebx,edx
shl ebx,1
shl ebx,1
add ebx,ecx
mov eax,[ebx]
;$21=($20==#0)

mov ebx,[ebp-014h]
mov esi,00h
mov ebx,00h
mov edi,01h
cmp eax,esi
cmove ebx,edi
;$23=__loop_r+c

mov edi,[ebp+08h]
add edx,edi
;$22=diag1[$23]

mov [ebp-014h],ebx
mov edi,[ebp-018h]
mov [ebp-010h],eax
push ebp
mov ebp,__scope6_fp
mov eax,[ebp-010h]
pop ebp
mov ebx,edx
shl ebx,1
shl ebx,1
add ebx,eax
mov edi,[ebx]
;$24=($22==#0)

mov ebx,[ebp-01ch]
mov ecx,00h
mov [ebp-020h],edx
mov ebx,00h
mov edx,01h
cmp edi,ecx
cmove ebx,edx
;$25=$21&$24

mov edx,[ebp-014h]
and edx,ebx
;$27=__loop_r+#7

mov esi,[ebp-04h]
mov [ebp-018h],edi
mov edi,07h
add esi,edi
;$28=$27-c

mov [ebp-024h],esi
mov eax,[ebp+08h]
sub esi,eax
;$26=diag2[$28]

mov [ebp-01ch],ebx
mov ecx,[ebp-028h]
mov [ebp-02ch],edx
push ebp
mov ebp,__scope6_fp
mov edx,[ebp-014h]
pop ebp
mov ebx,esi
shl ebx,1
shl ebx,1
add ebx,edx
mov ecx,[ebx]
;$29=($26==#0)

mov ebx,[ebp-030h]
mov [ebp-034h],esi
mov esi,00h
mov ebx,00h
mov edi,01h
cmp ecx,esi
cmove ebx,edi
;$30=$25&$29

mov edi,[ebp-02ch]
and edi,ebx
;if $30!=0 then label16 else label17

mov [ebp-030h],ebx
mov [ebp-028h],ecx
mov [ebp-038h],edi
cmp edi,00h
jne label16
jmp label17

label15:
;jump label13

jmp label13

label16:
;row[__loop_r]=#1

push ebp
mov ebp,__scope6_fp
mov eax,[ebp-08h]
pop ebp
mov ecx,[ebp-04h]
mov edx,01h
mov ebx,ecx
shl ebx,1
shl ebx,1
add ebx,eax
mov [ebx],edx
;$31=__loop_r+c

mov ebx,[ebp+08h]
add ecx,ebx
;diag1[$31]=#1

push ebp
mov ebp,__scope6_fp
mov esi,[ebp-010h]
pop ebp
mov edi,01h
mov ebx,ecx
shl ebx,1
shl ebx,1
add ebx,esi
mov [ebx],edi
;$32=__loop_r+#7

mov ebx,[ebp-04h]
mov eax,07h
add ebx,eax
;$33=$32-c

mov [ebp-03ch],ebx
mov [ebp-040h],ecx
mov ecx,[ebp+08h]
sub ebx,ecx
;diag2[$33]=#1

mov [ebp-044h],ebx
push ebp
mov ebp,__scope6_fp
mov edx,[ebp-014h]
pop ebp
mov esi,[ebp-044h]
mov edi,01h
mov ebx,esi
shl ebx,1
shl ebx,1
add ebx,edx
mov [ebx],edi
;col[c]=__loop_r

push ebp
mov ebp,__scope6_fp
mov eax,[ebp-0ch]
pop ebp
mov edx,[ebp-04h]
mov ebx,ecx
shl ebx,1
shl ebx,1
add ebx,eax
mov [ebx],edx
;$34=c+#1

mov ebx,01h
add ecx,ebx
;push $34

push ecx
;$35=call tryof #1 args

mov [ebp-048h],ecx
call try
add esp,04h
;row[__loop_r]=#0

push ebp
mov ebp,__scope6_fp
mov ecx,[ebp-08h]
pop ebp
mov edx,[ebp-04h]
mov esi,00h
mov ebx,edx
shl ebx,1
shl ebx,1
add ebx,ecx
mov [ebx],esi
;$36=__loop_r+c

mov ebx,[ebp+08h]
add edx,ebx
;diag1[$36]=#0

push ebp
mov ebp,__scope6_fp
mov edi,[ebp-010h]
pop ebp
mov esi,00h
mov ebx,edx
shl ebx,1
shl ebx,1
add ebx,edi
mov [ebx],esi
;$37=__loop_r+#7

mov ebx,[ebp-04h]
mov edi,07h
add ebx,edi
;$38=$37-c

mov [ebp-04ch],ebx
mov [ebp-050h],eax
mov eax,[ebp+08h]
sub ebx,eax
;diag2[$38]=#0

mov [ebp-054h],ebx
push ebp
mov ebp,__scope6_fp
mov ecx,[ebp-014h]
pop ebp
mov [ebp-058h],edx
mov edx,[ebp-054h]
mov esi,00h
mov ebx,edx
shl ebx,1
shl ebx,1
add ebx,ecx
mov [ebx],esi
;jump label17

jmp label17

label17:
;__loop_r=__loop_r+#1

mov eax,[ebp-04h]
mov ebx,01h
add eax,ebx
;$39=N-#1

push ebp
mov ebp,__scope6_fp
mov ecx,[ebp-04h]
pop ebp
mov edx,01h
sub ecx,edx
;$40=(__loop_r>$39)

mov esi,[ebp-05ch]
mov esi,00h
mov edi,01h
cmp eax,ecx
cmovg esi,edi
;if $40==0 then label14 else label15

mov [ebp-04h],eax
mov [ebp-060h],ecx
mov [ebp-05ch],esi
cmp esi,00h
je label14
jmp label15

label13:
;frame_leave

mov eax,eax
add esp,060h
pop __try_fp
pop ebp
ret




end
