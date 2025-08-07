package br.universolivros.catalogo;

import br.universolivros.catalogo.model.Autor;
import br.universolivros.catalogo.model.Livro;
import br.universolivros.catalogo.repository.AutorRepository;
import br.universolivros.catalogo.repository.LivroRepository;
import br.universolivros.catalogo.service.GutendexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class CatalogoApplication implements CommandLineRunner {

	@Autowired
	private GutendexService gutendexService;

	@Autowired
	private LivroRepository livroRepo;

	@Autowired
	private AutorRepository autorRepo;

	public static void main(String[] args) {
		SpringApplication.run(CatalogoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("------------------------------------------------------------------");
			System.out.println("\nEscolha o número da sua opção:");
			System.out.println("1 -> Buscar Livro pelo título");
			System.out.println("2 -> Listar Livros registrados");
			System.out.println("3 -> Listar Autores registrados");
			System.out.println("4 -> Listar Autores vivos em um determinado ano");
			System.out.println("5 -> Listar Livros em um determinado idioma");
			System.out.println("0 -> Sair");
			System.out.print("-> ");

			String option = scanner.nextLine().trim();

			switch (option) {
				case "1":
					System.out.print("Digite o título para buscar: ");
					String tituloBusca = scanner.nextLine().trim();
					Optional<Livro> saved = gutendexService.buscarSalvarPorTitulo(tituloBusca);
					if (saved.isPresent()) {
						System.out.println("Livro salvo: " + saved.get().getTitulo() +
								" — Autor: " + saved.get().getAutor().getNome());
					} else {
						System.out.println("Nenhum resultado encontrado para: " + tituloBusca);
					}
					break;

				case "2":
					List<Livro> livros = livroRepo.findAll();
					if (livros.isEmpty()) {
						System.out.println("Nenhum livro cadastrado.");
					} else {
						System.out.println("Livros cadastrados:");
						System.out.printf("%-5s %-30s %-10s %-10s %-20s%n", "ID", "Título", "Idioma", "Downloads", "Autor");
						System.out.println("--------------------------------------------------------------------------------");
						for (Livro l : livros) {
							String autorNome = (l.getAutor() != null && l.getAutor().getNome() != null)
									? l.getAutor().getNome()
									: "—";
							String idioma = l.getIdioma() != null ? l.getIdioma() : "—";
							String downloads = l.getDownloads() != null ? String.valueOf(l.getDownloads()) : "—";
							System.out.printf("%-5d %-30s %-10s %-10s %-20s%n",
									l.getId(), l.getTitulo(), idioma, downloads, autorNome);
						}
					}
					break;

				case "3":
					List<Autor> autores = autorRepo.findAll();
					if (autores.isEmpty()) {
						System.out.println("Nenhum autor cadastrado.");
					} else {
						System.out.println("Autores cadastrados:");
						System.out.printf("%-5s %-25s %-20s %-20s%n", "ID", "Nome", "Ano Nascimento", "Ano Falecimento");
						System.out.println("--------------------------------------------------------------------------");
						for (Autor a : autores) {
							String anoFalecimento = a.getAnoFalecimento() != null ? a.getAnoFalecimento().toString() : "Vivo";
							String anoNascimento = a.getAnoNascimento() != null ? a.getAnoNascimento().toString() : "Desconhecido";
							System.out.printf("%-5d %-25s %-20s %-20s%n",
									a.getId(), a.getNome(), anoNascimento, anoFalecimento);
			}
					}
					break;

				case "4":
					System.out.print("Digite o ano: ");
					String anoStr = scanner.nextLine().trim();
					try {
						int ano = Integer.parseInt(anoStr);
						List<Autor> vivos = autorRepo.findAutoresVivosEm(ano);
						if (vivos.isEmpty()) {
							System.out.println("Nenhum autor vivo nesse ano.");
						} else {
							System.out.println("Autores vivos em " + ano + ":");
							System.out.printf("%-25s %-15s %-15s%n", "Nome", "Ano Nascimento", "Ano Falecimento");
							System.out.println("--------------------------------------------------------");
							for (Autor a : vivos) {
								String anoFalecimento = a.getAnoFalecimento() != null ? a.getAnoFalecimento().toString() : "Vivo";
								String anoNascimento = a.getAnoNascimento() != null ? a.getAnoNascimento().toString() : "Desconhecido";
								System.out.printf("%-25s %-15s %-15s%n",
										a.getNome(), anoNascimento, anoFalecimento);
							}
						}
					} catch (NumberFormatException e) {
						System.out.println("Ano inválido.");
					}
					break;

				case "5":
					System.out.print("Digite a sigla do idioma:\n" +
							"pt -> Português\n" +
							"en -> Inglês\n" +
							"es -> Espanhol\n" +
							"fr -> Francês\n" +
							">>> ");

					String idiomaBusca = scanner.nextLine().trim();
					List<Livro> livrosPorIdioma = livroRepo.findByIdiomaIgnoreCase(idiomaBusca);
					if (livrosPorIdioma.isEmpty()) {
						System.out.println("Nenhum livro encontrado para o idioma: " + idiomaBusca);
					} else {
						System.out.println("Livros no idioma " + idiomaBusca + ":");
						System.out.printf("%-30s %-20s%n", "Título", "Autor");
						System.out.println("--------------------------------------------");
						for (Livro l : livrosPorIdioma) {
							String autorNome = (l.getAutor() != null && l.getAutor().getNome() != null)
									? l.getAutor().getNome()
									: "—";
							System.out.printf("%-30s %-20s%n", l.getTitulo(), autorNome);
						}
					}
					break;

				case "0":
					System.out.println("Saindo...");
					scanner.close();
					return;

				default:
					System.out.println("Opção inválida. Tente novamente.");
			}
		}
	}
}
