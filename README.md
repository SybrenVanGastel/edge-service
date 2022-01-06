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

### Screenshots SwaggerUI
**POST /build**
![screenshot swagger](https://r0785524.sinners.be/apt/post_build.png)
**DELETE /build/{name}**
![screenshot swagger](https://r0785524.sinners.be/apt/delete_build.png)
**GET /build/{name}**
![screenshot swagger](https://r0785524.sinners.be/apt/get_build_name.png)
**PUT /build/{name}**
![screenshot swagger](https://r0785524.sinners.be/apt/put_build.png)
**GET /builds**
![screenshot swagger](https://r0785524.sinners.be/apt/get_builds.png)
**GET /builds/tag/{name}**
![screenshot swagger](https://r0785524.sinners.be/apt/get_builds_tag.png)
**GET /builds/user/{name}**
![screenshot swagger](https://r0785524.sinners.be/apt/get_builds_user.png)
**GET /builds/weapon/{name}**
![screenshot swagger](https://r0785524.sinners.be/apt/get_builds_weapon.png)
**GET /builds/{name}**
![screenshot swagger](https://r0785524.sinners.be/apt/get_builds_name.png)
**GET /weapon/{name}**
![screenshot swagger](https://r0785524.sinners.be/apt/get_weapon_name.png)
**GET /weapons**
![screenshot swagger](https://r0785524.sinners.be/apt/get_weapons.png)
