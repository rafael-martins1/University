#Rafael Martins (l48596)

.globl main


.data
    msg: .asciz "Digite o personagem que quer encontrar Yoda(1) / Darth_Maul(2) / Mandalorian(3) : "
    fileNameIn: .asciz "starwars.rgb"
    rgbBuffer: .space 172800
    fileNameOut: .asciz "resposta.rgb"

.text



##################################################################################
# Função: main
# Descrição: Pede ao utilizador que escolha um dos três personagens para localizar na imagem.
# Após isso, lê a imagem e chama a função location para dar início ao algoritmo.
##################################################################################

    
main:
	la a0, msg		# Syscall para dar print de uma string
	li a7, 4
	ecall
	
	li a7,5			# Syscall para ler um inteiro do input
	ecall
	mv a3, a0
	jal ra, read_rgb_image
	mv a0,a1
	mv a1,a2
	mv a2,a3
	jal ra, location
	

##################################################################################
# Função: read_rgb_image
# Descrição: Lê uma imagem do tipo rgb e passa o conteúdo da mesma para um buffer.
# Retorna:
#  a1 - Endereço do buffer
#  a2 - Tamanho do buffer
##################################################################################


read_rgb_image:


    	li a7, 1024 	  	#Syscall para abrir o ficheiro	
    	la a0, fileNameIn 	#Ficheiro a abrir
    	li a1, 0          	#Modo read
    	ecall               
    	mv s6, a0		


    	li a7, 63	 	#Syscall para ler do ficheiro
    	la a1, rgbBuffer 	#Buffer para onde escrever
    	li a2, 172800 	 	#Tamanho do buffer
    	ecall
    

    	li a7,57	  	#Syscall para fechar o ficheiro
    	mv a0,s6		
    	ecall
	
	jalr zero, 0(ra)
	
	
##################################################################################
# Função: write_rgb_image
# Descrição: Escreve num ficheiro usando um buffer.
##################################################################################


write_rgb_image:

	li a7, 1024        	# Syscall para abrir o ficheiro
	la a0, fileNameOut 	# Ficheiro a abrir
	li a1, 1	   	# Modo write
	ecall
	mv s6,a0
	
	li a7,64	   	# Syscall para escrever no ficheiro
	mv a0,s6
	la a1,rgbBuffer	   	# Buffer para escrever
	li a2,172800	   	# Tamanho do buffer
	ecall
	
	li a7,57	   	# Syscall para fechar o ficheiro
	mv a0,s6
	ecall
	
	jalr zero, 0(ra)
	

##################################################################################
# Função: hue
# Descrição: Recebe como argumento os valores R,G,B e calcula o seu grau.
# Argumentos:
# a0 - R
# a1 - G
# a2 - B
# Retorna:
# a0 - Grau
##################################################################################

hue:
	
	li t0,60
	li t1,0
	li t2,0
	li t3,100
	
case1:
	# R > G >= B			
	bge a1, a0, case2
	blt a1, a2, case2
   	
   	sub t1,a1,a2 	
   	sub t2,a0,a2	
   	blt t1,t2,menor0
   	div t2,t1,t2	
   	mul t2,t2,t0	
   	jal zero, final
   	
   	menor0:
   	mul t1,t1,t3
   	mul t1,t1,t0
   	div t2,t1,t2
   	div t2,t2,t3
   	jal zero, final
   	
   	
case2:
	# G >= R > B
	bge a2,a0,case3
	
	sub t1,a0,a2
	sub t2,a1,a2
	blt t1,t2,menor1
	div t2,t1,t2
	mul t2,t2,t0
	slli t0,t0,1
	sub t2,t0,t2
	jal zero, final

	menor1:
   	mul t1,t1,t3
   	mul t1,t1,t0
   	div t2,t1,t2
   	div t2,t2,t3
   	slli t0,t0,1
   	sub t2,t0,t2
   	jal zero, final
	
	
case3:
	# G > B >= R
	bge a2, a1, case4
	blt a2, a0, case4

	sub t1,a2,a0
	sub t2,a1,a0
	blt t1,t2,menor2
	div t2,t1,t2
	mul t2,t2,t0
	slli t0,t0,1
	add t2,t0,t2
	
	jal zero, final
	
	menor2:
   	mul t1,t1,t3
   	mul t1,t1,t0
   	div t2,t1,t2
   	div t2,t2,t3
   	slli t0,t0,1
   	add t2,t0,t2
   	jal zero, final


case4:
	# B >= G > R
	blt a2,a1,case5
	bge a0,a1,case5

	sub t1,a1,a0
	sub t2,a2,a0
	blt t1,t2,menor3
	div t2,t1,t2
	mul t2,t2,t0
	slli t0,t0,4
	sub t2,t0,t2
	
	jal zero, final
	
	menor3:
   	mul t1,t1,t3
   	mul t1,t1,t0
   	div t2,t1,t2
   	div t2,t2,t3
   	slli t0,t0,4
   	sub t2,t0,t2
   	jal zero, final

	
case5:
	# B > R >= G
	bge a3,a2,case6
	blt a3,a1,case6

	sub t1,a0,a1
	sub t2,a2,a1
	blt t1,t2,menor4
	div t2,t1,t2
	mul t2,t0,t2
	slli t0,t0,2
	add t2,t0,t2
	
	jal zero, final
	
	menor4:
   	mul t1,t1,t3
   	mul t1,t1,t0
   	div t2,t1,t2
   	div t2,t2,t3
   	slli t0,t0,2
   	add t2,t0,t2
   	jal zero, final
	
