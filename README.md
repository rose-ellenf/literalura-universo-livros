# 📚 Literalura - Universo dos Livros

Projeto desenvolvido em Java com Spring Boot para explorar autores e obras a partir da API pública do [Gutendex](https://gutendex.com/).

## 🔍 Funcionalidades

- Buscar livros por título
- Listar livros registrados
- Listar autores vivos em um determinado ano
- Salvar e consultar dados em um banco de dados PostgreSQL

## 🛠️ Tecnologias utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL + pgAdmin 4
- API Gutendex
- Maven

## 🧪 Como executar o projeto

1. Clone o repositório:
2. Crie um banco no pgAdmin 4 chamado literalura.
3. Configure o arquivo application.properties em src/main/resources com seu usuário e senha do PostgreSQL:
4. Rode o projeto via IDE (IntelliJ, Eclipse) ou pelo terminal:
5. Use o terminal para interagir com o menu do sistema.

## 💡 Melhorias Futuras
 - Implementar autenticação de usuário para proteger funcionalidades.
 - Desenvolver interface web para facilitar a navegação e busca.
 - Adicionar cache para melhorar a performance das consultas à API Gutendex.
 - Suporte a mais filtros de busca, como por idioma, data e autor.
 - Internacionalização para suportar múltiplos idiomas.


## 📝 Licença
Este projeto está licenciado sob a licença MIT — veja o arquivo LICENSE para detalhes.

