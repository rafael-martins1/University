  add t1,zero,zero
  addi t0,zero,1
  addi t3,zero,11
  
CICLE:
  bge t0,t3,END
  add t1,t0,t0
  addi t0,t0,1
  beq zero,zero,CICLE
  
END:
