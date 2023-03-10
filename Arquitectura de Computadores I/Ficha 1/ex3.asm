  slli t1, t0, 24
  srli t2, t0, 8
  slli t2, t2, 24
  srli t2, t2, 16
  slli t3, t0, 8
  srli t3, t3, 16
  or t0, t1, t2
  or t0, t0, t3
