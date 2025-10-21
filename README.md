# NLayerLanche

## Descrição do Projeto
O **NLayerLanche** é um sistema de gerenciamento de produtos de uma lanchonete, desenvolvido em Java.  
O projeto segue uma arquitetura em camadas (Repository, Service, Application, Facade) e inclui funcionalidades de:

- Cadastro de produtos
- Atualização de produtos e suas imagens
- Remoção de produtos
- Listagem de produtos
- Controle de existência de produtos
- Venda de produtos (controle de estoque)
- Testes unitários e de integração

O projeto tem como objetivo aprendizado e aplicação de boas práticas de desenvolvimento em Java, manipulação de arquivos e testes automatizados.

---

## Estrutura do Projeto

NLayerLanche/
│
├─ src/main/java/com/snack/
│ ├─ applications/ # Lógica de aplicação (ProductApplication)
│ ├─ entities/ # Classes de entidades (Product)
│ ├─ facade/ # Facade para integração (ProductFacade)
│ ├─ repositories/ # Camada de acesso a dados (ProductRepository)
│ └─ services/ # Serviços de manipulação (ProductService, App)
│
├─ src/test/java/com/snack/
│ ├─ applications/ # Testes unitários da aplicação (ProductApplicationTest)
│ ├─ repositories/ # Testes unitários dos repositórios (ProductRepositoryTest)
│ ├─ services/ # Testes unitários dos serviços (ProductServiceTest)
│ └─ entities/ # Testes das entidades (ProductTest)
│
├─ src/test/resources/
│ └─ test-image.jpg # Imagem usada nos testes
│
├─ target/ # Build do Maven
│
└─ README.md
