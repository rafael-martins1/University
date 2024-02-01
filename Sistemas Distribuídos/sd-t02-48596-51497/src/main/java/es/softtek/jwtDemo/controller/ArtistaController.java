package es.softtek.jwtDemo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import es.softtek.jwtDemo.dto.Artista;
import es.softtek.jwtDemo.service.DBService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@RestController
public class ArtistaController {


	private final DBService DBService;

	public ArtistaController(DBService DBService) {
		this.DBService = DBService;
	}

	// Controlador para registar um artista
	@PostMapping("artista")
	public synchronized String criarArtista(@RequestBody String jsonParams){
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(jsonParams, JsonObject.class);

		String nome = jsonObject.get("nome").getAsString();
		String tipo_arte = jsonObject.get("tipo_arte").getAsString();
		String localizacao = jsonObject.get("localizacao").getAsString();

		Artista artista = new Artista(nome, tipo_arte,localizacao);

		return this.DBService.adicionarArtista(artista);
	}

	// Controlador para registar uma atuação
	@PostMapping("atuacao")
	public synchronized String criarAtuacao(@RequestBody String jsonParams){
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(jsonParams, JsonObject.class);

		int id_artista = jsonObject.get("id_artista").getAsInt();
		float latitude = jsonObject.get("latitude").getAsFloat();
		float longitude = jsonObject.get("longitude").getAsFloat();
		String data = jsonObject.get("data").getAsString();

		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
		LocalDate localDate = LocalDate.parse(data, formatter);

		return this.DBService.adicionarAtuacao(id_artista, latitude, longitude, localDate);
	}

	@GetMapping("procurarArtistasPorEstado")
	public String procurarArtistasPorEstado(@RequestParam("estado") boolean estado) {
		try {
			List<Artista> artistas = DBService.procurarArtistasPorEstado(estado);

			Gson gson = new Gson();
			return gson.toJson(artistas);
		} catch (Exception e) {
			e.printStackTrace();
			return "Erro ao procurar artistas por estado.";
		}
	}

	// Controlador para procurar artistas com filtros
	@GetMapping("procurarArtistasLocalArte")
	public String procurarArtistasLocalArte(@RequestParam("localizacao") String localizacao, @RequestParam("tipoArte") String tipoArte) {
		try {
			List<Artista> artistas = DBService.procurarArtistasLocalArte(localizacao, tipoArte);

			Gson gson = new Gson();
			return gson.toJson(artistas);
		} catch (Exception e) {
			e.printStackTrace();
			return "Erro ao procurar artistas por localização e tipo de arte.";
		}
	}

	// Controlador para listar todos os artistas
	@GetMapping("obterArtistas")
	public String obterArtistas() {
		try {
			List<Artista> artistas = DBService.obterArtistas();

			Gson gson = new Gson();
			return gson.toJson(artistas);
		} catch (Exception e) {
			e.printStackTrace();
			return "Erro ao obter a lista de artistas.";
		}
	}

	// Controlador para obter localizações com artistas a atuar
	@GetMapping("obterLocalizacoesAtuar")
	public String obterLocalizacoesAtuar() {
		try {
			List<String> localizacoes = DBService.obterLocalizacoesAtuar();

			Gson gson = new Gson();
			return gson.toJson(localizacoes);
		} catch (Exception e) {
			e.printStackTrace();
			return "Erro ao obter a lista de localizações de atuações.";
		}
	}

	// Controlador para aprovar um artista por ‘id’
	@PostMapping("aprovarArtista/{id}")
	public synchronized String aprovarArtistaPorId(@PathVariable int id) {
		try {
			return DBService.aprovarArtistaPorId(id);
		} catch (Exception e) {
			e.printStackTrace();
			return "Erro ao obter artistas aprovados por id.";
		}
	}

