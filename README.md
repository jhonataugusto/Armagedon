## Armagedon
Este é um projeto que visa implementar uma network de Minecraft, utilizando BungeeCord e Bukkit, além de tecnologias como Redis e MongoDB. O projeto é dividido em módulos, sendo eles:

Core: módulo principal da rede, contendo as configurações globais e comuns a todos os servidores;

Bungee: módulo responsável pelo servidor proxy, gerenciando as conexões dos jogadores aos diferentes servidores da rede;

br.com.armagedon.Practice: módulo responsável pela implementação de um sistema de duelos e partidas rápidas;

Hub: módulo que implementa um lobby central, onde os jogadores podem se encontrar e se preparar para acessar outros servidores da rede.

# Dependências
O projeto depende das seguintes tecnologias e bibliotecas:

Java 11
BungeeCord API 1.16.5
Bukkit API 1.16.5
Jedis 3.6.1
MongoDB Java Driver 4.3.1
# Como executar
Para executar a rede, é necessário seguir os seguintes passos:

Clonar o repositório para a máquina local;
Configurar o arquivo de configuração config.yml presente no módulo Core, alterando as configurações de acordo com a necessidade do usuário;
Iniciar o servidor proxy (BungeeCord) executando o arquivo BungeeCord.jar;
Iniciar os servidores de jogos (Bukkit) executando o arquivo spigot.jar;
Conectar os servidores de jogos ao servidor proxy configurando o arquivo spigot.yml presente no diretório do servidor, inserindo as configurações de IP e porta do servidor proxy;
Iniciar o servidor Redis;
Iniciar o servidor MongoDB.

# Futuras implementações
Implementação de um front-end utilizando React;
Implementação de um back-end utilizando Spring.

# Contribuindo
Contribuições são bem-vindas! Sinta-se à vontade para enviar pull requests, relatar problemas e sugerir melhorias.

Licença
Este projeto é licenciado sob a Licença MIT. Veja o arquivo LICENSE para mais informações.
