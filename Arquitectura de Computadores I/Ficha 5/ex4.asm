Collatz:
  andi t0,a0,1
  beqz t0,PAR

  
  slli t0,a0,1
  add t0,t0,t0
  addi a0,t0,1
  jalr zero,0(ra)
  
  
PAR:
  srai a0,a0,1
  jalr zero,0(ra)
  