	// Controlador para obter atuações de um artista
	@GetMapping("obterAtuacoesArtista/{id}")
	public synchronized List<String> obterAtuacoesArtista(@PathVariable int id) {
		try {
			return DBService.obterAtuacoesArtista(id);
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	// Controlador para verificar se existe um artista com x nome e tipo de arte
	@GetMapping("existeArtista")
	public boolean existeArtista(@RequestParam("nome") String nome, @RequestParam("tipo_arte") String tipo_arte) {
		try {
			return DBService.existeArtista(nome, tipo_arte);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// Controlador para verificar se existe um artista com x ‘id’
	@GetMapping("existeIDArtista")
	public boolean existeIDArtista(@RequestParam("id") int id) {
		try {
			return DBService.existeIDArtista(id);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// Controlador para adicionar um donativo
	@PostMapping("adicionarDonativo")
	public synchronized String adicionarDonativo(@RequestBody String jsonParams) {
		try {
			Gson gson = new Gson();
			JsonObject jsonObject = gson.fromJson(jsonParams, JsonObject.class);

			int id_artista = jsonObject.get("id_artista").getAsInt();
			float valor = jsonObject.get("valor").getAsFloat();
			String username = jsonObject.get("username").getAsString();

			return this.DBService.adicionarDonativo(id_artista, valor, username);
		} catch (Exception e) {
			e.printStackTrace();
			return "Erro ao adicionar donativo.";
		}
	}

	// Controlador para obter donativos de um artista pelo ‘id’
	@GetMapping("obterDonativosArtista/{id_artista}")
	public synchronized List<String> obterDonativosArtista(@PathVariable int id_artista) {
		try {
			return DBService.obterDonativosArtista(id_artista);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Collections.emptyList();
	}

	// Controlador para alterar a descrição e foto de um artista
	@PostMapping("alterarDescFoto/{id}/{descricao}/{foto}")
	public String alterarDescFoto(@PathVariable int id, @PathVariable String descricao, @PathVariable String foto) {
		try {
			return DBService.alterarDescFoto(id, descricao, foto);
		} catch (Exception e) {
			e.printStackTrace();
			return "Erro ao processar a solicitação.";
		}
	}

	// Controlador para consultar a descrição e imagem
	@GetMapping("consultarDescricaoEImagem/{id}")
	public String consultarDescricaoEImagem(@PathVariable int id) {
		return DBService.consultDescFoto(id);
	}

	// Controlador para verificar se o artista tem localização
	@GetMapping("artistaTemLocalizacao")
	public synchronized boolean artistaTemLocalizacao(@RequestParam("name") String name) {
		try {
			return DBService.artistaTemLocalizacao(name);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	// Controlador para atualizar a localização de um artista
	@PostMapping("atualizarLocalizacaoArtista/{nome}/{novaLocalizacao}")
	public String atualizarLocalizacaoArtista(@PathVariable String nome, @PathVariable String novaLocalizacao) {
		return DBService.atualizarLocalizacaoArtista(nome, novaLocalizacao);
	}

	// Controlador para verificar a data das atuações e colocar a true as que são neste dia
	@PostMapping("verificarDataAtuacoes")
	public void verificarDataAtuacoes() {
		try {
			DBService.verificarDataAtuacoes();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Controlador para obter as atuações ou passadas, ou futuras
	@GetMapping("obterAtuacoesTempo/{id_artista}/{escolha}")
	public synchronized List<String> obterAtuacoesTempo(@PathVariable int id_artista, @PathVariable int escolha) {
		try {
			return DBService.obterAtuacoesTempo(id_artista, escolha);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Collections.emptyList();
	}

	// Controlador para adicionar classificação a um artista
	@PostMapping("adicionarRating/{id}/{rating}")
	public String adicionarRating(@PathVariable int id, @PathVariable float rating) {
		try {
			return DBService.adicionarRating(id, rating);
		} catch (Exception e) {
			e.printStackTrace();
			return "Erro ao processar a solicitação.";
		}
	}

	// Controlador para atualizar o tipo de utilizador
	@PostMapping("atualizarTipoUtilizador")
	public String atualizarTipoUtilizador(@RequestParam String username) throws Exception {
		return DBService.atualizarTipoUtilizador(username);
	}

}
