# EventGo

- Aplicativo desenvolvido para consumo de API com Retrofit, o app possui SplashScreen, uma tela inicial com eventos retornados da API. Exibindo:
- Lista com eventos. <br>
- Na tela inicial é possivel clicar em um evento e sera aberta uma tela com os detalhes do evento <br>

-Para utilizar o app, abra no Android Studio e faça o clone do projeto e execute o app no emulador ou um dispositivo fisico <br>


<img src="https://github.com/DEVenicio/EventGo/blob/master/splash.png" width="300"> <img src="https://github.com/DEVenicio/EventGo/blob/master/tela%20inicial.png" width="300"><img src="https://github.com/DEVenicio/EventGo/blob/master/tela%20detalhes.png" width="300">



## Tecnologias e Ferramentas utilizadas

- Arquitetura MVVM
- Consumo de API Rest através do Retrofit
- Layout em XML
- RecyclerView
- LiveData
- Coroutines
- Fragments
- Lifecycle
- ViewBinding
- Gson
- Glide (para carregamento de imagens)
- ViewModel
- Injeção de dependência com Koin
- ItextPDF
- SDP
- Conscrypt
- Google Maps

Foi escolhida a arquitetura MVVM pela facilidade e separação de camadas, facilitando e permitindo a escalabilidade do projeto.
Retrofit é o mais adotado para consumo de API REST pela comunidade android. 
Itens do Android Jetpack que facilitam o desenvolvimento e deixa a aplicação mais fluída. 
Glide para carregamento de imagens
Injeção de dependencia com Koin
Itext para gerar PDF 
SDP para layouts mais flexiveis independente de pixels 
Conscrypt lib de criptografia TLS para subistituir o OpenSSL como provedor de seguranca no Android para permitir a comunicação utilizando TLSv1 presente <br>
na API 19 e aceitação de todos os certificados. (Em produção JAMAIS faça isso como está no código desse app)<br>
Mas para efeitos de estudo foi feito dessa maneira. (Possui diversas vulnerabilidades) <br>
Google Maps para plotar a localização do evento no mapa na tela de detalhes do evento. 




:mailbox: Como me achar **---->**   [![Linkedin Badge](https://img.shields.io/badge/-LinkedIn-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/venicio-almeida/)](https://www.linkedin.com/in/venicio-almeida/)                                           [![Gmail Badge](https://img.shields.io/badge/-Gmail-c14438?style=flat-square&logo=Gmail&logoColor=white&link=mailto:engineer.venicio@gmail.com)](mailto:engineer.venicio@gmail.com)

