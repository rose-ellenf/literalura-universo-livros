package br.universolivros.catalogo.service;

import br.universolivros.catalogo.model.Autor;
import br.universolivros.catalogo.model.Livro;
import br.universolivros.catalogo.repository.AutorRepository;
import br.universolivros.catalogo.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class GutendexService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final RestTemplate restTemplate;

    public GutendexService(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.restTemplate = new RestTemplate();
    }

    public Optional<Livro> buscarSalvarPorTitulo(String tituloBusca) {
        String url = "https://gutendex.com/books?search=" + tituloBusca.replace(" ", "+");
        GutendexResponse response = restTemplate.getForObject(url, GutendexResponse.class);

        if (response != null && response.results != null && !response.results.isEmpty()) {
            GutendexBook gutendexBook = response.results.get(0);


            Optional<Livro> livroExistente = livroRepository.findByTituloIgnoreCase(gutendexBook.title);
            if (livroExistente.isPresent()) {
                return livroExistente;
            }

            Autor autor = null;
            if (!gutendexBook.authors.isEmpty()) {
                GutendexAuthor autorInfo = gutendexBook.authors.get(0);
                String nomeAutor = autorInfo.name;
                Integer anoNasc = autorInfo.birth_year;
                Integer anoFal = autorInfo.death_year;

                Optional<Autor> autorExistente = autorRepository.findByNome(nomeAutor);
                if (autorExistente.isPresent()) {
                    autor = autorExistente.get();

                    boolean atualizado = false;
                    if (autor.getAnoNascimento() == null && anoNasc != null) {
                        autor.setAnoNascimento(anoNasc);
                        atualizado = true;
                    }
                    if (autor.getAnoFalecimento() == null && anoFal != null) {
                        autor.setAnoFalecimento(anoFal);
                        atualizado = true;
                    }
                    if (atualizado) {
                        autor = autorRepository.save(autor);
                    }
                } else {
                    autor = new Autor(nomeAutor, anoNasc, anoFal);
                    autor = autorRepository.save(autor);
                }
            }

            Livro livro = new Livro();
            livro.setTitulo(gutendexBook.title);
            livro.setIdioma(gutendexBook.languages.isEmpty() ? "desconhecido" : gutendexBook.languages.get(0));
            livro.setDownloads(gutendexBook.download_count);
            livro.setAutor(autor);

            livro = livroRepository.save(livro);
            return Optional.of(livro);
        }

        return Optional.empty();
    }

    private static class GutendexResponse {
        public java.util.List<GutendexBook> results;
    }

    private static class GutendexBook {
        public String title;
        public java.util.List<GutendexAuthor> authors;
        public java.util.List<String> languages;
        public int download_count;
    }

    private static class GutendexAuthor {
        public String name;
        public Integer birth_year;
        public Integer death_year;
    }
}
