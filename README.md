# Edge-service
### Beschrijving gekozen thema
We zijn op een gemeenschappelijke interesse gekomen, namelijk New World, een game die onlangs werd uitgebracht. De game werkt met builds van wapens, abilities en skills/attributes. Wij hebben een tool gemaakt die het maken en opslaan van zo'n build mogelijk maakt. Hier maken we een microservice voor het wapen. Dit wapen wordt sterker dankzij een skill/attribute en heeft bepaalde ability opties. In de tweede microservice kiezen we dan uit deze lijst van wapens een primary en secondary wapen en de daarbij gekozen skills/attributes en abilities. In onze edge service verbinden we dan deze twee services zodat we hier een mooi overzicht voor kunnen geven. Met andere woorden, alle info over het wapen en jouw zelfgekozen waarden voor de skills en abilities. 

### Architectuur
![architectuur microservices](https://luukopthoog.sinners.be/MS-Diagram.JPG)

### Links back-end repo's
[https://github.com/SybrenVanGastel/builder-service](https://github.com/SybrenVanGastel/builder-service)
<br/>
[https://github.com/SybrenVanGastel/weapon-service](https://github.com/SybrenVanGastel/weapon-service)

### Links extra repo's
[https://github.com/SybrenVanGastel/apt-frontend](https://github.com/SybrenVanGastel/apt-frontend)
<br/>
[https://github.com/LuukOptHoog/SeleniumTestingAPT](https://github.com/LuukOptHoog/SeleniumTestingAPT)

### SwaggerUI
Dit is de link naar de Swagger documentatie ([https://edge-service-server-sybrenvangastel.cloud.okteto.net/swagger-ui.html#/](https://edge-service-server-sybrenvangastel.cloud.okteto.net/swagger-ui.html#/)). Hieronder vindt u de screenshots met responses voor elke API call die er gemaakt kan worden vanuit de edge service.
<br/>
<br/>
**POST /build**
<br/>
![screenshot swagger](https://r0785524.sinners.be/apt/post_build.png)
<br/>
**DELETE /build/{name}**
<br/>
![screenshot swagger](https://r0785524.sinners.be/apt/delete_build.png)
<br/>
**GET /build/{name}**
<br/>
![screenshot swagger](https://r0785524.sinners.be/apt/get_build_name.png)
<br/>
**PUT /build/{name}**
<br/>
![screenshot swagger](https://r0785524.sinners.be/apt/put_build.png)
<br/>
**GET /builds**
<br/>
![screenshot swagger](https://r0785524.sinners.be/apt/get_builds.png)
<br/>
**GET /builds/tag/{name}**
<br/>
![screenshot swagger](https://r0785524.sinners.be/apt/get_builds_tag.png)
<br/>
**GET /builds/user/{name}**
<br/>
![screenshot swagger](https://r0785524.sinners.be/apt/get_builds_user.png)
<br/>
**GET /builds/weapon/{name}**
<br/>
![screenshot swagger](https://r0785524.sinners.be/apt/get_builds_weapon.png)
<br/>
**GET /builds/{name}**
<br/>
![screenshot swagger](https://r0785524.sinners.be/apt/get_builds_name.png)
<br/>
**GET /weapon/{name}**
<br/>
![screenshot swagger](https://r0785524.sinners.be/apt/get_weapon_name.png)
<br/>
**GET /weapons**
<br/>
![screenshot swagger](https://r0785524.sinners.be/apt/get_weapons.png)