case6:
	# R >= B > G
	blt a0,a2,case7
	bge a1,a2,case7
	
	sub t1,a2,a1
	sub t2,a3,a4
	blt t1,t2,menor5
	div t2,t1,t2
	mul t2,t0,t2
	slli t0,t0,3
	sub t2,t0,t2
	
	jal zero, final
	
	menor5:
   	mul t1,t1,t3
   	mul t1,t1,t0
   	div t2,t1,t2
   	div t2,t2,t3
   	slli t0,t0,3
   	sub t2,t0,t2
   	jal zero, final

case7:
	# R = G = B
	li a0,0
	jal zero, final
	

final:
	mv a0,t2
	jalr zero, 0(ra)
	
		
				
##################################################################################
# Função: indicator
# Descrição: Indica qual é o personagem consoante o grau recebido como argumento.
# Argumentos:
# a0 - Grau dos valores RGB
# Retorna:
# a0 - Numero do personagem
##################################################################################

indicator:

yoda:
	li t0,40
	li t1,81
	blt a0,t0,darth_maul
	bge a0,t1,darth_maul
	li a0,1
	jalr zero, 0(ra)
 
	
darth_maul:
	li t0,1
	li t1,16
	blt a0,t0,mandalorian
	bge a0,t1,mandalorian
	li a0,2
	jalr zero, 0(ra)
	
mandalorian:
	li t0,160
	li t1,181
	blt a0,t0,nenhum
	bge a0,t1,nenhum
	li a0,3
	jalr zero, 0(ra)
	
nenhum:
	li a0,0
	jalr zero, 0(ra)
	
##################################################################################
# Função: location
# Descrição: Percorre o buffer, recolhe os valores R,G,B, chama outras duas funções
# para calcular o grau e verificar a que personagem pertence, quando acaba chama a 
# função desenhar para desenhar a cruz no centro do personagem pedido.
# Argumentos:
# a0 - Endereço do buffer
# a1 - Tamanho do buffer
# a2 - Personagem pedido
# Retorna
# a0 - cx
# a1 - cy
# a2 - Endereço do buffer
##################################################################################


location:
	addi sp,sp,-36
	sw ra,0(sp)
	sw s1,4(sp)
	sw s2,8(sp)
	sw s3,12(sp)
	sw s4,16(sp)
	sw s5,20(sp)
	sw s6,24(sp)
	sw s7,28(sp)
	sw s8,32(sp)
    
	li s7,0			# cx
	li s8,0			# cy
	li s1,0			# Contador para o número de pixeis do personagem
	li s2,0			# Inicializar contador para controlar o deslocamento no buffer
	mv s3,a0		# Buffer
	mv s4,a1		# Tamanho do buffer
	mv s5,a2		# Personagem (1,2,3)
	li s6,0			# Iterador
	mv a3,a0		# Buffer
    
loop:
	bge s6, s4, desenhar	# Se o contador alcançou ou ultrapassou o tamanho do buffer, alterar os bytes das coordenadas do cm.


        lbu a0, 0(s3)		# Carregar o valor R do pixel
        lbu a1, 1(s3)		# Carregar o valor G do pixel
        lbu a2, 2(s3)		# Carregar o valor B do pixel
        
	jal ra, hue
	jal ra, indicator 
	
	bne a0,s5,skip		# Se não for o personagem que procuramos, dar skip
	addi s1,s1,1	 	# Se for, adicionar 1 ao número de píxeis.
	li t0,320 	 	# Largura da imagem
	div t1,s6,t0     	# Coordenada x
	rem t2,s6,t0	 	# Coordenada y
	add s7,s7,t1	 	# cx
	add s8,s8,t2	 	# cy
	
																			
	skip:
        addi s3, s3, 3  	# Avançar no buffer
        addi s6, s6, 1  	# Incrementar iterador

        jal zero, loop
            	
      	
##################################################################################
# Função: desenhar
# Descrição: Percorre o buffer, verifica se a linha e a coluna atual corresponde
# a cy ou cx, se corresponder, altera o byte do green para 255 e chama a função
# para escrever no ficheiro.
# Argumentos:
# a0 - cx
# a1 - cy
# a2 - Endereço do buffer
##################################################################################
desenhar:
    	div s7,s7,s1		# Média aritmética cy
    	div s8,s8,s1		# Média aritmética cx
    	mv a0,s7  		# cx
    	mv a1,s8  		# cy
    	mv a2,a3  		# Endereço original do buffer
    	
    	li t0,0
    	li t4,255
    	li t1,320
    	
    loop2:
    	bge t0,s4,acabou	# Verifica se chegou ao final do buffer
    	
    	div t3,t0,t1 		#coordenada x
    	rem t2,t0,t1 		#coordenada y
    	
    	bne t2,a1,verifica	# Verifica se nessa coordenada o y corresponde
    	sb t4,1(a2)		# Se sim, altera o byte do G
    	
    verifica:
    	bne t3,a0,skip2		# Verifica se nessa coordenada o x corresponde
    	sb t4,1(a2)		# Se sim, altera o byte do G
    	
    skip2:
    	
    	addi t0,t0,1		# Incrementar iterador
    	addi a2,a2,3		# Avançar no buffer
    	
    	jal zero, loop2
	
    acabou:
	lw ra,0(sp)
	lw s1,4(sp)
	lw s2,8(sp)
    	lw s3,12(sp)
    	lw s4,16(sp)
    	lw s5,20(sp)
    	lw s6,24(sp)
    	lw s7,28(sp)
    	lw s8,32(sp)
      	addi sp,sp,36

	jal ra,  write_rgb_image
	
