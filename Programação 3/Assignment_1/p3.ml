type piece = I | S | O | T

let n = 4  (* Altura do tabuleiro *)
let m = 4  (* Largura do tabuleiro *)

(* Função para imprimir o tabuleiro *)
let print_board board =
  for i = 0 to n - 1 do
    for j = 0 to m - 1 do
      print_char board.(i).(j);
      print_string " ";
    done;
    print_newline ();
  done;
  print_newline ()

(* Função principal que verifica se uma sequência de jogadas cabe no tabuleiro *)
let paktris moves =
  let board = Array.make_matrix n m '-' in (* Inicializa o tabuleiro com espaços vazios *)

  let fits_piece_overlap board piece nrot ndir i j =
    let rot = nrot mod 4 in
    match piece, rot with
    | I, (0 | 2) ->
      let rec for_loop_decrement i =
        if i < n && i >= 0 && j >= 0 && j + 3 < m then
          if board.(i).(j) = '-' && board.(i).(j + 1) = '-' && board.(i).(j + 2) = '-' && board.(i).(j + 3) = '-' then begin
            board.(i).(j) <- '*';
            board.(i).(j + 1) <- '*';
            board.(i).(j + 2) <- '*';
            board.(i).(j + 3) <- '*';
            true
          end else
            for_loop_decrement (i - 1)
        else
          false
      in
      for_loop_decrement (i)
    | I, (1 | 3) ->
      let rec for_loop_decrement i =
        if i < n && i - 3 >= 0  && j >= 0 && j < m then
          if board.(i).(j) = '-' && board.(i - 1).(j) = '-' && board.(i - 2).(j) = '-' && board.(i - 3).(j) = '-' then begin
            board.(i).(j) <- '*';
            board.(i - 1).(j) <- '*';
            board.(i - 2).(j) <- '*';
            board.(i - 3).(j) <- '*';
            true
          end else
            for_loop_decrement (i - 1)
        else
          false
      in
      for_loop_decrement (i)
    | S, (0 | 2) ->
      let rec for_loop_decrement i =
        if i < n && i - 1 >= 0 && j >= 0 && j + 2 < m then
          if board.(i).(j) = '-' && board.(i).(j + 1) = '-' && board.(i - 1).(j + 1) = '-' && board.(i - 1).(j + 2) = '-' then begin
            board.(i).(j) <- '*';
            board.(i).(j + 1) <- '*';
            board.(i - 1).(j + 1) <- '*';
            board.(i - 1).(j + 2) <- '*';
            true
          end else
            for_loop_decrement (i - 1)
        else
          false
      in
      for_loop_decrement (i)
    | S, (1 | 3) ->
      let rec for_loop_decrement i =
        if i < n && i - 2 >= 0 && j - 1 >= 0 && j < m then
          if board.(i).(j) = '-' && board.(i - 1).(j) = '-' && board.(i - 1).(j - 1) = '-' && board.(i - 2).(j - 1) = '-' then begin
            board.(i).(j) <- '*';
            board.(i - 1).(j) <- '*';
            board.(i - 1).(j - 1) <- '*';
            board.(i - 2).(j - 1) <- '*';
            true
          end else
            for_loop_decrement (i - 1)
        else
          false
      in
      for_loop_decrement (i)
    | O, _ ->
      let rec for_loop_decrement i =
        if i < n && i - 1 >= 0 && j >= 0 && j + 1 < m then
          if board.(i).(j) = '-' && board.(i - 1).(j) = '-' && board.(i - 1).(j + 1) = '-' && board.(i).(j + 1) = '-' then begin
            board.(i).(j) <- '*';
            board.(i - 1).(j) <- '*';
            board.(i - 1).(j + 1) <- '*';
            board.(i).(j + 1) <- '*';
            true
          end else
            for_loop_decrement (i - 1)
        else
          false
      in
      for_loop_decrement (i)
    | T, 0 ->
      let rec for_loop_decrement i =
        if i < n && i - 1 >= 0 && j - 1 >= 0 && j + 1 < m then
          if board.(i).(j) = '-' && board.(i - 1).(j - 1) = '-' && board.(i - 1).(j) = '-' && board.(i - 1).(j + 1) = '-' then begin
            board.(i).(j) <- '*';
            board.(i - 1).(j - 1) <- '*';
            board.(i - 1).(j) <- '*';
            board.(i - 1).(j + 1) <- '*';
            true
          end else
            for_loop_decrement (i - 1)
        else
          false
      in
      for_loop_decrement (i)
    | T, 1 ->
      let rec for_loop_decrement i =
        if i < n && i - 2 >= 0 && j - 1 >= 0 && j + 1 < m then
          if board.(i).(j) = '-' && board.(i - 1).(j) = '-' && board.(i - 2).(j) = '-' && board.(i - 1).(j - 1) = '-' then begin
            board.(i).(j) <- '*';
            board.(i - 1).(j) <- '*';
            board.(i - 2).(j) <- '*';
            board.(i - 1).(j - 1) <- '*';
            true
          end else
            for_loop_decrement (i - 1)
        else
          false
      in
      for_loop_decrement (i)
    | T, 2 ->
      let rec for_loop_decrement i =
        if i < n && i - 1 >= 0 && j - 1 >= 0 && j + 1 < m then
          if board.(i).(j) = '-' && board.(i).(j - 1) = '-' && board.(i - 1).(j) = '-' && board.(i).(j + 1) = '-' then begin
            board.(i).(j) <- '*';
            board.(i).(j - 1) <- '*';
            board.(i - 1).(j) <- '*';
            board.(i).(j + 1) <- '*';
            true
          end else
            for_loop_decrement (i - 1)
        else
          false
      in
      for_loop_decrement (i)
    | T, 3 ->
      let rec for_loop_decrement i =
        if i < n && i - 2 >= 0 && j >= 0 && j + 1 < m then
          if board.(i).(j) = '-' && board.(i - 1).(j) = '-' && board.(i - 1).(j + 1) = '-' && board.(i - 2).(j) = '-' then begin
            board.(i).(j) <- '*';
            board.(i - 1).(j) <- '*';
            board.(i - 1).(j + 1) <- '*';
            board.(i - 2).(j) <- '*';
            true
          end else
            for_loop_decrement (i - 1)
        else
          false
      in
      for_loop_decrement (i)
    | _ -> false
  in
  
  (* Função principal para processar as jogadas *)
  let rec process_moves moves =
    match moves with
    | [] -> true
    | (piece, nrot, ndir) :: rest ->
      if fits_piece_overlap board piece nrot ndir 3 ndir then begin
        print_board board;  (* Exibe o tabuleiro após cada jogada *)
        process_moves rest
      end else
        false
  in
  
  process_moves moves

(* Exemplos de uso *)

let () =
  print_endline (string_of_bool (paktris [(I, 6, 0); (I, 0, 0); (O, 0, 2); (O, 0, 0)]));
  print_newline ();
  print_endline (string_of_bool (paktris [(I, 1, 0); (S, 1, 1); (O, 0, 1)]));