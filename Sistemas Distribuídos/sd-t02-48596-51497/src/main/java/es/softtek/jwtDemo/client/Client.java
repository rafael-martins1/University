package es.softtek.jwtDemo.client;


import es.softtek.jwtDemo.dto.Artista;
import es.softtek.jwtDemo.service.ClientService;
import es.softtek.jwtDemo.service.DBService;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {

        Scanner s=new Scanner(System.in);
        System.out.println("BEM VINDO!");
        System.out.println("Antes de aceder à aplicação, terá de se Autenticar.");

        boolean autorizado = false;
        String login =
                "+=======================+\n" +
                        "|     Autenticação      |\n" +
                        "|-----------------------|\n" +
                        "| 1 - Entrar            |\n" +
                        "| 2 - Registar          |\n" +
                        "| 3 - Sair              |\n" +
                        "+=======================+\n" +
                        "Opção: ";

        String menuCliente = "\n+==================================================+\n" +
                "|                  Menu Cliente                    |\n" +
                "|--------------------------------------------------|\n" +
                "| 1 - Registo de um novo artista.                  |\n" +
                "| 2 - Registo de uma nova atuação.                 |\n" +
                "| 3 - Localizações onde existem artistas a atuar.  |\n" +
                "| 4 - Listar artistas.                             |\n" +
                "| 5 - Listar artistas, com filtros.                |\n" +
                "| 6 - Listar todas as atuações do artista.         |\n" +
                "| 7 - Registo de um donativo.                      |\n" +
                "| 8 - Donativos de cada artista.                   |\n" +
                "| 9 - Listar atuações passadas.                    |\n" +
                "| 10 - Listar próximas atuações.                   |\n" +
                "| 11 - Adicionar rating a um artista.              |\n" +
                "| s - Sair.                                        |\n" +
                "+==================================================+\n "+
                "Opção: ";


        String menuAdm = "\n+==============================================================+\n" +
                "|                     Menu Administrador                       |\n" +
                "|--------------------------------------------------------------|\n" +
                "| 1 - Registo de um novo artista.                              |\n" +
                "| 2 - Registo de uma nova atuação.                             |\n" +
                "| 3 - Localizações onde existem artistas a atuar.              |\n" +
                "| 4 - Listar artistas.                                         |\n" +
                "| 5 - Listar artistas, com filtros.                            |\n" +
                "| 6 - Listar todas as atuações do artista.                     |\n" +
                "| 7 - Registo de um donativo.                                  |\n" +
                "| 8 - Donativos de cada artista.                               |\n" +
                "| 9 - Listar atuações passadas.                                |\n" +
                "| 10 - Listar próximas atuações.                               |\n" +
                "| 11 - Adicionar rating a um artista.                          |\n" +
                "| 12 - Dar permissão de administrador.                         |\n" +
                "| 13 - Listar artistas por estado.                             |\n" +
                "| 14 - Aprovar um artista.                                     |\n" +
                "| 15 - Consultar e alterar a descrição e foto de um artista.   |\n" +
                "| s - Sair.                                                    |\n" +
                "+==============================================================+\n "+
                "Opção: ";

            DBService DBService = new DBService();
            String token = null;
            String []user;
            String info_user, email, password;
            String username = "";
            boolean existe_user;

            while (!autorizado) {

                System.out.print(login);
                String escolher = s.nextLine();
                System.out.println();


                switch (escolher) {
                    case "1":
                        // ‘Login’ de um utilizador
                        System.out.println("Insira username e password, separado por vírgulas:");
                        info_user = s.nextLine();
                        user = info_user.split(", ");
                        username = user[0];
                        password = user[1];
                        existe_user = DBService.existeUtilizador(username, password);

                        if (existe_user) {
                            autorizado = true;
                            token = Objects.requireNonNull(ClientService.autenticarUtilizador(username, DBService.isAdm(username),password)).getToken();
                            System.out.println("Autenticado com sucesso!");
                        } else {
                            System.out.println("\nUsername ou senha errados, tente novamente.");
                        }
                        break;

                    case "2":
                        // Registo de um utilizador
                        System.out.println("Insira username, email e password, separado por vírgulas:");
                        info_user = s.nextLine();
                        user = info_user.split(", ");
                        username = user[0];
                        email = user[1];
                        password = user[2];
                        existe_user = DBService.verificaRegisto(username, email);

                        if (!existe_user) {
                            if (DBService.registarUtilizador(username, password, email)) {
                                System.out.println("Registado com sucesso!");
                            } else {
                                System.out.println("Ocorreu um erro no registo.");
                            }
                        } else {
                            System.out.println("Username ou email já utilizados.");
                        }
                        break;

                    case "3":
                        // Termina a conexão
                        System.out.println("Conexão encerrada.");
                        return;

                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            }

            String menu;
            if(DBService.isAdm(username).equals("y")){menu = menuAdm;}
            else{menu = menuCliente;}


        while (true) {

            // Variáveis de controlo
            System.out.print(menu);
            String opcao=s.nextLine();
            System.out.println();
            String []partes;
            String info_artista, nome_artista, tipo_arte, localizacao, mensagem, data_atuacao;
            int id_artista;
            float valor_doacao;
            boolean existe;

            switch (opcao) {
                case "1":
                    // Registo de um novo artista
                    System.out.println("\nDigite as informações do artista, separadas por vírgulas (Nome, Arte, Localização):");
                    info_artista = s.nextLine();
                    partes = info_artista.split(", ");
                    nome_artista = partes[0];
                    tipo_arte = partes[1];
                    localizacao = partes[2];

                    if (ClientService.existeArtista(nome_artista, tipo_arte, token)) {

                        if (ClientService.artistaTemLocalizacao(nome_artista, token)) {
                            System.out.println("Artista já registado.");
                        } else {
                            System.out.println("Artista encontrado, digite a localização: ");
                            info_artista = s.nextLine();
                            ClientService.atualizarLocalizacaoArtista(nome_artista, info_artista, token);
                            System.out.println("Localização adicionada.");
                        }

                    } else {

                        mensagem = ClientService.postArtista(nome_artista, tipo_arte, token, localizacao);
                        System.out.println(mensagem);

                    }
                    break;

                case "2":
                    // Registo de uma nova atuação
                    System.out.println("\nDigite o id do artista, a localização da atuação e a data da atuação (AAAA-MM-DD), por vírgulas:");
                    info_artista = s.nextLine();
                    partes = info_artista.split(", ");
                    id_artista = Integer.parseInt((partes[0]));
                    float latitude = Float.parseFloat((partes[1]));
                    float longitude = Float.parseFloat((partes[2]));
                    data_atuacao = (partes[3]); // Convertendo a string da data para o tipo LocalDate
                    mensagem = ClientService.postAtuacao(id_artista, latitude, longitude, data_atuacao, token);
                    ClientService.verificarDataAtuacoes(token);
                    System.out.println(mensagem);
                    break;

                case "3":
                    // Localizações onde existem artistas a atuar
                    List<String> localizacoesAtuar = ClientService.obterLocalizacoesAtuar(token);

                    if (localizacoesAtuar.isEmpty()) {
                        System.out.println("Nenhuma localização encontrada com artistas a atuar.");
                    } else {
                        System.out.println("Localizações com artistas a atuar:");
                        for (String local : localizacoesAtuar) {
                            System.out.println(local);
                        }
                    }
                    break;

                case "4":
                    // Listar todos os artistas
                    List<Artista> artistasFiltrados = ClientService.obterArtistas(token);

                    if (artistasFiltrados.isEmpty()) {
                        System.out.println("Nenhum artista encontrado.");
                    } else {
                        artistasFiltrados.sort(Comparator.comparingInt(Artista::getId));
                        System.out.println("\nLista de artistas:");
                        for (Artista artista : artistasFiltrados) {
                            System.out.println("ID = " + artista.getId() + ", Nome = " + artista.getNome() + ", Arte = " + artista.getTipoArte() +  ", Rating= " + artista.getRating()+"✦");
                        }
                    }
                    break;

                case "5":
                    // Listar artistas com filtros
                    System.out.println("\nDigite a localização e o tipo de arte, por vírgulas:");
                    info_artista = s.nextLine();
                    partes = info_artista.split(", ");
                    localizacao = partes[0];
                    tipo_arte = partes[1];
                    List<Artista> artistasFiltrados1 = ClientService.procurarArtistasLocalArte(localizacao, tipo_arte, token);

                    if (artistasFiltrados1.isEmpty()) {
                        System.out.println("Nenhum artista encontrado com os filtros fornecidos.");
                    } else {
                        artistasFiltrados1.sort(Comparator.comparingInt(Artista::getId));
                        System.out.println("\nLista de artistas encontrados:");
                        for (Artista artista : artistasFiltrados1) {
                            System.out.println(artista);
                        }
                    }
                    break;

                case "6":
                    // Data e local das atuações do artista
                    System.out.println("\nDigite o id do artista:");
                    id_artista = Integer.parseInt(s.nextLine());
                    existe = ClientService.existeIDArtista(id_artista,token);

                    if(!existe) System.out.println("Artista não existe.");
                    else {
                        List<String> atuacoes = ClientService.obterAtuacoesArtista(id_artista,token);

                        if(atuacoes.isEmpty()) {
                            System.out.println("Artista não tem atuações registadas! ");
                        } else {
                            System.out.println("Atuações do artista: ");
                            for (String atuacao : atuacoes){
                                System.out.println(atuacao);
                            }
                        }
                    }
                    break;

                case "7":
                    // Registo de um donativo
                    System.out.println("\nDigite o id do artista e o valor da doação, separados por vírgulas:");
                    info_artista = s.nextLine();
                    partes = info_artista.split(", ");
                    id_artista = Integer.parseInt((partes[0]));
                    valor_doacao = Float.parseFloat(partes[1]);

                    if(!ClientService.existeIDArtista(id_artista,token)){
                        System.out.println("Artista não existe.");
                    } else {
                        mensagem = ClientService.adicionarDonativo(id_artista,valor_doacao,username,token);
                        System.out.println(mensagem);
                    }
                    break;

                case "8":
                    // Listar os donativos de um artista
                    System.out.println("Digite o id do artista que deseja listar os donativos:");
                    id_artista = Integer.parseInt(s.nextLine());

                    if(!ClientService.existeIDArtista(id_artista,token)){
                        System.out.println("Artista não existe.");
                    } else {
                        List<String> donativos = ClientService.obterDonativosArtista(id_artista,token);

                        if(donativos.isEmpty()){
                            System.out.println("Não existem donativos.");
                        } else {
                            System.out.println("Todos os donativos:");
                            for (String d : donativos) {
                                System.out.println(d);
                            }
                        }
                    }
                    break;

                case "9":
                    // Data e local das atuações do artista
                    System.out.println("\nDigite o id do artista:");
                    id_artista = Integer.parseInt(s.nextLine());

                    existe = ClientService.existeIDArtista(id_artista,token);

                    if(!existe) System.out.println("Artista não existe.");

                    else{
                        List<String> atuacoes = ClientService.obterAtuacoesTempo(id_artista,1,token);

                        if(atuacoes.isEmpty()) {
                            System.out.println("Artista não tem atuações passadas! ");
                        } else {
                            System.out.println("Atuações passadas do artista: ");
                            for (String atuacao : atuacoes){
                                System.out.println(atuacao);
                            }
                        }
                    }
                    break;

                case "10":
                    // Data e local das atuações do artista
                    System.out.println("\nDigite o id do artista:");
                    id_artista = Integer.parseInt(s.nextLine());
                    existe = ClientService.existeIDArtista(id_artista,token);

                    if(!existe) System.out.println("Artista não existe.");

                    else{
                        List<String> atuacoes = ClientService.obterAtuacoesTempo(id_artista,2,token);

                        if(atuacoes.isEmpty()) {
                            System.out.println("Artista não próximas atuações. ");
                        } else {
                            System.out.println("Próxima atuação do artista: ");
                            for (String atuacao : atuacoes){
                                System.out.println(atuacao);
                            }
                        }
                    }
                    break;

                case "11":
                    // Dar uma classificação a um artista
                    System.out.println("\nDigite o id do artista e o rating (0-5) , separados por vírgula:");
                    info_artista = s.nextLine();
                    partes = info_artista.split(", ");
                    id_artista = Integer.parseInt(partes[0]);
                    float rating = Float.parseFloat((partes[1]));

                    if(rating < 0 || rating > 5){
                        System.out.println("Insira um valor válido.");
                    } else {

                        existe = ClientService.existeIDArtista(id_artista,token);
                        if(!existe) System.out.println("Artista não existe.");

                        else {
                            System.out.println(ClientService.adicionarRating(id_artista, rating,token));
                        }
                    }
                    break;

                case "12":
                    // Dar permissões de administrador a um utilizador
                    System.out.println("\nIntroduza o username do Utilizador:");
                    info_artista = s. nextLine();
                    System.out.println(ClientService.atualizarTipoUtilizador(info_artista,token));
                    break;

                case "13":
                    // Ver artistas por estado
                    System.out.println("\nIntroduza o estado que deseja verificar:");
                    info_artista = s.nextLine();
                    boolean estado;
                    if("true".equals(info_artista) || "false".equals(info_artista)) {
                        estado = Boolean.parseBoolean(info_artista);
                        List<Artista> artistasFiltradosEstado = ClientService.procurarArtistasPorEstado(estado,token);
                          if(!artistasFiltradosEstado.isEmpty()){
                            artistasFiltradosEstado.sort(Comparator.comparingInt(Artista::getId));
                            System.out.println("\nArtistas com estado " + estado + ":");
                            for (Artista estado_str : artistasFiltradosEstado) {
                                System.out.println("ID = " + estado_str.getId() + ", " + estado_str);
                            }
                        }
                    } else {
                        System.out.println("\nEstado introduzido inválido!");
                    }

                    break;

                case "14":
                    // Aprovar um artista
                    System.out.println("\nIntroduza o id do artista que deseja aprovar:");
                    int id1 = Integer.parseInt(s.nextLine());
                    System.out.println(ClientService.aprovarArtistaPorId(id1,token));

                    break;

                case "15":
                    // Consultar e alterar a descrição de um artista
                    System.out.println("\nIntroduza o id do artista:");
                    int id2 = Integer.parseInt(s.nextLine());
                    String res2 = ClientService.consultDescFoto(id2,token);

                    if(Objects.equals(res2, "1")){
                        System.out.println("Artista não encontrado.");
                        break;
                    } else if (Objects.equals(res2, "2")){
                        System.out.println("Não tem permissão de administrador.");
                        break;
                    }

                    System.out.println(ClientService.consultDescFoto(id2,token));
                    System.out.println("\nAlterar descrição:");
                    String descricao = s.nextLine();
                    System.out.println("\nAlterar foto:");
                    String foto = s.nextLine();
                    System.out.printf(ClientService.alterarDescFoto(id2, descricao, foto,token));

                    break;

                case "s":
                    // Termina o loop.
                    System.out.println("Conexão encerrada.");
                    return;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}
