package es.softtek.jwtDemo.service;

import es.softtek.jwtDemo.config.PostgresConnector;
import es.softtek.jwtDemo.dto.Artista;
import es.softtek.jwtDemo.dto.Atuacoes;
import org.postgis.PGgeometry;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class DBService {

    PostgresConnector setDbProperties() throws Exception {
        String host, db, user, pw;
        try (InputStream input = Files.newInputStream(Paths.get("src/main/resources/application.properties"))) {
            Properties prop = new Properties();
            prop.load(input);
            host = prop.getProperty("host");
            db = prop.getProperty("db");
            user = prop.getProperty("user");
            pw = prop.getProperty("password");
        }
        return new PostgresConnector(host, db, user, pw);

    }

    // Método para adicionar artista
    public String adicionarArtista(Artista a) {
        String localizacao = a.getLocalizacao();
        if (localizacao.equals(" ")) localizacao = null;
        int num_avaliacoes = 0;
        double rating = 0.0;
        String result = "Artista adicionado com sucesso.";
        String query_sql = "INSERT INTO artista VALUES (DEFAULT, ?, ?, ?, null, 'Novo artista.', ?, ?, ?)";

        try {
            PostgresConnector pc = setDbProperties();
            pc.connect();
            PreparedStatement statement = pc.prepareStatement(query_sql);


            statement.setString(1, a.getNome());
            statement.setString(2, a.getTipoArte());
            statement.setBoolean(3, false);
            statement.setString(4, localizacao);
            statement.setInt(5, num_avaliacoes);
            statement.setDouble(6, rating);
            statement.executeUpdate();

            pc.disconnect();
        } catch (Exception e) {
            result = "Artista não foi adicionado.";
            e.printStackTrace();
        }

        return result;
    }


    // Método para adicionar atuação
    public String adicionarAtuacao(int id, float latitude, float longitude, LocalDate data_atuacao) {
        String result = "Atuação adicionada com sucesso.";
        String query_sql1 = "INSERT INTO atuacoes VALUES (DEFAULT, ?, false, ?, ST_Point(?, ?))";

        try {
            PostgresConnector pc = setDbProperties();
            pc.connect();
            PreparedStatement statement = pc.prepareStatement(query_sql1);

            statement.setInt(1, id);
            statement.setTimestamp(2, Timestamp.valueOf(data_atuacao.atStartOfDay()));
            statement.setFloat(3, longitude);
            statement.setFloat(4, latitude);

            statement.executeUpdate();

            pc.disconnect();

        } catch (Exception e) {
            result = "Atuação não foi adicionada.";
            e.printStackTrace();
        }

        return result;
    }

    // Método para procurar artistas por estado
    public List<Artista> procurarArtistasPorEstado(boolean estado) throws Exception {
        List<Artista> artistasEncontrados = new ArrayList<>();
        String query_sql = "SELECT * FROM artista WHERE estado = ?";

        try {
            PostgresConnector pc = setDbProperties();
            pc.connect();
            PreparedStatement statement = pc.prepareStatement(query_sql);

            statement.setBoolean(1, estado);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Artista artista = new Artista(
                        resultSet.getInt("id_artista"),
                        resultSet.getString("nome"),
                        resultSet.getString("tipo_arte"),
                        resultSet.getBoolean("estado"),
                        resultSet.getString("descricao"),
                        resultSet.getString("imagem"),
                        resultSet.getFloat("rating")
                );
                artistasEncontrados.add(artista);
            }
            resultSet.close();
            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return artistasEncontrados;
    }


    // Método para listar artistar por local e arte
    public List<Artista> procurarArtistasLocalArte(String localizacao, String tipoArte) throws Exception {
        List<Artista> artistasEncontrados = new ArrayList<>();
        String query_sql = "SELECT id_artista, nome, tipo_arte, estado, imagem, descricao, localizacao, num_avaliacoes, rating FROM artista WHERE localizacao = ? AND tipo_arte = ?";


        try {
            PostgresConnector pc = setDbProperties();
            pc.connect();
            PreparedStatement statement = pc.prepareStatement(query_sql);

            statement.setString(1, localizacao);
            statement.setString(2, tipoArte);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Artista artista = new Artista(
                        resultSet.getInt("id_artista"),
                        resultSet.getString("nome"),
                        resultSet.getString("tipo_arte"),
                        resultSet.getBoolean("estado"),
                        resultSet.getString("descricao"),
                        resultSet.getString("imagem"),
                        resultSet.getFloat("rating")
                );
                artistasEncontrados.add(artista);
            }
            resultSet.close();
            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return artistasEncontrados;
    }

    // Método para listar todos os artistas
    public List<Artista> obterArtistas() throws Exception {
        List<Artista> artistasEncontrados = new ArrayList<>();
        String query_sql = "SELECT * FROM artista";

        try {
            PostgresConnector pc = setDbProperties();
            pc.connect();
            PreparedStatement statement = pc.prepareStatement(query_sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Artista artista = new Artista(
                        resultSet.getInt("id_artista"),
                        resultSet.getString("nome"),
                        resultSet.getString("tipo_arte"),
                        resultSet.getBoolean("estado"),
                        resultSet.getString("descricao"),
                        resultSet.getString("imagem"),
                        resultSet.getFloat("rating")
                );
                artistasEncontrados.add(artista);
            }
            resultSet.close();
            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return artistasEncontrados;
    }

    // Método para obter localizações com artistas a atuar
    public List<String> obterLocalizacoesAtuar() throws Exception {
        List<String> localizacoes = new ArrayList<>();
        String query_sql = "SELECT DISTINCT a.coordenadas FROM atuacoes a JOIN artista ar ON a.id_artista = ar.id_artista WHERE a.atuar = true;";


        try {
            PostgresConnector pc = setDbProperties();
            pc.connect();
            PreparedStatement statement = pc.prepareStatement(query_sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                PGgeometry pgGeometry = (PGgeometry) resultSet.getObject("coordenadas");

                if (pgGeometry != null) {
                    String coordenadas = pgGeometry.toString();

                    localizacoes.add("Coordenadas: " + coordenadas.substring(15));
                }
            }

            resultSet.close();
            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return localizacoes;
    }

    // Método para aprovar artistas por ID
    public String aprovarArtistaPorId(int id) throws Exception {
        String query_sql = "SELECT estado FROM artista WHERE id_artista = ?";
        String query_sql1 = "UPDATE artista SET estado = true WHERE id_artista = ?";

        String result = "";

        try {
            PostgresConnector pc = setDbProperties();
            pc.connect();
            PreparedStatement statement = pc.prepareStatement(query_sql);
            statement.setInt(1, id);

            ResultSet resultSet1 = statement.executeQuery();

            if (resultSet1.next()) {
                boolean estado = resultSet1.getBoolean("estado");

                if (!estado) {
                    PreparedStatement statement2 = pc.prepareStatement(query_sql1);
                    statement2.setInt(1, id);

                    statement2.executeUpdate();

                    result = "Artista aprovado com sucesso!";
                } else {
                    result = "Artista já está aprovado.";
                }
            } else {
                result = "Artista não foi encontrado";
            }
            pc.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    // Método para obter atuações de um artista
    public List<String> obterAtuacoesArtista(int id) throws Exception {
        List<String> atuacoesArtista = new ArrayList<>();
        String query_sql = "SELECT data_atuacao, coordenadas FROM atuacoes WHERE id_artista = ?";

        try {
            PostgresConnector pc = setDbProperties();
            pc.connect();
            PreparedStatement statement = pc.prepareStatement(query_sql);

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                LocalDate data = resultSet.getDate("data_atuacao").toLocalDate();
                PGgeometry pgGeometry = (PGgeometry) resultSet.getObject("coordenadas");

                if (pgGeometry != null) {
                    String coordenadas = pgGeometry.toString();

                    atuacoesArtista.add("Data: " + data + ", Coordenadas: " + coordenadas.substring(15));
                }
            }

            resultSet.close();
            pc.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return atuacoesArtista;
    }


    // Método para verificar se já existe um artista com esse nome e tipo de arte
    public boolean existeArtista(String nome, String tipo_arte) throws Exception {
        String query_sql = "SELECT COUNT(*) AS count FROM artista WHERE nome = ? AND tipo_arte = ?";

        boolean res = false;

        try {
            PostgresConnector pc = setDbProperties();
            pc.connect();
            PreparedStatement statement = pc.prepareStatement(query_sql);

            statement.setString(1, nome);
            statement.setString(2, tipo_arte);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                res = count > 0; // Se count for maior que 0, o artista existe; caso contrário, não existe.
            }

            resultSet.close();
            pc.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }


    public boolean existeIDArtista(int id) throws Exception {
        String query_sql = "SELECT COUNT(*) AS count FROM artista WHERE id_artista = ?";

        boolean res = false;

        try {
            PostgresConnector pc = setDbProperties();
            pc.connect();
            PreparedStatement statement = pc.prepareStatement(query_sql);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                res = count > 0; // Se count for maior que 0, o artista existe; caso contrário, não existe.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }


    // Método para adicionar donativo
    public String adicionarDonativo(int id_artista, float valor, String username) throws Exception {
        String result = "Donativo adicionado com sucesso.";
        String query_sql = "SELECT id_artista FROM artista WHERE id_artista = ? AND estado = true";
        String query_sql1 = "INSERT INTO donativos (id_artista, username, valor) VALUES (?, ?, ?)";

        try {
            PostgresConnector pc = setDbProperties();
            pc.connect();

            PreparedStatement statement = pc.prepareStatement(query_sql);
            statement.setInt(1, id_artista);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                PreparedStatement statement2 = pc.prepareStatement(query_sql1);
                statement2.setInt(1, id_artista);
                statement2.setString(2, username);
                statement2.setFloat(3, valor);

                statement2.executeUpdate();
            } else {
                result = "Donativo não foi adicionado. Artista não está aprovado!";
            }
            pc.disconnect();
        } catch (Exception e) {
            result = "Donativo não foi adicionado.";
            e.printStackTrace();
        }

        return result;
    }

    // Método para obter donativos de certo artista
    public List<String> obterDonativosArtista(int id_artista) throws Exception {
        List<String> donativos = new ArrayList<>();
        float total = 0;
        String query_sql = "SELECT valor, username FROM donativos WHERE id_artista = ?";

        try {
            PostgresConnector pc = setDbProperties();
            pc.connect();

            PreparedStatement statement = pc.prepareStatement(query_sql);
            statement.setInt(1, id_artista);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                float valor = resultSet.getFloat("valor");
                total += valor;
                String username = resultSet.getString("username");
                donativos.add("User: " + username + ", Valor: " + valor + "€");
            }

            donativos.add("Total: " + total + "€");
            resultSet.close();

            pc.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return donativos;
    }


    // Método para alterar a descrição e a foto de um artista
    public String alterarDescFoto(int id, String descricao, String foto) throws Exception {
        String result = "Alteração realizada com sucesso.";
        String query_sql = "UPDATE artista SET descricao = ?, imagem = ? WHERE id_artista = ?";

        try {
            PostgresConnector pc = setDbProperties();
            pc.connect();

            try (PreparedStatement statement = pc.prepareStatement(query_sql)) {
                statement.setString(1, descricao);
                statement.setString(2, foto);
                statement.setInt(3, id);

                statement.executeUpdate();
            }

            pc.disconnect();
        } catch (Exception e) {
            result = "Alteração não foi realizada.";
            e.printStackTrace();
        }

        return result;
    }


    // Método para consultar a descrição e foto de um artista
    public String consultDescFoto(int id) {
        String query_sql = "SELECT descricao, imagem FROM artista WHERE id_artista = ?";
        String result = "";

        try {
            PostgresConnector pc = setDbProperties();
            pc.connect();

            PreparedStatement statement = pc.prepareStatement(query_sql);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String descricao = resultSet.getString("descricao");
                String imagem = resultSet.getString("imagem");

                result = "Descrição: " + descricao + ", Imagem: " + imagem;
            }
            resultSet.close();
            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    // Método para registar novo utilizador
    public boolean registarUtilizador(String username, String password, String email) throws Exception {
        String query_sql = "INSERT INTO users (username, password, email, user_role) VALUES ('" + username + "', '" + password + "', '" + email + "', '" + "NORMAL" + "')";

        try {
            PostgresConnector pc = setDbProperties();
            pc.connect();
            PreparedStatement statement = pc.prepareStatement(query_sql);
            statement.execute();
            pc.disconnect();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // Método para verficiar login
    public boolean existeUtilizador(String username, String password) throws Exception {
        String query_sql = "SELECT COUNT(*) AS count FROM users WHERE username = ? AND password = ?";
        boolean res = false;

        try {

            PostgresConnector pc = setDbProperties();
            pc.connect();
            PreparedStatement statement = pc.prepareStatement(query_sql);

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                res = count > 0; // Se count for maior que 0, o utilizador existe; caso contrário, não existe.
            }
            pc.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
            return res;

        }
        return res;
    }

    // Método para verificar se já existe username ou email
    public boolean verificaRegisto(String username, String email) throws Exception {
        String query_sql = "SELECT COUNT(*) AS count FROM users WHERE username = '" + username + "' OR email = '" + email + "'";

        boolean res = false;

        try {
            PostgresConnector pc = setDbProperties();
            pc.connect();
            PreparedStatement statement = pc.prepareStatement(query_sql);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                res = count > 0;
            }
            pc.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error executing SQL query", e);
        }
        return res;
    }

    // Método para verificar se o utilizador tem a role de "ADMINISTRADOR"
    public String isAdm(String username) throws Exception {
        String query_sql = "SELECT COUNT(*) AS count FROM users WHERE username = '" + username + "' AND user_role = 'ADMINISTRADOR'";

        String res = "n";


        try {
            PostgresConnector pc = setDbProperties();
            pc.connect();
            PreparedStatement statement = pc.prepareStatement(query_sql);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                if(count > 0){
                    res = "y";
                }
            }
            pc.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error executing SQL query", e);
        }

        return res;
    }

    // Método para atualizar o tipo de utilizador
    public String atualizarTipoUtilizador(String username) throws Exception {
        String query_sql = "SELECT user_role FROM users WHERE username = '" + username + "'";
        String query_sql1 = "UPDATE users SET user_role = 'ADMINISTRADOR' WHERE username = '" + username + "'";

        String result = "Ocorreu um erro";


        try {
            PostgresConnector pc = setDbProperties();
            pc.connect();
            PreparedStatement statement = pc.prepareStatement(query_sql);
            ResultSet resultSet1 = statement.executeQuery();
            if (resultSet1.next()) {
                String userType = resultSet1.getString("user_role");
                if ("ADMINISTRADOR".equals(userType)) {
                    result = "Utilizador já é um administrador!";
                } else {
                    PreparedStatement statement1 = pc.prepareStatement(query_sql1);
                    statement1.executeUpdate();
                    result = "Atualização feita com sucesso!";
                }
            } else {
                result = "Não foi encontrado nenhum utilizador com esse nome.";
            }
            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    // Método para ver se o artista tem localização
    public boolean artistaTemLocalizacao(String name) throws Exception {
        String query_sql = "SELECT COUNT(*) AS count FROM artista WHERE nome = ? AND localizacao IS NOT NULL";

        boolean res = false;

        try {
            PostgresConnector pc = setDbProperties();
            pc.connect();
            PreparedStatement statement = pc.prepareStatement(query_sql);
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                res = count > 0;
            }

            resultSet.close();
            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    // Método para atualizar a localização do artista
    public String atualizarLocalizacaoArtista(String nome, String novaLocalizacao) {
        String query_sql = "UPDATE artista SET localizacao = ? WHERE nome = ?";
        String mensagem = "Localização atualizada com sucesso";

        try {
            PostgresConnector pc = setDbProperties();
            pc.connect();
            PreparedStatement statement = pc.prepareStatement(query_sql);
            statement.setString(1, novaLocalizacao);
            statement.setString(2, nome);

            statement.executeUpdate();

            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            mensagem = "Erro ao atualizar a localização";
        }

        return mensagem;
    }


    // Método para atualizar a data das atuações
    public void verificarDataAtuacoes() throws Exception {
        String query_sql = "SELECT * FROM atuacoes";

        List<Atuacoes> atuacoesList = new ArrayList<>();


        PostgresConnector pc = setDbProperties();
        pc.connect();
        PreparedStatement statement = pc.prepareStatement(query_sql);

        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int idAtuacao = resultSet.getInt("id_atuacao");
                LocalDate dataStr = LocalDate.parse(resultSet.getString("data_atuacao"));
                boolean atuar = resultSet.getBoolean("atuar");
                Atuacoes atuacao = new Atuacoes(idAtuacao, dataStr, atuar);
                atuacoesList.add(atuacao);
            }
            pc.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Atuacoes atuacao : atuacoesList) {
            boolean atuar = atuacao.getData().equals(LocalDate.now());
            String updateQuery = "UPDATE atuacoes SET atuar = " + atuar + " WHERE id_atuacao = " + atuacao.getId();
            try {
                pc.connect();
                PreparedStatement statement2 = pc.prepareStatement(updateQuery);
                statement2.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                pc.disconnect();
            }
        }
    }


    // Método para escolher se queremos ver as atualizações passadas ou futuras
    public List<String> obterAtuacoesTempo(int id_artista, int escolha) {
        List<String> atuacoesArtista = new ArrayList<>();
        String query_sql;

        if (escolha == 1) {
            query_sql = "SELECT data_atuacao, coordenadas FROM atuacoes WHERE id_artista = ? AND data_atuacao < CURRENT_DATE";
        } else {
            query_sql = "SELECT data_atuacao, coordenadas FROM atuacoes WHERE id_artista = ? AND data_atuacao > CURRENT_DATE ORDER BY data_atuacao ASC LIMIT 1";
        }

        try {
            PostgresConnector pc = setDbProperties();
            pc.connect();
            PreparedStatement statement = pc.prepareStatement(query_sql);
            statement.setInt(1, id_artista);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                LocalDate data = resultSet.getDate("data_atuacao").toLocalDate();
                PGgeometry pgGeometry = (PGgeometry) resultSet.getObject("coordenadas");

                if (pgGeometry != null) {
                    String coordenadas = pgGeometry.toString();
                    atuacoesArtista.add("Data: " + data + ", Coordenadas: " + coordenadas.substring(15));
                }
            }

            resultSet.close();
            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return atuacoesArtista;
    }

    // Método para adicionar rating
    public String adicionarRating(int id, float rating) {
        String query_sql1 = "SELECT id_artista FROM artista WHERE id_artista = ? AND estado = true";
        String query_sql2 = "SELECT rating, num_avaliacoes FROM artista WHERE id_artista = ?";
        String query_sql3 = "UPDATE artista SET rating = ?, num_avaliacoes = ? WHERE id_artista = ?";

        String result = "Rating adicionado com sucesso.";

        try {
            PostgresConnector pc = setDbProperties();
            pc.connect();

            PreparedStatement statement1 = pc.prepareStatement(query_sql1);
            statement1.setInt(1, id);
            ResultSet resultSet1 = statement1.executeQuery();

            if (resultSet1.next()) {
                // Obter a classificação e o número de avaliações atuais
                PreparedStatement statement2 = pc.prepareStatement(query_sql2);
                statement2.setInt(1, id);
                ResultSet resultSet2 = statement2.executeQuery();

                if (resultSet2.next()) {
                    float ratingAtual = resultSet2.getFloat("rating");
                    int numAvaliacoesAtual = resultSet2.getInt("num_avaliacoes");

                    // Calcular a nova média
                    float novaMedia = ((ratingAtual * numAvaliacoesAtual) + rating) / (numAvaliacoesAtual + 1);

                    // Atualizar o registo com a nova média e o número total de avaliações
                    PreparedStatement statement3 = pc.prepareStatement(query_sql3);
                    statement3.setFloat(1, novaMedia);
                    statement3.setInt(2, numAvaliacoesAtual + 1);
                    statement3.setInt(3, id);
                    statement3.executeUpdate();
                }
            } else {
                result = "Artista não aprovado.";
            }

            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


}