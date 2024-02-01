package es.softtek.jwtDemo.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import es.softtek.jwtDemo.dto.Artista;
import es.softtek.jwtDemo.dto.User;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ClientService {

    private static final String URL = "http://localhost:8080";

    public static String postArtista(String nome, String tipo_arte, String token, String localizacao) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(URL + "/artista").openConnection();

            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Converte o objeto Artista para JSON
            String jsonParams = String.format("{\"nome\":\"%s\",\"tipo_arte\":\"%s\",\"localizacao\":\"%s\"}", nome, tipo_arte,localizacao);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonParams.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Falha ao realizar a solicitação.";
        }
    }

    public static String postAtuacao(int idArtista, float latitude, float longitude, String data, String token) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(URL + "/atuacao").openConnection();

            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Constrói a string de parâmetros manualmente
            String jsonParams = "{\"id_artista\":" + idArtista + ",\"latitude\":" + latitude +
                    ",\"longitude\":" + longitude + ",\"data\":\"" + data + "\"}";

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonParams.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Falha ao realizar a solicitação.";
        }
    }

    public static List<Artista> procurarArtistasPorEstado(boolean estado, String token) {
        try {

            String urlString = URL + "/procurarArtistasPorEstado?estado=" + estado;
            HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();

            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }
                // Converte a resposta JSON para uma lista de artistas
                Gson gson = new Gson();
                Type listaArtista = new TypeToken<List<Artista>>() {}.getType();
                List<Artista> artistas = gson.fromJson(response.toString(), listaArtista);

                if (artistas.isEmpty()) {
                    System.out.println("Nenhum artista encontrado.");
                }

                return artistas;

            } else if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                System.out.println("Não tem permissão de administrador.");
                return Collections.emptyList();
            } else {
                System.out.println("Ocorreu um Erro - Código: " + responseCode);
                return Collections.emptyList();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Falha ao realizar a solicitação.");
            return Collections.emptyList();
        }

    }


    private static StringBuilder getStringBuilder(String urlString, String token) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();

        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        return response;
    }

    public static List<Artista> procurarArtistasLocalArte(String localizacao, String tipoArte, String token) {
        try {
            String urlString = URL + "/procurarArtistasLocalArte?localizacao=" + localizacao + "&tipoArte=" + tipoArte;
            StringBuilder response = getStringBuilder(urlString, token);

            // Converte a resposta JSON para uma lista de artistas
            Gson gson = new Gson();
            Type artistListType = new TypeToken<List<Artista>>() {
            }.getType();
            List<Artista> artistas = gson.fromJson(response.toString(), artistListType);

            return artistas;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Falha ao realizar a solicitação.");
            return Collections.emptyList();
        }
    }

    public static List<Artista> obterArtistas(String token) {
        try {

            String urlString = URL + "/obterArtistas";
            StringBuilder response = getStringBuilder(urlString, token);

            // Converte a resposta JSON para uma lista de artistas
            Gson gson = new Gson();
            Type artistListType = new TypeToken<List<Artista>>() {
            }.getType();

            return gson.fromJson(response.toString(), artistListType);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Falha ao realizar a solicitação.");
            return Collections.emptyList();
        }
    }

    public static List<String> obterLocalizacoesAtuar(String token) {
        try {
            String urlString = URL + "/obterLocalizacoesAtuar";
            StringBuilder response = getStringBuilder(urlString, token);

            // Converte a resposta JSON para uma lista de localizações
            Gson gson = new Gson();
            Type stringListType = new TypeToken<List<String>>() {
            }.getType();
            List<String> localizacoes = gson.fromJson(response.toString(), stringListType);

            return localizacoes;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Falha ao realizar a solicitação.");
            return Collections.emptyList();
        }
    }

    public static String aprovarArtistaPorId(int id, String token) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(URL + "/aprovarArtista/" + id).openConnection();

            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            int responseCode = connection.getResponseCode();

            // Se a resposta for 200 (OK)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    return response.toString();
                }
            } else if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                return "Não tem permissão de administrador.";
            } else {
                return "Ocorreu um Erro - Código: " + responseCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Falha ao realizar a solicitação.";
        }
    }


    public static List<String> obterAtuacoesArtista(int id, String token) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(URL + "/obterAtuacoesArtista/" + id).openConnection();

            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            // Se a resposta for 200 (OK)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<String>>() {
                    }.getType();
                    return gson.fromJson(br, listType);
                }
            } else {
                System.out.println("Ocorreu um Erro - Código: " + responseCode);
                return Collections.emptyList();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Falha ao realizar a solicitação.");
            return Collections.emptyList();
        }
    }

    public static boolean existeArtista(String nome, String tipo_arte, String token) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(URL + "/existeArtista?nome=" + URLEncoder.encode(nome, "UTF-8") + "&tipo_arte=" + URLEncoder.encode(tipo_arte, "UTF-8")).openConnection();

            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String response = br.readLine();
                    return Boolean.parseBoolean(response);
                }
            } else {
                System.out.println("Ocorreu um Erro - Código: " + responseCode);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Falha ao realizar a solicitação.");
            return false;
        }
    }

    public static boolean existeIDArtista(int id, String token) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(URL + "/existeIDArtista?id=" + id).openConnection();

            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String response = br.readLine();
                    return Boolean.parseBoolean(response);
                }
            } else {
                System.out.println("Ocorreu um Erro - Código: " + responseCode);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Falha ao realizar a solicitação.");
            return false;
        }
    }

    public static String adicionarDonativo(int id_artista, float valor, String username, String token) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(URL + "/adicionarDonativo").openConnection();

            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Montar o JSON com os parâmetros
            JsonObject jsonParams = new JsonObject();
            jsonParams.addProperty("id_artista", id_artista);
            jsonParams.addProperty("valor", valor);
            jsonParams.addProperty("username", username);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonParams.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    return br.readLine();
                }
            } else {
                return "Ocorreu um Erro - Código: " + responseCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Falha ao realizar a solicitação.";
        }
    }

    public static List<String> obterDonativosArtista(int id, String token) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(URL + "/obterDonativosArtista/" + id).openConnection();

            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            // Se a resposta for 200 (OK)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<String>>() {
                    }.getType();
                    return gson.fromJson(br, listType);
                }
            } else {
                System.out.println("Ocorreu um Erro - Código: " + responseCode);
                return Collections.emptyList();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Falha ao realizar a solicitação.");
            return Collections.emptyList();
        }
    }

    public static String alterarDescFoto(int id, String descricao, String foto, String token) {
        try {
            String descricaoEncoded = URLEncoder.encode(descricao, StandardCharsets.UTF_8.toString()).replace("+", "%20");
            String fotoEncoded = URLEncoder.encode(foto, StandardCharsets.UTF_8.toString()).replace("+", "%20");

            URL url = new URL(URL + "/alterarDescFoto/" + id + "/" + descricaoEncoded + "/" + fotoEncoded);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestMethod("POST");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    return br.lines().collect(Collectors.joining(System.lineSeparator()));
                }
            } else if(responseCode == HttpURLConnection.HTTP_FORBIDDEN){
                return "Não tem permissão de administrador.";
            } else {
                return "Ocorreu um Erro - Código: " + responseCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Falha ao realizar a solicitação.";
        }
    }

    public static String consultDescFoto(int id, String token) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(URL + "/consultarDescricaoEImagem/" + id).openConnection();

            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }


                    String result = response.toString();

                    if (result.isEmpty()) {
                        return "1";
                    } else {
                        return result;
                    }
                }
            } else if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                return "2";
            } else {
                return "Ocorreu um Erro - Código: " + responseCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Falha ao realizar a solicitação.";
        }
    }


    public static boolean artistaTemLocalizacao(String name, String token) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(URL + "/artistaTemLocalizacao?name=" + name).openConnection();

            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    return Boolean.parseBoolean(br.readLine());
                }
            } else {
                System.out.println("Ocorreu um Erro - Código: " + responseCode);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Falha ao realizar a solicitação.");
            return false;
        }
    }

    public static void atualizarLocalizacaoArtista(String nome, String novaLocalizacao, String token) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(URL + "/atualizarLocalizacaoArtista/" + URLEncoder.encode(nome, StandardCharsets.UTF_8.toString()) + "/" + URLEncoder.encode(novaLocalizacao, StandardCharsets.UTF_8.toString())).openConnection();

            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestMethod("POST");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    br.lines().collect(Collectors.joining(System.lineSeparator()));
                }
            } else {
                System.out.println("Ocorreu um Erro - Código: " + responseCode);
            }
        } catch (IOException e) {
            System.out.println("Falha ao realizar a solicitação.");
            e.printStackTrace();
        }
    }

    public static void verificarDataAtuacoes(String token) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(URL + "/verificarDataAtuacoes").openConnection();

            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestMethod("POST");

            connection.getResponseCode();

        } catch (IOException e) {
            System.out.println("Falha ao realizar a solicitação.");
            e.printStackTrace();
        }
    }

    public static List<String> obterAtuacoesTempo(int id_artista, int escolha, String token) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(URL + "/obterAtuacoesTempo/" + id_artista + "/" + escolha).openConnection();

            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<String>>() {
                    }.getType();
                    return gson.fromJson(br, listType);
                }
            } else {
                System.out.println("Ocorreu um Erro - Código: " + responseCode);
                return Collections.emptyList();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Falha ao realizar a solicitação.");
            return Collections.emptyList();
        }
    }

    public static String adicionarRating(int id, float rating, String token) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(URL + "/adicionarRating/" + id + "/" + rating).openConnection();

            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestMethod("POST");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    return br.lines().collect(Collectors.joining(System.lineSeparator()));
                }
            } else {
                return "Ocorreu um Erro - Código: " + responseCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Falha ao realizar a solicitação.";
        }
    }

    public static User autenticarUtilizador(String username, String adm, String password) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(URL + "/login").openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Cria os dados do formulário
            String formData = "user=" + URLEncoder.encode(username, "UTF-8") +
                    "&adm=" + URLEncoder.encode(adm, "UTF-8") +
                    "&password=" + URLEncoder.encode(password, "UTF-8");


            try (OutputStream os = connection.getOutputStream();
                 OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
                osw.write(formData);
                osw.flush();
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    Gson gson = new Gson();
                    return gson.fromJson(br, User.class);
                }
            } else {
                System.out.println("Ocorreu um Erro - Código: " + responseCode);
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Falha ao realizar a solicitação.");
            return null;
        }
    }

    public static String atualizarTipoUtilizador(String username, String token) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(URL + "/atualizarTipoUtilizador").openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Cria os dados do formulário
            String formData = "username=" + URLEncoder.encode(username, "UTF-8");

            // Escreve os dados do formulário na conexão
            try (OutputStream os = connection.getOutputStream();
                 OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
                osw.write(formData);
                osw.flush();
            }

            int responseCode = connection.getResponseCode();

            // Se a resposta for 200 (OK)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }


                    return response.toString();
                }
            } else {
                if(responseCode == HttpURLConnection.HTTP_FORBIDDEN) return "Não tem permissão de administrador.";
                return "Ocorreu um Erro - Código: " + responseCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Falha ao realizar a solicitação.";
        }
    }


}
